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

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.tomtom.ivi.appsuite.media.api.common.core.IviMediaItem
import com.tomtom.ivi.appsuite.media.api.common.frontend.ClickAction
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaEntryGroupItem
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaGroupItem
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel
import com.tomtom.tools.android.api.livedata.ImmutableLiveData
import com.tomtom.tools.android.api.uicontrols.recyclerview.group.ListGroupItem

/**
 * The view model transforms the panel's data into information ready to use in a view.
 *
 * Each category in [categories] can be selected to be the [selectedCategory], after which its
 * contents will appear in [contents]. There is also a "back" button, which triggers the [dismiss]
 * function to dismiss the panel.
 */
internal class ExampleMediaSourceViewModel(panel: ExampleMediaSourcePanel)
    : FrontendViewModel<ExampleMediaSourcePanel>(panel) {

    val isLoading: LiveData<Boolean> = panel.isLoading

    val categories: LiveData<List<MediaGroupItem>> = panel.categories.map { list ->
        list.map { category ->
            category.asMediaEntryGroupItem { panel.selectCategory(category)}
        }
    }

    val selectedCategory: LiveData<IviMediaItem?> = panel.selectedCategory

    val contents: LiveData<List<MediaGroupItem>> = panel.contents.map { list ->
        list.map { item -> item.asMediaEntryGroupItem { panel.playMediaId(item.id) } }
    }

    fun dismiss(): Unit = panel.dismiss()

    private fun IviMediaItem.asMediaEntryGroupItem(onClick: ClickAction) = MediaEntryGroupItem(
        item = panel.policyProvider.itemMappingPolicy(this),
        itemState = ImmutableLiveData(MediaEntryGroupItem.ItemState.IDLE),
        type = ListGroupItem.ItemType.LIST_ITEM,
        clickAction = onClick
    )
}
