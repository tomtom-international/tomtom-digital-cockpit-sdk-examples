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
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppDrivingUiHorizonPanelStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppDrivingUiStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppMapStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppResponsiveSpacingThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppSearchUiStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppShortcutStripStyleThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.IviThemeComponent
import com.tomtom.ivi.platform.theming.api.common.attributes.animatorThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.colorThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.durationThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.fontThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.iconSizeThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.iconThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.radiusThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.responsiveHorizontalSpacingThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.responsiveVerticalSpacingThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.sizeThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.spacingThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.stylesThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.textAppearanceThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.textSizeThemeCategory

// Custom theme components are declared as `public` because custom frontend modules can define their
// own theme resource. And hence need to extend the registered categories/flavors.

/**
 * Custom __Animator__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customAnimatorThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomAnimatorThemeCategoryStylingFlavor>> =
        animatorThemeCategory.createCustomThemeComponents()

/**
 * Custom __Color__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customColorThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomColorThemeCategoryStylingFlavor>> =
        colorThemeCategory.createCustomThemeComponents()

/**
 * Custom __Duration__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customDurationThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomDurationThemeCategoryStylingFlavor>> =
        durationThemeCategory.createCustomThemeComponents()

/**
 * Custom __Font__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customFontThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomFontThemeCategoryStylingFlavor>> =
        fontThemeCategory.createCustomThemeComponents()

/**
 * Custom __Icon__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customIconThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomIconThemeCategoryStylingFlavor>> =
        iconThemeCategory.createCustomThemeComponents()

/**
 * Custom __Icon Size__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customIconSizeThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomIconSizeThemeCategoryStylingFlavor>> =
        iconSizeThemeCategory.createCustomThemeComponents()

/**
 * Custom __Radius__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customRadiusThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomRadiusThemeCategoryStylingFlavor>> =
        radiusThemeCategory.createCustomThemeComponents()

/**
 * Custom __Responsive Horizontal Spacing__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customResponsiveHorizontalSpacingThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<
            CustomResponsiveHorizontalSpacingThemeCategoryStylingFlavor,
            >,
        > =
        responsiveHorizontalSpacingThemeCategory.createCustomThemeComponents()

/**
 * Custom __Responsive Vertical Spacing__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customResponsiveVerticalSpacingThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<
            CustomResponsiveVerticalSpacingThemeCategoryStylingFlavor,
            >,
        > =
        responsiveVerticalSpacingThemeCategory.createCustomThemeComponents()

/**
 * Custom __Size__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customSizeThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomSizeThemeCategoryStylingFlavor>> =
        sizeThemeCategory.createCustomThemeComponents()

/**
 * Custom __Spacing__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customSpacingThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomSpacingThemeCategoryStylingFlavor>> =
        spacingThemeCategory.createCustomThemeComponents()

/**
 * Custom __Styles__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customStylesThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomStylesThemeCategoryStylingFlavor>> =
        stylesThemeCategory.createCustomThemeComponents()

/**
 * Custom __Text Appearance__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customTextAppearanceThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomTextAppearanceThemeCategoryStylingFlavor>> =
        textAppearanceThemeCategory.createCustomThemeComponents()

/**
 * Custom __Text Size__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customTextSizeThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomTextSizeThemeCategoryStylingFlavor>> =
        textSizeThemeCategory.createCustomThemeComponents()

/**
 * Custom __NavApp DrivingUi Style__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customNavAppDrivingUiStyleThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<
            CustomNavAppDrivingUiStyleThemeCategoryStylingFlavor,
            >,
        > =
        navAppDrivingUiStyleThemeCategory.createCustomThemeComponents()

/**
 * Custom __NavApp DrivingUi HorizontalPanel Style__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customNavAppDrivingUiHorizonPanelStyleThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<
            CustomNavAppDrivingUiHorizonPanelStyleThemeCategoryStylingFlavor,
            >,
        > =
        navAppDrivingUiHorizonPanelStyleThemeCategory.createCustomThemeComponents()

/**
 * Custom __NavApp Map Style__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customNavAppMapStyleThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<CustomNavAppMapStyleThemeCategoryStylingFlavor>> =
        navAppMapStyleThemeCategory.createCustomThemeComponents()

/**
 * Custom __NavApp Shortcut Strip Style__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customNavAppShortcutStripStyleThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<
            CustomNavAppShortcutStripStyleThemeCategoryStylingFlavor,
            >,
        > =
        navAppShortcutStripStyleThemeCategory.createCustomThemeComponents()

/**
 * Custom __NavApp Responsive Spacing__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customNavAppResponsiveSpacingThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<
            CustomNavAppResponsiveSpacingThemeCategoryStylingFlavor,
            >,
        > =
        navAppResponsiveSpacingThemeCategory.createCustomThemeComponents()

/**
 * Custom __NavApp SearchUi style__ [IviThemeComponent]s.
 */
@OptIn(IviExperimental::class)
val customNavAppSearchUiStyleThemeComponents:
    Set<IviThemeComponent.WithStylingFlavorType<
            CustomNavAppSearchUiStyleThemeCategoryStylingFlavor,
            >,
        > =
        navAppSearchUiStyleThemeCategory.createCustomThemeComponents()
