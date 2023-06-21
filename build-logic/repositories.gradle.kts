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

val artifactoryRepo: String? by settings

fun RepositoryHandler.tomtomArtifactory() {
    val repoName = artifactoryRepo ?: "ivi"
    maven("https://repository.tomtom.com/artifactory/$repoName") {
        credentials {
            username = extra["artifactoryEdgeUser"].toString()
            password = extra["artifactoryEdgeToken"].toString()
        }
        content {
            includeGroupByRegex("com\\.tomtom\\..+")
        }
    }
}

pluginManagement.repositories {
    // Local artifact cache.
    mavenLocal()
    
    if (extra.has("artifactoryEdgeUser") && extra.has("artifactoryEdgeToken")) {
        tomtomArtifactory()
    } else if (extra.has("nexusUsername") && extra.has("nexusPassword")) {
        maven("https://repo.tomtom.com/repository/ivi") {
            credentials {
                username = extra["nexusUsername"].toString()
                password = extra["nexusPassword"].toString()
            }
            content {
                includeGroupByRegex("com\\.tomtom\\..+")
            }
        }
    }
    // External repositories.
    mavenCentral()
    google()
    maven("https://plugins.gradle.org/m2/")
}

dependencyResolutionManagement {
    repositories {
        // Local artifact cache.
        mavenLocal()
        
        if (extra.has("artifactoryEdgeUser") && extra.has("artifactoryEdgeToken")) {
            tomtomArtifactory()
        } else if (extra.has("nexusUsername") && extra.has("nexusPassword")) {
            maven("https://repo.tomtom.com/repository/ivi") {
                credentials {
                    username = extra["nexusUsername"].toString()
                    password = extra["nexusPassword"].toString()
                }
                content {
                    includeGroupByRegex("com\\.tomtom\\..+")
                    includeGroup("com.tomtom")
                    includeGroup("com.amazon.alexa.aace")
                }
            }
        }
        // External repositories.
        mavenCentral()
        google()
        maven("https://plugins.gradle.org/m2/")
    }
}
