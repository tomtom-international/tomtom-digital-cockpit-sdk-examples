/*
 * Copyright © 2024 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.tomtom.ivi.buildsrc.environment

/**
 * Class to wrap and compare module versions of the format x.y.z.
 * @param string The version of the module.
 */
class ModuleVersion(version: String?) : Comparable<ModuleVersion?> {
    private val version: String

    init {
        requireNotNull(version) { "Version can not be null" }
        require(version.matches(SEMANTIC_VERSION_REGEX)) {
            "Invalid version format"
        }
        this.version = version
    }

    private fun splitIntoParts(): Array<String> = version.split("\\.".toRegex()).toTypedArray()

    override operator fun compareTo(other: ModuleVersion?): Int {
        if (other == null) return 1
        val thisParts = this.splitIntoParts()
        val otherParts = other.splitIntoParts()
        val length = Math.max(thisParts.size, otherParts.size)
        for (i in 0 until length) {
            val thisPart = if (i < thisParts.size) thisParts[i].toInt() else 0
            val otherPart = if (i < otherParts.size) otherParts[i].toInt() else 0
            if (thisPart != otherPart) return thisPart.compareTo(otherPart)
        }
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        return if (this.javaClass != other.javaClass) {
            false
        } else {
            this.compareTo(other as ModuleVersion) == 0
        }
    }

    override fun hashCode(): Int = version.hashCode()

    companion object {
        public val SEMANTIC_VERSION_REGEX = Regex("[0-9]+(\\.[0-9]+)(\\.[0-9]+)*")
    }
}
