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

package com.example.ivi.example.telephony.functionaltest.integrationtest

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Instrumentation
import android.telecom.DisconnectCause
import android.telecom.DisconnectCause.REMOTE
import android.telecom.TelecomManager
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ivi.example.telephony.customconnection.CustomConnectionFacade
import com.tomtom.ivi.platform.telecom.api.common.model.CallState
import com.tomtom.ivi.platform.tools.api.testing.functional.util.waitForNonUi
import com.tomtom.tools.android.testing.functional.TtFunctionalTestCase
import com.tomtom.tools.android.testing.functional.util.withIdleMainThread
import com.tomtom.tools.android.testing.functional.util.withMainThread
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class CustomConnectionTest : TtFunctionalTestCase() {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    private val customConnectionFacade = CustomConnectionFacade(instrumentation.targetContext)

    private val application = instrumentation.targetContext.applicationContext as Application

    private lateinit var defaultDialer: String

    @SuppressLint("VisibleForTests")
    @Before
    fun registerDebugPhoneAccount() {
        with(instrumentation) {
            defaultDialer = setDefaultDialerForAutomationTesting(this)
            customConnectionFacade.registerCustomPhoneAccount()
        }
    }

    @SuppressLint("VisibleForTests")
    @After
    fun unregisterDebugPhoneAccount() {
        with(instrumentation) {
            withIdleMainThread {
                runBlocking { customConnectionFacade.unregisterCustomPhoneAccount() }
            }
            setDefaultDialerForAutomationTesting(this, defaultDialer)
        }
    }

    @Test
    fun incomingCallIsCreatedProperlyAndAnswered() {
        // GIVEN there are no ongoing calls
        assertFalse(customConnectionFacade.isInCall())

        // WHEN an incoming call is done
        // API classes that are using LiveData should be called from the main thread
        withMainThread {
            customConnectionFacade.createIncomingCall(phoneNumber = PHONE_NUMBER)
        }

        // AND the CustomConnectionService is ready
        waitForConnectionServiceToBeReady()

        // THEN there is an incoming call
        assertTrue(
            waitForNonUi(CONNECTION_SERVICE_TIMEOUT_MS) {
                customConnectionFacade.isInCall()
            }
        )

        assertTrue(
            withMainThread {
                customConnectionFacade.getCallState(PHONE_NUMBER) == CallState.RINGING
            }
        )

        // WHEN incoming call is accepted
        customConnectionFacade.applyCallState(PHONE_NUMBER, CallState.ACTIVE, null)

        // THEN verify call state
        assertTrue(
            waitForNonUi(CONNECTION_SERVICE_TIMEOUT_MS) {
                customConnectionFacade.getCallState(PHONE_NUMBER) == CallState.ACTIVE
            }
        )
    }

    @Test
    fun outgoingCallIsCreatedProperlyThenGetsDisconnected() {
        // GIVEN there are no ongoing calls
        assertFalse(customConnectionFacade.isInCall())

        // WHEN an outgoing call is done
        // API classes that are using LiveData should be called from the main thread
        withMainThread {
            customConnectionFacade.createOutgoingCall(phoneNumber = PHONE_NUMBER)
        }
        // AND the CustomConnectionService is ready,
        waitForConnectionServiceToBeReady()

        // THEN there is an ongoing call
        assertTrue(
            waitForNonUi(CONNECTION_SERVICE_TIMEOUT_MS) {
                customConnectionFacade.isInCall()
            }
        )

        assertTrue(
            withMainThread {
                customConnectionFacade.getCallState(PHONE_NUMBER) == CallState.DIALING
            }
        )

        // WHEN call is disconnected
        customConnectionFacade.applyCallState(
            PHONE_NUMBER,
            CallState.DISCONNECTED,
            DisconnectCause(REMOTE)
        )

        // THEN verify call is removed
        assertTrue(
            waitForNonUi(CONNECTION_SERVICE_TIMEOUT_MS) {
                customConnectionFacade.getCallState(PHONE_NUMBER) == null
            }
        )
    }

    private fun setDefaultDialerForAutomationTesting(
        instrumentation: Instrumentation,
        packageName: String = instrumentation.targetContext.applicationContext.packageName
    ): String {
        val telecomManager = instrumentation
            .targetContext.getSystemService(Activity.TELECOM_SERVICE) as TelecomManager
        val defaultDialer = telecomManager.defaultDialerPackage
        if (!isDefaultDialer()) {
            instrumentation.uiAutomation
                .executeShellCommand("telecom set-default-dialer $packageName")
                .use {
                    BufferedReader(InputStreamReader(FileInputStream(it.fileDescriptor))).readLine()
                }
            check(telecomManager.defaultDialerPackage == packageName)
        }
        return defaultDialer!!
    }

    private fun isDefaultDialer(): Boolean {
        val telecomManager =
            application.getSystemService(Activity.TELECOM_SERVICE) as TelecomManager?
        requireNotNull(telecomManager)
        return application.packageName == telecomManager.defaultDialerPackage
    }

    private fun waitForConnectionServiceToBeReady() =
        assertTrue(
            waitForNonUi(CONNECTION_SERVICE_TIMEOUT_MS) {
                customConnectionFacade.isCustomConnectionServiceReady()
            }
        )

    companion object {
        const val PHONE_NUMBER = "+12345678"
        const val CONNECTION_SERVICE_TIMEOUT_MS = 10_000L
    }
}
