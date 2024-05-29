/*
 * Copyright © 2024 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.theming

import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.theming.api.common.attributes.IviThemeComponent
import com.tomtom.ivi.platform.theming.api.common.attributes.get
import com.tomtom.ivi.platform.theming.api.common.registry.componentselection.toComponentSelector

/**
 * The default [IviThemeComponent]s when the first time the application is launched.
 */
@OptIn(IviExperimental::class)
private val defaultThemeComponents: Set<IviThemeComponent> =
    // The theme components defined below are selected as the default one per category.
    setOf(
        customAnimatorThemeComponents.single(),
        customColorThemeComponents[CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK],
        customDurationThemeComponents.single(),
        customFontThemeComponents.single(),
        customIconThemeComponents.single(),
        customIconSizeThemeComponents.single(),
        customRadiusThemeComponents.single(),
        customResponsiveHorizontalSpacingThemeComponents.single(),
        customResponsiveVerticalSpacingThemeComponents.single(),
        customSizeThemeComponents.single(),
        customSpacingThemeComponents.single(),
        customStylesThemeComponents.single(),
        customTextAppearanceThemeComponents.single(),
        customTextSizeThemeComponents.single(),
        // NavApp
        customNavAppDrivingUiStyleThemeComponents.single(),
        customNavAppDrivingUiHorizonPanelStyleThemeComponents.single(),
        customNavAppMapStyleThemeComponents.single(),
        customNavAppResponsiveSpacingThemeComponents.single(),
        customNavAppSearchUiStyleThemeComponents.single(),
        customNavAppShortcutStripStyleThemeComponents.single(),
    )

@OptIn(IviExperimental::class)
internal val defaultThemeComponentsSelector = defaultThemeComponents.toComponentSelector()
