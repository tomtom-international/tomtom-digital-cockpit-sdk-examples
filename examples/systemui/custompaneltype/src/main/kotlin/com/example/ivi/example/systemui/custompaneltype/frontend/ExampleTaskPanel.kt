package com.example.ivi.example.systemui.custompaneltype.frontend

import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel

/**
 * A minimal [TaskPanel] used by the [ExampleFrontend], so it becomes the focused frontend when
 * selecting it in the main menu.
 */
internal class ExampleTaskPanel(frontendContext: FrontendContext) : TaskPanel(frontendContext) {
    override fun createInitialFragmentInitializer(): IviFragment.Initializer<*, *> =
        IviFragment.Initializer(ExampleTaskFragment(), this)
}

internal class ExampleTaskFragment :
    IviFragment<ExampleTaskPanel, ExampleTaskViewModel>(ExampleTaskViewModel::class)

internal class ExampleTaskViewModel(panel: ExampleTaskPanel) :
    FrontendViewModel<ExampleTaskPanel>(panel) {

}
