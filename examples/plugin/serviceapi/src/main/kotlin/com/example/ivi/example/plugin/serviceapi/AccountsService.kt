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

package com.example.ivi.example.plugin.serviceapi

import com.example.ivi.example.plugin.common.Account
import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.IviDataSource
import com.tomtom.ivi.platform.framework.api.ipc.iviserviceannotations.IviService
import com.tomtom.ivi.platform.framework.api.ipc.iviserviceannotations.IviServiceFun

/**
 * IVI service to manage an account state.
 */
@IviService(
    serviceId = "com.example.ivi.example.plugin.service",
)
interface AccountsService {
    /**
     * Indicates which account is currently active.
     * `null` if no account is logged in.
     */
    val activeAccount: Account?

    /**
     * Data set of accounts. The accounts can be queried and sorted.
     */
    @IviExperimental
    val accounts: IviDataSource<Account, AccountsDataSourceQuery>

    /**
     * Tries to log an user in under [username] with [password].
     * Returns `true` if the user is logged in successfully, `false` otherwise.
     */
    @IviServiceFun
    suspend fun logIn(username: String, password: SensitiveString): Boolean

    /**
     * Logs the currently logged in user out.
     */
    @IviServiceFun
    suspend fun logOut()

    companion object
}
