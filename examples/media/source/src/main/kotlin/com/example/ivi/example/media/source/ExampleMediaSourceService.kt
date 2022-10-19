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

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.media.MediaBrowserServiceCompat
import com.tomtom.kotlin.traceevents.TraceEventListener
import com.tomtom.kotlin.traceevents.TraceLog
import com.tomtom.kotlin.traceevents.TraceLogLevel
import com.tomtom.kotlin.traceevents.Tracer

/**
 * Example Media Source Service.
 *
 * This service provides static media to be browsed and played (without sound) via the
 * Android Automotive Media framework.
 * It can be used via the TomTom Digital Cockpit Media Plugin and via the standard AOSP Automotive
 * media browser.
 *
 * Not meant for production usage.
 */
internal class ExampleMediaSourceService : MediaBrowserServiceCompat() {

    private lateinit var dataSource: ExampleDataSource
    private lateinit var mediaSession: ExampleMediaSourceSession

    override fun onCreate() {
        super.onCreate()
        dataSource = ExampleDataSource()
        mediaSession = ExampleMediaSourceSession(
            applicationContext,
            dataSource,
            ::onMediaSessionStart,
            ::onMediaSessionStop
        ).apply {
            setSessionToken(sessionToken)
        }
        tracer.onCreate()
    }

    override fun onDestroy() {
        tracer.onDestroy(this.toString())
        destroy()
        super.onDestroy()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        val root = dataSource.getRoot()
        tracer.onGetRoot(clientPackageName, clientUid, root)
        return BrowserRoot(root, null)
    }

    override fun onLoadChildren(parentMediaId: String, result: Result<List<MediaItem>>) {
        val children = dataSource.getChildren(parentMediaId)
        result.sendResult(children)
        tracer.onLoadChildrenComplete(parentMediaId, children?.size)
    }

    private fun destroy() {
        mediaSession.release()
    }

    private fun onMediaSessionStart() =
        startService(Intent(applicationContext, ExampleMediaSourceService::class.java))

    private fun onMediaSessionStop() =
        stopSelf()

    private companion object {
        private val tracer = Tracer.Factory.create<ExampleMediaSourceServiceEvents>(this)

        interface ExampleMediaSourceServiceEvents : TraceEventListener {
            @TraceLogLevel(TraceLog.LogLevel.INFO)
            fun onCreate()

            @TraceLogLevel(TraceLog.LogLevel.DEBUG)
            fun onDestroy(service: String)

            @TraceLogLevel(TraceLog.LogLevel.DEBUG)
            fun onGetRoot(clientPackageName: String, clientUid: Int, rootId: String)

            @TraceLogLevel(TraceLog.LogLevel.DEBUG)
            fun onLoadChildrenComplete(parentMediaId: String, childrenCount: Int?)
        }
    }
}
