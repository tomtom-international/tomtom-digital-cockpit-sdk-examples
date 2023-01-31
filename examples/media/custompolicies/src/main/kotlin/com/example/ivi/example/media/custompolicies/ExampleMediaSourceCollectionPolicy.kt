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
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.CollectionPolicy
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.DefaultCollectionPolicy

/**
 * This policy ensures that, when playing content from the example media source, collections are
 * correctly recognized as such. This ensures that when selecting a collection, the correct UI
 * layout will be used.
 *
 * Possible examples of a collection:
 * - a playlist.
 * - an album.
 * - an artist page (contains songs, albums, etc).
 * - a podcast page with a list of episodes.
 * - an audiobook (contains a list of chapters).
 *
 * The [DefaultCollectionPolicy] determines a collection based on collection media item with
 * [IviMediaItem.isPlayable] and [IviMediaItem.isBrowsable] properties. Here we change this default
 * behavior for this example source: a collection is determined if the media item id contains the
 * "playlist" string.
 */
internal class ExampleMediaSourceCollectionPolicy : CollectionPolicy {
    override fun isCollection(mediaItem: IviMediaItem): Boolean =
        mediaItem.id.contains("playlist", ignoreCase = true)
}
