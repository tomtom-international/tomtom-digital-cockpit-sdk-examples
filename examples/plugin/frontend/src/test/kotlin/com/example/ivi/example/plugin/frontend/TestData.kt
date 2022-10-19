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

import com.example.ivi.example.plugin.common.Account
import com.example.ivi.example.plugin.frontend.login.AccountLoginViewModel.Companion.allAccountsQuery
import com.example.ivi.example.plugin.frontend.login.AccountLoginViewModel.Companion.lastLoginQuery
import java.time.Instant

internal object TestData {
    const val USERNAME = "Username"
    const val PASSWORD = "Password"

    private const val ANOTHER_USERNAME = "AnotherUsername"

    val testAccount = Account(username = USERNAME)
    val anotherTestAccount = Account(
        username = ANOTHER_USERNAME,
        loggedIn = true,
        lastLogIn = Instant.now()
    )

    val allAccounts = listOf(testAccount, anotherTestAccount)
    val lastLoginAccount = anotherTestAccount

    val accountsDataSourceData = mapOf(
        allAccountsQuery to allAccounts,
        lastLoginQuery to listOf(lastLoginAccount)
    )
}
