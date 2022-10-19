/*
 * Copyright © 2022 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.systemui.custompaneltype.frontend

import androidx.lifecycle.map
import com.example.ivi.example.systemui.custompaneltype.common.CustomSystemUiPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.Frontend
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel

/**
 * A frontend that demonstrates using the custom panel type [CustomSystemUiPanel].
 */
internal class ExampleFrontend(frontendContext: FrontendContext) : Frontend(frontendContext) {

    /**
     * Adds the [CustomSystemUiPanel] implementation [ExampleCustomPanel].
     */
    override fun onCreate() {
        super.onCreate()

        val isTaskPanelOpened = panels.map { it.any { panel -> panel is TaskPanel } }
        addPanel(ExampleCustomPanel(frontendContext, isTaskPanelOpened))
    }

    /**
     * Open the [ExampleTaskPanel] when the menu item is tapped. Creating the panel here causes the
     * [ExampleFrontend] to become the focused [Frontend].
     */
    override fun createMainTaskPanel(): TaskPanel? = ExampleTaskPanel(frontendContext)
}
