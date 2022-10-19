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

import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.PolicyProvider

/**
 * The [PolicyProvider] selects which behavior to customize.
 * There are more policies available than those in this example. If a policy is not provided here
 * the frontend uses a default one.
 */
internal val exampleFallbackPolicyProvider = PolicyProvider(
    sourceAttributionPolicy = ExampleFallbackSourceAttributionPolicy()
)
