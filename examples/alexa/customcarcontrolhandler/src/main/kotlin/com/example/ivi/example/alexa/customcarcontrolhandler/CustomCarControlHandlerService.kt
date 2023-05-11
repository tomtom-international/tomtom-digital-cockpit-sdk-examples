/*
 * Copyright © 2020 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.alexa.customcarcontrolhandler

import android.content.Context
import com.amazon.aacsconstants.Action
import com.amazon.aacsconstants.Topic
import com.tomtom.ivi.platform.alexa.api.common.util.AacsMessage
import com.tomtom.ivi.platform.alexa.api.common.util.AacsSenderWrapper
import com.tomtom.ivi.platform.alexa.api.common.util.parseJsonMessage
import com.tomtom.ivi.platform.alexa.api.service.alexahandler.AlexaHandlerService
import com.tomtom.ivi.platform.alexa.api.service.alexahandler.AlexaHandlerServiceBase
import com.tomtom.ivi.platform.framework.api.common.iviinstance.createTracer
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.kotlin.traceevents.TraceEventListener
import com.tomtom.kotlin.traceevents.TraceLog
import com.tomtom.kotlin.traceevents.TraceLogLevel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal class CustomCarControlHandlerService(
    iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider
) : AlexaHandlerServiceBase(iviServiceHostContext, serviceIdProvider) {

    private val tracer = iviServiceHostContext.createTracer<CustomCarControlHandlerEvents> { this }

    private val aacsSender = AacsSenderWrapper(iviServiceHostContext)

    /**
     * The same message action ([Action.CarControl.SET_CONTROLLER_VALUE]) corresponds to multiple
     * AASB message types:
     * - `SetPowerControllerValue`
     * - `SetModeControllerValue`
     * - `SetRangeControllerValue`
     * - `SetToggleControllerValue`
     *
     * The `payload.capabilityType` field can be used to differentiate among them.
     */
    object SetControllerValueMessagePayloadSerializer :
        JsonContentPolymorphicSerializer<SetControllerValueIncomingMessagePayloadBase>(
            SetControllerValueIncomingMessagePayloadBase::class
        ) {
        override fun selectDeserializer(element: JsonElement):
            DeserializationStrategy<SetControllerValueIncomingMessagePayloadBase> =
            when (element.jsonObject[JSON_KEY_CAPABILITY_TYPE]?.jsonPrimitive?.content) {
                JSON_VALUE_CONTROLLER_MODE ->
                    SetModeControllerValueIncomingMessagePayload.serializer()
                JSON_VALUE_CONTROLLER_POWER ->
                    SetPowerControllerValueIncomingMessagePayload.serializer()
                JSON_VALUE_CONTROLLER_TOGGLE ->
                    SetToggleControllerValueIncomingMessagePayload.serializer()
                JSON_VALUE_CONTROLLER_RANGE ->
                    SetRangeControllerValueIncomingMessagePayload.serializer()
                else -> throw IllegalArgumentException("Unknown controller type: $element")
            }
    }

    /**
     * Data class representation of the
     * [CarControl](https://alexa.github.io/alexa-auto-sdk/docs/aasb/car-control/CarControl)
     * AASB messages.
     */

    @Serializable(with = SetControllerValueMessagePayloadSerializer::class)
    sealed class SetControllerValueIncomingMessagePayloadBase

    @Serializable
    data class SetModeControllerValueIncomingMessagePayload(
        val capabilityType: String,
        val endpointId: String,
        val instanceId: String,
        val value: String
    ) : SetControllerValueIncomingMessagePayloadBase()

    @Serializable
    data class SetPowerControllerValueIncomingMessagePayload(
        val capabilityType: String,
        val endpointId: String,
        val turnOn: Boolean
    ) : SetControllerValueIncomingMessagePayloadBase()

    @Serializable
    data class SetRangeControllerValueIncomingMessagePayload(
        val capabilityType: String,
        val endpointId: String,
        val instanceId: String,
        val value: Double
    ) : SetControllerValueIncomingMessagePayloadBase()

    @Serializable
    data class SetToggleControllerValueIncomingMessagePayload(
        val capabilityType: String,
        val endpointId: String,
        val instanceId: String,
        val turnOn: Boolean
    ) : SetControllerValueIncomingMessagePayloadBase()

    @Serializable
    data class SetControllerValueOutgoingMessagePayload(
        val success: Boolean
    )

    object AdjustControllerValueMessageSerializer :
        JsonContentPolymorphicSerializer<AdjustControllerValueIncomingMessagePayloadBase>(
            AdjustControllerValueIncomingMessagePayloadBase::class
        ) {
        override fun selectDeserializer(element: JsonElement):
            DeserializationStrategy<AdjustControllerValueIncomingMessagePayloadBase> =
            when (element.jsonObject[JSON_KEY_CAPABILITY_TYPE]?.jsonPrimitive?.content) {
                JSON_VALUE_CONTROLLER_MODE ->
                    AdjustModeControllerValueIncomingMessagePayload.serializer()
                JSON_VALUE_CONTROLLER_RANGE ->
                    AdjustRangeControllerValueIncomingMessagePayload.serializer()
                else -> throw IllegalArgumentException("Unknown controller type: $element")
            }
    }

    @Serializable(with = AdjustControllerValueMessageSerializer::class)
    sealed class AdjustControllerValueIncomingMessagePayloadBase

    @Serializable
    data class AdjustModeControllerValueIncomingMessagePayload(
        val capabilityType: String,
        val endpointId: String,
        val instanceId: String,
        val delta: Int
    ) : AdjustControllerValueIncomingMessagePayloadBase()

    @Serializable
    data class AdjustRangeControllerValueIncomingMessagePayload(
        val capabilityType: String,
        val endpointId: String,
        val instanceId: String,
        val delta: Double
    ) : AdjustControllerValueIncomingMessagePayloadBase()

    @Serializable
    data class AdjustControllerValueOutgoingMessagePayload(
        val success: Boolean
    )

    override fun onCreate() {
        super.onCreate()

        // This [AlexaHandlerService] implementation will handle CarControl AASB messages.
        topic = Topic.CAR_CONTROL

        // This [AlexaHandlerService] implementation has default priority, so CarControl AASB
        // messages will be forwarded to this handler first, before reaching the stock
        // TomTom Digital Platform CarControl message handler.
        priority = AlexaHandlerService.DEFAULT_HANDLER_PRIORITY

        // Copy the `custom_assets.json` file from the assets storage to the internal storage, so
        // that it can be found by AACS.
        copyCustomAssets()

        // This configuration adds 2 additional endpoints to the CarControl configuration that
        // will be sent to AACS:
        // - a "default.light" endpoint, associated to "light" assets that are already available in
        //   the default automotive catalog assets
        //   (see https://github.com/alexa/alexa-auto-sdk/blob/master/modules/car-control/assets/assets-1P.json)
        // - a "default.custom_device" endpoint, associated to a custom
        //   "My.Alexa.Automotive.DeviceName.CustomDevice" asset, defined in the
        //   "custom_assets.json" file.
        // This will allow us to receive a CarControl message when the user says: "Alexa, switch
        // on/off the light" or "Alexa, turn on/off my custom device".
        aacsConfiguration = readAacsConfig(context)

        // All required properties have been set, so the service is now ready.
        serviceReady = true
    }

    @Suppress("kotlin:S6300") // Custom assets file does not contain sensitive data.
    private fun copyCustomAssets() {
        try {
            with(context) {
                val customAssetsPath =
                    getString(R.string.custom_control_config_directory) +
                        File.separator +
                        CUSTOM_ASSETS_FILENAME
                assets.open(customAssetsPath).use { inputFile ->
                    FileOutputStream(filesDir?.resolve(CUSTOM_ASSETS_FILENAME)).use {
                        inputFile.copyTo(it)
                    }
                }
            }
        } catch (exception: IOException) {
            tracer.e("Failed to copy custom assets file.", exception)
        }
    }

    private fun readAacsConfig(context: Context): String? =
        try {
            with(context) {
                val filePath = getString(R.string.custom_control_config_directory) +
                    File.separator +
                    AACS_CONFIG_FILENAME

                assets.open(filePath).bufferedReader().use { it.readText() }
            }
        } catch (exception: IOException) {
            tracer.e("AACS configuration file not found.", exception)
            null
        }

    override suspend fun onMessageReceived(message: AacsMessage): Boolean =
        when (message.action) {
            Action.CarControl.SET_CONTROLLER_VALUE ->
                handleSetControllerValue(message.id, message.payload)
            Action.CarControl.ADJUST_CONTROLLER_VALUE ->
                handleAdjustControllerValue(message.id, message.payload)
            // We are only interested in handling `SET_CONTROLLER_VALUE` and
            // `ADJUST_CONTROLLER_VALUE` incoming messages.
            // We return `false` for any other action, so that the message can be forwarded to other
            // CarControl message handlers.
            else -> false
        }

    private fun handleSetControllerValue(messageId: String, message: String): Boolean {
        parseJsonMessage<SetControllerValueIncomingMessagePayloadBase>(message)?.let { setMessage ->
            tracer.setControllerValueMessageReceived(setMessage)
            return when (setMessage) {
                is SetPowerControllerValueIncomingMessagePayload ->
                    setPowerControllerValue(messageId, setMessage)
                is SetRangeControllerValueIncomingMessagePayload ->
                    setRangeControllerValue(messageId, setMessage)
                is SetModeControllerValueIncomingMessagePayload ->
                    setModeControllerValue(messageId, setMessage)
                is SetToggleControllerValueIncomingMessagePayload ->
                    setToggleControllerValue(messageId, setMessage)
            }
        }
        return false
    }

    private fun handleAdjustControllerValue(messageId: String, message: String): Boolean {
        parseJsonMessage<AdjustControllerValueIncomingMessagePayloadBase>(message)
            ?.let { adjustMessage ->
                tracer.adjustControllerValueMessageReceived(adjustMessage)

                return when (adjustMessage) {
                    is AdjustRangeControllerValueIncomingMessagePayload ->
                        adjustRangeControllerValue(messageId, adjustMessage)
                    is AdjustModeControllerValueIncomingMessagePayload ->
                        adjustModeControllerValue(messageId, adjustMessage)
                }
            }
        return false
    }

    /**
     * SetPowerControllerValue message handling: can be triggered by saying:
     * - "Turn on the light"
     * - "Turn off the custom device"
     */
    private fun setPowerControllerValue(
        messageId: String,
        message: SetPowerControllerValueIncomingMessagePayload
    ): Boolean =
        when (message.endpointId.toDeviceName()) {
            LIGHT_ID,
            CUSTOM_DEVICE_ID -> {
                val turnOn = message.turnOn
                tracer.d("Setting power value $turnOn for endpoint ${message.endpointId}.")
                sendSetControllerValueReply(messageId, true)
                true
            }
            // We are only interested in handling `SetPowerControllerValue` messages for the "light"
            // and the "custom_device" endpoint devices.
            // We return `false` for any other device, so that the message can be forwarded to other
            // CarControl message handlers.
            else -> false
        }

    /**
     * setRangeControllerValue message handling: can be triggered by saying:
     * - "Set the light brightness to 5"
     * - "Change the brightness of the light to maximum"
     */
    private fun setRangeControllerValue(
        messageId: String,
        message: SetRangeControllerValueIncomingMessagePayload
    ): Boolean =
        when (message.endpointId.toDeviceName()) {
            LIGHT_ID -> {
                val rangeValue = message.value
                val instanceId = message.instanceId
                tracer.d("Setting range value $rangeValue for light instance $instanceId.")
                sendSetControllerValueReply(messageId, true)
                true
            }
            // We are only interested in handling `SetRangeControllerValue` messages for the "light"
            // endpoint devices.
            // We return `false` for any other device, so that the message can be forwarded to other
            // CarControl message handlers.
            else -> false
        }

    /**
     * setModeControllerValue message handling: can be triggered by saying:
     * - "Set the light color to blue"
     * - "Change the color of the light to red"
     */
    private fun setModeControllerValue(
        messageId: String,
        message: SetModeControllerValueIncomingMessagePayload
    ): Boolean {
        return when (message.endpointId.toDeviceName()) {
            LIGHT_ID -> {
                val modeValue = message.value
                val instanceId = message.instanceId
                tracer.d("Setting mode value $modeValue for light instance $instanceId.")
                sendSetControllerValueReply(messageId, true)
                true
            }
            // We are only interested in handling `SetModeControllerValue` messages for the "light"
            // endpoint devices.
            // We return `false` for any other device, so that the message can be forwarded to other
            // CarControl message handlers.
            else -> false
        }
    }

    /**
     * setToggleControllerValue message handling: can be triggered by saying:
     * - "Turn on the light sensor"
     */
    private fun setToggleControllerValue(
        messageId: String,
        message: SetToggleControllerValueIncomingMessagePayload
    ): Boolean = when (message.endpointId.toDeviceName()) {
        LIGHT_ID -> {
            val turnOn = message.turnOn
            val instanceId = message.instanceId
            tracer.d("Setting toggle value $turnOn for light instance $instanceId.")
            sendSetControllerValueReply(messageId, true)
            true
        }
        // We are only interested in handling `setToggleControllerValue` messages for the
        // "light" endpoint devices.
        // We return `false` for any other device, so that the message can be forwarded to other
        // CarControl message handlers.
        else -> false
    }

    private fun sendSetControllerValueReply(messageId: String, success: Boolean) {
        aacsSender.sendReplyMessage(
            messageId,
            Topic.CAR_CONTROL,
            Action.CarControl.SET_CONTROLLER_VALUE,
            SetControllerValueOutgoingMessagePayload(success)
        )
    }

    /**
     * adjustRangeControllerValue message handling: can be triggered by saying
     * - "Increase the light brightness"
     * - "Decrease the light brightness by 2"
     */
    private fun adjustRangeControllerValue(
        messageId: String,
        message: AdjustRangeControllerValueIncomingMessagePayload
    ): Boolean = when (message.endpointId.toDeviceName()) {
        LIGHT_ID -> {
            val deltaValue = message.delta
            val instanceId = message.instanceId
            tracer.d("Adjusting light instance $instanceId by delta value $deltaValue.")
            sendAdjustControllerValueReply(messageId, true)
            true
        }
        // We are only interested in handling `AdjustRangeControllerValue` messages for the
        // "light" endpoint devices.
        // We return `false` for any other device, so that the message can be forwarded to other
        // CarControl message handlers.
        else -> false
    }

    /**
     * adjustModeControllerValue message handling: can be triggered by saying
     * - "Increase the light color"
     * Only available if the "ordered" field in the capability configuration is `true`.
     */
    private fun adjustModeControllerValue(
        messageId: String,
        message: AdjustModeControllerValueIncomingMessagePayload
    ): Boolean = when (message.endpointId.toDeviceName()) {
        LIGHT_ID -> {
            val deltaValue = message.delta
            val instanceId = message.instanceId
            tracer.i("Adjusting light instance $instanceId by delta value $deltaValue.")
            sendAdjustControllerValueReply(messageId, true)
            true
        }
        // We are only interested in handling `AdjustModeControllerValue` messages for the
        // "light" endpoint devices.
        // We return `false` for any other device, so that the message can be forwarded to other
        // CarControl message handlers.
        else -> false
    }

    private fun sendAdjustControllerValueReply(messageId: String, success: Boolean) {
        aacsSender.sendReplyMessage(
            messageId,
            Topic.CAR_CONTROL,
            Action.CarControl.ADJUST_CONTROLLER_VALUE,
            AdjustControllerValueOutgoingMessagePayload(success)
        )
    }

    /**
     * Returns the Alexa device name from an endpoint ID.
     * The format of an endpoint ID is either "zone.deviceName" for zoned properties or just
     * "deviceName" for zoneless properties.
     * For example:
     * driver.ac -> ac
     * rear.passenger.ac -> ac
     * steeringWheel -> steeringWheel
     */
    private fun String.toDeviceName() = substringAfterLast(".")

    /**
     * There is no CarControl event or request that needs to be sent periodically while AACS is
     * active, so [onEnginesStarted] can just be an empty implementation.
     */
    override suspend fun onEnginesStarted() {}

    /**
     * There is no CarControl event or request that needs to be sent periodically while AACS is
     * active, so [onEnginesStopped] can just be an empty implementation.
     */
    override suspend fun onEnginesStopped() {}

    interface CustomCarControlHandlerEvents : TraceEventListener {

        @TraceLogLevel(TraceLog.LogLevel.DEBUG)
        fun setControllerValueMessageReceived(payload: SetControllerValueIncomingMessagePayloadBase)

        @TraceLogLevel(TraceLog.LogLevel.DEBUG)
        fun adjustControllerValueMessageReceived(
            payload: AdjustControllerValueIncomingMessagePayloadBase
        )
    }

    private companion object {
        const val AACS_CONFIG_FILENAME = "aacs_customcarcontrol_config.json"
        const val CUSTOM_ASSETS_FILENAME = "custom_assets.json"

        const val JSON_VALUE_CONTROLLER_MODE = "MODE"
        const val JSON_VALUE_CONTROLLER_POWER = "POWER"
        const val JSON_VALUE_CONTROLLER_TOGGLE = "TOGGLE"
        const val JSON_VALUE_CONTROLLER_RANGE = "RANGE"
        const val JSON_KEY_CAPABILITY_TYPE = "capabilityType"

        const val LIGHT_ID = "light"
        const val CUSTOM_DEVICE_ID = "custom_device"
    }
}
