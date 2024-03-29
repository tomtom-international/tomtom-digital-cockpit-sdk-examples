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

package com.example.ivi.example.notifications

import androidx.lifecycle.LiveData
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.NotificationPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel
import com.tomtom.ivi.platform.frontend.api.template.notificationpanel.NotificationViewModel
import com.tomtom.ivi.platform.frontend.api.template.notificationpanel.stock.StockNotificationPanel
import com.tomtom.tools.android.api.livedata.ImmutableLiveData
import com.tomtom.tools.android.api.resourceresolution.drawable.ResourceDrawableResolver
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver
import com.tomtom.tools.android.api.resourceresolution.string.StringResolver
import com.tomtom.tools.android.api.uicontrols.button.TtButton
import com.tomtom.tools.android.api.uicontrols.button.TtButtonViewModel
import com.tomtom.tools.android.api.uicontrols.imageview.ImageDescriptor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class NotificationCreationViewModel(panel: NotificationCreationPanel) :
    FrontendViewModel<NotificationCreationPanel>(panel) {

    private val _highPriorityChecked = MutableStateFlow(true)
    val highPriorityChecked = _highPriorityChecked.asStateFlow()

    private val _customPanelChecked = MutableStateFlow(false)
    val customPanelChecked = _customPanelChecked.asStateFlow()

    fun onStockNotificationButtonClicked() {
        if (customPanelChecked.value) {
            panel.addPanel(createExampleNotificationPanel())
        } else {
            panel.addPanel(createStockNotificationPanel())
        }
    }

    fun onHighPriorityCheckedChanged(checked: Boolean) {
        _highPriorityChecked.value = checked
    }

    fun onCustomPanelCheckedChanged(checked: Boolean) {
        _customPanelChecked.value = checked
    }

    private fun createStockNotificationPanel() = StockNotificationPanel.create {
        frontendContext = panel.frontendContext
        priority = highPriorityChecked.toNotificationPriority()
        headerViewModel = HEADER
        bodyText = BODY_TEXT
        primaryActionButtonViewModel = PRIMARY_BUTTON
        secondaryActionButtonViewModel = SECONDARY_BUTTON
        optionViewModels = NOTIFICATION_OPTIONS
    }

    private fun createExampleNotificationPanel() = ExampleNotificationPanel(
        panel.frontendContext,
        highPriorityChecked.toNotificationPriority(),
    )

    private companion object {
        val HEADER = NotificationViewModel.HeaderViewModel(
            imageDescriptor = ImageDescriptor(
                ResourceDrawableResolver(R.drawable.ttivi_notification_icon_placeholder),
            ),
            title = StaticStringResolver("Stock"),
            description = StaticStringResolver("Created by StockNotificationPanel.create"),
        )

        val BODY_TEXT: LiveData<StringResolver?> =
            ImmutableLiveData(StaticStringResolver("Body text"))

        val PRIMARY_BUTTON: LiveData<TtButtonViewModel?> = ImmutableLiveData(
            TtButtonViewModel(
                text = StaticStringResolver("Primary"),
                actionType = TtButton.ActionType.PRIMARY,
            ),
        )

        val SECONDARY_BUTTON: LiveData<TtButtonViewModel?> = ImmutableLiveData(
            TtButtonViewModel(
                text = StaticStringResolver("Secondary"),
                actionType = TtButton.ActionType.DESTRUCTIVE,
            ),
        )

        val NOTIFICATION_OPTIONS = ImmutableLiveData(
            listOf(
                NotificationViewModel.OptionViewModel(
                    description = ImmutableLiveData(
                        StaticStringResolver("Notification option"),
                    ),
                ),
            ),
        )
    }
}

private fun StateFlow<Boolean>.toNotificationPriority() =
    if (value) NotificationPanel.Priority.HIGH else NotificationPanel.Priority.LOW
