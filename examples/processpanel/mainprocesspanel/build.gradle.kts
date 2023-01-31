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
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.FrontendCreationPolicy
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

android {
    namespace = "com.example.ivi.example.processpanel.mainprocesspanel"
    buildFeatures {
        dataBinding = true
    }
}

val processCreationFrontend = FrontendConfig(
    frontendBuilderName = "ProcessCreationFrontendBuilder",
    implementationModule = ExampleModuleReference("examples_processpanel_mainprocesspanel"),
    creationPolicy = FrontendCreationPolicy.CREATE_ON_DEMAND
)

val processCreationMenuItem =
    processCreationFrontend.toMenuItem("processCreationMenuItem")

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
                frontends {
                    add(processCreationFrontend)
                }
                menuItems {
                    addLast(processCreationMenuItem to processCreationFrontend)
                }
            }
        }
    }
}

dependencies {
    implementation(project(":examples_common"))
    implementation(libraries.iviPlatformFrontendApiCommonFrontend)
    implementation(libraries.iviPlatformFrontendApiCommonUicontrols)
    implementation(libraries.iviPlatformThemingApiCommonAttributes)
    implementation(libraries.iviPlatformFrontendApiTemplateCompactProcessPanel)
}
