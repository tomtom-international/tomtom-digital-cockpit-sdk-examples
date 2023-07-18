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

import com.tomtom.ivi.appsuite.gradle.media.api.appsuitedefaults.media.fallbackMediaPolicyFrontendExtension
import com.tomtom.ivi.appsuite.gradle.media.api.appsuitedefaults.media.mediaFrontend
import com.tomtom.ivi.buildsrc.dependencies.ExampleModuleReference
import com.tomtom.ivi.platform.gradle.api.common.annotations.EXPERIMENTAL_API_USAGE
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
val exampleFallbackPolicyFrontendExtension = FrontendExtensionConfig(
    frontendExtensionName = "exampleFallbackPolicyFrontendExtension",
    implementationModule = ExampleModuleReference("examples_media_fallbackpolicy")
)

/**
 * IVI configuration for this example application.
 *
 * Not having the `replaceExtension` part in the project's IVI configuration will make the media
 * player UI use a default configuration for all non-customized media sources.
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
                    configure(mediaFrontend) {
                        @Suppress(EXPERIMENTAL_API_USAGE)
                        replaceExtension(
                            fallbackMediaPolicyFrontendExtension,
                            exampleFallbackPolicyFrontendExtension
                        )
                    }
                }
            }
        }
    }
}

android {
    namespace = "com.example.ivi.example.media.fallbackpolicy"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":examples_common"))
    implementation(libraries.iviAppsuiteMediaApiCommonFrontend)
}
