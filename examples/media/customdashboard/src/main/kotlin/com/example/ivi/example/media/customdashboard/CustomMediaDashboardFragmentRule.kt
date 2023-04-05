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

import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaDashboardPanelBase
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.CustomFragmentRule

/**
 * The rule that replaces the stock fragment of a [MediaDashboardPanelBase] with a
 * [CustomMediaDashboardFragment].
 *
 * This rule is applied in [CustomSystemUiHost.fragmentFactory].
 */
internal class CustomMediaDashboardFragmentRule : CustomFragmentRule<MediaDashboardPanelBase> {

    /** Accept any [MediaDashboardPanelBase]. */
    override fun accepts(panel: MediaDashboardPanelBase) = true

    /** Create a [CustomMediaDashboardFragment] to display [panel]. */
    override fun createInitialFragmentInitializer(panel: MediaDashboardPanelBase) =
        IviFragment.Initializer(CustomMediaDashboardFragment(), panel)
}
