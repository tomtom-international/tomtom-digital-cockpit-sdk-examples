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


val isCiBuild = System.getenv("BUILD_BUILDNUMBER") != null

buildCache {
    local {
        // Local cache is for local developent only.
        isEnabled = !isCiBuild
    }

    remote<HttpBuildCache> {
        // Remote cache is for CI builds only (as it requires corp network and password).
        isEnabled = isCiBuild
        isPush = isCiBuild
        url = uri("https://ivi-gradle-cache.ado-prod.tomtomgroup.com/cache/")
        credentials {
            username = System.getenv("GRADLE_REMOTE_CACHE_USER")
            password = System.getenv("GRADLE_REMOTE_CACHE_PASSWORD")
        }
    }
}
