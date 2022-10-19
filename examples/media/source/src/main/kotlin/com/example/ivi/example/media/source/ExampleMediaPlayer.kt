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

import android.media.MediaPlayer
import android.os.CountDownTimer

/**
 * Fake implementation of a [android.media.MediaPlayer] with [CountDownTimer]
 * to be used by [ExampleMediaSourceSession].
 *
 * Not meant for production usage.
 */
internal class ExampleMediaPlayer(private val onPlaybackComplete: () -> Unit) : MediaPlayer() {

    private var currentTimer: CountDownTimer? = null

    private var isPaused = true

    var remainingTime: Long = 0L

    override fun start() {
        currentTimer?.cancel()
        currentTimer = createTimer(remainingTime)
        isPaused = false
        currentTimer?.start()
    }

    fun startFrom(remainingTime: Long) {
        this.remainingTime = remainingTime
        start()
    }

    override fun pause() {
        isPaused = true
        currentTimer?.cancel()
    }

    override fun stop() {
        currentTimer?.cancel()
    }

    private fun createTimer(durationMs: Long) =
        object : CountDownTimer(durationMs, 100) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isPaused) {
                    remainingTime = millisUntilFinished
                }
            }

            override fun onFinish() {
                onPlaybackComplete()
            }
        }
}
