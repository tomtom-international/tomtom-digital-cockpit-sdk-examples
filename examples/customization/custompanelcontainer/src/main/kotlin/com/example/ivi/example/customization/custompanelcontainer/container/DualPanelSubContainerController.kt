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
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.findPanelFragmentContainerAndSetId
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.inflateSubContainerAndBindViewModel
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.panelfragmentadapter.PanelFragmentAdapterBuilder
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.panelfragmentadapter.SinglePanelAttachment
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.subcontainercontroller.StateDrivenSubContainerController

internal typealias DualPanelContainerViewModel =
    PanelContainerViewModel<
        DualPanelContainerData?,
        DualPanelSubContainerViewModel,
        DualPanelContainerData
        >

internal typealias DualPanelSubContainerDataUpdate =
    StateDrivenSubContainerController.SubContainerDataUpdate<
        DualPanelSubContainerViewModel,
        DualPanelContainerData
        >

/**
 * Contains the custom logic to create and update sub-containers when underlying data changes.
 * @param subContainerLayoutId layout of sub-containers that this container creates.
 * It must have [FragmentContainerView]s with ids `ttivi_custompanelcontainer_panel_1` and
 * `ttivi_custompanelcontainer_panel_2`.
 */
internal class DualPanelSubContainerController(
    @LayoutRes private val subContainerLayoutId: Int? = null
) :
    StateDrivenSubContainerController<
        DualPanelContainerData?,
        DualPanelSubContainerViewModel,
        DualPanelContainerData> {

    override fun getNewSubContainerData(
        panelContainerViewModel: DualPanelContainerViewModel
    ) = listOfNotNull(
        panelContainerViewModel
            .panelContainerData
            ?.takeUnless { panelContainerViewModel.hasSubContainerViewModelWithData(it) }
    )

    override fun createSubContainer(
        panelContainer: ViewGroup,
        subContainerViewModel: DualPanelSubContainerViewModel,
        panelContainerViewModel: DualPanelContainerViewModel
    ): StateDrivenSubContainerController.SubContainerCreationResult<
        DualPanelSubContainerViewModel,
        DualPanelContainerData
        > {

        val subContainerHolder = inflateSubContainerAndBindViewModel(
            panelContainer,
            subContainerLayoutId,
            subContainerViewModel
        )

        val panelFragment1Holder = subContainerHolder.findPanelFragmentContainerAndSetId(
            subContainerViewModel.panelFragmentContainer1Id,
            R.id.ttivi_custompanelcontainer_panel_1
        )

        val panelFragment2Holder = subContainerHolder.findPanelFragmentContainerAndSetId(
            subContainerViewModel.panelFragmentContainer2Id,
            R.id.ttivi_custompanelcontainer_panel_2
        )

        val panelFragmentAdapterBuilder: PanelFragmentAdapterBuilder.() -> Unit = {
            createSingleFragmentPanelAdapter(panelFragment1Holder) {
                SinglePanelAttachment(subContainerViewModel.subContainerData.panel1) { it }
            }

            createSingleFragmentPanelAdapter(panelFragment2Holder) {
                SinglePanelAttachment(subContainerViewModel.subContainerData.panel2) { it }
            }
        }

        return StateDrivenSubContainerController.SubContainerCreationResult(
            subContainerHolder,
            panelFragmentAdapterBuilder
        )
    }

    override fun getSubContainerDataUpdates(
        panelContainerViewModel: DualPanelContainerViewModel
    ): Collection<DualPanelSubContainerDataUpdate> {
        val data = panelContainerViewModel.panelContainerData ?: return emptyList()
        val subContainerViewModel = panelContainerViewModel
            .subContainerViewModels
            .firstOrNull() ?: return emptyList()

        val update = StateDrivenSubContainerController.SubContainerDataUpdate(
            subContainerViewModel,
            data
        )

        return listOf(update)
    }

    override fun isSubContainerExitAllowed(subContainerViewModel: DualPanelSubContainerViewModel) =
        true
}

private fun <T> PanelContainerViewModel<*, *, T>.hasSubContainerViewModelWithData(data: T) =
    subContainerViewModels.any { it.subContainerData == data && !it.isExiting() }
