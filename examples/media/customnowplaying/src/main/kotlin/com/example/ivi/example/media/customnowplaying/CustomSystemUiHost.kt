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

package com.example.ivi.example.media.customnowplaying

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaExpandedProcessPanelBase
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ControlCenterPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.DebugPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ExpandedProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.GuidancePanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.HomePanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.MainMenuPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.MainProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ModalPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.NotificationPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.OverlayPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PanelTypeSet
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.SearchPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.SettingsPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.panelTypeSetOf
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.IviFragmentFactory
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHost
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHostContext
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.bindSystemUiView
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.StockAdaptiveSystemUiHelper
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.StockSystemUiHost
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.BackPressConsumersSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.ControlCenterPanelSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.DebugPanelSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.DebugSystemUiLayoutTagSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.IviPanelRegistrySystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.MainProcessPanelPositionSystemUiHostExtension

/**
 * This is a custom implementation of a [SystemUiHost], mostly based on [StockSystemUiHost].
 *
 * We used custom system UI host here to add [CustomMediaExpandedProcessFragmentRule] to
 * replace the stock fragment of the [MediaExpandedProcessPanelBase]. This is done by overriding the
 * [fragmentFactory].
 *
 * See the
 * [system UI guide](https://developer.tomtom.com/tomtom-digital-cockpit/documentation/development/system-ui)
 * for more information on creating a custom system UI.
 */
internal class CustomSystemUiHost(
    systemUiHostContext: SystemUiHostContext
) : SystemUiHost(systemUiHostContext) {

    override val supportedPanelTypes: PanelTypeSet = panelTypeSetOf(
        ControlCenterPanel::class,
        DebugPanel::class,
        ExpandedProcessPanel::class,
        GuidancePanel::class,
        SettingsPanel::class,
        HomePanel::class,
        MainMenuPanel::class,
        MainProcessPanel::class,
        ModalPanel::class,
        NotificationPanel::class,
        OverlayPanel::class,
        SearchPanel::class,
        TaskPanel::class,
        TaskProcessPanel::class,
    )

    /**
     * Here we apply our [CustomMediaExpandedProcessFragmentRule] to replace the stock media main
     * process fragment.
     */
    override val fragmentFactory: IviFragmentFactory =
        IviFragmentFactory.create {
            addRule(CustomMediaExpandedProcessFragmentRule())
        }

    private lateinit var panelRegistryExtension: IviPanelRegistrySystemUiHostExtension

    private lateinit var systemUiHostExtensions: List<SystemUiHostExtension>

    private val adaptiveSystemUiHelper =
        StockAdaptiveSystemUiHelper(context, bindDataFunction = ::bindSystemUiView)

    override val viewFactory: ViewFactory by adaptiveSystemUiHelper::viewFactory

    override val isReadyToPresentSystemUi: LiveData<Boolean> by lazy {
        panelRegistryExtension.areImmediatelyStartedFrontendsReady
    }

    override fun onCreate() {
        panelRegistryExtension =
            IviPanelRegistrySystemUiHostExtension(systemUiHostExtensionContext)
        systemUiHostExtensions = listOf(
            panelRegistryExtension,
            MainProcessPanelPositionSystemUiHostExtension(
                systemUiHostExtensionContext,
                panelRegistryExtension.panelRegistry
            ),
            ControlCenterPanelSystemUiHostExtension(
                systemUiHostExtensionContext,
                panelRegistryExtension.panelRegistry.controlCenterPanels
            ),
            BackPressConsumersSystemUiHostExtension(systemUiHostExtensionContext),
            DebugPanelSystemUiHostExtension(systemUiHostExtensionContext),
            DebugSystemUiLayoutTagSystemUiHostExtension(systemUiHostExtensionContext),
        )
    }

    private fun bindSystemUiView(binding: ViewDataBinding) {
        systemUiHostExtensions.bindSystemUiView(binding)
    }
}
