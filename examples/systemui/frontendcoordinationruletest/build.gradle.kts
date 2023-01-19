/*
 * Copyright © 2023 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

import com.tomtom.ivi.buildsrc.extensions.androidTest
import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

ivi {
    optInToExperimentalApis = true
}

androidTest {
    targetProjectPath = ":examples_systemui_frontendcoordinationrule"
}

android {
    namespace = "com.example.ivi.example.systemui.frontendcoordinationrule.test"
    navTest.android {
        androidTest {
            enabled.set(true)
            testTags.add("e2e")
        }
    }
}

dependencies {
    implementation(project(":examples_commonfunctionaltest"))

    implementation(iviDependencies.tomtomToolsTestingFunctional)
    implementation(libraries.iviPlatformDebugApiTestingTools)
    implementation(libraries.iviPlatformLocationSimulationApiServiceLocationSimulation)
    implementation(libraries.iviPlatformMainmenuApiTestingFrontend)
}
