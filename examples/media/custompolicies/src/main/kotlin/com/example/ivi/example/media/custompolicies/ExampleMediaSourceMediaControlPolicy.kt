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

import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.InvisibleControlFactory
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.MediaControlFactory
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.standard.StandardMediaControls
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.MediaControlPolicy
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.MediaControlsDisplayLimit

/**
 * Customization of media controls shown when playing content from the example media source.
 */
internal class ExampleMediaSourceMediaControlPolicy : MediaControlPolicy {

    /***
     * When adding custom controls, they must be registered here.
     */
    override val customControls: List<MediaControlFactory> = emptyList()

    /**
     * This example replacement of standard controls only removes the seek actions using
     * [InvisibleControlFactory], since the example media source does not support seeking.
     * In a real custom [MediaControlPolicy], [StandardMediaControls] are replaced with new
     * implementations which alter the normal behavior. Check the [StandardMediaControls] enum to
     * see the default implementation [MediaControlFactory] implementation for each type of control.
     */
    override val replacedStandardControls: Map<StandardMediaControls, MediaControlFactory> =
        mapOf(
            StandardMediaControls.SEEK_BACKWARD to InvisibleControlFactory,
            StandardMediaControls.SEEK_FORWARD to InvisibleControlFactory,
        )

    /**
     * This changes how many controls at a time are visible at all times when playing content from
     * this media source.
     * This can be useful if the source exposes too much controls but you want to limit their
     * number in the stock media player UI.
     */
    override val mediaControlsDisplayLimit: MediaControlsDisplayLimit =
        super.mediaControlsDisplayLimit.copy(
            primaryControlsLargeLimit = 3,
            secondaryControlsSmallLimit = 1,
        )
}
