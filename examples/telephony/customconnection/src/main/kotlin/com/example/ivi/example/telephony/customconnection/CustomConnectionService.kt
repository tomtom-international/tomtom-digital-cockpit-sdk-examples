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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.telecom.Connection
import android.telecom.Connection.STATE_ACTIVE
import android.telecom.Connection.STATE_DIALING
import android.telecom.Connection.STATE_DISCONNECTED
import android.telecom.Connection.STATE_HOLDING
import android.telecom.Connection.STATE_INITIALIZING
import android.telecom.Connection.STATE_NEW
import android.telecom.Connection.STATE_PULLING_CALL
import android.telecom.Connection.STATE_RINGING
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.ServiceLifecycleDispatcher
import com.tomtom.ivi.platform.telecom.api.common.model.CallState
import com.tomtom.ivi.platform.telecom.api.common.utils.toPhoneUri
import com.tomtom.kotlin.traceevents.TraceEventListener
import com.tomtom.kotlin.traceevents.Tracer

/**
 * A custom connection service for communication (outgoing and incoming calls). This service
 * behaves like a standard [ConnectionService].
 */
internal class CustomConnectionService : ConnectionService(), LifecycleOwner {
    /**
     * Needed dispatcher as it is not possible to inherit from [LifecycleService] directly.
     */
    private val dispatcher = ServiceLifecycleDispatcher(this)

    override fun onCreate() {
        dispatcher.onServicePreSuperOnCreate()
        CustomConnectionServiceHolder.setCustomConnectionService(this)
        super.onCreate()
    }

    override fun onDestroy() {
        dispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
        CustomConnectionServiceHolder.setCustomConnectionService(null)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        dispatcher.onServicePreSuperOnBind()
        return super.onUnbind(intent)
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?,
    ): Connection? {
        if (uriAlreadyHasAConnection(request?.address)) {
            tracer.uriConnectionAlreadyCreated(request?.address)
            return null
        }
        return OutgoingCustomConnection(request).apply {
            setInitializing()
            setDialing()
            tracer.onOutgoingConnectionCreated(this)
        }
    }

    private fun uriAlreadyHasAConnection(uri: Uri?) = allConnections.any { it.address == uri }

