# Module examples_media_miniplayer

## Example custom media mini player

This is an example demonstrating how to control media playback with a customized UI visible when
not browsing for media in the stock media player UI.
The stock media player UI uses a main process panel to display what is currently being played, but 
it can be disabled when necessary: for example when media controls are always present in the UI.

The example demonstrates concepts explained in the following guides on the
[TomTom Digital Cockpit developer portal](https://developer.tomtom.com/tomtom-digital-cockpit/developers/introduction):

- [Configure the Media Plugins](https://developer.tomtom.com/tomtom-digital-cockpit/developers/tutorials-and-examples/media/configure-the-media-plugins)
- [Customize a media source](https://developer.tomtom.com/tomtom-digital-cockpit/developers/tutorials-and-examples/media/customize-a-media-source)

To be able to try the customizations in this example, other than installing it on a device, it is
also necessary to compile and install the example module `examples_media_source`, as that contains
the media source customized by the current example. Trying another example or the template app will
clarify the differences even more.

## System UI integration note

This example only demonstrates how to make a new media playback UI; it does not provide an example
on how to place this UI in a different part of the screen, or on another display.

To accomplish those kinds of modifications, the
[System UI guide](https://developer.tomtom.com/tomtom-digital-cockpit/developers/development/system-ui)
contains more information about customizing the UI and create new panel types to integrate in it.

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.
