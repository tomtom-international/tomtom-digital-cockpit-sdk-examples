# Using a custom sub-container layout

The system UI of the TomTom Digital Cockpit is built with `PanelContainer`s, that control
how `Panel`s are shown on the screen. Each `PanelContainer` creates zero or more sub-containers to
do this, concrete logic depends on the type of the `PanelContainer`. TomTom Digital Cockpit comes
with a set of `PanelContainer`s, sub-containers and their layout files to cover the needs of the
stock system UI. It also allows to provide custom layouts for sub-containers.

This directory contains an example that uses a custom sub-container layout with a default
`PanelContainer`. It's possible to see this customization in action by following these steps:

1. Clone this repository.
2. Open it in Android Studio.
3. Run the `examples_customization_customsubcontainerlayout` configuration.
4. Tap any main menu item.
5. See the border around the main content of the screen. This border comes from the
custom layout file of the sub-container of the task panel container.

## Copyright

Copyright © 2023 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be used for
internal evaluation purposes or commercial use strictly subject to separate license agreement
between you and TomTom NV. If you are the licensee, you are only permitted to use this software in
accordance with the terms of your license agreement. If you are not the licensee, you are not
authorized to use this software in any manner and should immediately return or destroy it.
