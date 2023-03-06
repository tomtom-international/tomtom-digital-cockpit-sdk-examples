# Adding a custom panel type to the system UI

The System UI in the TomTom Digital Cockpit visualizes data represented by `Panel`s. They are the main
building blocks of the system UI. Each `Frontend` that has a UI adds one or
more new `Panel`s to visualize itself. The
[System UI Overview](https://developer.tomtom.com/tomtom-digital-cockpit/designers/system-ui/overview)
page introduces the anatomy of the system UI and contains examples of built-in `Panel` types.
Their implementations can be found in the package
`com.tomtom.ivi.platform.frontend.api.common.frontend.panels`.

If none of the built-in `Panel` types are suitable to visualize your data, you can implement a
custom `Panel` types for your system UI. This directory contains an example that shown how to do
this. It's possible to see this customization in action by following these steps:

1. Clone this repository.
2. Open it in Android Studio.
3. Run the `examples_customization_custompaneltype` configuration.
4. See the custom panel on the left of the screen.

## Copyright

Copyright © 2023 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.
