/*
 * Copyright © 2023 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.media.customnowplaying

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.tomtom.ivi.appsuite.media.api.common.core.IviMediaItem
import com.tomtom.ivi.appsuite.media.api.common.core.IviPlaybackState
import com.tomtom.ivi.appsuite.media.api.common.core.actions.standard.PauseAction
import com.tomtom.ivi.appsuite.media.api.common.core.actions.standard.PlayAction
import com.tomtom.ivi.appsuite.media.api.common.core.actions.standard.SkipToQueueItemAction
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.asMediaControlContext
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaExpandedProcessPanelBase
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.PolicyProvider
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.SourceAttributionFormat
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaButtonsConfiguration
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaButtonsViewModel
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaPlaybackViewModel
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.asMediaPlaybackParameters
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel
import com.tomtom.tools.android.api.graphics.drawable.RemoteDrawableResolver
import com.tomtom.tools.android.api.livedata.combine
import com.tomtom.tools.android.api.livedata.valueUpToDate
import com.tomtom.tools.android.api.resourceresolution.string.DurationStringResolver
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver
import com.tomtom.tools.android.api.uicontrols.imageview.ImageDescriptor
import com.tomtom.tools.android.api.uicontrols.imageview.ImageType
import com.tomtom.tools.android.api.uicontrols.recyclerview.ListItemContentViewModel

/**
 * This ViewModel replaces the stock ViewModel of the [MediaExpandedProcessPanelBase].
 */
internal class CustomMediaExpandedProcessViewModel(panel: MediaExpandedProcessPanelBase) :
    FrontendViewModel<MediaExpandedProcessPanelBase>(panel) {

    private val mediaService = panel.mediaFrontendContext.mediaService

    private val mediaPlaybackParameters = mediaService
        .asMediaPlaybackParameters(panel.mediaFrontendContext.mediaConfiguration)

    /**
     * [MediaPlaybackViewModel] provides all information about playing media in a directly usable
     * format.
     * All information is updated from what is provided by the current media source at the moment.
     */
    private val playbackViewModel = MediaPlaybackViewModel(
        panel.mediaFrontendContext.mediaConfiguration,
        mediaPlaybackParameters,
        viewModelScope,
        SourceAttributionFormat(preferPresentation = true)
    )

    val sourceIcon = playbackViewModel.sourceIcon
    val primaryText = playbackViewModel.primaryText
    val artist = playbackViewModel.artist
    val album = playbackViewModel.album
    val elapsedTime: LiveData<DurationStringResolver?> =
        playbackViewModel.elapsedTime.map { elapsedTime ->
            elapsedTime?.let { DurationStringResolver(it) }
        }
    val isBuffering = playbackViewModel.isBuffering
    val art = playbackViewModel.art
    val progressData = playbackViewModel.progress

    private val activePolicyProvider =
        mediaService.activeSource.map { source ->
            panel.mediaFrontendContext.mediaConfiguration
                .getPolicyProvider(source?.id)
        }

    /**
     * [MediaButtonsViewModel] provides the buttons for playback controls.
     * Buttons change depending on what actions are provided by the current media source at the
     * moment.
     */
    private val buttonsViewModel = MediaButtonsViewModel(
        mediaService.asMediaControlContext(viewModelScope),
        activePolicyProvider.map {
            MediaButtonsConfiguration(
                it.mediaControlPolicy.replacedStandardControls,
                it.mediaControlPolicy.customControls,
                it.mediaControlPolicy.mediaControlsDisplayLimit.primaryControlsSmallLimit,
                it.mediaControlPolicy.mediaControlsDisplayLimit.secondaryControlsSmallLimit
            )
        }
    )

    val primaryButtons = buttonsViewModel.primaryButtons
    val secondaryButtons = buttonsViewModel.secondaryButtons

    /**
     * The now playing queue shown in the panel. This uses a combine to ensure that the list is
     * refreshed every time any of the combined components change. The click callback needs to use
     * up-to-date information on what is currently playing. Additionally, this can be easily
     * expanded to show a visual indicator for the current item or otherwise enrich the UI.
     */
    val activeQueue: LiveData<List<ListItemContentViewModel>> = combine(
        mediaService.activeQueue,
        mediaService.activeMediaItem,
        mediaService.playbackState,
        activePolicyProvider
    ) { activeQueue, _, _, policyProvider ->
        activeQueue.map { iviMediaItem ->
            iviMediaItem.toListItemContentViewModel(
                onClickItem = createOnQueueItemClickCallback(
                    policyProvider = policyProvider,
                )
            )
        }
    }

    fun dismissPanel() = panel.dismiss()

    /**
     * Converts an [IviMediaItem] to a [ListItemContentViewModel].
     */
    private fun IviMediaItem.toListItemContentViewModel(onClickItem: (IviMediaItem) -> Unit) =
        ListItemContentViewModel(
            itemId = id,
            headImage = ImageDescriptor(
                RemoteDrawableResolver(artUri ?: Uri.EMPTY),
                ImageType.ARTWORK
            ),
            primaryTextArea = ListItemContentViewModel.TextAreaViewModel(
                title?.let {
                    StaticStringResolver(
                        it
                    )
                }
            ),
            onClick = { onClickItem(this) }
        )

    private fun createOnQueueItemClickCallback(
        policyProvider: PolicyProvider
    ): (IviMediaItem) -> Unit {
        return { iviMediaItemClicked ->
            val currentPlaybackState = mediaService.playbackState.valueUpToDate
                ?: IviPlaybackState.IDLE

            val isActiveQueueItem =
                mediaService.activeMediaItem.valueUpToDate?.let { currentActiveItem ->
                    policyProvider.compareItemsPolicy(iviMediaItemClicked, currentActiveItem)
                } ?: false

            val actionToLaunch = if (isActiveQueueItem) {
                // Click on current active item.
                if (currentPlaybackState == IviPlaybackState.PLAYING) {
                    PauseAction()
                } else {
                    PlayAction()
                }
            } else {
                val queuePositionClicked =
                    mediaService.activeQueue.valueUpToDate?.let { queue ->
                        queue.indexOfFirst {
                            policyProvider.compareItemsPolicy(iviMediaItemClicked, it)
                        }
                    }
                // Click on a different item to play.
                SkipToQueueItemAction(queuePositionClicked ?: 0)
            }
            mediaService.launchActionAsync(actionToLaunch)
        }
    }
}
