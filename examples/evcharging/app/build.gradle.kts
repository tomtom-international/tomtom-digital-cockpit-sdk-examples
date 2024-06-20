/*
 * Copyright © 2024 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

import com.tomtom.ivi.appsuite.gradle.evcharging.api.appsuitedefaults.evcharging.evChargingGroup
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviAppsuite
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviPlatform
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviServiceHostConfig
import com.tomtom.ivi.platform.gradle.api.defaults.config.evChargingGroup
import com.tomtom.ivi.platform.gradle.api.defaults.config.evChargingStationGroup
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
    id("com.tomtom.ivi.appsuite.evcharging.defaults.evcharging")
}

android {
    namespace = "com.example.ivi.example.evcharging.app"
    buildFeatures {
        dataBinding = true
    }
}

apply(from = rootProject.file("examples/evcharging/iviservicehosts.gradle.kts"))
val exampleEvChargingServiceHost: IviServiceHostConfig by project.extra

ivi {
    application {
        enabled = true

        iviInstances {
            create(IviInstanceIdentifier.default) {
                applyGroups {
                    includeDefaultPlatformGroups()
                    includeDefaultAppsuiteGroups()
                    include(
                        IviAppsuite.evChargingGroup, // Will add the EvCharging frontend.
                    )
                }
            }
        }
        services {
            applyGroups {
                includeDefaultPlatformGroups()
                includeDefaultAppsuiteGroups()
                include(
                    IviPlatform.evChargingGroup, // Will add the EvChargingMediatorService
                    IviPlatform.evChargingStationGroup, // Will add the EvChargingStationService
                    // To add deftpower appsuite service add the following group:
                    // IviAppsuite.deftpowerGroup,
                )
            }
            addHost(exampleEvChargingServiceHost)
        }
    }
}

dependencies {
    implementation(project(":examples_common"))
    implementation(project(":examples_evcharging_service"))
    implementation(project(":examples_evcharging_frontend"))
}
