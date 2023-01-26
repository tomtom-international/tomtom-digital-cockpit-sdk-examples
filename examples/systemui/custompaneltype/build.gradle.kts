/*
 * Copyright © 2022 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

import com.tomtom.ivi.buildsrc.dependencies.ExampleModuleReference
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.FrontendConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.PanelTypesConfig
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

android {
    namespace = "com.example.ivi.example.systemui.custompaneltype"
    buildFeatures {
        dataBinding = true
    }
}

val module = ExampleModuleReference("examples_systemui_custompaneltype")

/**
 * Defines the custom panel types.
 */
val customPanelTypes = PanelTypesConfig(
    "CUSTOM_SYSTEM_UI_PANEL_TYPES",
    module,
    "common"
)

/**
 * This is a custom frontend configuration.
 * In this case, the frontend config references this module, but it can be any other, to improve
 * separation of concerns.
 */
val exampleFrontend =
    FrontendConfig(
        frontendBuilderName = "ExampleFrontendBuilder",
        implementationModule = module,
        subPackageName = "frontend",
        availablePanelTypes = customPanelTypes
    )

val exampleMenuItem = exampleFrontend.toMenuItem("exampleMenuItem")

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
                frontends {
                    add(exampleFrontend)
                }
                menuItems {
                    addFirst(exampleMenuItem to exampleFrontend)
                }
            }
        }
    }
}

dependencies {
    implementation(project(":examples_common"))

    implementation(iviDependencies.tomtomToolsApiDatabinding)
    implementation(iviDependencies.tomtomToolsApiLivedata)
    implementation(iviDependencies.tomtomToolsApiUicontrols)
    implementation(libraries.iviPlatformFrameworkApiProductDefaultActivity)
    implementation(libraries.iviPlatformSystemuiApiStockSystemuihost)
}
