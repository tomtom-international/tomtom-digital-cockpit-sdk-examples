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
import androidx.lifecycle.switchMap
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaFrontendContext
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.asMediaControlContext
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.standard.StandardMediaControls
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.standard.StandardMediaControls.REPEAT
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.standard.StandardMediaControls.SEEK_BACKWARD
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.standard.StandardMediaControls.SEEK_FORWARD
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.standard.StandardMediaControls.SHUFFLE
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.standard.StandardMediaControls.SKIP_BACKWARD
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.standard.StandardMediaControls.SKIP_FORWARD
import com.tomtom.ivi.appsuite.media.api.common.frontend.controls.standard.StandardMediaControls.TOGGLE_PLAY
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaMainProcessPanelBase
import com.tomtom.ivi.appsuite.media.api.common.frontend.panels.MediaTaskProcessPanelBase
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.SourceAttributionFormat
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaButtonsConfiguration
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaButtonsViewModel
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.MediaPlaybackViewModel
import com.tomtom.ivi.appsuite.media.api.common.frontend.viewmodel.asMediaPlaybackParameters
import com.tomtom.tools.android.api.livedata.combine
import com.tomtom.tools.android.api.resourceresolution.string.DurationStringResolver
import com.tomtom.tools.android.api.uicontrols.button.TtButtonViewModel
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
                .getPolicyProvider(source?.id)
        }

    private val activeMediaControlPolicy = activePolicyProvider.map {
        it.mediaControlPolicy
    }

    /**
     * The media button configuration depends on the current [activeMediaControlPolicy].
     */
    private val mediaButtonsConfiguration: LiveData<MediaButtonsConfiguration> =
        activeMediaControlPolicy.map {
            MediaButtonsConfiguration(
                it.replacedStandardControls,
                it.customControls,
                it.mediaControlsDisplayLimit.primaryControlsSmallLimit,
                it.mediaControlsDisplayLimit.secondaryControlsSmallLimit
            )
        }

    private val mediaControlContext =
        mediaFrontendContext.mediaService.asMediaControlContext(viewModelScope)

    /**
     * The [StandardMediaControls] contained in [mediaButtonsConfiguration].
     *
     * [mediaButtonsConfiguration] provides the buttons for playback controls.
     * Buttons change depending on what actions are provided by the current media source at the
     * moment.
     *
     * __Note__: Here we are not reusing the [MediaButtonsViewModel.primaryButtons] and
     * [MediaButtonsViewModel.secondaryButtons], because the split of primary and secondary buttons
     * does not match our custom layout.
     */
    val standardControls: LiveData<List<TtButtonViewModel>> =
        mediaButtonsConfiguration.switchMap { buttonConfiguration ->
            listOf(
                buttonConfiguration.getReplacedMediaControlFactoryFor(SHUFFLE),
                buttonConfiguration.getReplacedMediaControlFactoryFor(SEEK_BACKWARD),
                buttonConfiguration.getReplacedMediaControlFactoryFor(SKIP_BACKWARD),
                buttonConfiguration.getReplacedMediaControlFactoryFor(TOGGLE_PLAY),
                buttonConfiguration.getReplacedMediaControlFactoryFor(SKIP_FORWARD),
                buttonConfiguration.getReplacedMediaControlFactoryFor(SEEK_FORWARD),
                buttonConfiguration.getReplacedMediaControlFactoryFor(REPEAT)
            ).map { mediaControlFactory ->
                mediaControlFactory.createControlFor(mediaControlContext)
            }.map { mediaControl ->
                mediaControl.asIconTtButtonViewModel()
            } // Combine the list of LiveData<TtButtonViewModel> to a list of TtButtonViewModel
                .combine { buttons ->
                    buttons
                }
        }

    /**
     * The custom controls contained in [mediaButtonsConfiguration].
     *
     * [mediaButtonsConfiguration] provides the buttons for playback controls.
     * Buttons change depending on what actions are provided by the current media source at the
     * moment.
     *
     * __Note__: Here we are not reusing the [MediaButtonsViewModel.primaryButtons] and
     * [MediaButtonsViewModel.secondaryButtons], because the split of primary and secondary buttons
     * does not match our custom layout.
     */
    val customControls: LiveData<List<TtButtonViewModel>> = mediaButtonsConfiguration.switchMap {
        it.customControls.map { mediaControlFactory ->
            mediaControlFactory.createControlFor(mediaControlContext)
        }.map { mediaControl ->
            mediaControl.asIconTtButtonViewModel()
        } // Combine the list of LiveData<TtButtonViewModel> to a list of TtButtonViewModel
            .combine { it }
    }

    /**
     * This opens the expanded process panel via both [MediaMainProcessPanelBase] and
     * [MediaTaskProcessPanelBase].
     */
    fun openExpandedProcessPanel() =
        mediaFrontendContext.mediaFrontendNavigation.openExpandedNowPlayingPanel()
}
