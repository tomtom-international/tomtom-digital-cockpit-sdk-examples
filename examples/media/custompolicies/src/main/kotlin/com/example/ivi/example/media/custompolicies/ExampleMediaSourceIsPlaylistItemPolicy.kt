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

package com.example.ivi.example.media.custompolicies

import com.tomtom.ivi.appsuite.media.api.common.core.IviMediaItem
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.IsPlaylistItemPolicy

/**
 * This policy ensures that, when playing content from the example media source, playlists are
 * correctly recognized as such. This ensures that when selecting a playlist, the correct UI layout
 * will be used.
 *
 * Without this policy, the default behavior is that when browsing to a new media item, if all
 * content is playable and none is browsable it is then treated as a playlist.
 */
internal class ExampleMediaSourceIsPlaylistItemPolicy : IsPlaylistItemPolicy {
    override fun invoke(mediaItem: IviMediaItem): Boolean =
        mediaItem.id.contains("playlist", ignoreCase = true)
}
