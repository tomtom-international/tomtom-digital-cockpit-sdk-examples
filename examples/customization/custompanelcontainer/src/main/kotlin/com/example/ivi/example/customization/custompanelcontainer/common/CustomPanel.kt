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

package com.example.ivi.example.customization.custompanelcontainer.common

import com.example.ivi.example.customization.custompanelcontainer.container.DualPanelContainer
import com.example.ivi.example.customization.custompanelcontainer.container.DualPanelContainerData
import com.example.ivi.example.customization.custompanelcontainer.frontend.CustomFragment
import com.example.ivi.example.customization.custompanelcontainer.systemui.CustomPanelRegistry
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PLATFORM_PANEL_TYPES
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.Panel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PanelTypeSet

/**
 * [Panel]s to be displayed by [DualPanelContainer].
 *
 * These panels come to the [DualPanelContainer] inside [DualPanelContainerData].
 *
 * @param text text to be displayed on screen.
 * @param frontendContext a context to be passed to superclass.
 *
 * @see [CustomPanelRegistry]
 */
internal class CustomPanel(val text: String, frontendContext: FrontendContext) :
    Panel(frontendContext) {

    override fun createInitialFragmentInitializer(): IviFragment.Initializer<*, *> =
        IviFragment.Initializer(CustomFragment(), this)
}

/**
 * Declares a new [Panel] type to be used in the system UI. This variable is used in
 * build.gradle.kts as a parameter of `PanelTypesConfig()`.
 */
val CUSTOM_SYSTEM_UI_PANEL_TYPES: PanelTypeSet = PLATFORM_PANEL_TYPES + CustomPanel::class
