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
import com.tomtom.ivi.platform.mainmenu.api.common.attributes.R as RAttr
import com.tomtom.ivi.platform.theming.api.common.attributes.animatorThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.colorThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.iconSizeThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.sizeThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.spacingThemeCategory
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySource
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySourceContext
import com.tomtom.ivi.platform.theming.api.common.registry.component.IviThemeCategorySetConfigurator

/**
 * The [IviThemeRegistrySource] of custom main menu theme components.
 *
 * Main menu theme components define the styles specific for TTDC main menu domain.
 */
@OptIn(IviExperimental::class)
internal class CustomMainMenuThemeRegistrySource(
    registrySourceContext: IviThemeRegistrySourceContext,
) : IviThemeRegistrySource.Components(registrySourceContext) {

    override suspend fun IviThemeCategorySetConfigurator.applyConfig() {
        /**
         * Extends theme categories with main menu specific theme styles.
         *
         * Each category is extended similarly as the following:
         * ```kotlin
         * // Configures the color category.
         * configure(colorThemeCategory) {
         *     // To style main menu color stylable `TtiviMainMenuThemeCategoryColors`.
         *     toStyle(RAttr.styleable.TtiviMainMenuThemeCategoryColors) {
         *         // For each custom color theme component(DARK and LIGHT in this example).
         *         onComponents(customColorThemeComponents) {
         *             when (component.stylingFlavor) {
         *                 // When Dark flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
         *                     // Applies `R.style.CustomThemeMainMenuColorDark`.
         *                     applyStyle(R.style.CustomThemeMainMenuColorDark)
         *                 // When Light flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
         *                     // Applies `R.style.CustomThemeMainMenuColorLight`.
         *                     applyStyle(R.style.CustomThemeMainMenuColorLight)
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

        extendAnimatorThemeComponents()
        extendColorThemeComponents()
        extendIconSizeThemeComponents()
        extendSizeThemeComponents()
        extendSpacingThemeComponents()
    }

    private fun IviThemeCategorySetConfigurator.extendAnimatorThemeComponents() {
        configure(animatorThemeCategory) {
            toStyle(RAttr.styleable.TtiviMainMenuThemeCategoryAnimators) {
                onComponent(customAnimatorThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeMainMenuAnimator)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendColorThemeComponents() {
        configure(colorThemeCategory) {
            toStyle(RAttr.styleable.TtiviMainMenuThemeCategoryColors) {
                onComponents(customColorThemeComponents) {
                    when (component.stylingFlavor) {
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                            applyStyle(R.style.CustomThemeMainMenuColorDark)
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                            applyStyle(R.style.CustomThemeMainMenuColorLight)
                    }
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendIconSizeThemeComponents() {
        configure(iconSizeThemeCategory) {
            toStyle(RAttr.styleable.TtiviMainMenuThemeCategoryDimensIconSize) {
                onComponent(customIconSizeThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeMainMenuIconSize)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendSizeThemeComponents() {
        configure(sizeThemeCategory) {
            toStyle(RAttr.styleable.TtiviMainMenuThemeCategoryDimensSize) {
                onComponent(customSizeThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeMainMenuSizeMedium)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendSpacingThemeComponents() {
        configure(spacingThemeCategory) {
            toStyle(RAttr.styleable.TtiviMainMenuThemeCategoryDimensSpacing) {
                onComponent(customSpacingThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeMainMenuSpacing)
                }
            }
        }
    }
}
