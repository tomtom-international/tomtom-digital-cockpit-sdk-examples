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

import com.tomtom.ivi.platform.gradle.api.common.annotations.EXPERIMENTAL_API_USAGE
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.FrontendConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviPlatform
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceHostConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.MenuItemConfig
import com.tomtom.ivi.platform.gradle.api.defaults.config.testing.activityTestGroup

import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.platform.framework.config.activity-test")
}

apply(from = rootProject.file("examples/plugin/iviservicehosts.gradle.kts"))

val accountsServiceHosts: List<IviServiceHostConfig> by project.extra

apply(from = rootProject.file("examples/plugin/frontends-and-menuitems.gradle.kts"))

val accountFrontend: FrontendConfig by project.extra
val accountMenuItem: MenuItemConfig by project.extra

android {
    namespace = "com.example.ivi.example.plugin.functionaltest.integrationtest"
}

ivi {
    application {
        enabled = true
        iviInstances {
            create(IviInstanceIdentifier.default) {
                applyGroups {
                    includeDefaultGroups()
                    @Suppress(EXPERIMENTAL_API_USAGE)
                    include(IviPlatform.activityTestGroup)
                }
                frontends {
                    add(accountFrontend)
                }
                menuItems {
                    addLast(accountMenuItem to accountFrontend)
                }
            }
        }
        services {
            applyGroups {
                @Suppress(EXPERIMENTAL_API_USAGE)
                include(IviPlatform.activityTestGroup)
            }
            addHosts(accountsServiceHosts)
        }
    }
    optInToExperimentalApis = true
}

dependencies {
    androidTestImplementation(project(":examples_plugin_serviceapi"))
    androidTestImplementation(project(":examples_plugin_settingsservice"))
    androidTestImplementation(libraries.iviPlatformFrontendApiTestingFrontend)
}
