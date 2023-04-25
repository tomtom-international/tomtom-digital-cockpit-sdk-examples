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

package com.example.ivi.example.media.customminiplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaFrontendContext
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.asMediaControlContext
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaMainProcessPanelBase
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaTaskProcessPanelBase
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.SourceAttributionFormat
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaButtonsConfiguration
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaButtonsViewModel
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaPlaybackViewModel
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.asMediaPlaybackParameters
import com.tomtom.tools.android.api.resourceresolution.string.DurationStringResolver
import kotlinx.coroutines.CoroutineScope

/**
 * This common ViewModel replaces the stock ViewModel of the [MediaMainProcessPanelBase] and
 * [MediaTaskProcessPanelBase].
 */
internal class CustomMediaCommonProcessViewModel(
    private val mediaFrontendContext: MediaFrontendContext,
    viewModelScope: CoroutineScope
) {

    /**
     * [MediaPlaybackViewModel] provides all information about playing media in a directly usable
     * format.
     * All information is updated from what is provided by the current media source at the moment.
     */
    private val playbackViewModel = MediaPlaybackViewModel(
        mediaFrontendContext.mediaConfiguration,
        mediaFrontendContext.mediaService.asMediaPlaybackParameters(
            mediaFrontendContext.mediaConfiguration
        ),
        viewModelScope,
        SourceAttributionFormat(preferSimplified = true)
    )

    val primaryText = playbackViewModel.primaryText
    val secondaryText = playbackViewModel.secondaryText
    val elapsedTime: LiveData<DurationStringResolver?> =
        playbackViewModel.elapsedTime.map { elapsedTime ->
            elapsedTime?.let { DurationStringResolver(it) }
        }
    val isBuffering = playbackViewModel.isBuffering
    val art = playbackViewModel.art
    val progressData = playbackViewModel.progress

    private val activePolicyProvider =
        mediaFrontendContext.mediaService.activeSource.map { source ->
            mediaFrontendContext.mediaConfiguration
                .getPolicyProvider(source?.id).mediaControlPolicy
        }

    /**
     * [MediaButtonsViewModel] provides the buttons for playback controls.
     * Buttons change depending on what actions are provided by the current media source at the
     * moment.
     */
    private val buttonsViewModel = MediaButtonsViewModel(
        mediaFrontendContext.mediaService.asMediaControlContext(viewModelScope),
        activePolicyProvider.map {
            MediaButtonsConfiguration(
                it.replacedStandardControls,
                it.customControls,
                it.mediaControlsDisplayLimit.primaryControlsSmallLimit,
                it.mediaControlsDisplayLimit.secondaryControlsSmallLimit
            )
        }
    )

    val primaryButtons = buttonsViewModel.primaryButtons
    val secondaryButtons = buttonsViewModel.secondaryButtons

    /**
     * This open the expanded process panel via both [MediaMainProcessPanelBase] and
     * [MediaTaskProcessPanelBase].
     */
    fun openExpandedProcessPanel() =
        mediaFrontendContext.mediaFrontendNavigation.openExpandedNowPlayingPanel()
}
