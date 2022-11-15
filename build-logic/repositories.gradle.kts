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

fun RepositoryHandler.tomtomNexus() {
    // Configure TomTom Digital Cockpit repository.
    maven("https://repo.tomtom.com/repository/ivi") {
        credentials {
            username =
                if (extra.has("nexusUsername")) {
                    extra["nexusUsername"].toString()
                } else {
                    throw GradleException("Error: You must set gradle property 'nexusUsername'. " +
                        "See https://developer.tomtom.com/tomtom-digital-cockpit/documentation/" +
                        "getting-started/accessing-the-artifact-repository")
                }
            password =
                if (extra.has("nexusPassword")) {
                    extra["nexusPassword"].toString()
                } else {
                    throw GradleException("Error: You must set gradle property 'nexusPassword'. " +
                        "See https://developer.tomtom.com/tomtom-digital-cockpit/documentation/" +
                        "getting-started/accessing-the-artifact-repository")
                }
        }
    }
}

pluginManagement {
    repositories {
        // Local artifact cache.
        mavenLocal()

        // External repositories.
        mavenCentral()
        google()
        maven("https://plugins.gradle.org/m2/")
    }
}

// Doing it in this way (as opposed to `pluginManagement{repositories{...}}`) allows using the
// `tomtomNexus` function defined above and avoids duplicating the repository details.
// The downside is that you can not use plugins in the `settings.gradle.kts` file, if that plugin
// depends on any of the repositories from this block.
// See https://docs.gradle.org/current/userguide/upgrading_version_5.html#the_pluginmanagement_block_in_settings_scripts_is_now_isolated
pluginManagement.repositories {
    // TomTom's Nexus repository for the Digital Cockpit.
    tomtomNexus()
}

dependencyResolutionManagement {
    repositories {
        // Local artifact cache.
        mavenLocal()

        // TomTom's Nexus repository for the Digital Cockpit.
        tomtomNexus()

        // TomTom's Nexus repository for the Connectivity Agent.
        maven("https://maven.tomtom.com:8443/nexus/content/repositories/releases/")

        // External repositories.
        mavenCentral()
        google()
        maven("https://plugins.gradle.org/m2/")
    }
}
