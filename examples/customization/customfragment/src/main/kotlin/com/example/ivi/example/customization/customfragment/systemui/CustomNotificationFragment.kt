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

package com.example.ivi.example.customization.customfragment.systemui

import com.example.ivi.example.customization.customfragment.databinding.TtiviCustomfragmentNotificationfragmentBinding
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.template.notificationpanel.NotificationViewModel
import com.tomtom.ivi.platform.frontend.api.template.notificationpanel.R
import com.tomtom.ivi.platform.frontend.api.template.notificationpanel.stock.StockNotificationPanel
import com.tomtom.ivi.platform.frontend.api.template.notificationpanel.stock.StockNotificationPanel.StockNotificationViewModel
import com.tomtom.tools.android.api.viewprovider.ViewProvider

/**
 * This fragment replaces the normal fragment of the [StockNotificationPanel].
 *
 * In this example we chose to use the regular view model of the panel, the
 * [StockNotificationViewModel], since this allowed us to see what the notification is about. This
 * is no requirement though, it could just as well be a sub class of the normal view model, or a
 * different class altogether. The regular view model of the package might even be declared as
 * package-private, then you'll have to create a new view model.
 */
internal class CustomNotificationFragment :
    IviFragment<StockNotificationPanel, StockNotificationViewModel>(
        StockNotificationViewModel::class,
    ) {
    override val viewFactory: ViewFactory<*> =
        ViewFactory(TtiviCustomfragmentNotificationfragmentBinding::inflate) {
            it.viewProvider =
                ViewProvider<NotificationViewModel.OptionViewModel>(
                    viewLifecycleOwner,
                    R.layout.ttivi_notification_option,
                )
        }
}
