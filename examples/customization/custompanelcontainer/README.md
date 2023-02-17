# Using a custom panel container in system UI

System UI in the TomTom Digital Cockpit visualizes data represented by `Panel`s. How panels are
positioned relative to each other, how they are animated or if they can be dismissed - all this
coordination is defined by the `PanelContainer` which displays the concrete `Panel`. The closest
counterpart of a `PanelContainer` in Android OS is a
[ViewGroup](https://developer.android.com/reference/android/view/ViewGroup).
In fact, all `PanelContainer`s that come with TomTom Digital Cockpit are based on
[ViewGroup](https://developer.android.com/reference/android/view/ViewGroup) and implement the
`PanelContainer` interface. You can see all `PanelContainer`s used by stock system UI in the package
`com.tomtom.ivi.platform.systemui.api.common.systemuihost.panelcontainer`.

This example shows how to create a custom `PanelContainer` to display two `CustomPanel`s
side-by-side. It's possible to see this custom `DualPanelContainer` in action by following these
steps:

1. Clone this repository.
1. Open it in Android Studio.
1. Run the `examples_customization_custompanelcontainer` configuration.
1. See two panels on the screen.

## Copyright

Copyright © 2023 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be used for
internal evaluation purposes or commercial use strictly subject to separate license agreement
between you and TomTom NV. If you are the licensee, you are only permitted to use this software in
accordance with the terms of your license agreement. If you are not the licensee, you are not
authorized to use this software in any manner and should immediately return or destroy it.
