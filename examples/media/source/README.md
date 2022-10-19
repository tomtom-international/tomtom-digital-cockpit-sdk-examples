# Module examples_media_source

# Example media source

This is a simple example to demonstrate a basic implementation of a standard Android Automotive media
source. It allows browsing through fake media, and responds appropriately to user actions.

It can be used to try out media examples, components and customizations.

It is implemented using the AndroidX Media library
([androidx.media](https://developer.android.com/reference/kotlin/androidx/media/package-summary.html))
and the base AOSP
[android.media](https://developer.android.com/reference/kotlin/android/media/package-summary) API.
The AndroidX Media library is TomTom Digital Cockpit's supported and recommended media API. Use of
this library ensures full compatibility with TomTom Digital Cockpit and with the AOSP media
framework.

The implementation is built following the basic concepts of the official guide
[Android Media apps for cars](https://developer.android.com/training/cars/media#implement_browser).

This app does not provide activities, and is thus not visible in a normal phone's app launcher.
It must be installed alongside a TomTom Digital Cockpit-based app and will then be possible to
browse its contents via the Media app.
Its contents are also browsable via the standard Android Open Source Project car media browser app.

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.
