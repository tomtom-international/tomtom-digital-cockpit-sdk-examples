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
import com.tomtom.ivi.platform.frontend.api.template.notificationpanel.NotificationViewModel
import com.tomtom.tools.android.api.livedata.ImmutableLiveData
import com.tomtom.tools.android.api.resourceresolution.drawable.ResourceDrawableResolver
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver
import com.tomtom.tools.android.api.resourceresolution.string.StringResolver
import com.tomtom.tools.android.api.uicontrols.button.TtButton
import com.tomtom.tools.android.api.uicontrols.button.TtButtonViewModel
import com.tomtom.tools.android.api.uicontrols.imageview.ImageDescriptor

internal class ExampleNotificationViewModel(panel: ExampleNotificationPanel) :
    NotificationViewModel<ExampleNotificationPanel>(panel){

    override val headerViewModel = HeaderViewModel(
        imageDescriptor = ImageDescriptor(
            ResourceDrawableResolver(R.drawable.ttivi_notification_icon_placeholder_alternative)
        ),
        title = StaticStringResolver("Custom"),
        description = StaticStringResolver("Extend from NotificationPanel")
    )

    override val bodyText: LiveData<StringResolver?> =
        ImmutableLiveData(StaticStringResolver("Body text"))

    override val primaryActionButtonViewModel: LiveData<TtButtonViewModel?> =
        ImmutableLiveData(
            TtButtonViewModel(
                text = StaticStringResolver("Primary"),
                actionType = TtButton.ActionType.ACCEPTANCE,
                onClick = { onPrimaryButtonClicked() }
            )
        )

    private fun onPrimaryButtonClicked() {
        // Things to be executed when the primary button is clicked.
    }

    override val secondaryActionButtonViewModel: LiveData<TtButtonViewModel?> = ImmutableLiveData(
        TtButtonViewModel(
            text = StaticStringResolver("Secondary"),
            actionType = TtButton.ActionType.DESTRUCTIVE,
            onClick = { onSecondaryButtonClicked() }
        )
    )

    private fun onSecondaryButtonClicked() {
        // Things to be executed when the secondary button is clicked.
    }

    override val optionViewModels: LiveData<List<OptionViewModel>> = ImmutableLiveData(
        listOf(
            OptionViewModel(
                imageDescriptor = ImmutableLiveData(
                    ImageDescriptor(
                        ResourceDrawableResolver(R.drawable.ttivi_notificationoption_icon_placeholder)
                    ),
                ),
                description = ImmutableLiveData(
                    StaticStringResolver("Notification option")
                ),
            )
        )
    )
}
