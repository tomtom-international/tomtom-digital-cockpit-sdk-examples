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

import android.app.Application
import android.util.Log
import com.tomtom.kotlin.traceevents.TraceLog

/**
 * Enables logging on the Android LogCat instead of on `System.out`.
 *
 * Not meant for production usage.
 */
internal class ExampleMediaSourceApplication : Application() {
    override fun onCreate() {
        configureTracer()
        super.onCreate()
    }

    private fun configureTracer() {
        // Use the default Android logger.
        TraceLog.setLogger(object : TraceLog.Logger {
            override fun log(
                logLevel: TraceLog.LogLevel,
                tag: String,
                message: String,
                e: Throwable?
            ) {
                if (e == null) {
                    when (logLevel) {
                        TraceLog.LogLevel.VERBOSE -> Log.v(tag, message)
                        TraceLog.LogLevel.DEBUG -> Log.d(tag, message)
                        TraceLog.LogLevel.INFO -> Log.i(tag, message)
                        TraceLog.LogLevel.WARN -> Log.w(tag, message)
                        TraceLog.LogLevel.ERROR -> Log.e(tag, message)
                    }
                } else {
                    when (logLevel) {
                        TraceLog.LogLevel.VERBOSE -> Log.v(tag, message, e)
                        TraceLog.LogLevel.DEBUG -> Log.d(tag, message, e)
                        TraceLog.LogLevel.INFO -> Log.i(tag, message, e)
                        TraceLog.LogLevel.WARN -> Log.w(tag, message, e)
                        TraceLog.LogLevel.ERROR -> Log.e(tag, message, e)
                    }
                }
            }
        })
    }
}
