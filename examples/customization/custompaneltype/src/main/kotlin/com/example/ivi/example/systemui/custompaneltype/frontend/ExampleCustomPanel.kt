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

package com.example.ivi.example.customization.custompaneltype.frontend

import androidx.lifecycle.LiveData
import com.example.ivi.example.customization.custompaneltype.common.CustomSystemUiPanel
import com.example.ivi.example.customization.custompaneltype.databinding.TtiviCustompaneltypeCustompanelBinding
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel

/**
 * The implementation of [CustomSystemUiPanel].
 */
internal class ExampleCustomPanel(
    frontendContext: FrontendContext,
    val isTaskPanelOpened: LiveData<Boolean>,
) : CustomSystemUiPanel(frontendContext) {
    override fun createInitialFragmentInitializer(): IviFragment.Initializer<*, *> =
        IviFragment.Initializer(ExampleCustomFragment(), this)
}

internal class ExampleCustomFragment :
    IviFragment<ExampleCustomPanel, ExampleCustomViewModel>(ExampleCustomViewModel::class) {

    override val viewFactory: ViewFactory<*>? =
        ViewFactory(TtiviCustompaneltypeCustompanelBinding::inflate)
}

internal class ExampleCustomViewModel(panel: ExampleCustomPanel) :
    FrontendViewModel<ExampleCustomPanel>(panel) {

    val isTaskPanelOpened = panel.isTaskPanelOpened
}
