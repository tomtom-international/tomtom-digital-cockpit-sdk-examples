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

package com.example.ivi.example.media.customnowplaying

import com.tomtom.ivi.platform.framework.api.common.iviinstance.IviInstanceId
import com.tomtom.ivi.platform.framework.api.product.defaultactivity.DefaultActivity
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHost

/**
 * Custom activity that uses [CustomSystemUiHost] as the system UI host.
 */
class CustomActivity : DefaultActivity() {
    override fun createSystemUiHost(iviInstanceId: IviInstanceId): SystemUiHost =
        CustomSystemUiHost(getDefaultSystemUiHostContext(iviInstanceId))
}
