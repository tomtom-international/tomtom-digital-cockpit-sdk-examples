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

package com.example.ivi.example.plugin.functionaltest.integrationtest

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.ivi.example.plugin.frontend.accountMenuItem
import com.example.ivi.example.plugin.serviceapi.AccountsService
import com.example.ivi.example.plugin.serviceapi.AccountsServiceApi
import com.example.ivi.example.plugin.serviceapi.SensitiveString
import com.example.ivi.example.plugin.serviceapi.createApi
import com.tomtom.ivi.platform.frontend.api.testing.frontend.FrontendTestCase
import com.tomtom.ivi.platform.systemui.api.service.systemuimenuitems.SystemUiMenuItemsServiceMock
import com.tomtom.ivi.platform.systemui.api.testing.mockextension.injectMenuItemClicked
import com.tomtom.ivi.platform.tools.api.testing.functional.util.waitForLiveData
import com.tomtom.tools.android.testing.functional.waitForView
import com.tomtom.tools.android.testing.functional.withIdReference
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class AccountFrontendTest : FrontendTestCase() {
    // An API wrapper cannot be created before test environment is fully configured, hence create
    // it later with @Before method.
    private lateinit var accountsServiceApi: AccountsServiceApi

    @Before
    fun createServiceApi() {
        accountsServiceApi = createApiWrapper { lifecycleOwner, iviServiceProvider ->
            AccountsService.createApi(lifecycleOwner, iviServiceProvider)
        }
        assertTrue(accountsServiceApi.serviceAvailable.waitForLiveData { value -> value })
    }

    @After
    fun ensureNoUserLoggedIn() = runBlocking {
        accountsServiceApi.coLogOut()
    }

    @Test
    fun frontendOpensLoginPanelIfNoUserHasLoggedIn() {
        // GIVEN No user has logged in.
        // WHEN The frontend is invoked from the menu.
        tapAccountMenuItem()
        // THEN The login view is open.
        waitForView(thatIsAccountLoginView).check(matches(isDisplayed()))
    }

    @Test
    fun frontendOpensInfoPanelIfUserHasLoggedIn() {
        // GIVEN The user has logged in.
        runBlocking {
            accountsServiceApi.coLogIn("username", SensitiveString("password"))
        }

        // WHEN The frontend is invoked from the menu.
        tapAccountMenuItem()
        // THEN The info view is open, with the correct name in the title.
        waitForView(thatIsAccountInfoPanel)
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText("Welcome, Username"))))

        // WHEN The frontend is closed.
        tapAccountMenuItem()
        // AND The frontend is open again.
        tapAccountMenuItem()

        // THEN The info view is open, with the correct name in the title.
        waitForView(thatIsAccountInfoPanel)
            .check(matches(isDisplayed()))
            .check(matches(hasDescendant(withText("Welcome, Username"))))
    }

    @Test
    fun frontendOpensLoginPanelIfUserHasLoggedOut() {
        // GIVEN The user has logged in.
        runBlocking {
            accountsServiceApi.coLogIn("username", SensitiveString("password"))
        }
        // AND The frontend is invoked from the menu.
        tapAccountMenuItem()
        // AND The info view is open.
        waitForView(thatIsAccountInfoPanel).check(matches(isDisplayed()))

        // WHEN The user has logged out.
        runBlocking { accountsServiceApi.coLogOut() }

        // THEN The login view is open.
        waitForView(thatIsAccountLoginView).check(matches(isDisplayed()))

        // WHEN The frontend is closed.
        tapAccountMenuItem()
        // AND The frontend is open again.
        tapAccountMenuItem()

        // THEN The login view is open.
        waitForView(thatIsAccountLoginView).check(matches(isDisplayed()))
    }

    private fun tapAccountMenuItem() =
        withIviServiceMockOnTestThread(SystemUiMenuItemsServiceMock::class) {
            injectMenuItemClicked(accountMenuItem)
        }

    companion object {
        private val thatIsAccountLoginView = withIdReference(R.id::frontend_account_login_view)
        private val thatIsAccountInfoPanel = withIdReference(R.id::frontend_account_info_view)
    }
}
