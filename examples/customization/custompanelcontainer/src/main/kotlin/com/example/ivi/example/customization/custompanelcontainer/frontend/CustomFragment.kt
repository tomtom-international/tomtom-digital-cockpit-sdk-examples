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
import com.example.ivi.example.customization.custompanelcontainer.databinding.TtiviCustompanelcontainerCustomfragmentBinding
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel

/**
 * A visual representation of [CustomPanel]s.
 */
internal class CustomFragment :
    IviFragment<CustomPanel, CustomFragmentViewModel>(CustomFragmentViewModel::class) {

    override val viewFactory = ViewFactory(TtiviCustompanelcontainerCustomfragmentBinding::inflate)
}

/**
 * Exposes data that is displayed in [CustomPanel]s.
 */
internal class CustomFragmentViewModel(panel: CustomPanel) : FrontendViewModel<CustomPanel>(panel) {
    val text = panel.text
}
