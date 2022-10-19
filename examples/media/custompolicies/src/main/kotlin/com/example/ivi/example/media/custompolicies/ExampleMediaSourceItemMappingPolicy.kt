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

package com.example.ivi.example.media.custompolicies

import com.tomtom.ivi.appsuite.media.api.common.core.IviMediaItem
import com.tomtom.ivi.appsuite.media.api.common.core.IviMediaItemBuilder
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.MediaItemMappingPolicy

/**
 * [MediaItemMappingPolicy] helps change how the information from a source is displayed to
 * the user.
 * In this case, we want all media to always show a title and a subtitle even if the source doesn't
 * make any available.
 */
internal class ExampleMediaSourceItemMappingPolicy : MediaItemMappingPolicy {
    override fun invoke(item: IviMediaItem): IviMediaItem {
        return IviMediaItemBuilder(item)
            .withTitle(item.title ?: "No Title")
            .withDisplaySubtitle(item.subtitle ?: "No Artist")
            .build()
    }
}
