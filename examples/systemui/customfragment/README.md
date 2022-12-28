# Replacing a panels' fragment with a custom fragment

Every panel is able to create their own initial fragment. It's possible to adjust the look and
behavior of a panel by customizing this fragment. A system UI host can create custom fragment rules
that replace the normal fragment of a panel with a custom fragment.

This directory contains an example system UI that customizes the fragment of all stock notification
panels. It's possible to see this customization in action by following these steps:
1. Clone this repository.
2. Open this in Android Studio.
3. Run the `examples_systemui_customfragment` configuration.
4. Open the example frontend.
5. The example frontend contains a button to add a notification. Click it.

The primary button of the notification is now located to the right and the secondary button to the
left. In our default notification fragment offering, this is the other way around.

If you want to learn how this works, start at the `fragmentFactory` field of the
[`CustomSystemUiHost`](https://github.com/tomtom-international/tomtom-digital-cockpit-sdk-examples/blob/main/examples/systemui/customfragment/src/main/kotlin/com/example/ivi/example/systemui/customfragment/systemui/CustomSystemUiHost.kt).
The fragment factory has a list of custom fragment rules. The first rule in this list that accepts
to create the fragment for a given panel creates the fragment. If no rule accepts, the panel creates
the fragment.

The example frontend is only in this example to show that the fragment of stock notification panels
is customized. It doesn't contain any code related to fragment customization.

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.
