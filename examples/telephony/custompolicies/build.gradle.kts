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

import com.tomtom.ivi.appsuite.gradle.communications.api.appsuitedefaults.communications.communicationsFrontend
import com.tomtom.ivi.buildsrc.dependencies.ExampleModuleReference
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.FrontendExtensionConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

/**
 * This refers to this module's defined frontend extension, but it can be any other, to improve
 * separation of concerns.
 */
val exampleCommunicationsFrontendExtension = FrontendExtensionConfig(
    frontendExtensionName = "exampleCommunicationsFrontendExtension",
    implementationModule = ExampleModuleReference("examples_telephony_custompolicies")
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
                    configure(communicationsFrontend) {
                        addExtension(exampleCommunicationsFrontendExtension)
                    }
                }
            }
        }
    }
}

android {
    namespace = "com.example.ivi.example.telephony.custompolicies"
}

dependencies {
    implementation(project(":examples_common"))
    implementation(libraries.iviAppsuiteCommunicationsApiCommonFrontend)
    implementation(libraries.iviPlatformFrontendApiCommonUicontrols)
}
