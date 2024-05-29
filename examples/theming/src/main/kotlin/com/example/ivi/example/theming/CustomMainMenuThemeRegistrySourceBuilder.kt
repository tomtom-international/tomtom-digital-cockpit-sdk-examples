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

package com.example.ivi.example.theming

import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySource
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySourceBuilder
import com.tomtom.ivi.platform.theming.api.common.registry.IviThemeRegistrySourceContext

/**
 * The builder builds [CustomMainMenuThemeRegistrySource].
 *
 * The builder is created by the gradle plugin API: `IviThemeRegistrySourceConfig`.
 * See `themeregistrysources.gradle.kts` for more details.
 */
@OptIn(IviExperimental::class)
class CustomMainMenuThemeRegistrySourceBuilder : IviThemeRegistrySourceBuilder {

    override fun build(
        registrySourceContext: IviThemeRegistrySourceContext,
    ): IviThemeRegistrySource =
        CustomMainMenuThemeRegistrySource(registrySourceContext)
}
