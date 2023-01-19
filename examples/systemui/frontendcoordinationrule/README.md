# Adding a custom frontend coordination rule to a frontend

The business logic that coordinates the [Frontend]s within a system UI can be customized by
custom [FrontendCoordinationRule]s, which are explained in this example.

This directory contains an example that adds a custom [FrontendCoordinationRule] that closes debug
panel when any main menu item is tapped. It's possible to see this customization in action by
following these steps:

1. Clone this repository.
2. Open this in Android Studio.
3. Run the `examples_systemui_frontendcoordinationrule` configuration.
4. Hold volume down hardware button.
5. See debug menu on the right of the screen.
6. Tap any main menu item.
7. See debug menu being closed.

If you want to learn how this works, start at the `frontendCoordinator` field of the
[CustomSystemUiViewModel](https://github.com/tomtom-international/tomtom-digital-cockpit-sdk-examples/blob/main/examples/systemui/frontendcoordinationrule/src/main/kotlin/com/example/ivi/example/systemui/frontendcoordinationrule/systemui/CustomSystemUiViewModel.kt)
. The frontend coordinator adds a custom `FrontendCoordinationRule` to the collection of default
rules.

## Copyright

Copyright © 2023 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be used for
internal evaluation purposes or commercial use strictly subject to separate license agreement
between you and TomTom NV. If you are the licensee, you are only permitted to use this software in
accordance with the terms of your license agreement. If you are not the licensee, you are not
authorized to use this software in any manner and should immediately return or destroy it.
