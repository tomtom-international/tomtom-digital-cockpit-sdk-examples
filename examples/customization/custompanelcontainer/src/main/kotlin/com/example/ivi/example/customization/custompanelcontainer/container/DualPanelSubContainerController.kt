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

package com.example.ivi.example.customization.custompanelcontainer.container

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentContainerView
import com.example.ivi.example.customization.custompanelcontainer.R
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.PanelContainerViewModel
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.inflateSubContainerAndBindViewModel
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.panelfragmentadapter.SinglePanelAttachmentProvider
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.subcontainercontroller.StateDrivenSubContainerController
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.subcontainercontroller.SubContainerCreation

internal typealias DualPanelContainerViewModel =
    PanelContainerViewModel<
        DualPanelContainerData?,
        DualPanelSubContainerViewModel,
        DualPanelContainerData,
        >

internal typealias DualPanelSubContainerDataUpdate =
    StateDrivenSubContainerController.SubContainerDataUpdate<
        DualPanelSubContainerViewModel,
        DualPanelContainerData,
        >

/**
 * Contains the custom logic to create and update sub-containers when underlying data changes.
 * @param subContainerLayoutId layout of sub-containers that this container creates.
 * It must have [FragmentContainerView]s with ids `ttivi_custompanelcontainer_panel_1` and
 * `ttivi_custompanelcontainer_panel_2`.
 */
internal class DualPanelSubContainerController(
    @LayoutRes private val subContainerLayoutId: Int? = null,
) :
    StateDrivenSubContainerController<
        DualPanelContainerData?,
        DualPanelSubContainerViewModel,
        DualPanelContainerData,> {

    override fun getNewSubContainerData(
        panelContainerViewModel: DualPanelContainerViewModel,
    ) = listOfNotNull(
        panelContainerViewModel
            .panelContainerData
            ?.takeUnless { panelContainerViewModel.hasSubContainerViewModelWithData(it) },
    )

    override fun createSubContainer(
        panelContainer: ViewGroup,
        subContainerViewModel: DualPanelSubContainerViewModel,
        panelContainerViewModel: DualPanelContainerViewModel,
    ): SubContainerCreation<DualPanelSubContainerViewModel, DualPanelContainerData> =
        inflateSubContainerAndBindViewModel(
            panelContainer,
            subContainerLayoutId,
            subContainerViewModel,
        )
            .bindFragmentContainer(
                R.id.ttivi_custompanelcontainer_panel_1,
                subContainerViewModel.panelFragmentContainer1Id,
                SinglePanelAttachmentProvider { subContainerViewModel.subContainerData.panel1 },
            )
            .bindFragmentContainer(
                R.id.ttivi_custompanelcontainer_panel_2,
                subContainerViewModel.panelFragmentContainer2Id,
                SinglePanelAttachmentProvider { subContainerViewModel.subContainerData.panel2 },
            )

    override fun getSubContainerDataUpdates(
        panelContainerViewModel: DualPanelContainerViewModel,
    ): Collection<DualPanelSubContainerDataUpdate> {
        val data = panelContainerViewModel.panelContainerData ?: return emptyList()
        val subContainerViewModel = panelContainerViewModel
            .subContainerViewModels
            .firstOrNull() ?: return emptyList()

        val update = StateDrivenSubContainerController.SubContainerDataUpdate(
            subContainerViewModel,
            data,
        )

        return listOf(update)
    }

    override fun isSubContainerExitAllowed(subContainerViewModel: DualPanelSubContainerViewModel) =
        true
}

private fun <T> PanelContainerViewModel<*, *, T>.hasSubContainerViewModelWithData(data: T) =
    subContainerViewModels.any { it.subContainerData == data && !it.isExiting() }
