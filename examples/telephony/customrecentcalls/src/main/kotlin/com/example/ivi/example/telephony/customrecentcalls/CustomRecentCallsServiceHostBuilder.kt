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

package com.example.ivi.example.telephony.customrecentcalls

import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostBuilder
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.SimpleIviServiceHost

/**
 * The builder class for the `CustomRecentCallsServiceHost`.
 *
 * It must contain at least the empty companion object, which will be extended by the IVI service
 * framework. Kotlin does not allow extending the companion object if it has not been declared.
 */
class CustomRecentCallsServiceHostBuilder : IviServiceHostBuilder() {

    override fun build(iviServiceHostContext: IviServiceHostContext) =
        SimpleIviServiceHost(
            iviServiceHostContext,
            setOf(CustomRecentCallsService(iviServiceHostContext)),
        )

    companion object
}
