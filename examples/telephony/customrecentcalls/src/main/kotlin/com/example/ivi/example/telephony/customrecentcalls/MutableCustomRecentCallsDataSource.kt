/*
 * Copyright © 2023 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.telephony.customrecentcalls

import android.content.Context
import com.tomtom.ivi.platform.contacts.api.common.util.comparePhoneNumbers
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.IviPagingSource
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.MutableIviDataSource
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.MutableIviPagingSource
import com.tomtom.ivi.platform.recentcalls.api.common.model.RecentCall
import com.tomtom.ivi.platform.recentcalls.api.service.recentcalls.RecentCallsDataSourceElement
import com.tomtom.ivi.platform.recentcalls.api.service.recentcalls.RecentCallsDataSourceElement.RecentCallItem
import com.tomtom.ivi.platform.recentcalls.api.service.recentcalls.RecentCallsDataSourceElement.RecentCallItem.RecentCallMetaData
import com.tomtom.ivi.platform.recentcalls.api.service.recentcalls.RecentCallsDataSourceQuery
import com.tomtom.ivi.platform.recentcalls.api.service.recentcalls.RecentCallsDataSourceQuery.RecentCallFilter
import com.tomtom.ivi.platform.recentcalls.api.service.recentcalls.RecentCallsDataSourceQuery.RecentCallOrderBy.RecentCallItemOrder
import com.tomtom.ivi.platform.recentcalls.api.service.recentcalls.RecentCallsDataSourceQuery.RecentCallQueryFlags
import com.tomtom.ivi.platform.telecom.api.common.model.PhoneNumber
import java.text.Collator
import java.util.Locale

/**
 * Load data from a data set and create a page out of it.
 */
