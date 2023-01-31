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

package com.example.ivi.example.media.miniplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.asMediaControlContext
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.SourceAttributionFormat
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaButtonsConfiguration
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaButtonsViewModel
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaPlaybackViewModel
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.asMediaPlaybackParameters
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel
import com.tomtom.tools.android.api.resourceresolution.string.DurationStringResolver

internal class ExampleMiniPlayerViewModel(panel: ExampleMiniPlayerPanel) :
    FrontendViewModel<ExampleMiniPlayerPanel>(panel) {

    /**
     * In this small example, only the service availability is used to hide the panel's contents.
     * In a product the whole panel could be hidden, for example by removing the
     * [ExampleMiniPlayerPanel] from the [ExampleMiniPlayerFrontend] when needed.
     */
    val isAvailable = panel.mediaService.serviceAvailable

    /**
     * [MediaPlaybackViewModel] provides all information about playing media in a directly usable
     * format.
     * All information is updated from what is provided by the current media source at the moment.
     */
    private val playbackViewModel = MediaPlaybackViewModel(
        panel.mediaConfiguration,
        panel.mediaService.asMediaPlaybackParameters(panel.mediaConfiguration),
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

    private val activePolicyProvider = panel.mediaService.activeSource.map { source ->
        panel.mediaConfiguration.getPolicyProvider(source?.id).mediaControlPolicy
    }

    /**
     * [MediaButtonsViewModel] provides the buttons for playback controls.
     * Buttons change depending on what actions are provided by the current media source at the
     * moment.
     */
    private val buttonsViewModel = MediaButtonsViewModel(
        panel.mediaService.asMediaControlContext(viewModelScope),
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
}
