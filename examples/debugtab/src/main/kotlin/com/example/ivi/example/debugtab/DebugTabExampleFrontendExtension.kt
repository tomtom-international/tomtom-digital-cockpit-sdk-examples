/*
 * Copyright © 2021 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.debugtab

import com.tomtom.ivi.platform.debug.api.frontendextension.debugtab.DebugTabFrontendExtension
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendExtension
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.DebugPanel

/**
 * This [FrontendExtension] adds a new tab for the [DebugPanel] of the example app.
 * The new debug tab is added to the stock list of tabs by default.
 */
val debugTabFrontendExtension: FrontendExtension =
    DebugTabFrontendExtension(
        tabClass = DebugTabExampleFragment::class,
        titleResourceId = R.string.debugtab_title
    )
