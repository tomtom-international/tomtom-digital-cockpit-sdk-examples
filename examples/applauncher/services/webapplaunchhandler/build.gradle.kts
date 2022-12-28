/*
 * Copyright © 2022 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

import com.tomtom.ivi.platform.gradle.api.framework.config.ivi

ivi {
    // The AppSourceProviderService API is currently an experimental feature, and has to be
    // explicitly opted in.
    optInToExperimentalApis = true
}

android {
    namespace = "com.example.ivi.examples.applauncher.services.webapplaunchhandler"
}

dependencies {
    implementation(project(":examples_applauncher_common_webapp"))
    implementation(libraries.iviAppsuiteAppstoreApiServiceApplaunchhandler)
}
