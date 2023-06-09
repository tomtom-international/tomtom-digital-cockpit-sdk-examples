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

package com.tomtom.ivi.buildsrc.extensions

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

/**
 * Accessor for `libraries` version catalog for the cases where generated accessors are not
 * available and catalog can only be accessed through a type unsafe API.
 *
 * https://docs.gradle.org/current/userguide/platforms.html#sub:type-unsafe-access-to-catalog
 */
val Project.librariesVersionCatalog: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("libraries")

/**
 * Accessor for `iviDependencies` version catalog for the cases where generated accessors are not
 * available and catalog can only be accessed through a type unsafe API.
 *
 * https://docs.gradle.org/current/userguide/platforms.html#sub:type-unsafe-access-to-catalog
 */
val Project.iviDependenciesVersionCatalog: VersionCatalog
    get() = extensions.getByType(VersionCatalogsExtension::class.java).named("iviDependencies")

/**
 * Gets the version from version catalog by its alias.
 * Asserts that version is present.
 */
fun VersionCatalog.getVersionOrThrow(alias: String): String {
    val versionConstraint = findVersion(alias)
    gradleAssert(versionConstraint.isPresent) {
        "Version catalog does not specify the version for provided alias, " +
            "alias='$alias', versionCatalog='$name'."
    }
    return versionConstraint.get().toString()
}
