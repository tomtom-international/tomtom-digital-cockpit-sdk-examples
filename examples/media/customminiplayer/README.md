# Module examples_media_customminiplayer

## Example custom media mini player

This is an example demonstrating how to control media playback with a customized UI visible.
The stock media player UI uses a main process panel and a task process panel to display what is 
currently being played. Every panel is able to create their own initial fragment. It's possible to 
adjust the look and behavior of a panel by customizing this fragment. A system UI host can create 
custom fragment rules that replace the stock fragment of a panel with a custom fragment.

You can find more details on the different panels used in the
[System UI overview](https://developer.tomtom.com/tomtom-digital-cockpit/designers/system-ui/overview).

This directory contains an example system UI that customizes the fragment of
`MediaMainProcessPanelBase` and `MediaTaskProcessPanelBase`. It's possible to see this customization
in action by following these steps:
1. Clone this repository.
2. Open this in Android Studio.
3. Run the `examples_media_customminiplayer` configuration.
4. Open the media frontend.
5. Select and play any media from any media source.
6. Close the media frontend.

The custom media mini player is now shown and its layout differs from the stock one.

The example demonstrates concepts explained in the following guides:
[Replacing a panels' fragment with a custom fragment](https://developer.tomtom.com/tomtom-digital-cockpit/tutorials/core/customization/custom-fragments)

To be able to try the customizations in this example, other than installing it on a device, it is
also necessary to compile and install the example module `examples_media_source`, as that contains
the media source customized by the current example. Trying another example or the template app will
clarify the differences even more.

## System UI integration note

This example only demonstrates how to make a new media playback UI; it does not provide an example
on how to place this UI in a different part of the screen, or on another display.

To accomplish those kinds of modifications, the
[System UI introduction](https://developer.tomtom.com/tomtom-digital-cockpit/developers/development/system-ui)
contains more information about customizing the UI and create new panel types to integrate in it.

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.
