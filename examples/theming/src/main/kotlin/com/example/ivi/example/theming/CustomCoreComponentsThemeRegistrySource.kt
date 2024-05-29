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
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySource
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySourceContext
import com.tomtom.ivi.platform.theming.api.common.registry.component.IviThemeCategorySetConfigurator

/**
 * The [IviThemeRegistrySource] of custom core theme components.
 *
 * Core theme components define the styles which are general for each IVI domain.
 * For example, the custom Color theme component registered in this source defines the colors that
 * can be used by each IVI domain.
 */
@OptIn(IviExperimental::class)
internal class CustomCoreComponentsThemeRegistrySource(
    registrySourceContext: IviThemeRegistrySourceContext,
) : IviThemeRegistrySource.Components(registrySourceContext) {

    override suspend fun IviThemeCategorySetConfigurator.applyConfig() {
        /**
         * Registers custom core theme components for each category. By configuring categories,
         * the source can register custom [IviThemeComponent]s and then applying the corresponding
         * styles for them.
         *
         * Each category is configured similarly as the following:
         * ```kotlin
         * // Configures the color category.
         * configure(colorThemeCategory) {
         *     // Registers `customColorThemeComponents`.
         *     registerComponents(customColorThemeComponents)
         *     // To style the primary stylable resource in the color category.
         *     toStyle(primaryStyleableResource) {
         *         // For each custom color theme component(DARK and LIGHT in this example).
         *         onComponents(customColorThemeComponents) {
         *             when (component.stylingFlavor) {
         *                 // When Dark flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
         *                     // Applies `R.style.CustomThemeColorDark`.
         *                     applyStyle(R.style.CustomThemeColorDark)
         *                 // When Light flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
         *                     // Applies `R.style.CustomThemeColorLight`.
         *                     applyStyle(R.style.CustomThemeColorLight)
         *             }
         *         }
         *     }
         * }
         * ```
         */
        configureAnimatorThemeComponents()
        configureColorThemeComponents()
        configureDurationThemeComponents()
        configureFontThemeComponents()
        configureIconThemeComponents()
        configureIconSizeThemeComponents()
        configureRadiusThemeComponents()
        configureResponsiveHorizontalSpacingThemeComponents()
        configureResponsiveVerticalSpacingThemeComponents()
        configureSizeThemeComponents()
        configureSpacingThemeComponents()
        configureStylesThemeComponents()
        configureTextAppearanceThemeComponents()
        configureTextSizeThemeComponents()
    }

    private fun IviThemeCategorySetConfigurator.configureAnimatorThemeComponents() {
        configure(animatorThemeCategory) {
            registerComponents(customAnimatorThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customAnimatorThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeAnimator)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureColorThemeComponents() {
        configure(colorThemeCategory) {
            registerComponents(customColorThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponents(customColorThemeComponents) {
                    when (component.stylingFlavor) {
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                            applyStyle(R.style.CustomThemeColorDark)
                        CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                            applyStyle(R.style.CustomThemeColorLight)
                    }
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureDurationThemeComponents() {
        configure(durationThemeCategory) {
            registerComponents(customDurationThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customDurationThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeDuration)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureFontThemeComponents() {
        configure(fontThemeCategory) {
            registerComponents(customFontThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponents(customFontThemeComponents) {
                    when (component.stylingFlavor) {
                        CustomFontThemeCategoryStylingFlavor.CUSTOM ->
                            applyStyle(R.style.CustomThemeFont)
                    }
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureIconThemeComponents() {
        configure(iconThemeCategory) {
            registerComponents(customIconThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customIconThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeIcon)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureIconSizeThemeComponents() {
        configure(iconSizeThemeCategory) {
            registerComponents(customIconSizeThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customIconSizeThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeIconSize)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureRadiusThemeComponents() {
        configure(radiusThemeCategory) {
            registerComponents(customRadiusThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customRadiusThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeRadius)
                }
            }
        }
    }

    private fun
    IviThemeCategorySetConfigurator.configureResponsiveHorizontalSpacingThemeComponents() {
        configure(responsiveHorizontalSpacingThemeCategory) {
            registerComponents(customResponsiveHorizontalSpacingThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customResponsiveHorizontalSpacingThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeResponsiveSpacingHorizontal)
                }
            }
        }
    }

    private fun
    IviThemeCategorySetConfigurator.configureResponsiveVerticalSpacingThemeComponents() {
        configure(responsiveVerticalSpacingThemeCategory) {
            registerComponents(customResponsiveVerticalSpacingThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customResponsiveVerticalSpacingThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeResponsiveSpacingVertical)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureSizeThemeComponents() {
        configure(sizeThemeCategory) {
            registerComponents(customSizeThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customSizeThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeSize)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureSpacingThemeComponents() {
        configure(spacingThemeCategory) {
            registerComponents(customSpacingThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customSpacingThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeSpacing)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureStylesThemeComponents() {
        configure(stylesThemeCategory) {
            registerComponents(customStylesThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customStylesThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeStyle)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureTextAppearanceThemeComponents() {
        configure(textAppearanceThemeCategory) {
            registerComponents(customTextAppearanceThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customTextAppearanceThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeTextAppearance)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.configureTextSizeThemeComponents() {
        configure(textSizeThemeCategory) {
            registerComponents(customTextSizeThemeComponents)
            toStyle(primaryStyleableResource) {
                onComponent(customTextSizeThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeTextSize)
                }
            }
        }
    }
}
