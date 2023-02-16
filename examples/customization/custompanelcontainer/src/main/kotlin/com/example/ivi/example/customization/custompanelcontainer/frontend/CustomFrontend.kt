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

package com.example.ivi.example.customization.custompanelcontainer.frontend

import com.example.ivi.example.customization.custompanelcontainer.common.CustomPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.Frontend
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext

/**
 * Adds [CustomPanel]s to system UI.
 */
internal class CustomFrontend(frontendContext: FrontendContext) : Frontend(frontendContext) {

    override fun onCreate() {
        super.onCreate()

        addPanel(CustomPanel("Panel 1", frontendContext))
        addPanel(CustomPanel("Panel 2", frontendContext))
    }
}
