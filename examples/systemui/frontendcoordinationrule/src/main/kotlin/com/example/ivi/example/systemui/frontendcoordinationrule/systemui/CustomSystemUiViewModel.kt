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

package com.example.ivi.example.systemui.frontendcoordinationrule.systemui

import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.FrontendCoordinationRulesFactory
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.FrontendCoordinator
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.frontendcoordination.DefaultFrontendCoordinationRules
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.frontendcoordination.FrontendCoordinationRule
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.CoreSystemUiViewModel
import com.tomtom.ivi.platform.systemui.api.service.debugpanel.DebugPanelService
import com.tomtom.ivi.platform.systemui.api.service.debugpanel.createApiOrNull
import com.tomtom.tools.android.api.lifecycle.LifecycleViewModel
import com.tomtom.tools.android.api.livedata.ImmutableLiveData

/**
 * The view model for the system UI. It exposes a subset of the data from the [frontendCoordinator]
 * to the view layer.
 *
 * Custom [FrontendCoordinationRule]s are added by providing a
 * [FrontendCoordinationRulesFactory] to [frontendCoordinator] that creates new rules. In this example
 * [CloseDebugPanelOnMenuItemClickFrontendCoordinationRule] is added to a collection of default rules.
 */
internal class CustomSystemUiViewModel(
    coreViewModel: CoreSystemUiViewModel
) : LifecycleViewModel() {

    val debugPanelService =
        DebugPanelService.createApiOrNull(this, coreViewModel.iviServiceProvider)

    val shouldShowDebugPanel = debugPanelService?.shouldShowDebugPanel
        ?: ImmutableLiveData(false)

    private val frontendCoordinator = FrontendCoordinator.createDefault(
        lifecycleOwner = this,
        iviServiceProvider = coreViewModel.iviServiceProvider,
        frontendMetadata = coreViewModel.frontendMetadata,
        frontendContextFactory = coreViewModel.defaultFrontendContextFactory,
        frontendCoordinationRulesFactory = { frontendRegistry, panelRegistry ->
            val defaultRules = DefaultFrontendCoordinationRules.create(
                frontendRegistry,
                panelRegistry
            )
            defaultRules + CloseDebugPanelOnMenuItemClickFrontendCoordinationRule(debugPanelService)
        }
    )

    val panelRegistry = frontendCoordinator.panelRegistry

    val menuPanel = panelRegistry.mainMenuPanel
}
