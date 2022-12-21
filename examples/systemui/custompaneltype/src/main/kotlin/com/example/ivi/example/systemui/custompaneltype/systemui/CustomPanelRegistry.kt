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

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.example.ivi.example.systemui.custompaneltype.common.CustomSystemUiPanel
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviInstanceBoundIviServiceProvider
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.Panel
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.FrontendRegistry
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.IviPanelRegistry
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.IviPanelRegistry.Companion.extractPanels
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.PanelRegistry
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.panelcoordination.mapToSingle
import com.tomtom.tools.android.api.livedata.flatMap

/**
 * Contains the currently active [Panel]s.
 *
 * If any frontend added a [CustomSystemUiPanel], then it can be found in [customPanel]. The
 * [Panel]s for TomTom Digital Cockpit's stock system UI can be found in [iviPanelRegistry].
 */
internal class CustomPanelRegistry(
    val customPanel: LiveData<CustomSystemUiPanel?>,
    val iviPanelRegistry: IviPanelRegistry
) : PanelRegistry {

    companion object {

        /**
         * Maps panels from all frontends in the [FrontendRegistry] to a field per panel type.
         *
         * Assumes that there is at most one [customPanel] present, through usage of [mapToSingle].
         */
        fun create(
            frontendRegistry: FrontendRegistry,
            lifecycleOwner: LifecycleOwner,
            iviServiceProvider: IviInstanceBoundIviServiceProvider
        ) = CustomPanelRegistry(
            customPanel = frontendRegistry.frontends.flatMap { it.panels }.mapToSingle(),
            iviPanelRegistry = IviPanelRegistry.build(
                frontendRegistry.extractPanels(),
                lifecycleOwner,
                iviServiceProvider
            )
        )
    }
}
