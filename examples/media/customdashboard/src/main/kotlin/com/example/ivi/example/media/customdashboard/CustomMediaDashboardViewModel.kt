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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaDashboardPanelBase
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver
import com.tomtom.tools.android.api.resourceresolution.string.StringResolver

/**
 * This ViewModel replaces the stock ViewModel of the [MediaDashboardPanelBase].
 */
internal class CustomMediaDashboardViewModel(
    panel: MediaDashboardPanelBase
) : FrontendViewModel<MediaDashboardPanelBase>(panel) {

    private val counter = MutableLiveData(0)

    /**
     * This [LiveData] demonstrates an ability to show custom data in the media dashboard.
     */
    val label: LiveData<StringResolver> = counter.map { StaticStringResolver(it) }

    /**
     * This method demonstrates an ability to process custom user actions in the media dashboard.
     */
    fun onButtonClick() {
        counter.value = counter.value?.let { it + 1 }
    }
}
