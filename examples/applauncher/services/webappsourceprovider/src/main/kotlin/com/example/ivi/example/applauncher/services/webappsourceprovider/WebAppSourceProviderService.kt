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

package com.example.ivi.example.applauncher.services.webappsourceprovider

import com.tomtom.ivi.appsuite.appstore.api.common.model.ParcelableAppClass
import com.tomtom.ivi.appsuite.appstore.api.service.appsourceprovider.AppSourceProviderServiceBase
import com.example.ivi.example.applauncher.common.webapp.WebApp
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.tools.android.api.resourceresolution.drawable.ResourceDrawableResolver
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver

class WebAppSourceProviderService(
    iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider
) : AppSourceProviderServiceBase(iviServiceHostContext, serviceIdProvider) {

    override fun onCreate() {
        super.onCreate()

        appStore = null

        // An IVI service interface can use only [Parcelable] types, so the [supportedAppClass]
        // must be returned inside a [Parcelable] wrapper class.
        supportedAppClass = ParcelableAppClass(WebApp::class.java)

        installedApps = listOf(
            WebApp(
                url = "https://www.tomtom.com/",
                displayName = StaticStringResolver("TomTom"),
                summary = StaticStringResolver("TomTom Web"),
                icon = ResourceDrawableResolver(R.drawable.ttivi_appstore_icon_invalidpackage)
            )
        )
    }

    override fun onRequiredPropertiesInitialized() {
        serviceReady = true
    }
}
