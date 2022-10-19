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

package com.example.ivi.example.plugin.frontend

import androidx.lifecycle.map
import com.example.ivi.example.plugin.frontend.info.AccountInfoPanel
import com.example.ivi.example.plugin.frontend.login.AccountLoginPanel
import com.example.ivi.example.plugin.serviceapi.AccountsService
import com.example.ivi.example.plugin.serviceapi.createApi
import com.tomtom.ivi.platform.frontend.api.common.frontend.Frontend
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.filterPanels
import com.tomtom.tools.android.api.livedata.ifTrueOrDefault
import com.tomtom.tools.android.api.livedata.valueUpToDate

internal class AccountFrontend(frontendContext: FrontendContext) : Frontend(frontendContext) {

    private val accountsServiceApi =
        AccountsService.createApi(this, frontendContext.iviServiceProvider)

    private val isUserLoggedIn =
        accountsServiceApi.serviceAvailable.ifTrueOrDefault(false) {
            accountsServiceApi.activeAccount.map { it != null }
        }

    private val hasLoginPanel: Boolean
        get() = panels.valueUpToDate?.filterPanels<AccountLoginPanel>()?.size == 1

    private val hasInfoPanel: Boolean
        get() = panels.valueUpToDate?.filterPanels<AccountInfoPanel>()?.size == 1

    override fun onCreate() {
        super.onCreate()

        isUserLoggedIn.observe(this) { loggedIn ->
            // Switch panels if an user login status changed, iff there is an open panel.
            when {
                loggedIn && hasLoginPanel -> goToInfoPanel()
                !loggedIn && hasInfoPanel -> goToLoginPanel()
            }
        }
    }

    override fun openTaskPanels() {
        if (isUserLoggedIn.valueUpToDate == true) {
            goToInfoPanel()
        } else {
            goToLoginPanel()
        }
    }

    private fun goToInfoPanel() {
        removePanels<TaskPanel>()
        addPanel(AccountInfoPanel(frontendContext))
    }

    private fun goToLoginPanel() {
        removePanels<TaskPanel>()
        addPanel(AccountLoginPanel(frontendContext))
    }
}
