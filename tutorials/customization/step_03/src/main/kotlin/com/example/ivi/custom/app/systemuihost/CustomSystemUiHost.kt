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

package com.example.ivi.custom.app.systemuihost

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.example.ivi.custom.app.databinding.CustomSystemuiBinding
import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
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
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHost
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHostContext
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.bindSystemUiView
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.BackPressConsumersSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.ControlCenterPanelSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.DebugPanelSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.DebugSystemUiLayoutTagSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.IviPanelRegistrySystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.extensions.MainProcessPanelPositionSystemUiHostExtension
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.StockAdaptiveSystemUiHelper

/**
 * The stock system UI host implementation, providing TomTom Digital Cockpit's default look & feel.
 *
 * May be used by an activity and other components capable of displaying the system UI, like a
 * VirtualDisplay.
 */
@IviExperimental
public class CustomSystemUiHost(systemUiHostContext: SystemUiHostContext) :
    SystemUiHost(systemUiHostContext) {

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

    private lateinit var panelRegistryExtension: IviPanelRegistrySystemUiHostExtension

    private lateinit var systemUiHostExtensions: List<SystemUiHostExtension>

    private val adaptiveSystemUiHelper =
        StockAdaptiveSystemUiHelper(context, bindDataFunction = ::bindSystemUiView)

    override val viewFactory: ViewFactory = BindingViewFactory(
        CustomSystemuiBinding::inflate,
        ::bindSystemUiView
    )

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
                panelRegistryExtension.panelRegistry.mainProcessPanel
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

    override fun toString(): String {
        return "${this::class.simpleName}(panelRegistryExtension=$panelRegistryExtension)"
    }

    private fun bindSystemUiView(binding: ViewDataBinding) {
        systemUiHostExtensions.bindSystemUiView(binding)
    }
}
