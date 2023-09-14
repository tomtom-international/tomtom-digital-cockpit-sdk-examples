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

package com.example.ivi.example.media.customminiplayer

import com.example.ivi.example.media.customminiplayer.databinding.CustomMediaCommonprocessHorizontalLayoutBinding
import com.example.ivi.example.media.customminiplayer.databinding.CustomMediaCommonprocessVerticalLayoutBinding
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaMainProcessPanelBase
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaTaskProcessPanelBase
import com.tomtom.ivi.platform.frontend.api.common.adaptiveness.AdaptiveFragmentHelper
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.tools.android.api.resourceresolution.getColorByAttr
import com.tomtom.tools.android.core.theme.R as CoreThemeR

/**
 * This fragment replaces the stock fragment of the [MediaMainProcessPanelBase]. This replacement is
 * done by the [CustomMediaMainProcessFragmentRule].
 *
 * The stock fragment supports multiple layouts, if you replace it with a custom layout and
 * want to support multiple layouts, you need to implement an adaptive fragment using
 * [AdaptiveFragmentHelper].
 */
internal class CustomMediaMainProcessFragment :
    IviFragment<MediaMainProcessPanelBase, CustomMediaMainProcessViewModel>(
        CustomMediaMainProcessViewModel::class
    ) {

    /**
     * The different variants that the custom fragment needs to support.
     */
    private enum class ViewVariant { HORIZONTAL, VERTICAL }

    /**
     * This class helps to manage multiple layouts.
     */
    private val adaptiveFragmentHelper by lazy {
        AdaptiveFragmentHelper(
            fragment = this,
            viewVariantSelector = { widthDp, heightDp ->
                // Selects the right variant for the given width and height of the container.
                if (widthDp > heightDp) ViewVariant.HORIZONTAL else ViewVariant.VERTICAL
            },
            viewFactoryProvider = { viewVariant ->
                when (viewVariant) {
                    /**
                     * We are using here the same horizontal layout and view model for both
                     * [MediaTaskProcessPanelBase] and [MediaMainProcessPanelBase] to get the same
                     * look and behavior.
                     */
                    ViewVariant.HORIZONTAL -> ViewFactory(
                        CustomMediaCommonprocessHorizontalLayoutBinding::inflate
                    ) {
                        it.viewModel = viewModel.customMediaCommonProcessViewModel
                        requireContext().let { context ->
                            it.backgroundArt =
                                viewModel.customMediaCommonProcessViewModel.art.toBlurredDrawable(
                                    context,
                                    it.customMediaMiniplayerBlurredbackground
                                )
                            it.dominantColor =
                                viewModel.customMediaCommonProcessViewModel.art.toDominantColor(
                                    context,
                                    context.getColorByAttr(
                                        CoreThemeR.attr.tt_surface_content_color_emphasis_high
                                    )
                                )
                        }
                    }
                    // Returns a ViewFactory using the vertical layout.
                    else -> ViewFactory(
                        CustomMediaCommonprocessVerticalLayoutBinding::inflate
                    ) {
                        it.viewModel = viewModel.customMediaCommonProcessViewModel
                        requireContext().let { context ->
                            it.backgroundArt =
                                viewModel.customMediaCommonProcessViewModel.art.toBlurredDrawable(
                                    context,
                                    it.customMediaMiniplayerBlurredbackground
                                )
                            it.dominantColor =
                                viewModel.customMediaCommonProcessViewModel.art.toDominantColor(
                                    context,
                                    context.getColorByAttr(
                                        CoreThemeR.attr.tt_surface_content_color_emphasis_high
                                    )
                                )
                        }
                    }
                }
            }
        )
    }

    override val viewFactory: ViewFactory<*>? by adaptiveFragmentHelper::viewFactory
}
