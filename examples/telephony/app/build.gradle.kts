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
import com.tomtom.ivi.platform.gradle.api.common.dependencies.IviPlatformModuleReference
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceHostConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceInterfaceConfig
import com.tomtom.ivi.platform.gradle.api.defaults.config.contactsServiceHost
import com.tomtom.ivi.platform.gradle.api.defaults.config.recentCallsServiceHost
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

val customContactsServiceHost by extra {
    IviServiceHostConfig(
        serviceHostBuilderName = "CustomContactsServiceHostBuilder",
        implementationModule = ExampleModuleReference("examples_telephony_customcontacts"),
        interfaces = listOf(
            IviServiceInterfaceConfig(
                serviceName = "ContactsService",
                serviceApiModule = IviPlatformModuleReference("platform_contacts_api_service_contacts")
            )
        )
    )
}

val customRecentCallsServiceHost by extra {
    IviServiceHostConfig(
        serviceHostBuilderName = "CustomRecentCallsServiceHostBuilder",
        implementationModule = ExampleModuleReference("examples_telephony_customrecentcalls"),
        interfaces = listOf(
            IviServiceInterfaceConfig(
                serviceName = "RecentCallsService",
                serviceApiModule = IviPlatformModuleReference("platform_recentcalls_api_service_recentcalls")
            )
        )
    )
}

/**
 * IVI configuration for this example application.
 */
ivi {
    optInToExperimentalApis = true

    application {
        enabled = true
        services {
            // Replace the default contact service with the custom contacts service.
            removeHost(contactsServiceHost)
            addHost(customContactsServiceHost)

            // Replace the default recent calls service host with the custom recent calls service
            // host.
            removeHost(recentCallsServiceHost)
            addHost(customRecentCallsServiceHost)
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
    implementation(project(":examples_telephony_customconnection"))
    implementation(project(":examples_telephony_customcontacts"))
    implementation(project(":examples_telephony_customrecentcalls"))
}
