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

package com.example.ivi.example.customization.custompaneltype.systemui

import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.FrontendCoordinator
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.frontendcoordination.DefaultFrontendCoordinationRules
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.CoreSystemUiViewModel
import com.tomtom.tools.android.api.lifecycle.LifecycleViewModel

/**
 * The view model for the system UI. It exposes a subset of the data from the [frontendCoordinator]
 * to the view layer.
 */
internal class CustomSystemUiViewModel(
    coreViewModel: CoreSystemUiViewModel,
) : LifecycleViewModel() {

    val frontendCoordinator = FrontendCoordinator(
        lifecycleOwner = this,
        coreViewModel.iviServiceProvider,
        coreViewModel.frontendMetadata,
        coreViewModel.defaultFrontendContextFactory,
        { frontendRegistry ->
            CustomPanelRegistry.create(
                frontendRegistry,
                lifecycleOwner = this,
                coreViewModel.iviServiceProvider,
            )
        },
        { frontendRegistry, panelRegistry ->
            DefaultFrontendCoordinationRules
                .create(frontendRegistry, panelRegistry.iviPanelRegistry)
        },
    )

    val panelRegistry = frontendCoordinator.panelRegistry

    val menuPanel = panelRegistry.iviPanelRegistry.mainMenuPanel

    val customPanel = panelRegistry.customPanel
}
