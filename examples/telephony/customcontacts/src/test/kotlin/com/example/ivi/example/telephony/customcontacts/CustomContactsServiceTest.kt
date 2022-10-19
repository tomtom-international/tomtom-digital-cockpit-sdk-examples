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

package com.example.ivi.example.telephony.customcontacts

import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceElement
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactOrderBy.ContactItemOrder.PRIMARY_SORT_KEY
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactOrderBy.ContactItemOrderBy
import com.tomtom.ivi.platform.contacts.api.service.contacts.ContactsDataSourceQuery.ContactQueryFlags
import com.tomtom.ivi.platform.framework.api.testing.ipc.iviservice.datasource.assertIviDataSourceEquals
import com.tomtom.ivi.platform.telecom.api.common.model.PhoneBookSynchronizationStatus
import com.tomtom.ivi.platform.tools.api.testing.unit.IviTestCase
import com.tomtom.tools.android.testing.mock.niceMockk
import java.util.EnumSet
import kotlin.test.assertEquals
import org.junit.Before
import org.junit.Test

internal class CustomContactsServiceTest : IviTestCase() {

    private val sut = CustomContactsService(niceMockk())

    @Before
    fun createSut() {
        sut.onCreate()
    }

    @Test
    fun initialization() {
        // GIVEN-WHEN
        val contactsDataSource = mutableListOf(
            ContactsDataSourceElement.ContactItem(contact = sut.contactsSource[0]),
            ContactsDataSourceElement.ContactItem(contact = sut.contactsSource[1]),
        )
        val allContactsQuery = ContactsDataSourceQuery(
            selection = ContactsDataSourceQuery.ContactSelection.All,
            orderBy = ContactItemOrderBy(PRIMARY_SORT_KEY),
            map = null,
            flags = EnumSet.of(ContactQueryFlags.INCLUDE_IMAGE)
        )

        // THEN
        assertIviDataSourceEquals(contactsDataSource, sut.contacts, allContactsQuery)
        assertEquals(
            PhoneBookSynchronizationStatus.SYNCHRONIZATION_IN_PROGRESS,
            sut.phoneBookSynchronizationStatus
        )
    }
}
