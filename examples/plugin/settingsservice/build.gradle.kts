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

import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

plugins {
    `kotlinx-serialization`
}

android {
    namespace = "com.example.ivi.example.plugin.settingsservice"
}

ivi {
    configurationGenerator {
        enabled = true
    }
    optInToExperimentalApis = true
}

dependencies {
    implementation(project(":examples_plugin_settingsserviceapi"))

    implementation(iviDependencies.kotlinxSerializationJson)
    implementation(iviDependencies.tomtomToolsApiLivedata)
    implementation(libraries.iviPlatformSettingsApiServicesSettingsManagement)

    testImplementation(libraries.iviPlatformToolsApiTestingUnit)
}
