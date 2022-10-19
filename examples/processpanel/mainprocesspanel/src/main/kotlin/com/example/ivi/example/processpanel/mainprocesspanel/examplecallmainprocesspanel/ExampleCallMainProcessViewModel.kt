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

package com.example.ivi.example.processpanel.mainprocesspanel.examplecallmainprocesspanel

import androidx.lifecycle.viewModelScope
import com.example.ivi.example.processpanel.mainprocesspanel.R
import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.frontend.api.template.compactprocesspanel.MainCompactProcessViewModel
import com.tomtom.tools.android.api.resourceresolution.string.ResourceStringResolver
import kotlinx.coroutines.launch

@OptIn(IviExperimental::class)
internal class ExampleCallMainProcessViewModel(panel: ExampleCallMainProcessPanel) :
    MainCompactProcessViewModel<ExampleCallMainProcessPanel>(panel) {
    private val factory =
        ExampleCallMainProcessViewModelFactory(
            title = ResourceStringResolver(R.string.ttivi_processcreation_mainprocesspanel_title),
            doDismissCall = ::closePanel,
            doEndCall = ::closePanel
        )
    override val primaryControlsViewModel = factory.createPrimaryControls()
    override val metadataViewModel = factory.createMetadata()
    override val secondaryControlsViewModel = factory.createSecondaryControls()

    private fun closePanel() {
        viewModelScope.launch {
            panel.dismiss()
        }
    }
}
