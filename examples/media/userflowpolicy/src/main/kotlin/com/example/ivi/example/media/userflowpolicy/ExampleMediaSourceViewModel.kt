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
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel

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

    val categories: LiveData<List<DisplayMediaItem>> = panel.categories.map { list ->
        list.map { category ->
            category.asDisplayMediaItem { panel.selectCategory(category)}
        }
    }

    val selectedCategory: LiveData<IviMediaItem?> = panel.selectedCategory

    val contents: LiveData<List<DisplayMediaItem>> = panel.contents.map { list ->
        list.map { item -> item.asDisplayMediaItem { panel.playMediaId(item.id) } }
    }

    fun dismiss(): Unit = panel.dismiss()

    private fun IviMediaItem.asDisplayMediaItem(onClick: ClickAction) = DisplayMediaItem(
        item = panel.policyProvider.itemMappingPolicy(this),
        clickAction = onClick
    )
}

internal typealias ClickAction = (DisplayMediaItem) -> Unit

internal class DisplayMediaItem(
    val item: IviMediaItem,
    val clickAction: ClickAction
)
