package com.example.ivi.example.systemui.custompaneltype.common

import com.example.ivi.example.systemui.custompaneltype.frontend.ExampleFrontend
import com.example.ivi.example.systemui.custompaneltype.systemui.CustomSystemUiHost
import com.tomtom.ivi.platform.frontend.api.common.frontend.Frontend
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PLATFORM_PANEL_TYPES
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.Panel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PanelTypeSet

/**
 * The custom panel type that is the center of this example.
 *
 * The [CustomSystemUiHost] is responsible for displaying this panel type. The [ExampleFrontend]
 * supplies the content by adding a panel of this type.
 */
abstract class CustomSystemUiPanel(frontendContext: FrontendContext) : Panel(frontendContext)

/**
 * A set of panel types that extends the built-in [PLATFORM_PANEL_TYPES] with the
 * [CustomSystemUiPanel] panel type.
 *
 * A [Frontend] configuration in a `build.gradle.kts` file can use these to request support in the
 * system UI for these panel types.
 */
val CUSTOM_SYSTEM_UI_PANEL_TYPES: PanelTypeSet = PLATFORM_PANEL_TYPES + CustomSystemUiPanel::class
