/*
 * Copyright © 2023 TomTom NV. All rights reserved.
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
    namespace = "com.example.ivi.example.customization.custompanelcontainer"
    buildFeatures {
        dataBinding = true
    }
}

val module = ExampleModuleReference("examples_customization_custompanelcontainer")

val customPanelTypes = PanelTypesConfig(
    "CUSTOM_SYSTEM_UI_PANEL_TYPES",
    module,
    "common"
)

val customFrontend = FrontendConfig(
    frontendBuilderName = "CustomFrontendBuilder",
    implementationModule = module,
    subPackageName = "frontend",
    availablePanelTypes = customPanelTypes
)

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
                    add(customFrontend)
                }
            }
        }
    }
}

dependencies {
    implementation(project(":examples_common"))

    implementation(iviDependencies.androidxConstraintlayout)
    implementation(iviDependencies.tomtomToolsApiDatabinding)
    implementation(iviDependencies.tomtomToolsApiLivedata)
    implementation(libraries.iviPlatformFrameworkApiProductDebugPermissions)
    implementation(libraries.iviPlatformFrameworkApiProductDefaultActivity)
    implementation(libraries.iviPlatformSystemuiApiStockSystemuihost)
}
