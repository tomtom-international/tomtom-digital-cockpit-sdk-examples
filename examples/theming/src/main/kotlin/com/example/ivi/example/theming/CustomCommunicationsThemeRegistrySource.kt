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

import com.tomtom.ivi.appsuite.communications.api.common.attributes.R as RAttr
import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.theming.api.common.attributes.colorThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.iconSizeThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.radiusThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.sizeThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.stylesThemeCategory
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySource
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySourceContext
import com.tomtom.ivi.platform.theming.api.common.registry.component.IviThemeCategorySetConfigurator

/**
 * The [IviThemeRegistrySource] of custom Communications theme components.
 *
 * Communications theme components define the styles specific for TTDC Communications domain.
 */
@OptIn(IviExperimental::class)
class CustomCommunicationsThemeRegistrySource(
    registrySourceContext: IviThemeRegistrySourceContext,
) : IviThemeRegistrySource.Components(registrySourceContext) {

    override suspend fun IviThemeCategorySetConfigurator.applyConfig() {
        /**
         * Extends theme categories with Communications specific theme styles.
         *
         * Each category is extended similarly as the following:
         * ```kotlin
         * // Configures the color category.
         * configure(colorThemeCategory) {
         *     // To style Communications color stylable `TtiviCommunicationsThemeCategoryColors`.
         *     toStyle(RAttr.styleable.TtiviCommunicationsThemeCategoryColors) {
         *         // For each custom color theme component(DARK and LIGHT in this example).
         *         onComponents(customColorThemeComponents) {
         *             when (component.stylingFlavor) {
         *                 // When Dark flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
         *                     // Applies `R.style.CustomThemeCommunicationsColorDark`.
         *                     applyStyle(R.style.CustomThemeCommunicationsColorDark)
         *                 // When Light flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
         *                     // Applies `R.style.CustomThemeCommunicationsColorLight`.
         *                     applyStyle(R.style.CustomThemeCommunicationsColorLight)
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
        extendIconSizeThemeComponents()
        extendRadiusThemeComponents()
        extendSizeThemeComponents()
        extendStylesThemeComponents()
    }

    private fun IviThemeCategorySetConfigurator.extendColorThemeComponents() {
        configure(colorThemeCategory) {
            toStyle(RAttr.styleable.TtiviCommunicationsThemeCategoryColors) {
                onComponents(customColorThemeComponents) {
                    when (component.stylingFlavor) {
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                            applyStyle(R.style.CustomThemeCommunicationsColorDark)
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                            applyStyle(R.style.CustomThemeCommunicationsColorLight)
                    }
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendIconSizeThemeComponents() {
        configure(iconSizeThemeCategory) {
            toStyle(RAttr.styleable.TtiviCommunicationsThemeCategoryDimensIconSize) {
                onComponent(customIconSizeThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeCommunicationsIconSize)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendRadiusThemeComponents() {
        configure(radiusThemeCategory) {
            toStyle(RAttr.styleable.TtiviCommunicationsThemeCategoryDimensRadius) {
                onComponent(customRadiusThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeCommunicationsRadius)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendSizeThemeComponents() {
        configure(sizeThemeCategory) {
            toStyle(RAttr.styleable.TtiviCommunicationsThemeCategoryDimensSize) {
                onComponent(customSizeThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeCommunicationsSize)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendStylesThemeComponents() {
        configure(stylesThemeCategory) {
            toStyle(RAttr.styleable.TtiviCommunicationsThemeCategoryStyles) {
                onComponent(customStylesThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeCommunicationsStyle)
                }
            }
        }
    }
}
