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

package com.example.ivi.example.applauncher.services.launchableandroidapplaunchhandler

import com.tomtom.ivi.appsuite.appstore.api.common.model.App
import com.tomtom.ivi.appsuite.appstore.api.common.model.ParcelableAppClass
import com.tomtom.ivi.appsuite.appstore.api.common.model.androidapptypes.LaunchableAndroidApp
import com.tomtom.ivi.appsuite.appstore.api.service.applaunchhandler.AppLaunchHandlerServiceBase
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext

class LaunchableAndroidAppLaunchHandlerService(
    private val iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider
) : AppLaunchHandlerServiceBase(iviServiceHostContext, serviceIdProvider) {

    override fun onCreate() {
        super.onCreate()

        // An IVI service interface can use only [Parcelable] types, so the [supportedAppClass] must
        // be returned inside a [Parcelable] wrapper class.
        supportedAppClass = ParcelableAppClass(LaunchableAndroidApp::class.java)
    }

    override fun onRequiredPropertiesInitialized() {
        serviceReady = true
    }

    override suspend fun launchApp(app: App) {
        require(app is LaunchableAndroidApp)
        launchLaunchableAndroidApp(app)
    }

    private fun launchLaunchableAndroidApp(app: LaunchableAndroidApp) {
        val launchIntent =
            iviServiceHostContext.context.packageManager.getLaunchIntentForPackage(app.packageName)
        iviServiceHostContext.context.startActivity(launchIntent)
    }
}
