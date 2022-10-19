package com.example.ivi.example.systemui.custompaneltype.systemui

import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.FrontendCoordinator
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.frontendcoordination.DefaultFrontendCoordinationRules
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.CoreSystemUiViewModel
import com.tomtom.tools.android.api.lifecycle.LifecycleViewModel

/**
 * The view model for the system UI. It exposes a subset of the data from the [frontendCoordinator]
 * to the view layer.
 */
internal class CustomSystemUiViewModel(
    coreViewModel: CoreSystemUiViewModel
) : LifecycleViewModel() {

    private val frontendCoordinator = FrontendCoordinator(
        lifecycleOwner = this,
        coreViewModel.iviServiceProvider,
        coreViewModel.frontendMetadata,
        coreViewModel.defaultFrontendContextFactory,
        { frontendRegistry ->
            CustomPanelRegistry.create(
                frontendRegistry,
                lifecycleOwner = this,
                coreViewModel.iviServiceProvider
            )
        },
        { frontendRegistry, panelRegistry ->
            DefaultFrontendCoordinationRules
                .create(frontendRegistry, panelRegistry.iviPanelRegistry)
        }
    )

    val panelRegistry = frontendCoordinator.panelRegistry

    val menuPanel = panelRegistry.iviPanelRegistry.mainMenuPanel

    val customPanel = panelRegistry.customPanel
}
