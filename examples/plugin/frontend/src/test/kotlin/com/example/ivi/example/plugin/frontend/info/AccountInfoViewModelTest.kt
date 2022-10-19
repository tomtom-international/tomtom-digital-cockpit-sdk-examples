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

package com.example.ivi.example.plugin.frontend.info

import androidx.lifecycle.MutableLiveData
import com.example.ivi.example.plugin.frontend.TestData
import com.example.ivi.example.plugin.serviceapi.AccountsService
import com.example.ivi.example.plugin.serviceapi.createApi
import com.tomtom.ivi.platform.tools.api.testing.unit.IviTestCase
import com.tomtom.tools.android.testing.assertion.assertLiveDataEquals
import com.tomtom.tools.android.testing.mock.niceMockk
import io.mockk.every
import io.mockk.verify
import org.junit.Test

internal class AccountInfoViewModelTest : IviTestCase() {

    // Service mock must be configured before a view model is created.
    private val mockAccountsService = mockkService(AccountsService.Companion::createApi) {
        every { serviceAvailable } returns MutableLiveData(true)
        every { activeAccount } returns MutableLiveData(TestData.testAccount)
    }

    private val mockPanel = niceMockk<AccountInfoPanel>()

    private val sut = AccountInfoViewModel(mockPanel)

    @Test
    fun `username is prepared for display`() {
        assertLiveDataEquals(TestData.testAccount.username, sut.displayName)
    }

    @Test
    fun `logout button click calls service`() {
        // GIVEN-WHEN the logout button is clicked
        sut.onLogoutClick()

        // THEN logout is called on the account service.
        verify {
            mockAccountsService.logOutAsync(any())
        }
    }
}
