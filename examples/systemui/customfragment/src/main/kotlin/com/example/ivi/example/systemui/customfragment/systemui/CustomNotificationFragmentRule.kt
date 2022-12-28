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

package com.example.ivi.example.systemui.customfragment.systemui

import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.template.notificationpanel.stock.StockNotificationPanel
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.CustomFragmentRule

/**
 * The rule that replaces the normal fragment of a [StockNotificationPanel] with a
 * [CustomNotificationFragment].
 *
 * This rule is applied in [CustomSystemUiHost.fragmentFactory].
 */
internal class CustomNotificationFragmentRule : CustomFragmentRule<StockNotificationPanel> {

    /** Accept any [StockNotificationPanel]. */
    override fun accepts(panel: StockNotificationPanel) = true

    /** Create a [CustomNotificationFragment] to display [panel]. */
    override fun createInitialFragmentInitializer(panel: StockNotificationPanel) =
        IviFragment.Initializer(CustomNotificationFragment(), panel)
}
