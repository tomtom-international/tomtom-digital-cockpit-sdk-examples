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

import com.example.ivi.example.theming.CustomAnimatorThemeCategoryStylingFlavor.CUSTOM
import com.example.ivi.example.theming.CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK
import com.example.ivi.example.theming.CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT
import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppDrivingUiHorizonPanelStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppDrivingUiStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppMapStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppResponsiveSpacingThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppSearchUiStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppShortcutStripStyleThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.IviThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.IviThemeCategoryStylingFlavor
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
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver
import com.tomtom.tools.android.api.resourceresolution.string.StringResolver

/**
 * The interface for enum types where each enum value denotes a styling flavor of an
 * [IviThemeCategory].
 */
@OptIn(IviExperimental::class)
internal interface CustomThemeCategoryStylingFlavor : IviThemeCategoryStylingFlavor {
    val componentId: String

    val styleFlavorName: String

    override val label: StringResolver
        get() = StaticStringResolver(styleFlavorName)
}

// Flavor classes are declared as `public` because custom frontend modules can define their own
// theme resource. And hence need to extend the registered categories/flavors.

/**
 * Defines the [CUSTOM] flavor for [animatorThemeCategory].
 */
enum class CustomAnimatorThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom animator flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM_LIGHT], [CUSTOM_DARK] flavors for [colorThemeCategory].
 */
enum class CustomColorThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom Light color flavor.
     */
    CUSTOM_LIGHT("customlight", "Custom Light"),

    /**
     * The custom Dark color flavor.
     */
    CUSTOM_DARK("customdark", "Custom Dark"),
}

/**
 * Defines the [CUSTOM] flavor for [durationThemeCategory].
 */
enum class CustomDurationThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom duration flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [fontThemeCategory].
 */
enum class CustomFontThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom font flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [iconThemeCategory].
 */
enum class CustomIconThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom icon flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [iconSizeThemeCategory].
 */
enum class CustomIconSizeThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom icon size flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [radiusThemeCategory].
 */
enum class CustomRadiusThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom radius flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [responsiveHorizontalSpacingThemeCategory].
 */
enum class CustomResponsiveHorizontalSpacingThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom responsive horizontal spacing flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [responsiveVerticalSpacingThemeCategory].
 */
enum class CustomResponsiveVerticalSpacingThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom responsive vertical spacing flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [sizeThemeCategory].
 */
enum class CustomSizeThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom size flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [spacingThemeCategory].
 */
enum class CustomSpacingThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom spacing flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [stylesThemeCategory].
 */
enum class CustomStylesThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom styles flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [textAppearanceThemeCategory].
 */
enum class CustomTextAppearanceThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom text appearance flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [textSizeThemeCategory].
 */
enum class CustomTextSizeThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom text size flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [navAppDrivingUiStyleThemeCategory].
 */
enum class CustomNavAppDrivingUiStyleThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom NavApp DrivingUi style flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [navAppDrivingUiHorizonPanelStyleThemeCategory].
 */
enum class CustomNavAppDrivingUiHorizonPanelStyleThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom NavApp DrivingUi Horizon Panel style flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [navAppMapStyleThemeCategory].
 */
enum class CustomNavAppMapStyleThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom NavApp map style flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [navAppShortcutStripStyleThemeCategory].
 */
enum class CustomNavAppShortcutStripStyleThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom NavApp Shortcut Strip style flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [navAppResponsiveSpacingThemeCategory].
 */
enum class CustomNavAppResponsiveSpacingThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom NavApp responsive spacing flavor.
     */
    CUSTOM("custom", "Custom"),
}

/**
 * Defines the [CUSTOM] flavor for [navAppSearchUiStyleThemeCategory].
 */
enum class CustomNavAppSearchUiStyleThemeCategoryStylingFlavor(
    override val componentId: String,
    override val styleFlavorName: String,
) : CustomThemeCategoryStylingFlavor {

    /**
     * The custom NavApp SearchUi style flavor.
     */
    CUSTOM("custom", "Custom"),
}
