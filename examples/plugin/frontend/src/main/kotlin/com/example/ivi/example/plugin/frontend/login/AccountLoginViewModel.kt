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

package com.example.ivi.example.plugin.frontend.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ivi.example.plugin.common.Account
import com.example.ivi.example.plugin.serviceapi.AccountsDataSourceQuery
import com.example.ivi.example.plugin.serviceapi.AccountsService
import com.example.ivi.example.plugin.serviceapi.SensitiveString
import com.example.ivi.example.plugin.serviceapi.createApi
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.IviDataSource
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.first
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.mapQuery
import com.tomtom.ivi.platform.framework.api.ipc.iviserviceandroidpaging.mapPagingData
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel
import com.tomtom.tools.android.api.livedata.allTrue
import com.tomtom.tools.android.api.livedata.valueUpToDate
import kotlinx.coroutines.flow.Flow

internal class AccountLoginViewModel(panel: AccountLoginPanel) :
    FrontendViewModel<AccountLoginPanel>(panel) {

    private val accountsServiceApi =
        AccountsService.createApi(this, frontendContext.iviServiceProvider)

    val username = MutableLiveData("")
    val password = MutableLiveData("")

    val isLoginEnabled = allTrue(
        accountsServiceApi.serviceAvailable,
        username.map { it.isNotBlank() },
        password.map { it.isNotBlank() },
    )

    /**
     * Converts an [IviDataSource] [LiveData] to an [Account] [LiveData], the value of which is set
     * to the first item of the query result set.
     */
    @Suppress("unused")
    val lastLogin: LiveData<Account> =
        accountsServiceApi.accounts.mapQuery(lastLoginQuery).first()

    /**
     * Converts an [IviDataSource] [LiveData] to a [Flow] of [PagingData]. This
     * flow can be bound to an `RecyclerView`. See Android Paging library for details.
     */
    @Suppress("unused")
    val allAccountsPagingDataFlow: Flow<PagingData<Account>> = accountsServiceApi.accounts
        .mapPagingData(pagingConfig, allAccountsQuery, this)

    fun onLoginClick() {
        isLoginEnabled.valueUpToDate?.takeIf { it }?.let {
            val username = username.value ?: return
            val password = password.value ?: return

            /**
             * Log in a user with asynchronous call.
             */
            accountsServiceApi.logInAsync(username, SensitiveString(password))

            /**
             * Suspendable method, callable from a Kotlin co-routine
             */
            // runBlocking {
            //     accountsServiceApi.coLogIn(username, password)
            // }

            /**
             * It is also possible to call a setter when the service becomes available by using
             * the TomTom Digital Cockpit utility method [AccountsService.queueOrRun()]"
             */
            // accountsServiceApi.queueOrRun { service ->
            //     service.logInAsync(username, SensitiveString(password))
            // }
        }
    }

    companion object {
        @VisibleForTesting
        internal val lastLoginQuery = AccountsDataSourceQuery(
            selection = AccountsDataSourceQuery.Selection.LOGGED_IN_AT_LEAST_ONCE,
            orderBy = AccountsDataSourceQuery.Order.LAST_LOG_IN_TIME_DESCENDING,
        )

        @VisibleForTesting
        internal val allAccountsQuery = AccountsDataSourceQuery(
            selection = AccountsDataSourceQuery.Selection.ALL,
            orderBy = AccountsDataSourceQuery.Order.USERNAME,
        )

        private val pagingConfig = PagingConfig(
            pageSize = 10,
        )
    }
}
