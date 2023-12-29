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

package com.example.ivi.example.media.userflowpolicy

import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaPolicyFrontendExtension
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendExtension

/**
 * Stock media player UI extension to configure custom policies for the example media source. The
 * configured policies can be found in the [exampleMediaSourcePolicyProvider].
 *
 * This extension can be added to the stock media player UI through the corresponding IVI
 * configuration instance of the media player UI's frontend at build time. For details, see
 * [FrontendExtension] and the `gradle.build.kts` file of this example.
 *
 * Not adding this extension to the IVI configuration will make the media player UI use a default
 * configuration for this media source.
 */
internal val exampleMediaSourceFrontendExtension: FrontendExtension =
    MediaPolicyFrontendExtension(
        sourcePackageName = ExampleMediaSourceId.packageName,
        // With the class name provided, the policies are applied to exactly matching
        // MediaBrowserServiceCompat service. If you want to apply the policy to all
        // MediaBrowserServiceCompat services in the package, leave this field as default or null.
        sourceClassName = ExampleMediaSourceId.className,
        policyProvider = exampleMediaSourcePolicyProvider,
    )
