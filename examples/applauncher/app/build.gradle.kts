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

import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceHostConfig
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

apply(from = rootProject.file("examples/applauncher/iviservicehosts.gradle.kts"))

val androidAppSourceProviderServiceHost: IviServiceHostConfig by project.extra
val launchableAndroidAppLaunchHandlerServiceHost: IviServiceHostConfig by project.extra
val webAppLaunchTriggerServiceHost: IviServiceHostConfig by project.extra
val webAppLaunchHandlerServiceHost: IviServiceHostConfig by project.extra
val webAppSourceProviderServiceHost: IviServiceHostConfig by project.extra

/**
 * IVI configuration for this example application.
 */
ivi {
    optInToExperimentalApis = true

    application {
        enabled = true
        services {
            addHosts(
                androidAppSourceProviderServiceHost,
                launchableAndroidAppLaunchHandlerServiceHost,
                webAppLaunchTriggerServiceHost,
                webAppLaunchHandlerServiceHost,
                webAppSourceProviderServiceHost,
            )
        }
    }
}

android {
    namespace = "com.example.ivi.example.applauncher.app"
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(project(":examples_common"))
    implementation(project(":examples_applauncher_services_androidappsourceprovider"))
    implementation(project(":examples_applauncher_services_launchableandroidapplaunchhandler"))
    implementation(project(":examples_applauncher_services_webapplaunchtrigger"))
    implementation(project(":examples_applauncher_services_webapplaunchhandler"))
    implementation(project(":examples_applauncher_services_webappsourceprovider"))
}
