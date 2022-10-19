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

package com.example.ivi.example.applauncher.common.webapp

import com.tomtom.ivi.appsuite.appstore.api.common.model.App
import com.tomtom.tools.android.api.resourceresolution.drawable.DrawableResolver
import com.tomtom.tools.android.api.resourceresolution.string.StringResolver
import kotlinx.parcelize.Parcelize

@Parcelize
class WebApp(
    val url: String,
    override val displayName: StringResolver,
    override val summary: StringResolver,
    override val icon: DrawableResolver,
) : App {

    override val id: String = "${this::class.java.simpleName}.$url"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WebApp

        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int = url.hashCode()
}
