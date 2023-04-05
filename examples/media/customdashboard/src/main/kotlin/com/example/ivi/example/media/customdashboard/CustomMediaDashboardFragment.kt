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

package com.example.ivi.example.media.customdashboard

import com.example.ivi.example.media.customdashboard.databinding.TtiviCustomdashboardFragmentBinding
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaDashboardPanelBase
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment

/**
 * This fragment replaces the stock fragment of the [MediaDashboardPanelBase]. This replacement is
 * done by the [CustomMediaDashboardFragmentRule].
 */
internal class CustomMediaDashboardFragment :
    IviFragment<MediaDashboardPanelBase, CustomMediaDashboardViewModel>(
        CustomMediaDashboardViewModel::class
    ) {
    override val viewFactory: ViewFactory<*> =
        ViewFactory(TtiviCustomdashboardFragmentBinding::inflate) { binding ->

            // Source picker view requires media frontend context and lifecycle owner.
            // It is intended to be used within the stock media frontend.
            binding.ttiviMediaCustomdashboardSourcepickerview.setMediaFrontendContext(
                viewLifecycleOwner,
                panel.mediaFrontendContext
            )
        }
}
