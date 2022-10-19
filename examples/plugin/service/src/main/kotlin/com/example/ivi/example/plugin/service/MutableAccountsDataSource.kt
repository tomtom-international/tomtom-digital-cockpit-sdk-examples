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
import com.example.ivi.example.plugin.serviceapi.AccountsDataSourceQuery
import com.tomtom.ivi.platform.framework.api.common.uid.Uid
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.IviPagingSource
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.MutableIviDataSource
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.MutableIviPagingSource

internal class MutableAccountsDataSource : MutableIviDataSource<Account, AccountsDataSourceQuery>(
    jumpingSupported = true
) {
    private val mutableAccounts = mutableMapOf<Uid<Account>, Account>()

    val accounts: Map<Uid<Account>, Account> = mutableAccounts

    fun addOrUpdateAccount(account: Account) {
        mutableAccounts[account.accountUid] = account
        invalidateAllPagingSources()
    }

    override fun createPagingSource(
        query: AccountsDataSourceQuery
    ): MutableIviPagingSource<Account> =
        MutableAccountsPagingSource(
            when (query.selection) {
                AccountsDataSourceQuery.Selection.ALL -> {
                    mutableAccounts.values.toList()
                }
                AccountsDataSourceQuery.Selection.LOGGED_IN_AT_LEAST_ONCE -> {
                    mutableAccounts.values.filter { it.loggedIn }
                }
            }.let { data ->
                when (query.orderBy) {
                    AccountsDataSourceQuery.Order.USERNAME -> {
                        data.sortedBy { it.username }
                    }
                    AccountsDataSourceQuery.Order.LAST_LOG_IN_TIME_DESCENDING -> {
                        data.sortedByDescending { it.lastLogIn }
                    }
                }
            }
        )

    private class MutableAccountsPagingSource(
        val data: List<Account>
    ) : MutableIviPagingSource<Account>() {
        override val loadSizeLimit = DATA_SOURCE_MAX_PAGE_SIZE

        init {
            registerInvalidatedCallback {
                // Close resources if applicable.
            }
        }

        override suspend fun loadWithLoadSizeLimited(
            loadParams: IviPagingSource.LoadParams
        ): IviPagingSource.LoadResult<Account> {
            return when (loadParams) {
                is IviPagingSource.LoadParams.Refresh,
                is IviPagingSource.LoadParams.Append -> {
                    val dataIndex = minOf(loadParams.dataIndex, data.size)
                    createPage(
                        dataIndex = dataIndex,
                        pageSize = minOf(loadParams.loadSize, data.size - dataIndex),
                        placeholdersEnabled = loadParams.placeholdersEnabled
                    )
                }
                is IviPagingSource.LoadParams.Prepend -> {
                    val size = minOf(loadParams.loadSize, loadParams.dataIndex, data.size)
                    val dataIndex = loadParams.dataIndex - size
                    createPage(
                        dataIndex = dataIndex,
                        pageSize = size,
                        placeholdersEnabled = loadParams.placeholdersEnabled
                    )
                }
            }
        }

        private fun createPage(
            dataIndex: Int,
            pageSize: Int,
            placeholdersEnabled: Boolean
        ): IviPagingSource.LoadResult.Page<Account> {
            return IviPagingSource.LoadResult.Page(
                dataIndex = dataIndex,
                data = data.subList(dataIndex, dataIndex + pageSize),
                itemsBefore = dataIndex.takeIf { placeholdersEnabled },
                itemsAfter = (data.size - dataIndex - pageSize).takeIf { placeholdersEnabled }
            )
        }
    }

    companion object {
        const val DATA_SOURCE_MAX_PAGE_SIZE: Int = 100
    }
}
