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

import android.content.Context
import android.util.AttributeSet
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.Panel
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.subcontainer.PanelSubContainerViewModel
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.subcontainercontroller.StateDrivenSubContainerController
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.panelcontainer.PanelContainer
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.panelcontainer.StateDrivenPanelContainer

/**
 * Display two [Panel]s side-by-side.
 *
 * This class is a central point of this example - a custom [PanelContainer]. All logic to
 * orchestrate sub-containers and their [Panel]s is implemented in base classes, custom
 * [PanelContainer]s must override two factory methods to provide custom implementations of
 * [PanelSubContainerViewModel] and [StateDrivenSubContainerController].
 */
internal class DualPanelContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    StateDrivenPanelContainer<
        DualPanelContainerData?,
        DualPanelSubContainerViewModel,
        DualPanelContainerData>(
        context,
        attrs,
        defStyleAttr
    ) {
    override fun createSubContainerController() =
        DualPanelSubContainerController(ttiviSubContainerLayoutId)

    override fun createSubContainerViewModel() =
        DualPanelSubContainerViewModel()
}

/**
 * Data model used by [DualPanelContainer] to display two [Panel]s.
 */
internal data class DualPanelContainerData(
    val panel1: Panel,
    val panel2: Panel
)
