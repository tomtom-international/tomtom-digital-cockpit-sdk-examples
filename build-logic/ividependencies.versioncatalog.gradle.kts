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

val versionLibraryFile = "libraries.versions.toml"

/**
 * This is a workaround to be able to import the correct dependencies catalog produced and published
 * by the digital-cockpit repository. When the version catalogs are created below, there are no
 * accessors available. Therefore the value of `iviPlatform` can not be retrieved using the
 * accessors.
 */
fun getIviPlatformVersionFromTomlFile() : String {
    val localLibrariesTomlFile = File(files(versionLibraryFile).asPath)
    val versionRegex = Regex("""iviPlatform = "([^"]*)"""")
    localLibrariesTomlFile.useLines { lines ->
        return lines.mapNotNull { versionRegex.matchEntire(it) }.first().groupValues[1]
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    versionCatalogs {
        create("iviDependencies") {
            val group = "com.tomtom.ivi.platform"
            val artifact = "dependencies-catalog"
            val version = getIviPlatformVersionFromTomlFile()
            from("${group}:${artifact}:${version}")
        }
    }
}
