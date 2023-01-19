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

package com.example.ivi.example.plugin.settingsservice

import com.example.ivi.example.plugin.common.Account
import java.time.Instant
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json
import org.junit.Test

internal class AccountSerializerTest {
    @Test
    fun `serialize and deserialize an account with login`() {
        val account = Account(username = "username", loggedIn = true, lastLogIn = Instant.now())

        val serialized = Json.encodeToString(AccountSerializer, account)
        val deserialized = Json.decodeFromString(AccountSerializer, serialized)

        assertEquals(account, deserialized)
    }

    @Test
    fun `serialize and deserialize an account without login`() {
        val account = Account(username = "username", loggedIn = false, lastLogIn = null)

        val serialized = Json.encodeToString(AccountSerializer, account)
        val deserialized = Json.decodeFromString(AccountSerializer, serialized)

        assertEquals(account, deserialized)
    }
}
