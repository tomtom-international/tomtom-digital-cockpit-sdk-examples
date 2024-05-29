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
import com.tomtom.ivi.platform.theming.api.common.attributes.IviThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.IviThemeComponent
import com.tomtom.ivi.platform.theming.api.common.attributes.createComponents

/**
 * Creates [IviThemeComponent]s for each [CustomThemeCategoryStylingFlavor] option that belongs to
 * the receiver [IviThemeCategory].
 */
@OptIn(IviExperimental::class)
internal inline fun <reified SF> IviThemeCategory.createCustomThemeComponents():
    Set<IviThemeComponent.WithStylingFlavorType<SF>>
    where SF : Enum<SF>, SF : CustomThemeCategoryStylingFlavor =
    createComponents {
        formatComponentId(id, "custom.${it.componentId}")
    }

internal fun formatComponentId(categoryId: String, componentName: String): String =
    listOf(
        "com.example.ivi.example.themecomponent",
        categoryId,
        componentName,
    ).joinToString(".") { it.lowercase() }