    override fun onCreateOutgoingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?,
    ) {
        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
        tracer.onCreatedOutgoingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?,

    ): Connection? {
        if (uriAlreadyHasAConnection(request?.address)) {
            tracer.uriConnectionAlreadyCreated(request?.address)
            return null
        }
        return IncomingCustomConnection(request).apply {
            setAddress(request?.address, TelecomManager.PRESENTATION_ALLOWED)
            setInitializing()
            setRinging()
            tracer.onIncomingConnectionCreated(this)
        }
    }

    override fun onCreateIncomingConnectionFailed(
        connectionManagerPhoneAccount: PhoneAccountHandle?,
        request: ConnectionRequest?,
    ) {
        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request)
        tracer.onCreatedIncomingConnectionFailed(connectionManagerPhoneAccount, request)
    }

    /**
     * Changes the state of all calls with the specific [phoneNumber].
     *
     * @param phoneNumber The phone number for the connection.
     * @param callState The new call state.
     * @param disconnectCause The disconnection cause used for DISCONNECTED state.
     */
    internal fun changeConnectionState(
        phoneNumber: String,
        callState: CallState,
        disconnectCause: DisconnectCause?,
    ) {
        val uri = phoneNumber.toPhoneUri().uri
        allConnections.filter { it.address == uri }.forEach {
            when (callState) {
                CallState.ANSWERING,
                CallState.ACTIVE,
                -> it.setActive()
                CallState.CONNECTING -> it.setInitializing()
                CallState.DIALING -> it.setDialing()
                CallState.DISCONNECTED -> {
                    it.setDisconnected(disconnectCause)
                    it.destroy()
                }
                CallState.DISCONNECTING -> Unit
                CallState.HOLDING -> it.setOnHold()
                CallState.RINGING -> it.setRinging()
            }
        }
    }

    /**
     * Returns the state of the connection with the specific [phoneNumber].
     *
     * @param phoneNumber The phone number for the connection.
     * @return The state of the connection or `null` if no connection found for this [phoneNumber].
     */
    internal fun getConnectionState(
        phoneNumber: String,
    ): CallState? {
        val uri = phoneNumber.toPhoneUri().uri
        return allConnections.firstOrNull { it.address == uri }?.let {
            nativeCallStateToIviCallSate(it.state)
        }
    }

    /**
     * Disconnects and destroys all connections.
     */
    internal fun clearAllConnections() {
        tracer.clearingAllConnections()
        allConnections.forEach {
            it.setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
            it.destroy()
        }
    }

    override val lifecycle: Lifecycle = dispatcher.lifecycle

    inner class OutgoingCustomConnection(
        request: ConnectionRequest?,
    ) : CustomConnection(request) {
        override fun getCallLogType() = CallLog.Calls.OUTGOING_TYPE
    }

    inner class IncomingCustomConnection(
        request: ConnectionRequest?,
    ) : CustomConnection(request) {
        override fun getCallLogType() =
            if (activated) CallLog.Calls.INCOMING_TYPE else CallLog.Calls.MISSED_TYPE
    }

    abstract inner class CustomConnection(
        private val request: ConnectionRequest?,
    ) : Connection() {

        protected var activated = false
        private val id = ++connectionCounter

        init {
            setAddress(request?.address, TelecomManager.PRESENTATION_ALLOWED)
            connectionProperties =
                PROPERTY_SELF_MANAGED or CAPABILITY_HOLD or CAPABILITY_SUPPORT_HOLD
        }

        protected abstract fun getCallLogType(): Int

        override fun onStateChanged(state: Int) {
            super.onStateChanged(state)
            connectionTracer.onStateChanged(address, nativeCallStateToIviCallSate(state))
            when (state) {
                STATE_ACTIVE -> activated = true
                STATE_DISCONNECTED,
                STATE_INITIALIZING,
                STATE_NEW,
                STATE_RINGING,
                STATE_DIALING,
                STATE_HOLDING,
                STATE_PULLING_CALL,
                -> Unit
                else -> Unit
            }
        }

        override fun onAnswer() {
            super.onAnswer()
            connectionTracer.onAnswered(address)
            allConnections.filter {
                it.address != request?.address
            }.forEach {
                // Put other calls on hold.
                it.setOnHold()
            }
            setActive()
        }

        override fun onDisconnect() {
            super.onDisconnect()
            connectionTracer.onDisconnected(address)
            setDisconnected(DisconnectCause(DisconnectCause.REMOTE))
            destroy()
        }

        override fun onReject() {
            super.onReject()
            connectionTracer.onRejected(address)
            setDisconnected(DisconnectCause(DisconnectCause.REJECTED))
            destroy()
        }

        override fun onCallEvent(event: String?, extras: Bundle?) {
            super.onCallEvent(event, extras)
            connectionTracer.onCalledEvent(address, event, extras)
        }

        override fun onAbort() {
            super.onAbort()
            connectionTracer.onAborted(address)
            setDisconnected(DisconnectCause(DisconnectCause.UNKNOWN))
            destroy()
        }

        override fun onHold() {
            super.onHold()
            connectionTracer.onHold(address)
            setOnHold()
        }

        override fun onUnhold() {
            super.onUnhold()
            connectionTracer.onUnhold(address)
            allConnections.filter {
                it.address != request?.address
            }.forEach {
                // Put on hold other calls for the same Uri.
                it.setOnHold()
            }
            setActive()
        }

        override fun toString() = StringBuilder().append("[${this.javaClass.simpleName}={")
            .append("id=$id")
            .append(", state=$state")
            .append(", address=$address")
            .append(", request=$request")
            .append("}]").toString()
    }

    companion object {
        private var connectionCounter = 0L
        private val tracer = Tracer.Factory.create<CustomConnectionServiceEvents>(this)
        private val connectionTracer = Tracer.Factory.create<CustomConnectionEvents>(this)

        @Throws(IllegalArgumentException::class)
        private fun nativeCallStateToIviCallSate(nativeCallState: Int) =
            when (nativeCallState) {
                STATE_INITIALIZING -> CallState.CONNECTING
                STATE_NEW -> CallState.CONNECTING
                STATE_RINGING -> CallState.RINGING
                STATE_DIALING -> CallState.DIALING
                STATE_ACTIVE -> CallState.ACTIVE
                STATE_HOLDING -> CallState.HOLDING
                STATE_DISCONNECTED -> CallState.DISCONNECTED
                STATE_PULLING_CALL -> CallState.CONNECTING
                else -> throw IllegalArgumentException(
                    "Native call state not supported, nativeCallState='$nativeCallState'",
                )
            }
    }

    interface CustomConnectionServiceEvents : TraceEventListener {
        fun onIncomingConnectionCreated(connection: Connection)
        fun onOutgoingConnectionCreated(connection: Connection)

        fun onCreatedOutgoingConnectionFailed(
            connectionManagerPhoneAccount: PhoneAccountHandle?,
            request: ConnectionRequest?,
        )

        fun onCreatedIncomingConnectionFailed(
            connectionManagerPhoneAccount: PhoneAccountHandle?,
            request: ConnectionRequest?,
        )

        fun clearingAllConnections()
        fun uriConnectionAlreadyCreated(address: Uri?)
    }

    interface CustomConnectionEvents : TraceEventListener {
        fun onStateChanged(uri: Uri, callState: CallState)
        fun onCalledEvent(uri: Uri, event: String?, extras: Bundle?)
        fun onAnswered(uri: Uri)
        fun onDisconnected(uri: Uri)
        fun onRejected(uri: Uri)
        fun onAborted(uri: Uri)
        fun onHold(uri: Uri)
        fun onUnhold(uri: Uri)
    }
}
