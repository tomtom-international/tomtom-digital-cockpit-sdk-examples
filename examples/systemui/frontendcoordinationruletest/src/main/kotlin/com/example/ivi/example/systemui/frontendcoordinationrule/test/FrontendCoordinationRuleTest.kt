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

package com.example.ivi.example.systemui.frontendcoordinationrule.test

import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ivi.common.commonfunctionaltest.E2eTestCase
import com.example.ivi.example.systemui.frontendcoordinationrule.CustomActivity
import com.example.ivi.example.systemui.frontendcoordinationrule.R
import com.tomtom.ivi.platform.debug.api.testing.tools.openDebugPanel
import com.tomtom.ivi.platform.locationsimulation.api.service.locationsimulation.LocationSimulationData
import com.tomtom.ivi.platform.mainmenu.api.testing.frontend.MainMenuButton
import com.tomtom.ivi.platform.mainmenu.api.testing.frontend.onMainMenuView
import com.tomtom.ivi.platform.mainmenu.api.testing.frontend.waitForMainMenuView
import com.tomtom.tools.android.testing.functional.waitForView
import com.tomtom.tools.android.testing.functional.withIdReference
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test

internal class FrontendCoordinationRuleTest : E2eTestCase(customActivity) {
    override val initialLocationSimulationData: LocationSimulationData? = null

    @Before
    override fun waitForUiReady() {
        waitForView(withIdReference(R.id::example_systemui_menu_container))
            .check(matches(isDisplayed()))
    }

    @Test
    fun debugPanelClosesOnMainMenuClick() {
        // GIVEN
        waitForMainMenuView(MainMenuButton.MEDIA)
        openDebugPanel()

        // WHEN
        onMainMenuView(MainMenuButton.MEDIA)
            .perform(click())

        // THEN
        onView(thatIsDebugPanelTabSpinner)
            .check(matches(not(isDisplayed())))
    }
}

private val customActivity = Intent(
    InstrumentationRegistry.getInstrumentation().targetContext,
    CustomActivity::class.java
)

private val thatIsDebugPanelTabSpinner: Matcher<View>
    get() = withIdReference(R.id::ttivi_debug_fragment_spinner)
