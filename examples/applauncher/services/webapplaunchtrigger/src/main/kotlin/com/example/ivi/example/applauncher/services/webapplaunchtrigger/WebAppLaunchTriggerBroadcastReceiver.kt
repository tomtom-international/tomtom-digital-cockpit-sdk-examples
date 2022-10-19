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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ivi.example.applauncher.common.webapp.WebApp
import com.tomtom.tools.android.api.resourceresolution.drawable.ResourceDrawableResolver
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver

class WebAppLaunchTriggerBroadcastReceiver : BroadcastReceiver() {

    private val mutableTriggeredWebApp = MutableLiveData<WebApp>()
    val triggeredWebApp: LiveData<WebApp> = mutableTriggeredWebApp

    override fun onReceive(context: Context?, intent: Intent?) {
        // [WebApp.displayName], [WebApp.summary] and [WebApp.icon] are not necessary for launching
        // a [WebApp], therefore dummy data is assigned for these values.
        intent?.apply {
            mutableTriggeredWebApp.value = WebApp(
                url = getStringExtra("url") ?: "",
                displayName = StaticStringResolver("Dummy display name"),
                summary = StaticStringResolver("Dummy summary"),
                icon = ResourceDrawableResolver(R.drawable.ttivi_appstore_icon_invalidpackage)
            )
        }
    }
}
