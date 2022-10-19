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
import com.tomtom.ivi.platform.framework.api.common.uid.Uid
import java.time.Instant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
@SerialName("UserProfile")
private class AccountSurrogate(
    val accountUid: String,
    val username: String,
    val loggedIn: Boolean?,
    val lastLogIn: String?
)

internal object AccountSerializer : KSerializer<Account> {
    override val descriptor: SerialDescriptor = AccountSurrogate.serializer().descriptor

    override fun deserialize(decoder: Decoder): Account =
        decoder
            .decodeSerializableValue(AccountSurrogate.serializer())
            .let { surrogate ->
                Account(
                    accountUid = Uid.fromString(surrogate.accountUid),
                    username = surrogate.username,
                    loggedIn = surrogate.loggedIn == true,
                    lastLogIn = surrogate.lastLogIn?.let { Instant.parse(it) }
                )
            }

    override fun serialize(encoder: Encoder, value: Account) =
        AccountSurrogate(
            accountUid = value.accountUid.toString(),
            username = value.username,
            loggedIn = value.loggedIn,
            lastLogIn = value.lastLogIn?.toString()
        ).let { surrogate ->
            encoder.encodeSerializableValue(AccountSurrogate.serializer(), surrogate)
        }
}
