/*
 * Copyright © 2022 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.media.source

import android.content.Context
import android.media.session.PlaybackState
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.tomtom.kotlin.traceevents.TraceEventListener
import com.tomtom.kotlin.traceevents.TraceLog
import com.tomtom.kotlin.traceevents.TraceLogLevel
import com.tomtom.kotlin.traceevents.Tracer

/**
 * Example media session to be used by [ExampleMediaSourceService].
 *
 * Not meant for production usage.
 */
internal class ExampleMediaSourceSession(
    context: Context,
    private val dataSource: ExampleDataSource,
    private val onBeforeStart: () -> Unit,
    private val onBeforeStop: () -> Unit,
) : MediaSessionCompat(
    context,
    ExampleMediaSourceService::class.simpleName!!
) {
    private val initialState: PlaybackStateCompat = PlaybackStateCompat.Builder()
        .setActions(
            PlaybackStateCompat.ACTION_PLAY or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
        )
        .setState(PlaybackStateCompat.STATE_NONE, 0, 1f, SystemClock.elapsedRealtime())
        .build()

    private val mediaPlayer: ExampleMediaPlayer by lazy {
        ExampleMediaPlayer(mediaSessionCallback::onFinishPlayback)
    }

    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {

        override fun onPlay() {
            tracer.onPlay()
            val currentMediaId = getCurrentMediaId() ?: return
            val currentPlaybackState = getCurrentPlaybackState() ?: return
            startPlayback(currentMediaId, currentPlaybackState, null)
        }

        override fun onPlayFromMediaId(mediaId: String?, extras: Bundle?) {
            tracer.onPlayFromMediaId(mediaId)
            if (mediaId == null) {
                return
            }
            startPlayback(mediaId, null, dataSource.getMetadata(mediaId)?.durationMs)
        }

        override fun onPause() {
            tracer.onPause()
            mediaPlayer.pause()
            updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
        }

        override fun onStop() {
            tracer.onStop()
            onBeforeStop()
            return onIdle()
        }

        override fun onSkipToPrevious() = skip {
            tracer.onSkipToPrevious()
            dataSource.getPreviousMetadata(it)?.id
        }

        override fun onSkipToNext() = skip {
            tracer.onSkipToNext()
            dataSource.getNextMetadata(it)?.id
        }

        override fun onCustomAction(action: String, extras: Bundle?) {
            tracer.onCustomAction(action)
            super.onCustomAction(action, extras)
        }

        fun onFinishPlayback() =
            getCurrentMediaId()
                ?.let { dataSource.getNextMetadata(it) }
                ?.let { onSkipToNext() }
                ?: onIdle()

        fun onIdle() {
            tracer.onIdle()
            updatePlaybackState(PlaybackStateCompat.STATE_STOPPED, currentPosition = 0)
            mediaPlayer.stop()
        }

        private fun startPlayback(
            mediaId: String,
            playbackState: PlaybackStateCompat?,
            duration: Long?
        ) {
            onBeforeStart()

            dataSource.getMetadata(mediaId)?.let { setMetadata(it) }

            updatePlaybackState(
                PlaybackStateCompat.STATE_PLAYING,
                currentPosition = playbackState?.position ?: 0
            )
            when (duration) {
                null -> mediaPlayer.start()
                else -> mediaPlayer.startFrom(duration)
            }
        }

        private fun skip(newMediaIdGenerator: (currentMediaId: String) -> String?) {
            val currentMediaId = getCurrentMediaId() ?: return
            val newMediaId = newMediaIdGenerator(currentMediaId) ?: return
            onPlayFromMediaId(newMediaId, null)
        }
    }

    init {
        tracer.onInit(this.toString())

        setPlaybackState(initialState)
        setCallback(mediaSessionCallback)
        isActive = true
    }

    override fun release() {
        tracer.onRelease(this.toString())

        isActive = false
        mediaPlayer.stop()
        mediaSessionCallback.onStop()
        setQueue(emptyList())
        setQueueTitle(null)
        setMetadata(null)
        setPlaybackState(initialState)
        updatePlaybackState()

        super.release()
    }

    private fun getAvailablePlaybackActions(switchingToState: Int): Long {
        var newActions: Long = when (switchingToState) {
            PlaybackStateCompat.STATE_PLAYING -> (
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_PAUSE
                    or PlaybackStateCompat.ACTION_STOP
                )
            PlaybackStateCompat.STATE_PAUSED -> (
                PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_STOP
                )
            else -> initialState.actions
        }
        getCurrentMediaId()?.let { currentMediaId ->
            if (dataSource.getPreviousMetadata(currentMediaId) != null) {
                newActions = newActions or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            }
            if (dataSource.getNextMetadata(currentMediaId) != null) {
                newActions = newActions or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            }
        }
        return newActions
    }

    private fun updatePlaybackState(playbackState: Int? = null, currentPosition: Long? = null) {
        val state = playbackState
            ?: getCurrentPlaybackState()?.state
            ?: PlaybackState.STATE_STOPPED
        val position = currentPosition
            ?: getCurrentPlaybackState()?.position ?: 0

        with(PlaybackStateCompat.Builder()) {
            setActions(getAvailablePlaybackActions(state))
            setState(state, position, 1f, SystemClock.elapsedRealtime())
            setPlaybackState(build())
        }
    }

    private fun getCurrentMediaId() =
        controller?.metadata?.id

    private fun getCurrentPlaybackState() =
        controller?.playbackState

    private interface ExampleMediaSourceSessionEvents : TraceEventListener {
        @TraceLogLevel(TraceLog.LogLevel.DEBUG)
        fun onInit(session: String)

        @TraceLogLevel(TraceLog.LogLevel.INFO)
        fun onPlay()

        @TraceLogLevel(TraceLog.LogLevel.INFO)
        fun onPlayFromMediaId(mediaId: String?)

        @TraceLogLevel(TraceLog.LogLevel.INFO)
        fun onPause()

        @TraceLogLevel(TraceLog.LogLevel.INFO)
        fun onIdle()

        @TraceLogLevel(TraceLog.LogLevel.INFO)
        fun onStop()

        @TraceLogLevel(TraceLog.LogLevel.INFO)
        fun onCustomAction(action: String)

        @TraceLogLevel(TraceLog.LogLevel.INFO)
        fun onSkipToPrevious()

        @TraceLogLevel(TraceLog.LogLevel.INFO)
        fun onSkipToNext()

        @TraceLogLevel(TraceLog.LogLevel.DEBUG)
        fun onRelease(session: String)
    }

    companion object {
        private val tracer = Tracer.Factory.create<ExampleMediaSourceSessionEvents>(this)
    }
}
