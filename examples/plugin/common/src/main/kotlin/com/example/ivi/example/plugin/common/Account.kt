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

package com.example.ivi.example.plugin.common

import android.os.Parcelable
import com.tomtom.ivi.platform.framework.api.common.uid.Uid
import java.time.Instant
import kotlinx.parcelize.Parcelize

/**
 * Contains all data of a user account.
 */
@Parcelize
data class Account(
    /**
     * Unique ID for the account.
     */
    val accountUid: Uid<Account> = Uid.new(),

    /**
     * A string representing the name of the account.
     */
    val username: String,

    /**
     * `true` if the user is logged in.
     */
    val loggedIn: Boolean = false,

    /**
     * Date time when this user logged in for the last time.
     */
    val lastLogIn: Instant? = null
) : Parcelable
