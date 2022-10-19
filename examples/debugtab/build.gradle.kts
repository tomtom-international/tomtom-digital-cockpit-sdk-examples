/*
 * Copyright © 2020 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

import com.tomtom.ivi.buildsrc.dependencies.ExampleModuleReference
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.FrontendExtensionConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.defaults.config.debugFrontend
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

/**
 * This refers to the `DebugTab` debug tab frontend extension.
 * In this case, the frontend extension references this module, but it can be any other, to improve
 * separation of concerns.
 */
val debugTabFrontendExtension = FrontendExtensionConfig(
    frontendExtensionName = "debugTabFrontendExtension",
    implementationModule = ExampleModuleReference("examples_debugtab")
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
                applyGroups { includeDefaultGroups() }
                frontends {
                    configure(debugFrontend) {
                        addExtension(debugTabFrontendExtension)
                    }
                }
            }
        }
    }
}

android {
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":examples_common"))
    implementation(iviDependencies.androidxActivityKtx)
    implementation(iviDependencies.tomtomToolsApiUicontrols)
    implementation(libraries.iviPlatformDebugApiFrontendExtensionDebugTab)
}
