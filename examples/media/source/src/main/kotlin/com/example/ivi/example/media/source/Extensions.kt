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

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat

internal inline val MediaMetadataCompat.id: String?
    get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

internal inline val MediaBrowserCompat.MediaItem.id: String?
    get() = description.mediaId

internal inline val MediaMetadataCompat.durationMs: Long
    get() = getLong(MediaMetadataCompat.METADATA_KEY_DURATION)

internal fun MediaBrowserCompat.MediaItem.asMetadata(): MediaMetadataCompat =
    MediaMetadataCompat.Builder()
        .apply {
            description.mediaId
                ?.let { putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, it) }
            description.title
                ?.let { putText(MediaMetadataCompat.METADATA_KEY_TITLE, it) }
            description.subtitle
                ?.let { putText(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, it) }
            description.extras?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
                ?.let { putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it) }
        }
        .build()

internal fun makeItem(
    title: String,
    browsable: Boolean = false,
    playable: Boolean = false,
    subtitle: String? = null,
    duration: Long? = null
): MediaBrowserCompat.MediaItem {
    val extras = Bundle()
    duration?.let { extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, it) }

    val description = MediaDescriptionCompat.Builder()
    description.setMediaId(title)
    description.setTitle(title)
    description.setExtras(extras)
    subtitle?.let { description.setSubtitle(it) }

    val flags =
        (if (browsable) MediaBrowserCompat.MediaItem.FLAG_BROWSABLE else 0) or
            (if (playable) MediaBrowserCompat.MediaItem.FLAG_PLAYABLE else 0)
    return MediaBrowserCompat.MediaItem(description.build(), flags)
}
