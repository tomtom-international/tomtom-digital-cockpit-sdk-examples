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

pluginManagement {
    repositories {
        // Local artifact cache.
        mavenLocal()

        // TomTom Digital Cockpit's Nexus repository.
        maven("https://repo.tomtom.com/repository/ivi") {
            credentials {
                username =
                    if (extra.has("nexusUsername")) extra["nexusUsername"].toString() else ""
                password =
                    if (extra.has("nexusPassword")) extra["nexusPassword"].toString() else ""
            }
        }

        // External repositories.
        mavenCentral()
        google()
        maven("https://plugins.gradle.org/m2/")
    }
}

dependencyResolutionManagement {
    repositories {
        // Local artifact cache.
        mavenLocal()

        // TomTom Digital Cockpit's Nexus repository.
        maven("https://repo.tomtom.com/repository/ivi") {
            credentials {
                username =
                    if (extra.has("nexusUsername")) extra["nexusUsername"].toString() else ""
                password =
                    if (extra.has("nexusPassword")) extra["nexusPassword"].toString() else ""
            }
        }

        // TomTom's Nexus repository for the Connectivity Agent.
        maven("https://maven.tomtom.com:8443/nexus/content/repositories/releases/")

        // External repositories.
        mavenCentral()
        google()
        maven("https://plugins.gradle.org/m2/")
    }
}
