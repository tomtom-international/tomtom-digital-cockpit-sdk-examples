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

package com.tomtom.ivi.buildsrc.dependencies

import com.tomtom.ivi.platform.gradle.api.common.dependencies.ModuleReference

/**
 * Example [ModuleReference] extension.
 *
 * Used in this project to reference modules in the build config. It converts the module names to
 * package names.
 *
 * For example, the module `examples_my_module` is converted into the package
 * `com.example.ivi.example.service.my_module`.
 */
class ExampleModuleReference(moduleName: String) : ModuleReference(
    groupName = GROUP_NAME,
    moduleName = moduleName,
    packageName = convertModuleNameToPackageName(moduleName)
) {
    companion object {
        private fun convertModuleNameToPackageName(moduleName: String): String {
            val hierarchy = moduleName.split("_")
            val module = hierarchy.drop(1).joinToString(".")
            return "$GROUP_NAME.$ROOT_NAME.$module"
        }

        private const val ROOT_NAME = "example"
        private const val GROUP_NAME = "com.example.ivi"
    }
}
