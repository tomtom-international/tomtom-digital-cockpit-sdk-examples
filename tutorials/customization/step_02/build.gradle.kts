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

import com.tomtom.ivi.appsuite.gradle.appstore.api.appsuitedefaults.appstore.appStoreGroup
import com.tomtom.ivi.appsuite.gradle.bluetooth.api.appsuitedefaults.bluetooth.bluetoothGroup
import com.tomtom.ivi.appsuite.gradle.communications.api.appsuitedefaults.communications.communicationsGroup
import com.tomtom.ivi.appsuite.gradle.companionapp.api.appsuitedefaults.companionapp.companionAppGroup
import com.tomtom.ivi.appsuite.gradle.hvac.api.appsuitedefaults.hvac.hvacGroup
import com.tomtom.ivi.appsuite.gradle.media.api.appsuitedefaults.media.mediaGroup
import com.tomtom.ivi.appsuite.gradle.messaging.api.appsuitedefaults.messaging.messagingGroup
import com.tomtom.ivi.appsuite.gradle.navappcomponents.api.appsuitedefaults.navappcomponents.navAppComponentsGroup
import com.tomtom.ivi.appsuite.gradle.navigation.api.appsuitedefaults.navigation.navigationGroup
import com.tomtom.ivi.appsuite.gradle.systemstatus.api.appsuitedefaults.systemstatus.systemStatusGroup
import com.tomtom.ivi.appsuite.gradle.userprofiles.api.appsuitedefaults.userprofiles.userProfilesGroup
import com.tomtom.ivi.appsuite.gradle.vehiclesettings.api.appsuitedefaults.vehiclesettings.vehicleSettingsGroup
import com.tomtom.ivi.buildsrc.environment.ProjectAbis
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviAppsuite
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviInstanceIdentifier
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.configurators.IviDefaultsGroupsSelectionConfigurator
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    id("com.tomtom.ivi.product.defaults.core")
}

ivi {
    application {
        enabled = true
        iviInstances {
            create(IviInstanceIdentifier.default) {
                applyGroups {
                    selectGroups()
                }
            }
        }
        services {
            applyGroups {
                selectGroups()
            }
        }
    }
}

/**
 * Selects the build time configuration groups included in the IVI application.
 *
 * __Note:__ The build time configuration group `IviPlatform.debugGroup` is only added in `debug`
 * build types.
 */
fun IviDefaultsGroupsSelectionConfigurator.selectGroups() {
    includeDefaultPlatformGroups()
    include(
        IviAppsuite.appStoreGroup,
        IviAppsuite.bluetoothGroup,
        IviAppsuite.communicationsGroup,
        IviAppsuite.companionAppGroup,
        IviAppsuite.hvacGroup,
        IviAppsuite.mediaGroup,
        IviAppsuite.messagingGroup,
        IviAppsuite.navAppComponentsGroup,
        IviAppsuite.navigationGroup,
        IviAppsuite.systemStatusGroup,
        IviAppsuite.userProfilesGroup,
        IviAppsuite.vehicleSettingsGroup
    )
}

android {
    // Step 1: Change the package name.
    namespace = "com.example.ivi.custom.app"
    defaultConfig {
        // Step 1: Change the package name.
        applicationId = "com.example.ivi.custom.app"
    }

    buildFeatures {
        dataBinding = true
    }

    signingConfigs.maybeCreate("release")
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    // Split the output into multiple APKs based on their ABI.
    splits.abi {
        isEnable = true
        reset()
        include(*ProjectAbis.enabledAbis)
    }
}

dependencies {
    implementation(iviDependencies.tomtomAutomotiveAndroidCar)
    implementation(libraries.iviPlatformThemingApiCommonAttributes)
    implementation(libraries.iviPlatformFrameworkApiProductDebugPermissions)
    implementation(libraries.iviPlatformFrameworkApiProductDefaultActivity)
    implementation(libraries.iviPlatformFrameworkApiProductDefaultApplication)
}
