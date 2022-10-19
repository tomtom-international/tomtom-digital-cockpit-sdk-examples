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

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telecom.DisconnectCause
import android.telecom.PhoneAccount
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import com.tomtom.ivi.platform.telecom.api.common.model.CallState
import com.tomtom.ivi.platform.telecom.api.common.utils.toPhoneUri
import com.tomtom.kotlin.traceevents.TraceEventListener
import com.tomtom.kotlin.traceevents.TraceLog
import com.tomtom.kotlin.traceevents.TraceLogLevel
import com.tomtom.kotlin.traceevents.Tracer

/**
 * Helper class to simulate incoming and outgoing calls. It is also able to change the current
 * call's state. Simulated calls can be created after registering a custom phone account in the
 * system.
 *
 * Note that the helper classes ([CustomConnectionServiceHolder] and [CustomConnectionService]) have
 * to be part of [com.example.ivi.example.telephony.customconnection] package. Otherwise a
 * SecurityException is thrown due to this restriction when registering the phone account:
 * https://developer.android.com/reference/kotlin/android/telecom/TelecomManager#registerphoneaccount
 */
class CustomConnectionFacade(private val context: Context) {

    private val telecomManager by lazy {
        context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
    }

    private val customPhoneAccount = createCustomPhoneAccount(context)

    /**
     * Registers the custom phone account in the system. Needed before calling [createIncomingCall]
     * or [createOutgoingCall].
     *
     * Caller is responsible for unregistering the phone account with [unregisterCustomPhoneAccount].
     * @see [unregisterCustomPhoneAccount].
     * @return `true` if the phone account is registered, `false` otherwise.
     */
    fun registerCustomPhoneAccount(): Boolean {
        if (isCustomPhoneAccountRegisteredAsSelfManagedAccount()) return true

        telecomManager.registerPhoneAccount(customPhoneAccount)
        return isCustomPhoneAccountRegisteredAsSelfManagedAccount().apply {
            tracer.onRegisteredCustomPhoneAccount(this)
        }
    }

    /**
     * Unregisters the custom phone account from the system. The recent calls related to the custom
     * phone account will be deleted as well.
     *
     * @return `true` if the phone account is unregistered, `false` otherwise.
     */
    fun unregisterCustomPhoneAccount(): Boolean {
        if (!isCustomPhoneAccountRegisteredAsSelfManagedAccount()) return true

        CustomConnectionServiceHolder.withCustomConnectionService {
            // Clean and destroy all pending connections if there are any.
            it.clearAllConnections()
        }
        telecomManager.unregisterPhoneAccount(customPhoneAccount.accountHandle)
        return !isCustomPhoneAccountRegisteredAsSelfManagedAccount().apply {
            tracer.onUnregisteredCustomPhoneAccount(this)
        }
    }

    /**
     * Creates an incoming call using the [customPhoneAccount].
     *
     * The custom phone account needs to be registered first before calling this method.
     * @see [registerCustomPhoneAccount].
     *
     * @param phoneNumber The phone number to call. Should not be blank or empty.
     */
    fun createIncomingCall(phoneNumber: String) {
        require(phoneNumber.isNotBlank())
        require(isCustomPhoneAccountRegisteredAsSelfManagedAccount())
        val extras = Bundle()
        val uri = phoneNumber.toPhoneUri().uri
        extras.putParcelable(TelecomManager.EXTRA_INCOMING_CALL_ADDRESS, uri)
        telecomManager.addNewIncomingCall(customPhoneAccount.accountHandle, extras)
        tracer.onCreatedIncomingCall(uri)
    }

