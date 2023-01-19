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

package com.example.ivi.example.plugin.service

import androidx.lifecycle.MutableLiveData
import com.example.ivi.example.plugin.serviceapi.AccountsDataSourceQuery
import com.example.ivi.example.plugin.serviceapi.SensitiveString
import com.example.ivi.example.plugin.settingsserviceapi.AccountSettingsService
import com.example.ivi.example.plugin.settingsserviceapi.createApi
import com.tomtom.ivi.platform.framework.api.testing.ipc.iviservice.datasource.assertIviDataSourceEquals
import com.tomtom.ivi.platform.tools.api.testing.unit.IviTestCase
import com.tomtom.tools.android.testing.mock.niceMockk
import io.mockk.every
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class StockAccountsServiceTest : IviTestCase() {

    private val sut = run {
        // An account settings service mock must be configured before the account service is
        // created, because the account service is dependent upon the account settings service.
        mockkService(AccountSettingsService.Companion::createApi) {
            every { serviceAvailable } returns MutableLiveData(true)
            every { activeAccount } returns MutableLiveData()
            every { loginTimestamp } returns MutableLiveData(Instant.now().epochSecond)
            every { onlineLoginValidPeriodInDays } returns MutableLiveData(90L)
        }
    }.let { StockAccountsService(niceMockk()) }

    @Before
    fun createSut() {
        sut.onCreate()
    }

    @Test
    fun `no user is logged in by default`() {
        assertNull(sut.activeAccount)
        assertIviDataSourceEquals(emptyList(), sut.accounts, LOGGED_IN_AT_LEAST_ONCE_QUERY)
    }

    @Test
    fun `login failed if activeAccount or password are incorrect`() = runBlocking {
        assertFalse(sut.logIn(USERNAME, SensitiveString("")))
        assertNull(sut.activeAccount)
        assertIviDataSourceEquals(emptyList(), sut.accounts, LOGGED_IN_AT_LEAST_ONCE_QUERY)

        assertFalse(sut.logIn("", PASSWORD))
        assertNull(sut.activeAccount)
        assertIviDataSourceEquals(emptyList(), sut.accounts, LOGGED_IN_AT_LEAST_ONCE_QUERY)
    }

    @Test
    fun `activeAccount is set if user has logged in`() = runBlocking {
        assertTrue(sut.logIn(USERNAME, PASSWORD))
        assertEquals(USERNAME, sut.activeAccount?.username)
        assertIviDataSourceEquals(
            listOfNotNull(sut.activeAccount),
            sut.accounts,
            LOGGED_IN_AT_LEAST_ONCE_QUERY
        )
    }

    @Test
    fun `activeAccount is reset if user has logged out`() = runBlocking {
        // GIVEN
        sut.logIn(USERNAME, PASSWORD)
        assertIviDataSourceEquals(
            listOfNotNull(sut.activeAccount),
            sut.accounts,
            LOGGED_IN_AT_LEAST_ONCE_QUERY
        )

        // WHEN
        sut.logOut()

        // THEN
        assertNull(sut.activeAccount)
        assertIviDataSourceEquals(emptyList(), sut.accounts, LOGGED_IN_AT_LEAST_ONCE_QUERY)
    }

    @Test
    fun `logging out with no user logged in is no-op`() = runBlocking {
        // WHEN
        sut.logOut()

        // THEN
        assertNull(sut.activeAccount)
    }

    @Test
    fun `logging in with the user logged in updates the activeAccount`() = runBlocking {
        // GIVEN
        sut.logIn(USERNAME, PASSWORD)
        val firstAccount = assertNotNull(sut.activeAccount)
        val anotherTestUser = "anotherTestUser"

        // WHEN
        val result = sut.logIn(anotherTestUser, PASSWORD)

        // THEN
        assertTrue(result)
        val secondAccount = assertNotNull(sut.activeAccount)
        assertEquals(anotherTestUser, secondAccount.username)

        assertIviDataSourceEquals(
            listOf(secondAccount, firstAccount),
            sut.accounts,
            LOGGED_IN_AT_LEAST_ONCE_QUERY
        )
    }

    companion object {
        private const val USERNAME = "testUser"
        private val PASSWORD = SensitiveString("testPassword")

        private val LOGGED_IN_AT_LEAST_ONCE_QUERY = AccountsDataSourceQuery(
            AccountsDataSourceQuery.Selection.LOGGED_IN_AT_LEAST_ONCE,
            AccountsDataSourceQuery.Order.LAST_LOG_IN_TIME_DESCENDING
        )
    }
}
