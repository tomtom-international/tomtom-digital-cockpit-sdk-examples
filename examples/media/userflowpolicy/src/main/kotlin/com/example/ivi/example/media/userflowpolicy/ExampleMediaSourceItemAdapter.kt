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

package com.example.ivi.example.media.userflowpolicy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tomtom.tools.android.api.uicontrols.textview.TtTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

/**
 * The simplest adapter for [RecyclerView] to display [MediaItemViewModel]. It only supports the title
 * and click listener.
 */
internal class ExampleMediaSourceItemAdapter :
    ListAdapter<MediaItemViewModel, ExampleMediaSourceItemAdapter.MediaViewHolder>(differConfig) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.examples_media_userflowpolicy_listitem, parent, false)

        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val displayMediaItem = getItem(position)
        holder.itemView.setOnClickListener { displayMediaItem.clickAction(displayMediaItem) }
        holder.title.text = displayMediaItem.item.title
    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TtTextView = itemView.findViewById(R.id.textView)
    }

    private companion object {
        private val diffUtilCallback = object : DiffUtil.ItemCallback<MediaItemViewModel>() {
            override fun areItemsTheSame(
                oldItem: MediaItemViewModel,
                newItem: MediaItemViewModel
            ): Boolean
                = oldItem == newItem || oldItem.item.id == newItem.item.id

            override fun areContentsTheSame(
                oldItem: MediaItemViewModel,
                newItem: MediaItemViewModel
            ): Boolean
                = oldItem.item.title == newItem.item.title
        }

        private val differConfig =
            AsyncDifferConfig.Builder(diffUtilCallback)
                .setBackgroundThreadExecutor(Dispatchers.Default.asExecutor())
                .build()
    }
}
