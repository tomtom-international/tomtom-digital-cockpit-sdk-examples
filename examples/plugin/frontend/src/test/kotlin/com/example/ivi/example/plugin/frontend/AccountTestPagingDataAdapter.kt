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

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ivi.example.plugin.common.Account
import com.tomtom.tools.android.testing.mock.niceMockk

internal class AccountTestPagingDataAdapter : PagingDataAdapter<Account, RecyclerView.ViewHolder>(
    DiffCallback()
) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Nothing here.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        niceMockk()
}

private class DiffCallback : DiffUtil.ItemCallback<Account>() {
    override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
        oldItem.accountUid == newItem.accountUid

    override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
        oldItem == newItem
}
