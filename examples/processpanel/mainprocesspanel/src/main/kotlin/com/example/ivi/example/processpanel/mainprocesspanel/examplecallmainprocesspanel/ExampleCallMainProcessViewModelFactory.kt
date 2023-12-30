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

package com.example.ivi.example.processpanel.mainprocesspanel.examplecallmainprocesspanel

import androidx.lifecycle.LiveData
import com.example.ivi.example.processpanel.mainprocesspanel.R
import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.frontend.api.template.compactprocesspanel.CompactProcessControlViewModel
import com.tomtom.ivi.platform.frontend.api.template.compactprocesspanel.CompactProcessMetadataViewModel
import com.tomtom.tools.android.api.livedata.ImmutableLiveData
import com.tomtom.tools.android.api.resourceresolution.drawable.ResourceDrawableResolver
import com.tomtom.tools.android.api.resourceresolution.string.ResourceStringResolver
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver
import com.tomtom.tools.android.api.resourceresolution.string.StringResolver
import com.tomtom.tools.android.api.uicontrols.button.TtButton
import com.tomtom.tools.android.api.uicontrols.button.TtButtonViewModel
import com.tomtom.tools.android.api.uicontrols.compositeviewmodel.StockVisibilityProvidingCompositeViewModel
import com.tomtom.tools.android.api.uicontrols.compositeviewmodel.VisibilityProvidingCompositeViewModel
import com.tomtom.tools.android.api.uicontrols.imageview.ImageDescriptor
import com.tomtom.tools.android.api.uicontrols.imageview.ImageType

@OptIn(IviExperimental::class)
internal class ExampleCallMainProcessViewModelFactory(
    private val title: StringResolver,
    doEndCall: () -> Unit,
    doDismissCall: () -> Unit,
) {
    @OptIn(IviExperimental::class)
    private val endCallControl = CompactProcessControlViewModel(
        buttonViewModel = TtButtonViewModel(
            actionType = TtButton.ActionType.DESTRUCTIVE,
            image = ResourceDrawableResolver(
                R.drawable.ttivi_processcreation_icon_callhangup,
            ),
            isEnabled = true,
            isVisible = true,
            onClick = doEndCall,
        ),
        isFixedWidth = true,
    )

    private val toggleMuteControl = CompactProcessControlViewModel(
        buttonViewModel = TtButtonViewModel(
            isActivated = true,
            actionType = TtButton.ActionType.TERTIARY,
            isEnabled = true,
            onClick = {
                // Add your implementation to toggle mute control.
            },
            image = ResourceDrawableResolver(R.drawable.ttivi_processcreation_icon_microphonemuted),
            isVisible = true,
        ),
    )

    private val closeControl = CompactProcessControlViewModel(
        buttonViewModel = TtButtonViewModel(
            actionType = TtButton.ActionType.SECONDARY,
            text = ResourceStringResolver(R.string.ttivi_processcreation_common_action_close),
            isVisible = true,
            onClick = doDismissCall,
        ),
        isFixedWidth = true,
    )

    fun createPrimaryControls():
        LiveData<VisibilityProvidingCompositeViewModel<CompactProcessControlViewModel>> =
        ImmutableLiveData(
            StockVisibilityProvidingCompositeViewModel(
                endCallControl,
            ),
        )

    fun createSecondaryControls():
        LiveData<VisibilityProvidingCompositeViewModel<CompactProcessControlViewModel>> =
        ImmutableLiveData(
            StockVisibilityProvidingCompositeViewModel(
                toggleMuteControl,
                closeControl,
            ),
        )

    fun createMetadata() = CompactProcessMetadataViewModel(
        image = ImmutableLiveData(
            ImageDescriptor(
                ResourceDrawableResolver(R.drawable.ttivi_processcreation_icon_thumbnail),
                ImageType.AVATAR,
            ),
        ),
        primaryText = ImmutableLiveData(title),
        secondaryText = ImmutableLiveData(StaticStringResolver("1:00")),
        onClick = ImmutableLiveData {
            // Add click action when Metadata section has been clicked.
        },
    )
}
