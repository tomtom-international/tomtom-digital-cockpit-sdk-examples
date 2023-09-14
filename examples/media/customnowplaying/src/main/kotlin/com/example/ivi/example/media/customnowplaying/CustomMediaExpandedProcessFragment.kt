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

package com.example.ivi.example.media.customnowplaying

import com.example.ivi.example.media.customnowplaying.databinding.CustomMediaExpandedprocessLayoutBinding
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaExpandedProcessPanelBase
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.tools.android.api.resourceresolution.getColorByAttr
import com.tomtom.tools.android.core.theme.R as CoreThemeR

/**
 * This fragment replaces the stock fragment of the [MediaExpandedProcessPanelBase]. This
 * replacement is done by the [CustomMediaExpandedProcessFragmentRule].
 */
internal class CustomMediaExpandedProcessFragment :
    IviFragment<MediaExpandedProcessPanelBase, CustomMediaExpandedProcessViewModel>(
        CustomMediaExpandedProcessViewModel::class
    ) {

    override val viewFactory = ViewFactory(CustomMediaExpandedprocessLayoutBinding::inflate) {
        it.viewModel = viewModel
        it.lifecycleOwner = viewLifecycleOwner
        it.customMediaNowplayingQueue.lifecycleOwner = viewLifecycleOwner
        requireContext().let { context ->
            it.backgroundArt =
                viewModel.art.toBlurredDrawable(
                    context,
                    it.customMediaNowplayingBlurredbackground
                )
            it.dominantColor = viewModel.art.toDominantColor(
                context,
                context.getColorByAttr(CoreThemeR.attr.tt_surface_content_color_emphasis_high)
            )
        }
    }
}
