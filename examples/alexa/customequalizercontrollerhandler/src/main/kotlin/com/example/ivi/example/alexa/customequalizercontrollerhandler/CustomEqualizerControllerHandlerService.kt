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

package com.example.ivi.example.alexa.customequalizercontrollerhandler

import com.amazon.aacsconstants.Action
import com.amazon.aacsconstants.Topic
import com.tomtom.ivi.platform.alexa.api.common.util.AacsSenderWrapper
import com.tomtom.ivi.platform.alexa.api.common.util.AasbMessageNoPayload
import com.tomtom.ivi.platform.alexa.api.common.util.Header
import com.tomtom.ivi.platform.alexa.api.common.util.createAasbReplyHeader
import com.tomtom.ivi.platform.alexa.api.common.util.parseAasbMessage
import com.tomtom.ivi.platform.alexa.api.service.alexahandler.AlexaHandlerService
import com.tomtom.ivi.platform.alexa.api.service.alexahandler.AlexaHandlerServiceBase
import com.tomtom.ivi.platform.framework.api.common.iviinstance.createTracer
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.kotlin.traceevents.TraceEventListener
import com.tomtom.kotlin.traceevents.TraceLog
import com.tomtom.kotlin.traceevents.TraceLogLevel
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class CustomEqualizerControllerHandlerService(
    iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider
) : AlexaHandlerServiceBase(iviServiceHostContext, serviceIdProvider) {

    private val tracer =
        iviServiceHostContext.createTracer<CustomEqualizerControllerHandlerEvents> { this }

    // Instantiate a JSON object which will be used to parse and encode the AASB JSON messages.
    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    private val aacsSender = AacsSenderWrapper(iviServiceHostContext)

    /**
     * Data class representation of the
     * [EqualizerController](https://alexa.github.io/alexa-auto-sdk/docs/aasb/alexa/EqualizerController)
     * AASB messages.
     */

    @Serializable
    data class EqualizerBandLevel(
        val band: EqualizerBand,
        val level: Int
    )

    enum class EqualizerBand {
        BASS,
        MIDRANGE,
        TREBLE
    }

    @Serializable
    data class SetBandLevelsIncomingMessage(
        val header: Header,
        val payload: SetBandLevelsIncomingMessagePayload
    )

    @Serializable
    data class SetBandLevelsIncomingMessagePayload(
        val bandLevels: List<EqualizerBandLevel>
    )

    @Serializable
    data class GetBandLevelsOutgoingMessage(
        val header: Header,
        val payload: GetBandLevelsOutgoingMessagePayload
    )

    @Serializable
    data class GetBandLevelsOutgoingMessagePayload(
        val bandLevels: List<EqualizerBandLevel>
    )

    private var bandLevels = listOf(
        EqualizerBandLevel(EqualizerBand.BASS, 0),
        EqualizerBandLevel(EqualizerBand.MIDRANGE, 0),
        EqualizerBandLevel(EqualizerBand.TREBLE, 0)
    )

    override fun onCreate() {
        super.onCreate()

        // This [AlexaHandlerService] implementation will handle EqualizerController AASB messages.
        topic = Topic.EQUALIZER_CONTROLLER

        // This [AlexaHandlerService] implementation has default priority, so CarControl AASB
        // messages will be forwarded to this handler first, before reaching the stock
        // TomTom Digital Cockpit EqualizerController message handler.
        priority = AlexaHandlerService.DEFAULT_HANDLER_PRIORITY

        // All required properties have been set, so the service is now ready.
        serviceReady = true
    }

    override suspend fun onMessageReceived(
        action: String,
        messageContents: String
    ): Boolean =
        when (action) {
            Action.EqualizerController.GET_BAND_LEVELS -> handleGetBandLevels(messageContents)
            Action.EqualizerController.SET_BAND_LEVELS -> handleSetBandLevels(messageContents)
            // We are only interested in handling `GET_BAND_LEVELS` and `SET_BAND_LEVELS` incoming
            // messages. We return `false` for any other action, so that the message can be
            // forwarded to other EqualizerController message handlers.
            else -> false
        }

    private fun handleSetBandLevels(message: String): Boolean {
        parseAasbMessage<SetBandLevelsIncomingMessage>(
            jsonParser,
            message
        )?.let { setMessage ->
            tracer.setBandLevelsMessageReceived(setMessage)
            bandLevels = setMessage.payload.bandLevels
            return true
        }
        return false
    }

    private fun handleGetBandLevels(message: String): Boolean {
        parseAasbMessage<AasbMessageNoPayload>(jsonParser, message)?.let {
            val messageToSend = GetBandLevelsOutgoingMessage(
                createAasbReplyHeader(
                    it.header.id,
                    Topic.EQUALIZER_CONTROLLER,
                    Action.EqualizerController.GET_BAND_LEVELS
                ),
                GetBandLevelsOutgoingMessagePayload(bandLevels)
            )
            aacsSender.sendMessage(
                jsonParser.encodeToString(messageToSend),
                Topic.EQUALIZER_CONTROLLER,
                Action.EqualizerController.GET_BAND_LEVELS
            )
            tracer.sendingGetBandLevelsOutgoingMessage(messageToSend)
            return true
        }
        return false
    }

    /**
     * There is no EqualizerController event or request that needs to be sent periodically while
     * AACS is active, so [onEnginesStarted] can just be an empty implementation.
     */
    override suspend fun onEnginesStarted() {}

    /**
     * There is no EqualizerController event or request that needs to be sent periodically while
     * AACS is active, so [onEnginesStopped] can just be an empty implementation.
     */
    override suspend fun onEnginesStopped() {}

    interface CustomEqualizerControllerHandlerEvents : TraceEventListener {
        @TraceLogLevel(TraceLog.LogLevel.DEBUG)
        fun aacsMessageReceived(msg: String)

        @TraceLogLevel(TraceLog.LogLevel.DEBUG)
        fun setBandLevelsMessageReceived(message: SetBandLevelsIncomingMessage)

        @TraceLogLevel(TraceLog.LogLevel.DEBUG)
        fun sendingGetBandLevelsOutgoingMessage(message: GetBandLevelsOutgoingMessage)
    }
}
