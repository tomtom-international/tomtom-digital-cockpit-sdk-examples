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
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceDependencies
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceHostConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceInterfaceConfig

/**
 * This file defines the IVI service host implementations and the list of interfaces hosted by
 * these IVI service hosts. The IVI service hosts are defined in this file so it can be used in all
 * Gradle subprojects, including tests.
 *
 * The properties defined in this file can be accessed in the `build.gradle.kts` files by applying
 * this file in the `build.gradle.kts` file and by using Gradle's extra extension.
 *
 * ```kotlin
 * import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceHostConfig
 * import com.tomtom.ivi.platform.gradle.api.framework.config.ivi
 *
 * apply(from = rootProject.file("iviservicehosts.gradle.kts"))
 *
 * val accountServiceHost: IviServiceHostConfig by project.extra
 *
 * ivi {
 *     application {
 *         enabled = true
 *         services {
 *             addHost(accountServiceHost)
 *         }
 *     }
 * }
 *
 * ...
 * ```
 *
 * The above example obtains the `accountServiceHost` property as defined in this file and adds
 * the IVI service host to the IVI application.
 *
 * To allow the Gradle's extra extensions to work, the properties in this file need to use the
 * `by extra` delegation to assign the value.
 */

val accountSettingsServiceHost by extra {
    IviServiceHostConfig(
        serviceHostBuilderName = "AccountSettingsServiceHostBuilder",
        implementationModule = ExampleModuleReference("examples_plugin_settingsservice"),
        interfaces = listOf(
            IviServiceInterfaceConfig(
                serviceName = "AccountSettingsService",
                serviceApiModule = ExampleModuleReference("examples_plugin_settingsserviceapi")
            )
        )
    )
}

val accountsServiceHost by extra {
    IviServiceHostConfig(
        serviceHostBuilderName = "AccountsServiceHostBuilder",
        implementationModule = ExampleModuleReference("examples_plugin_service"),
        interfaces = listOf(
            IviServiceInterfaceConfig(
                serviceName = "AccountsService",
                serviceApiModule = ExampleModuleReference("examples_plugin_serviceapi")
            )
        ),
        dependencies = IviServiceDependencies(required = accountSettingsServiceHost.interfaces)
    )
}

val accountsServiceHosts by extra {
    listOf(accountsServiceHost, accountSettingsServiceHost)
}
