/*
 * Copyright © 2020 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.panelhierarchy

import com.example.ivi.example.panelhierarchy.root.HierarchyRootPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.Frontend
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel

internal class HierarchyFrontend(frontendContext: FrontendContext) : Frontend(frontendContext) {

    override fun openTaskPanels() {
        removePanels<TaskPanel>()
        addPanel(HierarchyRootPanel(frontendContext) { child -> addPanel(child) })
    }
}
