/*
 * Copyright © 2022 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.applauncher.services.webapplaunchtrigger

import android.content.IntentFilter
import com.tomtom.ivi.appsuite.appstore.api.service.applaunchtrigger.AppLaunchTriggerServiceBase
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext

class WebAppLaunchTriggerService(
    iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider
) : AppLaunchTriggerServiceBase(iviServiceHostContext, serviceIdProvider) {

    override fun onCreate() {
        super.onCreate()
        // To receive a broadcast, we need to register it with the service
        // [Context](https://developer.android.com/reference/android/content/Context), and with an
        // [IntentFilter](https://developer.android.com/reference/android/content/IntentFilter),
        // which identifies the broadcast action.
        val webAppLaunchTriggerBroadcastReceiver = WebAppLaunchTriggerBroadcastReceiver()
        val filter = IntentFilter(WEB_APP_BROADCAST_LAUNCH_TRIGGER_ACTION)

        // Register the receiver with a permission to prevent it being triggered insecurely.
        // Contact the 3rd-party app store provider to add the defined permission when sending the
        // intent.
        context.registerReceiver(
            webAppLaunchTriggerBroadcastReceiver,
            filter,
            WEB_APP_BROADCAST_LAUNCH_TRIGGER_PERMISSION,
            null
        )

        // To receive the triggered web apps from the previously registered broadcast, we need to
        // observe [webAppLaunchTriggerBroadcastReceiver.triggeredWebApp].
        webAppLaunchTriggerBroadcastReceiver.triggeredWebApp.observe(this) {
            appLaunchTriggerEventListener.onAppLaunchTriggered(it)
        }

        serviceReady = true
    }

    private companion object {
        const val WEB_APP_BROADCAST_LAUNCH_TRIGGER_ACTION =
            "com.example.ivi.example.applauncher.WEB_APP_LAUNCH_ACTION"

        const val WEB_APP_BROADCAST_LAUNCH_TRIGGER_PERMISSION =
            "com.example.ivi.example.applauncher.services.webapplaunchtrigger.permission." +
                "APP_LAUNCH_BROADCAST_PERMISSION"
    }
}
