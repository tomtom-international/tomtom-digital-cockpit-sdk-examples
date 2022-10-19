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

package com.example.ivi.example.media.userflowpolicy

import android.content.Context
import com.example.ivi.example.media.userflowpolicy.databinding.ExamplesMediaUserflowpolicyPanelBinding
import com.tomtom.ivi.appsuite.media.api.common.core.SourceClient
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment

/**
 * An [IviFragment] responsible for creating a layout and linking it to a ViewModel. This connects
 * the [ExampleMediaSourceViewModel] to the XML layout, represented by
 * [ExamplesMediaUserflowpolicyPanelBinding].
 */
internal class ExampleMediaSourceFragment :
    IviFragment<ExampleMediaSourcePanel, ExampleMediaSourceViewModel>(
        ExampleMediaSourceViewModel::class
    ) {

    override val viewFactory =
        ViewFactory(ExamplesMediaUserflowpolicyPanelBinding::inflate) { binding ->
            binding.viewModel = viewModel
        }

    /** Set the context on the panel, since its [SourceClient] needs it to connect. */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        panel.setContext(context)
    }

    /** Reset the context on the panel, to disconnect its [SourceClient]. */
    override fun onDetach() {
        panel.setContext(null)
        super.onDetach()
    }
}
