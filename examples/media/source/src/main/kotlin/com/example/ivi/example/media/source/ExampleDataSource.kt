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

package com.example.ivi.example.media.source

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat

/**
 * Fake media source data provider.
 *
 * Not meant for production usage.
 */
internal class ExampleDataSource {

    fun getRoot(): String = NAME_ROOT

    fun getChildren(parentId: String): List<MediaBrowserCompat.MediaItem>? =
        catalog[parentId]

    fun getMetadata(mediaId: String): MediaMetadataCompat? {
        catalog[mediaId]?.let { return it.first().asMetadata() }
        return catalog.values.flatten()
            .firstOrNull { it.mediaId == mediaId }
            ?.asMetadata()
    }

    fun getPreviousMetadata(mediaId: String): MediaBrowserCompat.MediaItem? =
        getItem(mediaId, -1)

    fun getNextMetadata(mediaId: String): MediaBrowserCompat.MediaItem? =
        getItem(mediaId, 1)

    private fun getItem(mediaId: String, indexShift: Int): MediaBrowserCompat.MediaItem? =
        catalog.values.firstNotNullOfOrNull { items ->
            val index = items.indexOfFirst { it.mediaId == mediaId }
            if (index != -1) {
                items.elementAtOrNull(index + indexShift)
                    ?.takeIf { !it.isBrowsable }
            } else {
                null
            }
        }

    private companion object {
        private const val NAME_ROOT = "i am root"
        private const val NAME_CATEGORY_HOME = "Home"
        private const val NAME_CATEGORY_LIKED = "Liked"
        private const val NAME_PLAYLIST = "Playlist"
    }

    private val catalog: Map<String, List<MediaBrowserCompat.MediaItem>> = mapOf(
        NAME_ROOT to listOf(
            makeItem(NAME_CATEGORY_HOME, browsable = true),
            makeItem(NAME_CATEGORY_LIKED, browsable = true),
        ),
        NAME_CATEGORY_HOME to listOf(
            makeItem("Song 1", playable = true, duration = 60_000L),
            makeItem("Song 2", playable = true, duration = 120_000L),
            makeItem(NAME_PLAYLIST, browsable = true, playable = true),
        ),
        NAME_CATEGORY_LIKED to listOf(),
        NAME_PLAYLIST to listOf(
            makeItem("Playlist song 1", playable = true, duration = 30_000L),
            makeItem("Playlist song 2", playable = true, duration = 45_000L),
            makeItem("Playlist song 3", playable = true, duration = 90_000L),
        )
    )
}
