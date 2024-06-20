/*
 * Copyright © 2024 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.evcharging.frontend

import com.tomtom.ivi.platform.evcharging.api.common.evcharging.EmspAuthenticationStatus
import com.tomtom.ivi.platform.evcharging.api.service.evchargingmediator.EvChargingMediatorService
import com.tomtom.ivi.platform.evcharging.api.service.evchargingmediator.createApi
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.queueOrRun
import com.tomtom.ivi.platform.frontend.api.common.frontend.Frontend
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext

internal class EvChargingLoginFrontend(frontendContext: FrontendContext) :
    Frontend(frontendContext) {

    private val evChargingMediatorService =
        EvChargingMediatorService.createApi(this, frontendContext.iviServiceProvider)

    private var loginPanel by createPanelDelegate<OpenIdLoginPanel>()

    override fun onCreate() {
        super.onCreate()

        evChargingMediatorService.queueOrRun { service ->
            service.emspAuthenticationStatus.observe(this) { authenticationStatuses ->
                val authenticationRequestInfo = authenticationStatuses?.values?.firstOrNull {
                    when (it) {
                        is EmspAuthenticationStatus.AuthenticationRequested ->
                            it.openIdAuthenticationRequestInfo?.authorizationRequestIntent != null
                        else -> false
                    }
                }
                setupLoginUi(authenticationRequestInfo)
            }
        }
    }

    private fun setupLoginUi(authenticationRequestInfo: EmspAuthenticationStatus?) {
        authenticationRequestInfo?.let {
            showLoginPanel()
        } ?: hideLoginPanel()
    }

    private fun showLoginPanel() {
        loginPanel = loginPanel ?: OpenIdLoginPanel(frontendContext) {
            hideLoginPanel()
        }
    }

    private fun hideLoginPanel() {
        loginPanel = null
    }
}
