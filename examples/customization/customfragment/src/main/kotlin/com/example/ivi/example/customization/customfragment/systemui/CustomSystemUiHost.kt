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

package com.example.ivi.example.customization.customfragment.systemui

import androidx.lifecycle.ViewModelProvider
import com.example.ivi.example.customization.customfragment.databinding.TtiviCustomfragmentCustomsystemuiBinding
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
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
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.Panel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PanelTypeSet
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.SettingsPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.panelTypeSetOf
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FixedConstructorFactory
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.CustomFragmentRule
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.IviFragmentFactory
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHost
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHostContext

/**
 * The system UI host is the overarching class of the system UI. It's responsible for creating the
 * [viewModel], and for creating the view through the [viewFactory].
 *
 * The [fragmentFactory] has the responsibility of creating [IviFragment]s for [Panel]s. It does so
 * by applying [CustomFragmentRule]s. These rules can customize the [IviFragment]s created for
 * [Panel]s.
 *
 * See the
 * [system UI guide](https://developer.tomtom.com/tomtom-digital-cockpit/documentation/development/system-ui)
 * for more information on creating custom system UI's.
 */
internal class CustomSystemUiHost(
    systemUiHostContext: SystemUiHostContext,
) : SystemUiHost(systemUiHostContext) {

    /**
     * The fragment factory determines which [IviFragment] is created for which [Panel].
     *
     * It accepts a list of [CustomFragmentRule]s in it's constructor. The first rule in the list
     * that accepts to create the fragment for a given panel, creates the fragment. If no rule
     * accepts, the panel creates the fragment.
     */
    override val fragmentFactory: IviFragmentFactory =
        IviFragmentFactory.create {
            addRule(CustomNotificationFragmentRule())
        }

    override val viewFactory: ViewFactory =
        BindingViewFactory(TtiviCustomfragmentCustomsystemuiBinding::inflate, ::bindSystemUiView)

    override val supportedPanelTypes: PanelTypeSet = panelTypeSetOf(
        NotificationPanel::class,
        TaskPanel::class,
        TaskProcessPanel::class,
    )

    override val unsupportedPanelTypes: PanelTypeSet = panelTypeSetOf(
        ControlCenterPanel::class,
        DebugPanel::class,
        ExpandedProcessPanel::class,
        HomePanel::class,
        MainMenuPanel::class,
        MainProcessPanel::class,
        ModalPanel::class,
        NavigationPanel::class,
        OverlayPanel::class,
        SettingsPanel::class,
    )

    private lateinit var viewModel: CustomSystemUiViewModel

    override fun onCreate() {
        viewModel = ViewModelProvider(
            viewModelStoreOwner,
            FixedConstructorFactory(coreViewModel),
        )[CustomSystemUiViewModel::class.java]
    }

    override fun onSystemUiPresented() {
        viewModel.frontendCoordinator.onSystemUiPresented()
    }

    private fun bindSystemUiView(binding: TtiviCustomfragmentCustomsystemuiBinding) {
        binding.viewModel = viewModel
        binding.panelRegistry = viewModel.panelRegistry

        setIviOnBackPressedCallbacks(listOf(binding.exampleCustomizationTaskPanelStackContainer))
    }
}
