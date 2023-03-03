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

import com.tomtom.ivi.appsuite.gradle.media.api.appsuitedefaults.media.defaultMediaPolicyFrontendExtensions
import com.tomtom.ivi.buildsrc.dependencies.ExampleModuleReference
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.FrontendConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

val miniPlayerFrontend = FrontendConfig(
    frontendBuilderName = "ExampleMiniPlayerFrontendBuilder",
    implementationModule = ExampleModuleReference("examples_media_miniplayer"),
    extensions = defaultMediaPolicyFrontendExtensions
)

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
                    add(miniPlayerFrontend)
                }
            }
        }
    }
}

android {
    namespace = "com.example.ivi.example.media.miniplayer"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":examples_common"))

    implementation(iviDependencies.androidxConstraintlayout)
    implementation(iviDependencies.tomtomToolsApiDatabinding)
    implementation(iviDependencies.tomtomToolsApiUicontrols)
    implementation(iviDependencies.tomtomToolsCoreAnimation)
    implementation(libraries.iviAppsuiteMediaApiCommonFrontend)
    implementation(libraries.iviPlatformFrontendApiCommonFrontend)
}
