/*
 * Copyright (c) 2024 TomTom N.V. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom N.V. and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * licensee agreement between you and TomTom. If you are the licensee, you are only permitted
 * to use this Software in accordance with the terms of your license agreement. If you are
 * not the licensee then you are not authorised to use this software in any manner and should
 * immediately return it to TomTom N.V.
 */

/**
 * This file defines IVI theme registry sources and their builders.
 *
 * Each theme registry source is declared similarly as following:
 * ```
 * // The theming module is `examples_theming`.
 * private val themingModule = ExampleModuleReference("examples_theming")
 *
 * val customCoreComponentsThemeRegistrySource by extra {
 *     IviThemeRegistrySourceConfig(
 *         // The builder for the theme registry source. The name has to be identical to the one in
 *         // Kotlin.
 *         registrySourceBuilderName = "CustomCoreComponentsThemeRegistrySourceBuilder",
 *         // The path of the implementation module.
 *         implementationModule = themingModule,
 *     )
 * }
 * ```
 *
 * The properties defined in this file can be accessed in the `build.gradle.kts` files by applying
 * this file in the `build.gradle.kts` file and by using Gradle's extra extension. For example,
 *
 * ```kotlin
 * import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviThemeRegistrySourceConfig
 *
 * apply(from = rootProject.file("themeregistrysources.gradle.kts"))
 *
 * val customCoreComponentsThemeRegistrySource: IviThemeRegistrySourceConfig by project.extra
 *
 * iviInstances {
 *     create(IviInstanceIdentifier.default) {
 *         theming {
 *             addRegistrySources(
 *                 customCoreComponentsThemeRegistrySource,
 *             )
 *         }
 *     }
 * }
 *
 * ...
 * ```
 *
 * The above example obtains the `customCoreComponentsThemeRegistrySource` property as defined in
 * this file and adds it to the IVI application.
 *
 * To allow the Gradle's extra extensions to work, the properties in this file need to use the
 * `by extra` delegation to assign the value.
 */

import com.tomtom.ivi.buildsrc.dependencies.ExampleModuleReference
import com.tomtom.ivi.platform.gradle.api.common.iviapplication.config.IviThemeRegistrySourceConfig

// The theming module is `examples_theming`.
private val themingModule = ExampleModuleReference("examples_theming")

val customCoreComponentsThemeRegistrySource by extra {
    IviThemeRegistrySourceConfig(
        registrySourceBuilderName = "CustomCoreComponentsThemeRegistrySourceBuilder",
        implementationModule = themingModule,
    )
}

val customCommunicationsThemeRegistrySource by extra {
    IviThemeRegistrySourceConfig(
        registrySourceBuilderName = "CustomCommunicationsThemeRegistrySourceBuilder",
        implementationModule = themingModule,
    )
}

val customHvacThemeRegistrySource by extra {
    IviThemeRegistrySourceConfig(
        registrySourceBuilderName = "CustomHvacThemeRegistrySourceBuilder",
        implementationModule = themingModule,
    )
}

val customMainMenuThemeRegistrySource by extra {
    IviThemeRegistrySourceConfig(
        registrySourceBuilderName = "CustomMainMenuThemeRegistrySourceBuilder",
        implementationModule = themingModule,
    )
}

val customMediaThemeRegistrySource by extra {
    IviThemeRegistrySourceConfig(
        registrySourceBuilderName = "CustomMediaThemeRegistrySourceBuilder",
        implementationModule = themingModule,
    )
}

val customNavAppComponentsThemeRegistrySource by extra {
    IviThemeRegistrySourceConfig(
        registrySourceBuilderName = "CustomNavAppComponentsThemeRegistrySourceBuilder",
        implementationModule = themingModule,
    )
}

val customSystemStatusThemeRegistrySource by extra {
    IviThemeRegistrySourceConfig(
        registrySourceBuilderName = "CustomSystemStatusThemeRegistrySourceBuilder",
        implementationModule = themingModule,
    )
}

val customSystemUiThemeRegistrySource by extra {
    IviThemeRegistrySourceConfig(
        registrySourceBuilderName = "CustomSystemUiThemeRegistrySourceBuilder",
        implementationModule = themingModule,
    )
}

val defaultThemeComponentsSelectorThemeRegistrySource by extra {
    IviThemeRegistrySourceConfig(
        registrySourceBuilderName = "DefaultThemeComponentsSelectorThemeRegistrySourceBuilder",
        implementationModule = themingModule,
    )
}
