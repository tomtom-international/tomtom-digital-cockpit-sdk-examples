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

package com.example.ivi.example.media.customnowplaying

import android.content.Context
import android.util.AttributeSet
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.tomtom.tools.android.api.lifecycle.withAttachedView
import com.tomtom.tools.android.api.uicontrols.button.TtButtonViewModel
import com.tomtom.tools.android.api.uicontrols.list.TtListLinearLayout
import com.tomtom.tools.android.api.viewprovider.ViewProvider

internal class MediaControlLayout(context: Context, attrs: AttributeSet) :
    TtListLinearLayout<TtButtonViewModel>(context, attrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ttViewProvider = checkNotNull(findViewTreeLifecycleOwner()) {
            "View tree lifecycle owner not found"
        }.withAttachedView(this).let {
            ViewProvider(it, R.layout.custom_media_mediacontrol_layout)
        }
    }
}
