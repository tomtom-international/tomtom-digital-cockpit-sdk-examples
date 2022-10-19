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

package com.example.ivi.example.plugin.frontend

import androidx.lifecycle.MutableLiveData
import com.example.ivi.example.plugin.common.Account
import com.example.ivi.example.plugin.frontend.info.AccountInfoPanel
import com.example.ivi.example.plugin.frontend.login.AccountLoginPanel
import com.example.ivi.example.plugin.serviceapi.AccountsService
import com.example.ivi.example.plugin.serviceapi.createApi
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.filterPanels
import com.tomtom.ivi.platform.frontend.api.testing.unit.mockkFrontendContext
import com.tomtom.ivi.platform.tools.api.testing.unit.IviTestCase
import com.tomtom.tools.android.api.livedata.ImmutableLiveData
import io.mockk.every
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test

internal class AccountFrontendTest : IviTestCase() {

    private val mutableAccount = MutableLiveData<Account?>(null)

    // Service mock must be configured before a frontend is created.
    @Suppress("unused")
    private val mockAccountsService =
        mockkService(AccountsService.Companion::createApi) {
            every { serviceAvailable } returns ImmutableLiveData(true)
            every { activeAccount } returns mutableAccount
        }

    private val sut = AccountFrontend(mockkFrontendContext())
        .apply { onCreate() }

    @Test
    fun `frontend does not open a panel after an user has logged in`() {
        // GIVEN no user is logged in
        mutableAccount.value = null

        // WHEN an user has logged in
        mutableAccount.value = TestData.testAccount

        // THEN the frontend does not open a panel.
        assertTrue(sut.panels.getOrAwaitValue().isEmpty())
    }

    @Test
    fun `frontend does not open a panel after an user has logged out`() {
        // GIVEN an user is logged in
        mutableAccount.value = TestData.testAccount

        // WHEN the user has logged out
        mutableAccount.value = null

        // THEN the frontend does not open a panel.
        assertTrue(sut.panels.getOrAwaitValue().isEmpty())
    }

    @Test
    fun `frontend opens a login panel if no user is logged in`() {
        // GIVEN no user is logged in
        mutableAccount.value = null

        // WHEN the frontend opens a task panel
        sut.openTaskPanels()

        // THEN the frontend opens a login panel.
        assertEquals(1, sut.panels.getOrAwaitValue().size)
        assertEquals(1, sut.panels.getOrAwaitValue().filterPanels<AccountLoginPanel>().size)
    }

    @Test
    fun `frontend opens an info panel if the user is logged in`() {
        // GIVEN the user is logged in
        mutableAccount.value = TestData.testAccount

        // WHEN the frontend opens a task panel
        sut.openTaskPanels()

        // THEN the frontend opens an info panel.
        assertEquals(1, sut.panels.getOrAwaitValue().size)
        assertEquals(1, sut.panels.getOrAwaitValue().filterPanels<AccountInfoPanel>().size)
    }

    @Test
    fun `frontend goes from login panel to info panel after login`() {
        // GIVEN no user is logged in
        mutableAccount.value = null

        // AND the frontend opens a login panel
        sut.openTaskPanels()

        // WHEN the user is logged in
        mutableAccount.value = TestData.testAccount

        // THEN the frontend opens an info panel.
        assertEquals(1, sut.panels.getOrAwaitValue().size)
        assertEquals(1, sut.panels.getOrAwaitValue().filterPanels<AccountInfoPanel>().size)
    }

    @Test
    fun `frontend goes from info panel to login panel after logout`() {
        // GIVEN the user is logged in
        mutableAccount.value = TestData.testAccount

        // AND the frontend opens an info panel
        sut.openTaskPanels()

        // WHEN the user is logged out
        mutableAccount.value = null

        // THEN the frontend opens an info panel.
        assertEquals(1, sut.panels.getOrAwaitValue().size)
        assertEquals(1, sut.panels.getOrAwaitValue().filterPanels<AccountLoginPanel>().size)
    }

    @Test
    fun `frontend does not re-open info panel if an user has changed`() {
        // GIVEN the user is logged in
        mutableAccount.value = TestData.testAccount

        // AND the frontend has an info panel opened
        sut.openTaskPanels()

        // WHEN another user is logged in
        mutableAccount.value = TestData.anotherTestAccount

        // THEN the frontend still has an info panel opened.
        assertEquals(1, sut.panels.getOrAwaitValue().size)
        assertEquals(1, sut.panels.getOrAwaitValue().filterPanels<AccountInfoPanel>().size)
    }
}
