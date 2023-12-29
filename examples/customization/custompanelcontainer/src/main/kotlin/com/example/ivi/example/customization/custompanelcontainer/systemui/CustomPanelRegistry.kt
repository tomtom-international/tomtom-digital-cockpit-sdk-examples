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

package com.example.ivi.example.customization.custompanelcontainer.systemui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.ivi.example.customization.custompanelcontainer.common.CustomPanel
import com.example.ivi.example.customization.custompanelcontainer.container.DualPanelContainerData
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviInstanceBoundIviServiceProvider
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.AnyPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.filterPanels
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.FrontendRegistry
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.IviPanelRegistry
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.IviPanelRegistry.Companion.extractPanels
import com.tomtom.ivi.platform.systemui.api.common.frontendcoordinator.PanelRegistry

/**
 * A [PanelRegistry] that is aware of [CustomPanel]s and exposes them as [LiveData] to be observed
 * by other parts of the system UI.
 */
internal class CustomPanelRegistry(
    val dualPanels: LiveData<DualPanelContainerData?>,
    val iviPanelRegistry: IviPanelRegistry,
) : PanelRegistry {

    companion object {
        fun create(
            frontendRegistry: FrontendRegistry,
            lifecycleOwner: LifecycleOwner,
            iviServiceProvider: IviInstanceBoundIviServiceProvider,
        ) = CustomPanelRegistry(
            dualPanels = frontendRegistry.frontends.extractPanels().toDualPanelContainerData(),
            iviPanelRegistry = IviPanelRegistry.build(
                frontendRegistry.extractPanels(),
                lifecycleOwner,
                iviServiceProvider,
            ),
        )
    }
}

private fun LiveData<Collection<AnyPanel>>.toDualPanelContainerData() = this
    .filterPanels<CustomPanel>()
    .map { it.toDualPanelContainerData() }

private fun List<CustomPanel>.toDualPanelContainerData() = this
    .takeLast(2)
    .takeIf { it.size >= 2 }
    ?.run { DualPanelContainerData(this[0], this[1]) }
