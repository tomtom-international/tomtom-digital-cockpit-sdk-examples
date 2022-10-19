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
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaPolicyFrontendExtension
import com.tomtom.ivi.appsuite.media.api.common.frontend.asMediaConfiguration
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.MediaSourceAttributionPolicy
import com.tomtom.ivi.platform.frontend.api.common.frontend.Frontend
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendBuilder
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ExpandedProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.MainProcessPanel

/**
 * This [FrontendBuilder] uses the [MediaPolicyFrontendExtension]s from the IVI Gradle configuration
 * of the media frontend to customize the media sources exactly as the stock media player UI does.
 *
 * It is convenient to customize visual and interaction aspects of the stock media player UI, such
 * as the browsing experience, the media playback [MainProcessPanel], or the media
 * [ExpandedProcessPanel].
 *
 * However, when making a completely new media player frontend to replace the stock one, it will be
 * necessary to evaluate which components of the stock [MediaConfiguration] can be re-used. For
 * instance, the stock [MediaSourceAttributionPolicy]s define colors and icons that might not match
 * an UI other than the stock one.
 */
internal class ExampleMiniPlayerFrontendBuilder : FrontendBuilder() {

    override fun build(frontendContext: FrontendContext): Frontend {
        val extensions = getFrontendExtensionsByType<MediaPolicyFrontendExtension>()
        return ExampleMiniPlayerFrontend(
            frontendContext,
            extensions.asMediaConfiguration()
        )
    }
}
