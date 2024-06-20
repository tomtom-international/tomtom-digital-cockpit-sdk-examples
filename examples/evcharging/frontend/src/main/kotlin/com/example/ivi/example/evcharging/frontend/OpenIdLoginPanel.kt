/*
 * Copyright © 2024 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.evcharging.frontend

import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ModalPanel

internal class OpenIdLoginPanel(
    frontendContext: FrontendContext,
    val onPanelRemovedFromFrontend: () -> Unit,
) : ModalPanel(frontendContext) {

    override fun createInitialFragmentInitializer() =
        IviFragment.Initializer(OpenIdLoginFragment(), this)

    override fun onRemovedFromFrontend() {
        super.onRemovedFromFrontend()
        onPanelRemovedFromFrontend()
    }
}
