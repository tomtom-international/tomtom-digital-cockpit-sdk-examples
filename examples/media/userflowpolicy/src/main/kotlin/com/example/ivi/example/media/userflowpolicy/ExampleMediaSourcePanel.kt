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
import com.tomtom.ivi.appsuite.media.api.common.core.IviMediaItem
import com.tomtom.ivi.appsuite.media.api.common.core.RootSourceClient
import com.tomtom.ivi.appsuite.media.api.common.core.SourceClient
import com.tomtom.ivi.appsuite.media.api.common.core.actions.standard.PlayMediaIdFromSourceAction
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaFrontendContext
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaTaskPanel
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.PolicyProvider
import com.tomtom.ivi.appsuite.media.api.service.core.MediaServiceApi
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel

/**
 * This panel enables browsing through the content of the example media source.
 *
 * Panels in TomTom Digital Cockpit are composed of three classes:
 * - a [TaskPanel] to define the logic, which is this class.
 * - a [FrontendViewModel] that transforms the panels' data into information ready to use in a view,
 *   [ExampleMediaSourceViewModel] in this example.
 * - an [IviFragment], defining the Android fragment and creating the instance of the view model,
 *   [ExampleMediaSourceFragment] in this example.
 */
internal class ExampleMediaSourcePanel(mediaContext: MediaFrontendContext) :
    MediaTaskPanel(mediaContext, ExampleMediaSourceId, null) {

    /**
     * The [RootSourceClient] is used to retrieve the available [categories] and browse the
     * [contents] of the example media source. Other [SourceClient]s are available.
     */
    private val sourceClient = RootSourceClient(
        mediaContext.frontendContext.applicationContext,
        ExampleMediaSourceId,
    )

    /**
     * The [mediaService] lets the user play a song.
     *
     * This uses the media service connection that the stock media UI has already initialized.
     */
    private val mediaService: MediaServiceApi = mediaContext.mediaService

    /** Allow usage of other policies in the view model. */
    val policyProvider: PolicyProvider =
        mediaFrontendContext.mediaConfiguration.getPolicyProvider(ExampleMediaSourceId)

    val isLoading: LiveData<Boolean> = sourceClient.isLoading

    /** In the case of the example media source, the categories are home and liked. */
    val categories: LiveData<List<IviMediaItem>> = sourceClient.categories
    val selectedCategory: LiveData<IviMediaItem?> = sourceClient.mediaItem
    val contents: LiveData<List<IviMediaItem>> = sourceClient.contents

    override fun createInitialFragmentInitializer() =
        IviFragment.Initializer(ExampleMediaSourceFragment(), this)

    override fun onAddedToFrontend() {
        super.onAddedToFrontend()
        sourceClient.connect()
    }

    override fun onRemovedFromFrontend() {
        sourceClient.disconnect()
        super.onRemovedFromFrontend()
    }

    fun selectCategory(category: IviMediaItem) = sourceClient.browseTo(category)

    fun playMediaId(id: String): Unit =
        mediaService.launchActionAsync(PlayMediaIdFromSourceAction(ExampleMediaSourceId, id))
}
