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

package com.example.ivi.example.customization.frontendcoordinationrule.systemui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviInstanceBoundIviServiceProvider
import com.tomtom.ivi.platform.frontend.api.common.frontend.Frontend
import com.tomtom.ivi.platform.mainmenu.api.common.menu.MenuItem
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.frontendcoordination.FrontendCoordinationRule
import com.tomtom.ivi.platform.systemui.api.service.debugpanel.DebugPanelServiceApi
import com.tomtom.ivi.platform.systemui.api.service.systemuimenuitems.SystemUiMenuItemsService
import com.tomtom.ivi.platform.systemui.api.service.systemuimenuitems.createApi

/**
 * Hides the debug panel container on any main menu item click.
 *
 * Custom [FrontendCoordinationRule] that is added by [CustomSystemUiViewModel].
 * This rule adds a custom listener to the system UI menu service to hide the debug panel container.
 * Other rules can do more customizations.
 *
 * @see [FrontendCoordinationRule] for more information.
 */
internal class CloseDebugPanelOnMenuItemClickFrontendCoordinationRule(
    private val debugPanelService: DebugPanelServiceApi?,
) : FrontendCoordinationRule {
    override fun activate(
        lifecycleOwner: LifecycleOwner,
        iviServiceProvider: IviInstanceBoundIviServiceProvider,
        frontends: LiveData<out Collection<Frontend>>,
    ) {
        SystemUiMenuItemsService.createApi(lifecycleOwner, iviServiceProvider)
            .addMenuItemEventListener(
                lifecycleOwner,
                object : SystemUiMenuItemsService.MenuItemEventListener {
                    override fun onMenuItemClicked(id: MenuItem.Id) {
                        debugPanelService?.showDebugPanelAsync(false)
                    }
                },
            )
    }
}
