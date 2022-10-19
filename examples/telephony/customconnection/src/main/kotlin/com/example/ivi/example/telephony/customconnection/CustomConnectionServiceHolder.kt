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
package com.example.ivi.example.telephony.customconnection

import android.telecom.TelecomManager
import com.tomtom.kotlin.traceevents.TraceEventListener
import com.tomtom.kotlin.traceevents.Tracer

/**
 * [CustomConnectionServiceHolder] creates and stores an instance of [CustomConnectionService]. It
 * is used by [CustomConnectionService] and [CustomConnectionFacade].
 */
internal object CustomConnectionServiceHolder {
    /**
     * The custom connection service used for communications.
     *
     * This will be set when the [TelecomManager] binds to the [CustomConnectionService], which will
     * happen when creating an incoming or outgoing call. If there is no more ongoing call, the
     * [TelecomManager] unbinds the [CustomConnectionService] and this shall be null.
     */
    @Volatile
    private var customConnectionService: CustomConnectionService? = null

    @Synchronized
    fun setCustomConnectionService(customConnectionService: CustomConnectionService?) {
        this.customConnectionService = customConnectionService
        tracer.onUpdatedCustomConnectionService(customConnectionService)
    }

    /**
     * Checks if the connection service is ready.
     */
    @Synchronized
    fun isCustomConnectionServiceReady() = (customConnectionService != null)

    /**
     * If the [customConnectionService] is ready, execute a code [block] within a synchronized scope.
     * @return The result of the block or `null` if the [customConnectionService] is not ready.
     */
    @Synchronized
    fun <T> withCustomConnectionService(block: (service: CustomConnectionService) -> T): T? {
        return customConnectionService?.let {
            tracer.onStartedCustomConnectionService()
            block(it).also {
                tracer.onEndedCustomConnectionService()
            }
        }
    }

    private val tracer = Tracer.Factory.create<CustomConnectionServiceHolderEvents>(this)

    interface CustomConnectionServiceHolderEvents : TraceEventListener {
        fun onUpdatedCustomConnectionService(customConnectionService: CustomConnectionService?)
        fun onStartedCustomConnectionService()
        fun onEndedCustomConnectionService()
    }
}
