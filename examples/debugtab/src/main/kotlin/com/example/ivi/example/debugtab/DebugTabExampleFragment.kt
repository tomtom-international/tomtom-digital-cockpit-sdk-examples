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

import com.example.ivi.example.debugtab.databinding.DebugtabFragmentBinding
import com.tomtom.ivi.platform.debug.api.frontendextension.debugtab.DebugTabFragment
import com.tomtom.ivi.platform.debug.api.frontendextension.debugtab.TabbedDebugPanel

/**
 * Fragment for a [DebugTabExampleFragment].
 *
 * [DebugTabExampleFragment]s are shown by the [TabbedDebugPanel] in the development debug panel.
 */
class DebugTabExampleFragment :
    DebugTabFragment<DebugTabExampleViewModel>(DebugTabExampleViewModel::class) {

    override val viewFactory = ViewFactory(DebugtabFragmentBinding::inflate)
}
