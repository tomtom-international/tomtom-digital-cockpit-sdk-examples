/*
 * Copyright © 2023 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.common.commonfunctionaltest

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ivi.common.ExampleActivity
import com.tomtom.ivi.platform.framework.api.common.iviinstance.IviInstanceId
import com.tomtom.ivi.platform.mainmenu.api.testing.frontend.MainMenuButton
import com.tomtom.ivi.platform.mainmenu.api.testing.frontend.waitForMainMenuButtonIsDisplayed
import com.tomtom.ivi.platform.systemui.api.testing.tools.SystemUiIdlingResourceRule
import com.tomtom.ivi.platform.systemui.api.testing.tools.assertCoreContentsAreDisplayed
import com.tomtom.ivi.platform.tools.api.testing.functional.CreateApiCallback
import com.tomtom.ivi.platform.tools.api.testing.functional.GetServiceIdsCallback
import com.tomtom.ivi.platform.tools.api.testing.functional.IviActivityTestCase
import com.tomtom.ivi.platform.tools.api.testing.functional.IviE2eTestCase
import org.junit.Before
import org.junit.Rule

/**
 * A base class for E2E tests of the example product that provides common functionality,
 * such as accessing frequently used containers or opening the debug panel.
 */
abstract class E2eTestCase(targetActivity: Intent = exampleActivity) :
    IviActivityTestCase(targetActivity),
    IviE2eTestCase {

    @get:Rule
    val systemUiIdlingResourceRule = SystemUiIdlingResourceRule(activityRule)

    /**
     * Wait for UI to become ready before each test.
     *
     * Override in tests with custom system ui layout to wait for its readiness.
     */
    @Before
    open fun waitForUiReady() {
        // Wait for core system UI views in stock system ui
        assertCoreContentsAreDisplayed()
    }

    /**
     * Wait for UI menu to become ready before each test.
     *
     * Override in tests with custom main menu buttons if [MainMenuButton.NAVIGATION]
     * is not included.
     */
    @Before
    open fun waitForMenuReady() {
        waitForMainMenuButtonIsDisplayed(
            MainMenuButton.NAVIGATION,
            timeoutMs = DEFAULT_WAIT_MAIN_MENU_TIMEOUT
        )
    }

    override fun <R> createApiWrapper(
        iviInstanceId: IviInstanceId,
        callback: CreateApiCallback<R>
    ) =
        createApiWrapperInternal(iviInstanceId, callback)

    override fun getServiceIdsWrapper(
        iviInstanceId: IviInstanceId,
        callback: GetServiceIdsCallback
    ) =
        getServiceIdsWrapperInternal(iviInstanceId, callback)

    companion object {
        /**
         * This is an insanely long timeout. Typically needed for the second running test when
         * running on the emulator. This is because the `lowmemorykiller` is having a party (aka
         * killing all kinds of processes) triggered by the start of the main activity for the
         * first time (which requires a lot of memory) by the first test. The party continues into
         * the second test. In between the first test and the second test the `lowmemorykiller` even
         * kills our service processes (rightfully, as the processes are low hanging fruit at this
         * time), making matters worse for the second test to restart the processes. The first
         * startup of the activity also triggers download of maps etc, so making
         * TomTom Digital Cockpit even more hungry for memory.
         * Once the party dies out, all is fine and our service processes keep running in between
         * tests and they lived happily ever after. Or so they say.
         */
        private const val DEFAULT_WAIT_MAIN_MENU_TIMEOUT = 20_000L
    }
}

private val exampleActivity = Intent(
    InstrumentationRegistry.getInstrumentation().targetContext,
    ExampleActivity::class.java
)
