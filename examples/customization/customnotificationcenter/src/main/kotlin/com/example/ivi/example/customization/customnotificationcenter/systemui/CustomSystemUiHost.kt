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

package com.example.ivi.example.customization.customnotificationcenter.systemui

import com.example.ivi.example.customization.customnotificationcenter.databinding.TtiviCustomnotificationcenterCustomsystemuiBinding
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ControlCenterPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.DebugPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ExpandedProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.HomePanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.MainMenuPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.MainProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ModalPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.NavigationPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.NotificationPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.OverlayPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PanelTypeSet
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.SettingsPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.panelTypeSetOf
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHost
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHostContext
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.ControlCenterPanelSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.IviPanelRegistrySystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.NotificationDisplaySystemUiHostExtension
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * The system UI host is the overarching class of the system UI. It is responsible for creating the
 * view through the [viewFactory].
 *
 * See the
 * [system UI guide](https://developer.tomtom.com/tomtom-digital-cockpit/documentation/development/system-ui)
 * for more information on creating custom system UI's.
 */
internal class CustomSystemUiHost(systemUiHostContext: SystemUiHostContext) :
    SystemUiHost(systemUiHostContext) {

    override val viewFactory: ViewFactory =
        BindingViewFactory(
            TtiviCustomnotificationcenterCustomsystemuiBinding::inflate,
            ::bindSystemUiView,
        )

    override val supportedPanelTypes: PanelTypeSet = panelTypeSetOf(
        ControlCenterPanel::class,
        NotificationPanel::class,
    )

    override val unsupportedPanelTypes: PanelTypeSet = panelTypeSetOf(
        DebugPanel::class,
        ExpandedProcessPanel::class,
        HomePanel::class,
        MainMenuPanel::class,
        MainProcessPanel::class,
        ModalPanel::class,
        NavigationPanel::class,
        OverlayPanel::class,
        SettingsPanel::class,
        TaskPanel::class,
        TaskProcessPanel::class,
    )

    private lateinit var panelRegistryExtension: IviPanelRegistrySystemUiHostExtension
    private lateinit var controlCenterExtension: ControlCenterPanelSystemUiHostExtension
    private lateinit var notificationDisplayExtension: NotificationDisplaySystemUiHostExtension

    private val createAfterStartupFrontendsTrigger = MutableStateFlow(false)

    override fun onCreate() {
        panelRegistryExtension =
            IviPanelRegistrySystemUiHostExtension(
                systemUiHostExtensionContext,
                createAfterStartupFrontendsTrigger,
            )
        controlCenterExtension = ControlCenterPanelSystemUiHostExtension(
            systemUiHostExtensionContext,
            panelRegistryExtension.panelRegistry,
        )
        notificationDisplayExtension = NotificationDisplaySystemUiHostExtension(
            systemUiHostExtensionContext,
            panelRegistryExtension.panelRegistry,
        )
    }

    private fun bindSystemUiView(binding: TtiviCustomnotificationcenterCustomsystemuiBinding) {
        panelRegistryExtension.bindSystemUiView(binding)
        controlCenterExtension.bindSystemUiView(binding)
        notificationDisplayExtension.bindSystemUiView(binding)
    }

    override fun onSystemUiPresented() {
        createAfterStartupFrontendsTrigger.value = true
    }
}
