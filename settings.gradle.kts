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

rootProject.name = "IVI_Examples"

apply(from = "build-logic/repositories.gradle.kts")
apply(from = "build-logic/libraries.versioncatalog.gradle.kts")
apply(from = "build-logic/ividependencies.versioncatalog.gradle.kts")

/**
 * Note:
 * This configuration file is intended for TomTom internal use and not relevant
 * to an external developer so it can be removed
 */
val cacheConfig: File = file("build-logic/cache.gradle.kts")
if (cacheConfig.exists()) {
    apply(from = cacheConfig.path)
}

val templateAppDir = File(rootProject.projectDir, "template")
fileTree(templateAppDir)
    .matching { include("**/build.gradle.kts") }
    .forEach { file ->
        val projectName = ":" + file.toProjectName(templateAppDir.parentFile)
        include(projectName)
        project(projectName).projectDir = file.parentFile
    }

val optInToAlexaExamples = (extra.get("optInToAlexaExamples") as String).toBoolean()
val examplesDir = File(rootProject.projectDir, "examples")
if (examplesDir.exists()) {
    fileTree(examplesDir)
        .matching {
            include("**/build.gradle.kts")
            if (!optInToAlexaExamples) {
                exclude("alexa/**")
            }
        }
        .forEach { file ->
            val projectName = ":" + file.toProjectName(examplesDir.parentFile)
            include(projectName)
            project(projectName).projectDir = file.parentFile
        }
}

fun File.toProjectName(dir: File): String = dir.toPath()
    .relativize(parentFile.toPath())
    .joinToString("_")