    /**
     * Creates an outgoing call using the [customPhoneAccount].
     *
     * The custom phone account needs to be registered first before calling this method.
     * @see [registerCustomPhoneAccount].
     * Requires [Manifest.permission.MANAGE_OWN_CALLS].
     *
     * @param phoneNumber The phone number for the outgoing call. Should not be blank or empty.
     */
    fun createOutgoingCall(phoneNumber: String) {
        require(phoneNumber.isNotBlank())
        require(isCustomPhoneAccountRegisteredAsSelfManagedAccount())
        val extras = Bundle()
        val uri = phoneNumber.toPhoneUri().uri
        extras.putParcelable(
            TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE,
            customPhoneAccount.accountHandle
        )

        if (context.checkSelfPermission(Manifest.permission.MANAGE_OWN_CALLS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            tracer.permissionMissing(Manifest.permission.MANAGE_OWN_CALLS)
            return
        }
        telecomManager.placeCall(uri, extras)
        tracer.onCreatedOutgoingCall(phoneNumber.toPhoneUri().uri)
    }

    /**
     * Checks if the connection service is ready.
     *
     * The connection will be ready if at least one call is created. The phone calls can be created
     * by calling [createIncomingCall], [createOutgoingCall], or directly calling
     * [TelecomManager.placeCall] with [TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE] and
     * [customPhoneAccount].
     *
     * Should be called before calling [applyCallState] or [getCallState] when creating the first
     * call.
     */
    fun isCustomConnectionServiceReady() =
        CustomConnectionServiceHolder.isCustomConnectionServiceReady()

    /**
     * Checks whether there is an ongoing phone call (can be in dialing, ringing, active or holding
     * states).
     *
     * It is `true` if there is an ongoing call in a managed [CustomConnectionService], ` false`
     * otherwise.
     */
    @SuppressLint("MissingPermission")
    fun isInCall() = telecomManager.isInCall

    /**
     * Updates a call state.
     *
     * The custom phone account needs to be registered first before calling this method.
     * The connection service needs to be ready before calling this method.
     * @see [isCustomConnectionServiceReady].
     *
     * The connection will be ready if at least one call is created. The calls can be created by
     * calling [createIncomingCall], [createOutgoingCall], or directly calling
     * [TelecomManager.placeCall] with [TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE] and
     * [customPhoneAccount].
     *
     * @param phoneNumber The phone number for the call.
     * @param callState The new call state.
     * @param disconnectCause The disconnection cause used for DISCONNECTED state.
     */
    fun applyCallState(
        phoneNumber: String,
        callState: CallState,
        disconnectCause: DisconnectCause?
    ) {
        require(isCustomPhoneAccountRegisteredAsSelfManagedAccount())
        require(isCustomConnectionServiceReady())
        CustomConnectionServiceHolder.withCustomConnectionService {
            it.changeConnectionState(phoneNumber, callState, disconnectCause)
        }
    }

    /**
     * Returns a call state for the given [phoneNumber]. If the call is disconnected, it can be
     * already destroyed and `null` can be returned.
     *
     * @param phoneNumber The phone number for the call.
     * @return The call state, if the call exists else `null`.
     */
    fun getCallState(
        phoneNumber: String
    ): CallState? {
        return CustomConnectionServiceHolder.withCustomConnectionService {
            it.getConnectionState(phoneNumber)
        }
    }

    /**
     * Returns true if the [customPhoneAccount] is registered in the system else return false.
     * Require [Manifest.permission.READ_PHONE_STATE].
     */
    private fun isCustomPhoneAccountRegisteredAsSelfManagedAccount(): Boolean {
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            tracer.permissionMissing(Manifest.permission.READ_PHONE_STATE)
            return false
        }
        return telecomManager.selfManagedPhoneAccounts.contains(customPhoneAccount.accountHandle)
    }

    companion object {
        private val tracer = Tracer.Factory.create<CustomConnectionHelperEvents>(this)

        /**
         * This ID is used to select the custom self-managed phone account when registered.
         */
        const val IVI_CUSTOM_PHONE_ACCOUNT_ID = "IviCustomPhoneAccountId"

        interface CustomConnectionHelperEvents : TraceEventListener {
            fun onRegisteredCustomPhoneAccount(b: Boolean)
            fun onUnregisteredCustomPhoneAccount(b: Boolean)
            fun onCreatedOutgoingCall(uri: Uri)
            fun onCreatedIncomingCall(uri: Uri)

            @TraceLogLevel(TraceLog.LogLevel.ERROR)
            fun permissionMissing(permission: String)
        }

        /**
         * Creates the custom phone account used to create custom calls.
         */
        fun createCustomPhoneAccount(context: Context): PhoneAccount = PhoneAccount.builder(
            PhoneAccountHandle(
                ComponentName(
                    context,
                    CustomConnectionService::class.qualifiedName!!
                ),
                IVI_CUSTOM_PHONE_ACCOUNT_ID
            ),
            "IviCustomPhoneAccountLabel"
        ).setCapabilities(PhoneAccount.CAPABILITY_SELF_MANAGED).build()
    }
}
