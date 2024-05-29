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

import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviThemeRegistrySourceConfig
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

/**
 * The custom theming registry sources. See `themeregistrysources.gradle.kts` for more details.
 */
apply(from = rootProject.file("examples/theming/themeregistrysources.gradle.kts"))

val customCoreComponentsThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra
val customCommunicationsThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra
val customHvacThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra
val customMainMenuThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra
val customMediaThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra
val customNavAppComponentsThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra
val customSystemStatusThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra
val customSystemUiThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra
val defaultThemeComponentsSelectorThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra

/**
 * IVI configuration for this example application.
 */
ivi {
    optInToExperimentalApis = true

    application {
        enabled = true
        iviInstances {
            create(IviInstanceIdentifier.default) {
                applyGroups {
                    includeDefaultPlatformGroups()
                    includeDefaultAppsuiteGroups()
                }

                // Configures theming sources.
                theming {
                    addRegistrySources(
                        // These are the theming registry sources per IVI domains.
                        customCoreComponentsThemeRegistrySource,
                        customCommunicationsThemeRegistrySource,
                        customHvacThemeRegistrySource,
                        customMainMenuThemeRegistrySource,
                        customMediaThemeRegistrySource,
                        customNavAppComponentsThemeRegistrySource,
                        customSystemStatusThemeRegistrySource,
                        customSystemUiThemeRegistrySource,
                        // While new theming sources are added, this source selects the defined
                        // theming categories as the default theming sources.
                        defaultThemeComponentsSelectorThemeRegistrySource,
                    )
                }
            }
        }
    }
}

android {
    namespace = "com.example.ivi.example.theming"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":examples_common"))

    implementation(libraries.iviAppsuiteCommunicationsApiCommonAttributes)
    implementation(libraries.iviAppsuiteCommunicationsApiThemingStock)
    implementation(libraries.iviAppsuiteHvacApiCommonAttributes)
    implementation(libraries.iviAppsuiteHvacApiThemingStock)
    implementation(libraries.iviAppsuiteMediaApiCommonAttributes)
    implementation(libraries.iviAppsuiteMediaApiThemingStock)
    implementation(libraries.iviAppsuiteSystemStatusApiCommonAttributes)
    implementation(libraries.iviAppsuiteSystemStatusApiThemingStock)
    implementation(libraries.iviPlatformMainmenuApiCommonAttributes)
    implementation(libraries.iviPlatformMainmenuApiThemingStock)
    implementation(libraries.iviPlatformNavappcomponentsApiCommonAttributes)
    implementation(libraries.iviPlatformNavappcomponentsApiThemingStock)
    implementation(libraries.iviPlatformSystemuiApiCommonAttributes)
    implementation(libraries.iviPlatformSystemuiApiThemingStock)
    implementation(libraries.iviPlatformThemingApiCommonRegistry)
    implementation(libraries.iviPlatformThemingApiThemingGeneric)
    implementation(libraries.iviPlatformThemingApiThemingStock)

    implementation(iviDependencies.tomtomNavAppCoreTheme)
}
