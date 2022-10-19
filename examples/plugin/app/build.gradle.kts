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

import com.tomtom.ivi.appsuite.gradle.userprofiles.api.appsuitedefaults.userprofiles.userProfileFrontend
import com.tomtom.ivi.appsuite.gradle.userprofiles.api.appsuitedefaults.userprofiles.userProfileMenuItem
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.FrontendConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceHostConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.MenuItemConfig
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.RuntimeDeploymentIdentifier
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

apply(from = rootProject.file("examples/plugin/frontends-and-menuitems.gradle.kts"))
val accountFrontend: FrontendConfig by project.extra
val accountMenuItem: MenuItemConfig by project.extra

apply(from = rootProject.file("examples/plugin/iviservicehosts.gradle.kts"))
val accountsServiceHosts: List<IviServiceHostConfig> by project.extra

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
                    replace(userProfileFrontend, accountFrontend)
                }
                menuItems {
                    replace(userProfileMenuItem, accountMenuItem to accountFrontend)
                }
            }
        }
        services {
            // Register the account and account settings service hosts in the application.
            addHosts(accountsServiceHosts)
        }
        runtime {
            globalDeployments {
                create(RuntimeDeploymentIdentifier.globalRuntime) {
                    // Apply the default runtime deployments. This deploys each IVI service host
                    // implementation in a separate process.
                    applyDefaultDeployments(all())

                    // Deploys the account and account settings service hosts in the same process.
                    deployServiceHosts(inList(accountsServiceHosts))
                        .withProcessName("account")
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
    implementation(project(":examples_plugin_frontend"))
    implementation(project(":examples_plugin_service"))
    implementation(project(":examples_plugin_settingsservice"))
}
