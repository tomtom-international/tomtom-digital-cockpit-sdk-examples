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

import com.tomtom.ivi.appsuite.media.api.common.attributes.R as RAttr
import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.theming.api.common.attributes.colorThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.radiusThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.sizeThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.spacingThemeCategory
import com.tomtom.ivi.platform.theming.api.common.attributes.stylesThemeCategory
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySource
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySourceContext
import com.tomtom.ivi.platform.theming.api.common.registry.component.IviThemeCategoryConfigurator
import com.tomtom.ivi.platform.theming.api.common.registry.component.IviThemeCategorySetConfigurator

/**
 * The [IviThemeRegistrySource] of custom media theme components.
 *
 * Media theme components define the styles specific for TTDC media domain.
 */
@OptIn(IviExperimental::class)
internal class CustomMediaThemeRegistrySource(
    registrySourceContext: IviThemeRegistrySourceContext,
) : IviThemeRegistrySource.Components(registrySourceContext) {

    override suspend fun IviThemeCategorySetConfigurator.applyConfig() {
        /**
         * Extends theme categories with media specific theme styles.
         *
         * Each category is extended similarly as the following:
         * ```kotlin
         * // Configures the color category.
         * configure(colorThemeCategory) {
         *     // To style media color stylable `TtiviMediaAlexaThemeCategoryColors`.
         *     toStyle(RAttr.styleable.TtiviMediaAlexaThemeCategoryColors) {
         *         // For each custom color theme component(DARK and LIGHT in this example).
         *         onComponents(customColorThemeComponents) {
         *             when (component.stylingFlavor) {
         *                 // When Dark flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
         *                     // Applies `R.style.CustomThemeMediaAlexaColorDark`.
         *                     applyStyle(R.style.CustomThemeMediaAlexaColorDark)
         *                 // When Light flavor
         *                 CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
         *                     // Applies `R.style.CustomThemeMediaAlexaColorLight`.
         *                     applyStyle(R.style.CustomThemeMediaAlexaColorLight)
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
        extendSizeThemeComponents()
        extendSpacingThemeComponents()
        extendStylesThemeComponents()
    }

    private fun IviThemeCategorySetConfigurator.extendColorThemeComponents() {
        configure(colorThemeCategory) {
            applyAlexaMediaColorThemeComponents()
            applyAmazonMusicColorThemeComponents()
            applyIHeartRadioColorThemeComponents()
            applySpotifyColorThemeComponents()
            applyTuneInColorThemeComponents()
            applyMediaColorThemeComponents()
        }
    }

    private fun IviThemeCategoryConfigurator.applyAlexaMediaColorThemeComponents() {
        toStyle(RAttr.styleable.TtiviMediaAlexaThemeCategoryColors) {
            onComponents(customColorThemeComponents) {
                when (component.stylingFlavor) {
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                        applyStyle(R.style.CustomThemeMediaAlexaColorDark)
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                        applyStyle(R.style.CustomThemeMediaAlexaColorLight)
                }
            }
        }
    }

    private fun IviThemeCategoryConfigurator.applyAmazonMusicColorThemeComponents() {
        toStyle(RAttr.styleable.TtiviMediaAmazonMusicThemeCategoryColors) {
            onComponents(customColorThemeComponents) {
                when (component.stylingFlavor) {
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                        applyStyle(R.style.CustomThemeMediaAmazonMusicColorDark)
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                        applyStyle(R.style.CustomThemeMediaAmazonMusicColorLight)
                }
            }
        }
    }

    private fun IviThemeCategoryConfigurator.applyIHeartRadioColorThemeComponents() {
        toStyle(RAttr.styleable.TtiviMediaIHeartRadioThemeCategoryColors) {
            onComponents(customColorThemeComponents) {
                when (component.stylingFlavor) {
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                        applyStyle(R.style.CustomThemeMediaIHeartRadioColorDark)
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                        applyStyle(R.style.CustomThemeMediaIHeartRadioColorLight)
                }
            }
        }
    }

    private fun IviThemeCategoryConfigurator.applySpotifyColorThemeComponents() {
        toStyle(RAttr.styleable.TtiviMediaSpotifyThemeCategoryColors) {
            onComponents(customColorThemeComponents) {
                when (component.stylingFlavor) {
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                        applyStyle(R.style.CustomThemeMediaSpotifyColorDark)
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                        applyStyle(R.style.CustomThemeMediaSpotifyColorLight)
                }
            }
        }
    }

    private fun IviThemeCategoryConfigurator.applyTuneInColorThemeComponents() {
        toStyle(RAttr.styleable.TtiviMediaTuneInThemeCategoryColors) {
            onComponents(customColorThemeComponents) {
                when (component.stylingFlavor) {
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                        applyStyle(R.style.CustomThemeMediaTuneInColorDark)
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                        applyStyle(R.style.CustomThemeMediaTuneInColorLight)
                }
            }
        }
    }

    private fun IviThemeCategoryConfigurator.applyMediaColorThemeComponents() {
        toStyle(RAttr.styleable.TtiviMediaThemeCategoryColors) {
            onComponents(customColorThemeComponents) {
                when (component.stylingFlavor) {
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_DARK ->
                        applyStyle(R.style.CustomThemeMediaColorDark)
                    CustomColorThemeCategoryStylingFlavor.CUSTOM_LIGHT ->
                        applyStyle(R.style.CustomThemeMediaColorLight)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendRadiusThemeComponents() {
        configure(radiusThemeCategory) {
            toStyle(RAttr.styleable.TtiviMediaThemeCategoryDimensRadius) {
                onComponent(customRadiusThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeMediaRadius)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendSizeThemeComponents() {
        configure(sizeThemeCategory) {
            toStyle(RAttr.styleable.TtiviMediaThemeCategoryDimensSize) {
                onComponent(customSizeThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeMediaSize)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendSpacingThemeComponents() {
        configure(spacingThemeCategory) {
            toStyle(RAttr.styleable.TtiviMediaThemeCategoryDimensSpacing) {
                onComponent(customSpacingThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeMediaSpacing)
                }
            }
        }
    }

    private fun IviThemeCategorySetConfigurator.extendStylesThemeComponents() {
        configure(stylesThemeCategory) {
            toStyle(RAttr.styleable.TtiviMediaThemeCategoryStyles) {
                onComponent(customStylesThemeComponents.single()) {
                    applyStyle(R.style.CustomThemeMediaStyle)
                }
            }
        }
    }
}
