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

package com.tomtom.ivi.buildsrc.extensions

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.TestExtension
import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

val Project.android: BaseExtension
    get() = extensions.getByType(BaseExtension::class.java)

fun Project.android(action: Action<BaseExtension>) =
    action.execute(android)

val Project.androidTest: TestExtension
    get() = extensions.getByType(TestExtension::class.java)

fun Project.androidTest(action: Action<TestExtension>) =
    action.execute(androidTest)

fun BaseExtension.kotlinOptions(action: Action<KotlinJvmOptions>) =
    action.execute((this as ExtensionAware).extensions.getByType(KotlinJvmOptions::class.java))

/**
 * Retrieves a value from a project's `gradle.properties` files.
 */
fun Project.getGradleProperty(key: String, default: Boolean) =
    properties[key]?.toString()?.toBoolean() ?: default

/**
 * Retrieves a property from a project's ExtraProperties extension if available, or a default value
 * if not.
 *
 * @param propertyName The name of the property to attempt to retrieve.
 * @param defaultValue The default value to return if the property is not set.
 */
fun ExtraPropertiesExtension.getOrDefault(propertyName: String, defaultValue: Any): Any =
    if (has(propertyName)) {
        get(propertyName)!!
    } else {
        defaultValue
    }

/**
 * Assert variant for Gradle scripts.
 *
 * Assertions don't normally work in Gradle: this variant fails the script execution immediately.
 *
 * @param value Expression to verify.
 * @param lazyMessage Expression to report as error message.
 */
fun gradleAssert(value: Boolean, lazyMessage: () -> String) {
    if (!value) {
        throw GradleException(lazyMessage())
    }
}
