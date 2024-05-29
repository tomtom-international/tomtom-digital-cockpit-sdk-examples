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
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.R as RAttr
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppDrivingUiHorizonPanelStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppDrivingUiStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppMapStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppResponsiveSpacingThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppSearchUiStyleThemeCategory
import com.tomtom.ivi.platform.navappcomponents.api.common.attributes.navAppShortcutStripStyleThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.IviThemeComponent
import com.tomtom.ivi.platform.theming.api.common.attributes.animatorThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.colorThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.sizeThemeCategory
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySource
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySourceContext
import com.tomtom.ivi.platform.theming.api.common.registry.component.IviThemeCategorySetConfigurator
import com.tomtom.navapp.ui.coreTheme.R as NavAppCoreThemeR

/**
 * The [IviThemeRegistrySource] of custom NavApp theme components.
 *
 * NavApp theme components define the styles specific for TTDC NavApp domain.
 */
@OptIn(IviExperimental::class)
internal class CustomNavAppComponentsThemeRegistrySource(
    registrySourceContext: IviThemeRegistrySourceContext,
) : IviThemeRegistrySource.Components(registrySourceContext) {

    override suspend fun IviThemeCategorySetConfigurator.applyConfig() {
        /**
         * Registers custom NavApp theme components for each NavApp specific category.
         * By configuring categories, the source can register custom [IviThemeComponent]s and then
         * applying the corresponding styles for them.
         *
         * Each category is configured similarly as the following:
         * ```kotlin
         * // Configures the NavApp DrivingUi Style category.
         * configure(navAppDrivingUiStyleThemeCategory) {
         *     // Registers `customNavAppDrivingUiStyleThemeComponents`.
         *     registerComponents(customNavAppDrivingUiStyleThemeComponents)
         *     // To style the primary stylable resource in the NavApp DrivingUi Style category.
         *     toStyle(primaryStyleableResource) {
         *         // For each custom color theme component(only one flavor in this example).
         *         onComponent(customNavAppDrivingUiStyleThemeComponents.single()) {
         *             // Applies `R.style.CustomThemeNavAppDrivingUiStyle`.
         *             applyStyle(R.style.CustomThemeNavAppDrivingUiStyle)
         *         }
         *     }
         * }
         * ```
         */
        configureDrivingUiStyleThemeComponents()
        configureDrivingUiHorizonPanelStyleThemeComponents()
        configureMapStyleThemeComponents()
        configureResponsiveSpacingThemeComponents()
        configureSearchUiStyleThemeComponents()
        configureShortcutStripStyleThemeComponents()

        /**
         * Extends theme categories with NavApp specific theme styles.
         */
        extendAnimatorThemeComponents()
        extendColorThemeComponents()
        extendSizeThemeComponents()
    }

    private fun IviThemeCategorySetConfigurator.configureDrivingUiStyleThemeComponents() {
        configure(navAppDrivingUiStyleThemeCategory) {
            registerComponents(customNavAppDrivingUiStyleThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customNavAppDrivingUiStyleThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeNavAppDrivingUiStyle)
                }
            }
        }
    }

    private fun
    IviThemeCategorySetConfigurator.configureDrivingUiHorizonPanelStyleThemeComponents() {
        configure(navAppDrivingUiHorizonPanelStyleThemeCategory) {
            registerComponents(customNavAppDrivingUiHorizonPanelStyleThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customNavAppDrivingUiHorizonPanelStyleThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeNavAppDrivingUiHorizonPanelStyle)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureMapStyleThemeComponents() {
        configure(navAppMapStyleThemeCategory) {
            registerComponents(customNavAppMapStyleThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customNavAppMapStyleThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeNavAppMapStyle)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureResponsiveSpacingThemeComponents() {
        configure(navAppResponsiveSpacingThemeCategory) {
            registerComponents(customNavAppResponsiveSpacingThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customNavAppResponsiveSpacingThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeNavAppResponsiveSpacingStandard)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureSearchUiStyleThemeComponents() {
        configure(navAppSearchUiStyleThemeCategory) {
            registerComponents(customNavAppSearchUiStyleThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customNavAppSearchUiStyleThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeNavAppSearchUIStyle)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureShortcutStripStyleThemeComponents() {
        configure(navAppShortcutStripStyleThemeCategory) {
            registerComponents(customNavAppShortcutStripStyleThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customNavAppShortcutStripStyleThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeNavAppShortcutStripStyle)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendAnimatorThemeComponents() {
        configure(animatorThemeCategory) {
            toStyle(NavAppCoreThemeR.styleable.TtNavAppThemeCategoryAnimators) {
                onComponent(customAnimatorThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeNavAppAnimator)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendColorThemeComponents() {
        configure(colorThemeCategory) {
            toStyle(NavAppCoreThemeR.styleable.TtNavColors) {
                onComponents(customColorThemeComponents) {
                    when (component.stylingFlavor) {
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                            applyStyle(R.style.CustomThemeNavAppColorDark)
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                            applyStyle(R.style.CustomThemeNavAppColorLight)
                    }
                }
            }
            toStyle(RAttr.styleable.TtiviMapDisplayThemeCategoryColors) {
                onComponents(customColorThemeComponents) {
                    when (component.stylingFlavor) {
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                            applyStyle(R.style.CustomThemeMapDisplayColorDark)
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                            applyStyle(R.style.CustomThemeMapDisplayColorLight)
                    }
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendSizeThemeComponents() {
        configure(sizeThemeCategory) {
            toStyle(NavAppCoreThemeR.styleable.TtNavDimens) {
                onComponent(customSizeThemeComponents.single()) {
                    applyStyle(R.style.CustomNavAppThemeSize)
                }
            }
        }
    }
}
