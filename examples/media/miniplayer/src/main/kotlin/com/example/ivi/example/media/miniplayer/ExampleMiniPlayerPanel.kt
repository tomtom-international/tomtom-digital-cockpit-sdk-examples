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

package com.example.ivi.example.media.miniplayer

import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaConfiguration
import com.tomtom.ivi.appsuite.media.api.service.core.MediaService
import com.tomtom.ivi.appsuite.media.api.service.core.createApi
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.MainProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.OverlayPanel

/**
 * This example is implemented as a normal [MainProcessPanel], but it is possible to reposition and
 * resize the panel to anywhere on the UI by changing it to another type, such as an [OverlayPanel]
 * or a custom panel type.
 *
 * The product's system UI needs to be configured to provide a place for such a panel to be
 * positioned and sized. See the
 * [System UI guide](https://developer.tomtom.com/tomtom-digital-cockpit/documentation/development/system-ui)
 * for more information.
 */
internal class ExampleMiniPlayerPanel(
    frontendContext: FrontendContext,
    val mediaConfiguration: MediaConfiguration
) : MainProcessPanel(frontendContext, Priority.LOW) {

    /**
     * The connection to the [MediaService] is here, and not in the [ExampleMiniPlayerViewModel],
     * so that it will survive eventual configuration changes, for which the view models and
     * fragments are recreated by Android.
     */
    val mediaService = MediaService.createApi(this, frontendContext.iviServiceProvider)

    override fun createInitialFragmentInitializer() =
        IviFragment.Initializer(ExampleMiniPlayerFragment(), this)
}
