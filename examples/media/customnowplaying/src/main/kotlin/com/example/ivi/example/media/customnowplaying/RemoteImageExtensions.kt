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
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.tomtom.ivi.appsuite.media.api.common.attributes.R as MediaAttributesR
import com.tomtom.tools.android.api.graphics.drawable.blur
import com.tomtom.tools.android.api.graphics.drawable.getBrightnessAdjustedDominantColor
import com.tomtom.tools.android.api.graphics.imageloader.RemoteImage
import com.tomtom.tools.android.api.graphics.imageloader.TtImageLoader
import com.tomtom.tools.android.api.resourceresolution.getFloatByAttr

/**
 * Transforms the [RemoteImage] into a blurred [Drawable] using [blur] function.
 */
internal fun LiveData<RemoteImage>.toBlurredDrawable(context: Context, image: ImageView):
    LiveData<Drawable> {
    val blurRadiusFraction =
        context.getFloatByAttr(
            MediaAttributesR.attr.ttivi_media_nowplayingview_backgroundblur_radius_fraction
        )

    return switchMap { TtImageLoader.download(context, it) }
        .switchMap {
            liveData {
                // Calculates the blur radius and takes into account that the blur radius will be
                // applied after the image is scaled with the sampling multiplier. We need to apply
                // the radius fraction to the image scaled width. See blur API for more details.
                val blurRadiusPx =
                    it.intrinsicWidth /
                        BACKGROUND_BLUR_SAMPLING_MULTIPLIER * blurRadiusFraction
                emit(it.blur(context, blurRadiusPx, BACKGROUND_BLUR_SAMPLING_MULTIPLIER))
            }
        }
}

/**
 * Transforms the [RemoteImage] into its dominant color. If there is no dominant color,
 * the [defaultColor] is used.
 */
internal fun LiveData<RemoteImage>.toDominantColor(
    context: Context,
    @ColorInt defaultColor: Int
): LiveData<Int> {
    return switchMap {
        // It is important to use the context to get the drawable independently of the image view
        // size and get always the same color for a given drawable.
        TtImageLoader.download(context, it)
    }.switchMap {
        liveData {
            emit(it.getBrightnessAdjustedDominantColor(defaultColor))
        }
    }
}

/**
 * Multiplier used to [blur] the drawable.
 */
private const val BACKGROUND_BLUR_SAMPLING_MULTIPLIER = 3f
