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

package com.example.ivi.example.plugin.service

import com.example.ivi.example.plugin.common.Account
import com.example.ivi.example.plugin.service.StaticConfiguration.onlineAccountEndpointConfigKey
import com.example.ivi.example.plugin.serviceapi.AccountsServiceBase
import com.example.ivi.example.plugin.serviceapi.SensitiveString
import com.example.ivi.example.plugin.settingsserviceapi.AccountSettingsService
import com.example.ivi.example.plugin.settingsserviceapi.createApi
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.queueOrRun
import com.tomtom.tools.android.api.livedata.requireValue
import com.tomtom.tools.android.api.livedata.valueUpToDate
import java.time.Instant
import java.time.temporal.ChronoUnit

internal class StockAccountsService(iviServiceHostContext: IviServiceHostContext) :
    AccountsServiceBase(iviServiceHostContext) {

    private val onlineAccountEndpoint =
        iviServiceHostContext.staticConfigurationProvider[onlineAccountEndpointConfigKey]

    private val settingsServiceApi = AccountSettingsService.createApi(this, iviServiceProvider)

    private val mutableAccountsDataSource = MutableAccountsDataSource()

    override fun onCreate() {
        super.onCreate()

        accounts = mutableAccountsDataSource

        // Executes an action once when the service becomes available.
        settingsServiceApi.queueOrRun { service ->
            // The client could have successfully logged in in the past, let's check if the login
            // is still valid.
            val daysSinceLogin = Instant.ofEpochSecond(service.loginTimestamp.requireValue())
                .until(Instant.now(), ChronoUnit.DAYS)
            if (daysSinceLogin >= service.onlineLoginValidPeriodInDays.requireValue()) {
                service.updateActiveAccountAsync(null)
            } else {
                service.activeAccount.valueUpToDate?.let { account ->
                    mutableAccountsDataSource.addOrUpdateAccount(account)
                    activeAccount = account
                }
            }
        }

        // The account service cannot run without the account settings service.
        settingsServiceApi.serviceAvailable.observe(this) {
            serviceReady = it
        }
    }

    override suspend fun logIn(username: String, password: SensitiveString): Boolean {
        val account = (
            mutableAccountsDataSource.accounts.values.find { it.username == username }
                ?: logInOnline(username, password)
            )
            ?.copy(loggedIn = true, lastLogIn = Instant.now())

        account?.let {
            mutableAccountsDataSource.addOrUpdateAccount(it)
            activeAccount = it
            settingsServiceApi.coUpdateActiveAccount(activeAccount)
        }

        return account != null
    }

    override suspend fun logOut() {
        activeAccount?.copy(loggedIn = false)?.let {
            mutableAccountsDataSource.addOrUpdateAccount(it)
            activeAccount = null
            settingsServiceApi.coUpdateActiveAccount(null)
        }
    }

    private fun logInOnline(username: String, password: SensitiveString): Account? =
        takeIf { isValidUsername(username) && isValidPassword(password.value) }?.run {
            // Simulate making an online request.
            onlineAccountEndpoint
            Account(username = username)
        }

    companion object {
        private fun isValidUsername(value: String) = value.trim().length > 1
        private fun isValidPassword(value: String) = value.trim().length > 2
    }
}
