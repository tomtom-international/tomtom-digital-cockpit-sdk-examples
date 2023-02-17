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

package com.example.ivi.example.telephony.customcontacts

import android.content.Context
import com.tomtom.ivi.platform.contacts.api.common.model.Contact
import com.tomtom.ivi.platform.contacts.api.common.util.comparePhoneNumbers
import com.tomtom.ivi.platform.contacts.api.common.util.contactGroup
import com.tomtom.ivi.platform.contacts.api.common.util.toContactItems
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceElement
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceElement.ContactGroup
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactFilter
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactFilter.CompanyName
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactFilter.DisplayName
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactFilter.FamilyName
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactFilter.Favorite
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactFilter.GivenName
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactFilter.PhoneNumber
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactFilter.Source
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactOrderBy.ContactItemOrder
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.IviPagingSource
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.MutableIviDataSource
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.datasource.MutableIviPagingSource
import java.text.Collator
import java.util.Locale

/**
 * Load data from a data set and create a page out of it.
 */
internal class MutableCustomContactsDataSource(private val context: Context) :
    MutableIviDataSource<ContactsDataSourceElement, ContactsDataSourceQuery>(
        jumpingSupported = true
    ) {

    private val mutableContacts = mutableListOf<Contact>()

    private val contacts: List<Contact> = mutableContacts

    private var collatorLocale: Locale = getSystemLocale()
    private lateinit var collator: Collator

    private fun getSystemLocale(): Locale = context.resources.configuration.locales.get(0)

    init {
        setupCollator()
    }

    private fun setupCollator() {
        collator = Collator.getInstance(collatorLocale).also {
            it?.strength = Collator.CANONICAL_DECOMPOSITION
        }
    }

    /**
     * Add or update contact page.
     */
    fun addOrUpdateContact(contact: Contact) {
        mutableContacts.add(contact)
        invalidateAllPagingSources()
    }

    override fun createPagingSource(query: ContactsDataSourceQuery):
        MutableIviPagingSource<ContactsDataSourceElement> {
        val allContacts: List<Contact> = contacts.toList()

        val filteredSelection = query.filter?.let {
            applyContactFilter(it, allContacts)
        } ?: allContacts

        val orderedSelection: List<Contact> = query.orderBy?.let {
            orderContactElements(it, filteredSelection)
        } ?: filteredSelection

        val groupedBySelection: List<ContactsDataSourceElement> = query.groupBy?.let {
            applyGroupBy(it, orderedSelection)
        } ?: orderedSelection.toContactItems()

        return MutableContactsPagingSource(groupedBySelection)
    }

    private fun applyContactFilter(
        filter: ContactFilter,
        allContacts: List<Contact>
    ): List<Contact> {
        return when (filter) {
            is CompanyName -> {
                allContacts.filter { contact ->
                    filter.companyNames.any { companyName ->
                        contact.companyName.startsWith(companyName, true)
                    }
                }
            }
            is DisplayName -> {
                allContacts.filter { contact ->
                    filter.displayNames.any { displayName ->
                        contact.displayName.startsWith(displayName, true)
                    }
                }
            }
            is FamilyName -> {
                allContacts.filter { contact ->
                    filter.familyNames.any { familyName ->
                        contact.familyName.startsWith(familyName, true)
                    }
                }
            }
            is Favorite -> {
                allContacts.filter { contact ->
                    contact.favorite
                }
            }
            is GivenName -> {
                allContacts.filter { contact ->
                    filter.givenNames.any { givenName ->
                        contact.givenName.startsWith(givenName, true)
                    }
                }
            }
            is PhoneNumber -> {
                allContacts.filter { contact ->
                    filter.phoneNumber?.let {
                        contact.phoneNumbers.any { phoneNumber ->
                            comparePhoneNumbers(it, phoneNumber.number)
                        }
                    } ?: contact.phoneNumbers.isEmpty()
                }
            }
            is Source -> {
                allContacts.filter { contact ->
                    filter.source == contact.source
                }
            }
            is ContactFilter.ContactFilterOperator -> {
                applyContactFilterGroup(filter, allContacts)
            }
        }
    }

    private fun applyContactFilterGroup(
        filterOperator: ContactFilter.ContactFilterOperator,
        selection: List<Contact>
    ): List<Contact> =
        when (filterOperator.filterOperator) {
            ContactFilter.ContactFilterOperator.FilterOperator.OR -> {
                // All the results of the filters in this group will be or-ed.
                filterOperator.filters.flatMap { applyContactFilter(it, selection) }
            }
            ContactFilter.ContactFilterOperator.FilterOperator.AND -> {
                // All the results of the filters in this group will be and-ed.
                val filterResults = filterOperator.filters.map {
                    applyContactFilter(it, selection).toSet()
                }

                val filteredSelections = selection.toMutableList()

                filterResults.forEach { filteredSelections.retainAll(it) }
                filteredSelections
            }
            ContactFilter.ContactFilterOperator.FilterOperator.NOT -> {
                // All the results in the group will be removed from the selection
                val filterResults = filterOperator.filters.map {
                    applyContactFilter(it, selection).toSet()
                }

                val filteredSelections = selection.toMutableList()

                filterResults.forEach { filteredSelections.removeAll(it) }
                filteredSelections
            }
            else -> {
                error("Invalid FilterGroup provided $filterOperator")
            }
        }.distinct()

    private fun sortContacts(
        order: ContactItemOrder,
        contacts: List<Contact>
    ): List<Contact> = when (order) {
        ContactItemOrder.FAMILY_NAME_ASC -> {
            contacts.sortedBy {
                it.familyName.ifBlank { it.displayName }
            }
        }
        ContactItemOrder.GIVEN_NAME_ASC -> {
            contacts.sortedBy {
                it.givenName.ifBlank { it.displayName }
            }
        }
        ContactItemOrder.PRIMARY_SORT_KEY -> {
            contacts.sortedBy {
                it.primarySortKey.ifBlank { it.displayName }
            }
        }
    }

    private fun applyGroupBy(
        groupBy: ContactsDataSourceQuery.ContactGroupBy,
        contacts: List<Contact>
    ): List<ContactsDataSourceElement> =
        contacts.groupBy {
            it.contactGroup(groupBy)
        }.toSortedMap(collator)
            .map { ContactGroup(it.key, it.value.toContactItems()) }

    private fun orderContactElements(
        orderBy: ContactsDataSourceQuery.ContactOrderBy,
        contactElements: List<Contact>
    ): List<Contact> = sortContacts(
        orderBy.order,
        contactElements
    )

    private class MutableContactsPagingSource(val data: List<ContactsDataSourceElement>) :
        MutableIviPagingSource<ContactsDataSourceElement>() {
        override val loadSizeLimit = DATA_SOURCE_MAX_PAGE_SIZE

        override suspend fun loadWithLoadSizeLimited(
            loadParams: IviPagingSource.LoadParams
        ): IviPagingSource.LoadResult<ContactsDataSourceElement> {
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
        ): IviPagingSource.LoadResult.Page<ContactsDataSourceElement> {
            return IviPagingSource.LoadResult.Page(
                dataIndex = dataIndex,
                data = data.subList(dataIndex, dataIndex + pageSize),
                itemsBefore = dataIndex.takeIf { placeholdersEnabled },
                itemsAfter = (data.size - dataIndex - pageSize).takeIf { placeholdersEnabled }
            )
        }
    }

    private companion object {
        // For optimal usage of data source, the maximum page should fit 500kb, including contacts
        // avatars. Setting page size to 50 ensures that page size remains under the optimal
        // recommended size.
        private const val DATA_SOURCE_MAX_PAGE_SIZE: Int = 50
    }
}
