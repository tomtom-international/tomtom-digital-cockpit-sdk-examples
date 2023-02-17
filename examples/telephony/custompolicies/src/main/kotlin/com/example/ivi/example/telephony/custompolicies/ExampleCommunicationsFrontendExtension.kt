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

package com.example.ivi.example.telephony.custompolicies

import com.tomtom.ivi.appsuite.communications.api.common.frontend.CommunicationsPolicyFrontendExtension
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendExtension

/**
 * Stock communications frontend extension to configure a policy for customizing calls associated
 * with a specific phone account ID.
 *
 * This extension can be added to the stock communications UI through the corresponding IVI
 * configuration instance of the communications UI's frontend at build time. For details, see
 * [FrontendExtension] and the `gradle.build.kts` file of this example.
 */
internal val exampleCommunicationsFrontendExtension: FrontendExtension =
    CommunicationsPolicyFrontendExtension(
        phoneAccountId = "ExampleID",
        callDetailsPolicy = ExampleCallDetailsPolicy()
    )
