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

package com.example.ivi.example.media.fallbackpolicy

import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaPolicyFrontendExtension
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendExtension

/**
 * Stock media player UI extension to configure custom policies for non configured media sources.
 * The configured policies can be found in the [exampleFallbackPolicyProvider].
 *
 * Specifying non specifically configured media sources here is done by setting the
 * [MediaPolicyFrontendExtension.sourcePackageName] parameter to `null`. Only one such extension can
 * be in use in the build.
 *
 * An extension such as this one should replace the stock fallback policy which is normally provided
 * by default. This is done through the IVI configuration instance of the media player UI's
 * frontend at build time.
 * For details, see the `gradle.build.kts` file of this example and [FrontendExtension].
 */
internal val exampleFallbackPolicyFrontendExtension: FrontendExtension =
    MediaPolicyFrontendExtension(
        sourcePackageName = null /* Match non configured media sources. */,
        policyProvider = exampleFallbackPolicyProvider,
    )
