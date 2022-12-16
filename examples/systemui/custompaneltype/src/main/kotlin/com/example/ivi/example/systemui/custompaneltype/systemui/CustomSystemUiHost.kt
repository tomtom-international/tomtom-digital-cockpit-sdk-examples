/*
 * Copyright © 2022 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.systemui.custompaneltype.systemui

import androidx.lifecycle.ViewModelProvider
import com.example.ivi.example.systemui.custompaneltype.common.CustomSystemUiPanel
import com.example.ivi.example.systemui.custompaneltype.databinding.TtiviCustompaneltypeCustomsystemuiBinding
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ControlCenterPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.DebugPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ExpandedProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.GuidancePanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.HomePanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.MainMenuPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ModalPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.NotificationPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.OverlayPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PanelTypeSet
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.MainProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.SearchPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.SettingsPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.panelTypeSetOf
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FixedConstructorFactory
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHost
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHostContext

/**
 * The system UI host is the overarching class of the system UI. It's responsible for creating the
 * [viewModel], and for creating the view through the [viewFactory].
 *
 * See the
 * [System UI guide](https://developer.tomtom.com/tomtom-digital-cockpit/documentation/development/system-ui)
 * for more information.
 */
internal class CustomSystemUiHost(systemUiHostContext: SystemUiHostContext) :
    SystemUiHost(systemUiHostContext) {

    private lateinit var viewModel: CustomSystemUiViewModel

    override val viewFactory: ViewFactory =
        BindingViewFactory(TtiviCustompaneltypeCustomsystemuiBinding::inflate, ::bindSystemUiView)

    override val supportedPanelTypes: PanelTypeSet = panelTypeSetOf(
        CustomSystemUiPanel::class,
        TaskPanel::class,
        TaskProcessPanel::class,
    )

    override val unsupportedPanelTypes: PanelTypeSet = panelTypeSetOf(
        ControlCenterPanel::class,
        DebugPanel::class,
        ExpandedProcessPanel::class,
        GuidancePanel::class,
        HomePanel::class,
        MainMenuPanel::class,
        MainProcessPanel::class,
        ModalPanel::class,
        NotificationPanel::class,
        OverlayPanel::class,
        SearchPanel::class,
        SettingsPanel::class,
    )

    override fun onCreate() {
        viewModel = ViewModelProvider(
            viewModelStoreOwner,
            FixedConstructorFactory(coreViewModel)
        )[CustomSystemUiViewModel::class.java]
    }

    private fun bindSystemUiView(binding: TtiviCustompaneltypeCustomsystemuiBinding) {
        binding.viewModel = viewModel
        binding.panelRegistry = viewModel.panelRegistry

        registerOnBackPressedConsumer(binding.exampleSystemuiTaskPanelContainer)
    }
}
