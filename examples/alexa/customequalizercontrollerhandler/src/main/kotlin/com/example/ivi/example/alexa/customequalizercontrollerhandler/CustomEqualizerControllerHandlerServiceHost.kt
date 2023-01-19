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

package com.example.ivi.example.alexa.customequalizercontrollerhandler

import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostBase
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext

/**
 * A [CustomEqualizerControllerHandlerService] host server.
 */
internal class CustomEqualizerControllerHandlerServiceHost(
    iviServiceHostContext: IviServiceHostContext,
    iviDiscoverableServiceIdProvider: IviDiscoverableServiceIdProvider
) :
    IviServiceHostBase(iviServiceHostContext) {

    override val iviServices = setOf(
        CustomEqualizerControllerHandlerService(
            iviServiceHostContext,
            iviDiscoverableServiceIdProvider
        )
    )
}
