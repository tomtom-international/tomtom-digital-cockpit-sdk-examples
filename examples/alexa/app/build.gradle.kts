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

import com.tomtom.ivi.appsuite.gradle.alexa.api.appsuitedefaults.alexa.alexaGroup
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviAppsuite
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceHostConfig
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
    id("com.tomtom.ivi.appsuite.alexa.defaults.alexa")
    id("com.tomtom.ivi.appsuite.alexa.defaults.config")
}

apply(from = rootProject.file("examples/alexa/iviservicehosts.gradle.kts"))

val customCarControlHandlerServiceHost: IviServiceHostConfig by project.extra
val customEqualizerControllerHandlerServiceHost: IviServiceHostConfig by project.extra
val exampleAlexaPreviewModeServiceHost: IviServiceHostConfig by project.extra

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
                    include(
                        IviAppsuite.alexaGroup
                    )
                }
            }
        }
        services {
            applyGroups {
                includeDefaultPlatformGroups()
                includeDefaultAppsuiteGroups()
                include(
                    IviAppsuite.alexaGroup
                )
            }
            addHost(customCarControlHandlerServiceHost)
            addHost(customEqualizerControllerHandlerServiceHost)
            addHost(exampleAlexaPreviewModeServiceHost)
        }
    }
}

android {
    namespace = "com.example.ivi.example.alexa.app"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":examples_common"))
    implementation(project(":examples_alexa_customcarcontrolhandler"))
    implementation(project(":examples_alexa_customequalizercontrollerhandler"))
    implementation(project(":examples_alexa_alexapreviewmode"))
}
