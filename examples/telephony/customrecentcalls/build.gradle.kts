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

android {
    namespace = "com.example.ivi.example.telephony.customrecentcalls"

    buildFeatures {
        buildConfig = true
    }

    buildTypes.configureEach {
        buildConfigField(
            "int",
            "ROBOLECTRIC_TARGET_SDK",
            rootProject.iviDependencies.versions.targetSdk.get(),
        )
    }
}

ivi {
    optInToExperimentalApis = true
}

dependencies {
    implementation(libraries.iviPlatformContactsApiCommonUtil)
    implementation(libraries.iviPlatformRecentcallsApiServiceRecentcalls)
    implementation(libraries.iviPlatformTelecomApiCommonModel)

    testImplementation(iviDependencies.kotlinTest)
    testImplementation(iviDependencies.kotlinxCoroutinesTest)
    testImplementation(iviDependencies.testingJunit)
    testImplementation(iviDependencies.testingMockk)
    testImplementation(iviDependencies.testingRobolectric)
    testImplementation(iviDependencies.tomtomToolsTestingMock)
    testImplementation(libraries.iviPlatformFrameworkApiTestingIpc)
}
