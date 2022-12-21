/*
 * Copyright © 2021 TomTom NV. All rights reserved.
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

/**
 * This is a custom frontend configuration.
 * In this case, the frontend config references this module, but it can be any other, to improve
 * separation of concerns.
 */
val activityViewFrontend = FrontendConfig(
    frontendBuilderName = "ActivityViewFrontendBuilder",
    implementationModule = ExampleModuleReference("examples_activityview"),
    creationPolicy = FrontendCreationPolicy.CREATE_ON_DEMAND
)

val activityViewMenuItem = activityViewFrontend.toMenuItem("activityViewMenuItem")

/**
 * IVI configuration for this example application.
 */
ivi {
    optInToExperimentalApis = true

    application {
        enabled = true
        iviInstances {
            create(IviInstanceIdentifier.default) {
                applyGroups { includeDefaultGroups() }
                frontends {
                    add(activityViewFrontend)
                }
                menuItems {
                    addLast(activityViewMenuItem to activityViewFrontend)
                }
            }
        }
    }
}

android {
    namespace = "com.example.ivi.example.activityview"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":examples_common"))
    implementation(iviDependencies.androidxActivityKtx)
    implementation(iviDependencies.tomtomToolsApiUicontrols)
    implementation(libraries.iviPlatformDebugApiFrontendExtensionDebugTab)
    implementation(libraries.iviPlatformFrameworkApiCommonUiEmbedding)
}
