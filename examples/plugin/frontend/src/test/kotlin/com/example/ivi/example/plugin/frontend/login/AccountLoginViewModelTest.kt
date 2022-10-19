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

package com.example.ivi.example.plugin.frontend.login

import androidx.lifecycle.MutableLiveData
import androidx.paging.ItemSnapshotList
import com.example.ivi.example.plugin.frontend.AccountTestPagingDataAdapter
import com.example.ivi.example.plugin.frontend.TestData
import com.example.ivi.example.plugin.serviceapi.AccountsService
import com.example.ivi.example.plugin.serviceapi.createApi
import com.example.ivi.example.plugin.serviceapi.SensitiveString
import com.tomtom.ivi.platform.framework.api.testing.ipc.iviservice.datasource.SimpleTestIviDataSource
import com.tomtom.ivi.platform.tools.api.testing.unit.IviTestCase
import com.tomtom.tools.android.testing.assertion.assertLiveDataEquals
import com.tomtom.tools.android.testing.assertion.assertLiveDataFalse
import com.tomtom.tools.android.testing.assertion.assertLiveDataTrue
import com.tomtom.tools.android.testing.mock.niceMockk
import io.mockk.every
import io.mockk.verify
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AccountLoginViewModelTest : IviTestCase() {

    private val mutableServiceAvailable = MutableLiveData(true)

    // Service mock must be configured before a view model is created.
    private val mockAccountsService = mockkService(AccountsService.Companion::createApi) {
        every { serviceAvailable } returns mutableServiceAvailable
        every { accounts } returns MutableLiveData(
            SimpleTestIviDataSource(TestData.accountsDataSourceData)
        )
        every { activeAccount } returns MutableLiveData(TestData.testAccount)
    }

    private val mockPanel = niceMockk<AccountLoginPanel>()

    private val sut = AccountLoginViewModel(mockPanel)

    @Test
    fun `lastLogin contains simulated data`() {
        assertLiveDataEquals(TestData.lastLoginAccount, sut.lastLogin)
    }

    @Test
    fun `allAccountsPagingDataFlow contains simulated data`() = runTest {
        // GIVEN
        val adapter = AccountTestPagingDataAdapter()

        // WHEN
        val job = launch {
            sut.allAccountsPagingDataFlow.collectLatest { adapter.submitData(it) }
        }
        this.runCurrent()
        val snapshot = adapter.snapshot()
        job.cancel()

        // THEN
        assertEquals(
            ItemSnapshotList(
                placeholdersBefore = 0,
                placeholdersAfter = 0,
                items = TestData.allAccounts
            ),
            snapshot
        )
    }

    @Test
    fun `login button is disabled if username and password field are empty`() {
        assertLiveDataFalse(sut.isLoginEnabled)
    }

    @Test
    fun `login button is disabled if password field is empty`() {
        // GIVEN-WHEN
        sut.username.value = TestData.USERNAME

        // THEN
        assertLiveDataFalse(sut.isLoginEnabled)
    }

    @Test
    fun `login button is disabled if username field is empty`() {
        // GIVEN
        sut.password.value = TestData.PASSWORD

        // WHEN-THEN
        assertLiveDataFalse(sut.isLoginEnabled)
    }

    @Test
    fun `login button is enabled if username and password are correct`() {
        // GIVEN
        sut.username.value = TestData.USERNAME
        sut.password.value = TestData.PASSWORD

        // WHEN-THEN
        assertLiveDataTrue(sut.isLoginEnabled)
    }

    @Test
    fun `login button is disabled if service is not available`() {
        // GIVEN
        sut.username.value = TestData.USERNAME
        sut.password.value = TestData.PASSWORD

        // WHEN
        mutableServiceAvailable.value = false

        // THEN
        assertLiveDataFalse(sut.isLoginEnabled)
    }

    @Test
    fun `login button click calls service`() {
        // GIVEN
        sut.username.value = TestData.USERNAME
        sut.password.value = TestData.PASSWORD

        // WHEN
        sut.onLoginClick()

        // THEN
        verify {
            mockAccountsService.logInAsync(TestData.USERNAME, SensitiveString(TestData.PASSWORD), any())
        }
    }
}
