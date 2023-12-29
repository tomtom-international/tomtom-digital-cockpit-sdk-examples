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

package com.example.ivi.example.applauncher.services.androidappsourceprovider

import com.tomtom.ivi.appsuite.appstore.api.common.model.ParcelableAppClass
import com.tomtom.ivi.appsuite.appstore.api.common.model.androidapptypes.AndroidApp
import com.tomtom.ivi.appsuite.appstore.api.common.model.androidapptypes.LaunchableAndroidApp
import com.tomtom.ivi.appsuite.appstore.api.service.appsourceprovider.AppSourceProviderServiceBase
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext

class AndroidAppSourceProviderService(
    iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider,
) : AppSourceProviderServiceBase(iviServiceHostContext, serviceIdProvider) {

    override fun onCreate() {
        super.onCreate()

        appStore = null

        // An IVI service interface can use only [Parcelable] types, so the [supportedAppClass] must
        // be returned inside a [Parcelable] wrapper class.
        supportedAppClass = ParcelableAppClass(AndroidApp::class.java)

        // The value of [launchablePackageName] should match the package name of an Android app
        // known to be installed on the device, otherwise the app will not appear in the app
        // launcher.
        installedApps = listOf(
            LaunchableAndroidApp(
                launchablePackageName = "com.android.example",
            ),
        )
        serviceReady = true
    }
}
