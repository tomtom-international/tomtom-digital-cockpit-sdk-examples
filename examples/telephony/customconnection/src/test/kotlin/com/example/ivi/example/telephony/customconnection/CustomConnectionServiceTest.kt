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

import android.content.ComponentName
import android.os.Bundle
import android.telecom.Connection.STATE_DIALING
import android.telecom.Connection.STATE_RINGING
import android.telecom.ConnectionRequest
import android.telecom.PhoneAccountHandle
import com.tomtom.ivi.platform.telecom.api.common.utils.toPhoneUri
import com.tomtom.ivi.platform.tools.api.testing.unit.IviTestCase
import io.mockk.mockkObject
import io.mockk.verify
import kotlin.test.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class CustomConnectionServiceTest : IviTestCase() {

    private val sut by lazy { CustomConnectionService() }

    @Before
    fun setUpDebugConnectionService() {
        sut.onDestroy()
    }

    @After
    fun tearDownDebugConnectionService() {
        sut.onDestroy()
    }

    @Test
    fun `on create`() {
        // GIVEN
        mockkObject(CustomConnectionServiceHolder)

        // WHEN
        sut.onCreate()

        // THEN
        verify {
            CustomConnectionServiceHolder.setCustomConnectionService(sut)
        }
    }

    @Test
    fun `on destroy`() {
        // GIVEN
        mockkObject(CustomConnectionServiceHolder)

        // WHEN
        sut.onDestroy()

        // THEN
        verify {
            CustomConnectionServiceHolder.setCustomConnectionService(null)
        }
    }

    @Test
    fun `on create incoming connection`() {
        // GIVEN
        val phoneAccountHandle = PhoneAccountHandle(
            ComponentName(
                context,
                CustomConnectionService::class.qualifiedName!!
            ),
            CustomConnectionFacade.IVI_CUSTOM_PHONE_ACCOUNT_ID
        )
        val request = ConnectionRequest(
            phoneAccountHandle, PHONE_NUMBER.toPhoneUri().uri, Bundle.EMPTY
        )

        // WHEN
        val connection = sut.onCreateIncomingConnection(phoneAccountHandle, request)

        // THEN
        assertEquals(PHONE_NUMBER.toPhoneUri().uri, connection?.address)
        assertEquals(STATE_RINGING, connection?.state)
    }

    @Test
    fun `on create outgoing connection`() {
        // GIVEN
        val phoneAccountHandle = PhoneAccountHandle(
            ComponentName(
                context,
                CustomConnectionService::class.qualifiedName!!
            ),
            CustomConnectionFacade.IVI_CUSTOM_PHONE_ACCOUNT_ID
        )
        val request = ConnectionRequest(
            phoneAccountHandle, PHONE_NUMBER.toPhoneUri().uri, Bundle.EMPTY
        )

        // WHEN
        val connection = sut.onCreateOutgoingConnection(phoneAccountHandle, request)

        // THEN
        assertEquals(PHONE_NUMBER.toPhoneUri().uri, connection?.address)
        assertEquals(STATE_DIALING, connection?.state)
    }

    companion object {
        private const val PHONE_NUMBER = "+123456789"
    }
}
