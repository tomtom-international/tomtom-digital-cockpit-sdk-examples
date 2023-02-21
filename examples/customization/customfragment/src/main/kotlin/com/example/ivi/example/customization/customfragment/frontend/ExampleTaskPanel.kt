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

package com.example.ivi.example.customization.customfragment.frontend

import com.example.ivi.example.customization.customfragment.databinding.TtiviCustomfragmentExampletaskpanelBinding
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel

/**
 * A minimal [TaskPanel] used by the [ExampleFrontend]. It allows the user to add notifications, to
 * be able to see the custom fragment.
 */
internal class ExampleTaskPanel(
    frontendContext: FrontendContext,
    val addNotificationPanel: () -> Unit
) : TaskPanel(frontendContext) {
    override fun createInitialFragmentInitializer(): IviFragment.Initializer<*, *> =
        IviFragment.Initializer(ExampleTaskFragment(), this)
}

internal class ExampleTaskFragment :
    IviFragment<ExampleTaskPanel, ExampleTaskViewModel>(ExampleTaskViewModel::class) {
    override val viewFactory = ViewFactory(TtiviCustomfragmentExampletaskpanelBinding::inflate)
}

internal class ExampleTaskViewModel(panel: ExampleTaskPanel) :
    FrontendViewModel<ExampleTaskPanel>(panel)
