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
import kotlinx.serialization.Serializable

internal class CustomEqualizerControllerHandlerService(
    iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider,
) : AlexaHandlerServiceBase(iviServiceHostContext, serviceIdProvider) {

    private val tracer =
        iviServiceHostContext.createTracer<CustomEqualizerControllerHandlerEvents> { this }

    private val aacsSender = AacsSenderWrapper(iviServiceHostContext)

    /**
     * Data class representation of the
     * [EqualizerController](https://alexa.github.io/alexa-auto-sdk/docs/aasb/alexa/EqualizerController)
     * AASB messages.
     */

    @Serializable
    data class EqualizerBandLevel(
        val band: EqualizerBand,
        val level: Int,
    )

    enum class EqualizerBand {
        BASS,
        MIDRANGE,
        TREBLE,
    }

    @Serializable
    data class SetBandLevelsIncomingMessagePayload(
        val bandLevels: List<EqualizerBandLevel>,
    )

    @Serializable
    data class GetBandLevelsOutgoingMessagePayload(
        val bandLevels: List<EqualizerBandLevel>,
    )

    private var bandLevels = listOf(
        EqualizerBandLevel(EqualizerBand.BASS, 0),
        EqualizerBandLevel(EqualizerBand.MIDRANGE, 0),
        EqualizerBandLevel(EqualizerBand.TREBLE, 0),
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

    override suspend fun onMessageReceived(message: AacsMessage): Boolean =
        when (message.action) {
            Action.EqualizerController.GET_BAND_LEVELS -> handleGetBandLevels(message.id)
            Action.EqualizerController.SET_BAND_LEVELS -> handleSetBandLevels(message.payload)
            // We are only interested in handling `GET_BAND_LEVELS` and `SET_BAND_LEVELS` incoming
            // messages. We return `false` for any other action, so that the message can be
            // forwarded to other EqualizerController message handlers.
            else -> false
        }

    private fun handleSetBandLevels(message: String): Boolean {
        parseJsonMessage<SetBandLevelsIncomingMessagePayload>(message)?.let { setMessage ->
            tracer.setBandLevelsMessageReceived(setMessage)
            bandLevels = setMessage.bandLevels
            return true
        }
        return false
    }

    private fun handleGetBandLevels(messageId: String): Boolean {
        val messageToSend = GetBandLevelsOutgoingMessagePayload(bandLevels)
        aacsSender.sendReplyMessage(
            messageId,
            Topic.EQUALIZER_CONTROLLER,
            Action.EqualizerController.GET_BAND_LEVELS,
            messageToSend,
        )
        tracer.sendingGetBandLevelsOutgoingMessage(messageToSend)
        return true
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
        fun setBandLevelsMessageReceived(message: SetBandLevelsIncomingMessagePayload)

        @TraceLogLevel(TraceLog.LogLevel.DEBUG)
        fun sendingGetBandLevelsOutgoingMessage(message: GetBandLevelsOutgoingMessagePayload)
    }
}
