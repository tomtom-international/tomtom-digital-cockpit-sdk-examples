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

package com.example.ivi.example.customization.customfragment.frontend

import com.example.ivi.example.customization.customfragment.R
import com.tomtom.ivi.platform.frontend.api.common.frontend.Frontend
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.NotificationPanel
import com.tomtom.ivi.platform.frontend.api.template.notificationpanel.stock.StockNotificationPanel
import com.tomtom.tools.android.api.resourceresolution.string.ResourceStringResolver
import com.tomtom.tools.android.api.uicontrols.button.TtButton
import com.tomtom.tools.android.api.uicontrols.button.TtButtonViewModel

/**
 * A frontend that adds a notification panel at the press of a button. Its purpose is to show that
 * the fragment for the [StockNotificationPanel] is replaced by the system UI.
 *
 * This frontend does not contain any code required to customize the fragment.
 */
internal class ExampleFrontend(frontendContext: FrontendContext) : Frontend(frontendContext) {

    /** Open the [ExampleTaskPanel] when the menu item is tapped. */
    override fun createMainTaskPanel() = ExampleTaskPanel(frontendContext, ::addNotificationPanel)

    /** Creates a [StockNotificationPanel] and adds it to [panels]. */
    private fun addNotificationPanel() {
        addPanel(createStockNotificationPanel())
    }

    private fun createStockNotificationPanel() =
        StockNotificationPanel.create {
            frontendContext = this@ExampleFrontend.frontendContext
            this.priority = NotificationPanel.Priority.HIGH

            setBodyText(
                ResourceStringResolver(
                    R.string.ttivi_customization_customfragment_frontend_notificationbody
                )
            )
            setPrimaryActionButtonViewModel(
                TtButtonViewModel(
                    text = ResourceStringResolver(
                        R.string.ttivi_customization_customfragment_frontend_notificationprimary
                    )
                )
            )
            setSecondaryActionButtonViewModel(
                TtButtonViewModel(
                    text = ResourceStringResolver(
                        R.string.ttivi_customization_customfragment_frontend_notificationsecondary
                    ),
                    actionType = TtButton.ActionType.SECONDARY
                )
            )
        }
}
