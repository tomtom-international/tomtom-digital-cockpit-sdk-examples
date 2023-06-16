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

package com.example.ivi.example.media.customminiplayer

import com.example.ivi.example.media.customminiplayer.databinding.CustomMediaCommonprocessHorizontalLayoutBinding
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaMainProcessPanelBase
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaTaskProcessPanelBase
import com.tomtom.ivi.appsuite.media.plugin.frontend.media.R
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.tools.android.api.resourceresolution.getColorByAttr

/**
 * This fragment replaces the stock fragment of the [MediaTaskProcessPanelBase]. This replacement is
 * done by the [CustomMediaMainProcessFragmentRule].
 */
internal class CustomMediaTaskProcessFragment :
    IviFragment<MediaTaskProcessPanelBase, CustomMediaTaskProcessViewModel>(
        CustomMediaTaskProcessViewModel::class
    ) {

    /**
     * We are using here the same layout and view model for both [MediaTaskProcessPanelBase] and
     * [MediaMainProcessPanelBase] to get the same look and behavior.
     */
    override val viewFactory = ViewFactory(
        CustomMediaCommonprocessHorizontalLayoutBinding::inflate
    ) {
        it.viewModel = viewModel.customMediaCommonProcessViewModel
        requireContext().let { context ->
            it.backgroundArt =
                viewModel.customMediaCommonProcessViewModel.art.toBlurredDrawable(
                    context,
                    it.customMediaMiniplayerBlurredbackground
                )
            it.dominantColor = viewModel.customMediaCommonProcessViewModel.art.toDominantColor(
                context,
                context.getColorByAttr(R.attr.tt_surface_content_color_emphasis_high)
            )
        }
    }
}
