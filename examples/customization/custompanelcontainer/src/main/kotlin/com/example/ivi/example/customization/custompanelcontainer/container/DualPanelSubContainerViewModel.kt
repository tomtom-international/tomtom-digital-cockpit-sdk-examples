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

package com.example.ivi.example.customization.custompanelcontainer.container

import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.PanelFragmentContainerId
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.subcontainer.PanelSubContainerViewModel

/**
 * ViewModel for sub-containers used by [DualPanelContainer].
 */
internal class DualPanelSubContainerViewModel :
    PanelSubContainerViewModel<DualPanelContainerData>() {

    internal val panelFragmentContainer1Id:
        PanelFragmentContainerId by generatePanelFragmentContainerId()

    internal val panelFragmentContainer2Id:
        PanelFragmentContainerId by generatePanelFragmentContainerId()
}
