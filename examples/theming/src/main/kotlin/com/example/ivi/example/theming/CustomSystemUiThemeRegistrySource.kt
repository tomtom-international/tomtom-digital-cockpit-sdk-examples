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
import com.tomtom.ivi.platform.systemui.api.common.attributes.R as RAttr
import com.tomtom.ivi.platform.theming.api.common.attributes.colorThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.radiusThemeCategory
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySource
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySourceContext
import com.tomtom.ivi.platform.theming.api.common.registry.component.IviThemeCategorySetConfigurator

/**
 * The [IviThemeRegistrySource] of custom system UI theme components.
 *
 * System UI theme components define the styles specific for TTDC system UI domain.
 */
@OptIn(IviExperimental::class)
internal class CustomSystemUiThemeRegistrySource(
    registrySourceContext: IviThemeRegistrySourceContext,
) : IviThemeRegistrySource.Components(registrySourceContext) {

    override suspend fun IviThemeCategorySetConfigurator.applyConfig() {
        /**
         * Extends theme categories with system UI specific theme styles.
         *
         * Each category is extended similarly as the following:
         * ```kotlin
         * // Configures the color category.
         * configure(colorThemeCategory) {
         *     // To style system UI color stylable `TtiviSystemUiThemeCategoryColors`.
         *     toStyle(RAttr.styleable.TtiviSystemUiThemeCategoryColors) {
         *         // For each custom color theme component(DARK and LIGHT in this example).
         *         onComponents(customColorThemeComponents) {
         *             when (component.stylingFlavor) {
         *                 // When Dark flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
         *                     // Applies `R.style.CustomThemeSystemUiColorDark`.
         *                     applyStyle(R.style.CustomThemeSystemUiColorDark)
         *                 // When Light flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
         *                     // Applies `R.style.CustomThemeSystemUiColorLight`.
         *                     applyStyle(R.style.CustomThemeSystemUiColorLight)
         *                 }
         *             }
         *         }
         *     }
         * }
         * ```
         *
         * See [CustomCoreComponentsThemeRegistrySource] to understand how custom core theme
         * components are registered.
         */
        extendColorThemeComponents()
        extendRadiusThemeComponents()
    }

    private fun IviThemeCategorySetConfigurator.extendColorThemeComponents() {
        configure(colorThemeCategory) {
            toStyle(RAttr.styleable.TtiviSystemUiThemeCategoryColors) {
                onComponents(customColorThemeComponents) {
                    when (component.stylingFlavor) {
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                            applyStyle(R.style.CustomThemeSystemUiColorDark)
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                            applyStyle(R.style.CustomThemeSystemUiColorLight)
                    }
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendRadiusThemeComponents() {
        configure(radiusThemeCategory) {
            toStyle(RAttr.styleable.TtiviSystemUiThemeCategoryDimensRadius) {
                onComponent(customRadiusThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeSystemUiRadius)
                }
            }
        }
    }
}