internal class MutableCustomRecentCallsDataSource(private val context: Context) :
    MutableIviDataSource<RecentCallsDataSourceElement, RecentCallsDataSourceQuery>(
        jumpingSupported = true
    ) {

    private val mutableRecentCalls = mutableListOf<RecentCall>()

    private val recentCalls: List<RecentCall> = mutableRecentCalls

    private var collatorLocale: Locale = getSystemLocale()
    private lateinit var collator: Collator

    private fun getSystemLocale(): Locale = context.resources.configuration.locales.get(0)

    init {
        setupCollator()
    }

    private fun setupCollator() {
        collator = Collator.getInstance(collatorLocale)
    }

    private fun ensureCollatorLocale() {
        if (collatorLocale != getSystemLocale()) {
            collatorLocale = getSystemLocale()
            setupCollator()
        }
    }

    /**
     * Add a list of recent calls.
     */
    fun addRecentCalls(recentCalls: List<RecentCall>) {
        mutableRecentCalls += recentCalls
    }

    override fun createPagingSource(query: RecentCallsDataSourceQuery):
        MutableIviPagingSource<RecentCallsDataSourceElement> {
        val allRecentCalls: List<RecentCall> = recentCalls.toList()

        val filteredResults = query.filter?.let {
            applyRecentCallsFilter(it, allRecentCalls)
        } ?: allRecentCalls

        val orderedSelection: List<RecentCall> = query.orderBy?.let {
            orderRecentCallsElements(it, filteredResults)
        } ?: filteredResults

        val recentCallItems: List<RecentCallItem> =
            query.queryFlags?.let { recentCallQueryFlags ->
                if (recentCallQueryFlags.contains(
                        RecentCallQueryFlags.MERGE_CONSECUTIVE_RECENT_CALLS
                    )
                ) {
                    orderedSelection.removeConsecutiveElements()
                } else {
                    orderedSelection.map { RecentCallItem(it) }
                }
            } ?: orderedSelection.map { RecentCallItem(it) }

        return MutableRecentCallsPagingSource(recentCallItems)
    }

    private fun applyRecentCallsFilter(
        filter: RecentCallFilter,
        allRecentCalls: List<RecentCall>
    ): List<RecentCall> {
        ensureCollatorLocale()
        collator.strength = Collator.PRIMARY
        return when (filter) {
            is RecentCallFilter.CallType -> {
                allRecentCalls.filter { recentCall ->
                    filter.callTypes.any { callType ->
                        recentCall.type == callType
                    }
                }
            }
            is RecentCallFilter.DisplayName -> {
                allRecentCalls.filter { recentCall ->
                    filter.displayNames.any { displayName ->
                        recentCall.displayName.contains(displayName, collator)
                    }
                }
            }
            is RecentCallFilter.PhoneNumber -> {
                allRecentCalls.filter { recentCall ->
                    comparePhoneNumbers(filter.phoneNumber, recentCall.phoneNumber.number)
                }
            }
        }
    }

    private fun orderRecentCallsElements(
        orderBy: RecentCallsDataSourceQuery.RecentCallOrderBy,
        recentCalls: List<RecentCall>
    ): List<RecentCall> = when (orderBy.order) {
        RecentCallItemOrder.CREATION_TIME_DESC -> {
            recentCalls.sortedByDescending {
                it.creationTime
            }
        }
    }

    /**
     * Converts a list of [RecentCall]s to a list of [RecentCallItem]s and adds meta data.
     */
    private fun List<RecentCall>.removeConsecutiveElements(): List<RecentCallItem> {
        val callsCount = mutableMapOf<RecentCall, Int>()
        val distinctRecentCalls =
            withoutDistinctConsecutiveElements(ComparableRecentCall::compare) { lastCall, _ ->
                callsCount[lastCall] = callsCount.getOrDefault(lastCall, 1).inc()
            }.map { recentCall ->
                RecentCallItem(
                    recentCall = recentCall,
                    RecentCallMetaData(callsCount.getOrDefault(recentCall, 1))
                )
            }
        return distinctRecentCalls
    }

    private fun <E> List<E>.withoutDistinctConsecutiveElements(
        compare: (E, E) -> Boolean,
        onConsecutiveElementFound: ((E, E) -> Unit)
    ): List<E> {
        val newList = mutableListOf<E>()
        if (size <= 1) return newList.also { it.addAll(this) }
        for (i in indices) {
            if (newList.isEmpty()) {
                newList.add(this[i])
            } else {
                if (compare(newList.last(), this[i])) {
                    onConsecutiveElementFound(newList.last(), this[i])
                } else {
                    // new element
                    newList.add(this[i])
                }
            }
        }
        return newList.toList()
    }

    private fun String.contains(target: String, collator: Collator): Boolean {
        if (target.length > length) {
            return false
        }
        val end = length - target.length + 1
        repeat(end) {
            val sourceSubstring = substring(it, it + target.length)
            if (collator.compare(sourceSubstring, target) == 0) {
                return true
            }
        }
        return false
    }

    companion object {
        internal data class ComparableRecentCall(
            private val phoneNumber: PhoneNumber,
            private val callType: RecentCall.RecentCallType
        ) {
            constructor(recentCall: RecentCall) : this(recentCall.phoneNumber, recentCall.type)

            companion object {
                fun compare(lhRecentCall: RecentCall, rhRecentCall: RecentCall) =
                    ComparableRecentCall(lhRecentCall) == ComparableRecentCall(rhRecentCall)
            }
        }
    }

    internal class MutableRecentCallsPagingSource(
        private val data: List<RecentCallsDataSourceElement>
    ) : MutableIviPagingSource<RecentCallsDataSourceElement>() {
        override val loadSizeLimit = RECENT_CALLS_MAX_PAGE_SIZE

        override suspend fun loadWithLoadSizeLimited(
            loadParams: IviPagingSource.LoadParams
        ): IviPagingSource.LoadResult<RecentCallsDataSourceElement> {
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
        ): IviPagingSource.LoadResult.Page<RecentCallsDataSourceElement> {
            return IviPagingSource.LoadResult.Page(
                dataIndex = dataIndex,
                data = data.subList(dataIndex, dataIndex + pageSize),
                itemsBefore = dataIndex.takeIf { placeholdersEnabled },
                itemsAfter = (data.size - dataIndex - pageSize).takeIf { placeholdersEnabled }
            )
        }

        private companion object {
            // For optimal usage of data source, the maximum page should fit 500kb.
            // Setting page size to 50 ensures that page size remains under the optimal
            // recommended size.
            const val RECENT_CALLS_MAX_PAGE_SIZE: Int = 50
        }
    }
}
