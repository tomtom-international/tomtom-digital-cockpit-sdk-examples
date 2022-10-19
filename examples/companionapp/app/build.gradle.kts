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
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceHostConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceInterfaceConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.RuntimeDeploymentIdentifier
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

val customCompanionExampleServiceHost = IviServiceHostConfig(
    serviceHostBuilderName = "CustomCompanionServiceHostBuilder",
    implementationModule = ExampleModuleReference("examples_companionapp_service"),
    interfaces = listOf(
        IviServiceInterfaceConfig(
            serviceName = "CompanionExampleService",
            serviceApiModule = ExampleModuleReference("examples_companionapp_serviceapi")
        )
    )
)

/**
 * IVI configuration for this example application.
 */
ivi {
    optInToExperimentalApis = true

    application {
        enabled = true
        services {
            // Register the companion app service in the application.
            addHost(customCompanionExampleServiceHost)
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
    implementation(project(":examples_companionapp_service"))
}
