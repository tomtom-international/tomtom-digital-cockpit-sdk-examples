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
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.MediaItemComparisonPolicy

/**
 * Normally [IviMediaItem]s are only compared by their [IviMediaItem.mediaUri] field.
 * Having a reliable comparison method in a [MediaItemComparisonPolicy] identifies the media
 * currently being played in the browser, and improves UI performance.
 */
internal class ExampleMediaSourceItemComparisonPolicy : MediaItemComparisonPolicy {
    override fun invoke(first: IviMediaItem, second: IviMediaItem?): Boolean =
        first.id == second?.id
}
