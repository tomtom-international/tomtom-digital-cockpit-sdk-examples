# Using custom animations

The system UI of the TomTom Digital Cockpit is built with `PanelContainer`s, that control
how `Panel`s are shown on the screen. Each `PanelContainer` creates sub-containers to 
do this, and controls how they enter and exit the screen. You can provide custom
animations to a `PanelContainer` for these sub-container transitions.

This directory contains an example showing how to use custom animations for animating
sub-containers when they are entering and exiting the screen. It is possible to see this
customization in action by following these steps:

1. Clone this repository.
2. Open it in Android Studio.
3. Run the `examples_customization_customanimation` configuration.
4. Tap any main menu item.
5. See the content sliding in from the left side of the screen.
6. Tap another main menu item.
7. See old content sliding out to the left and new content slide in from the left.

## Copyright

Copyright © 2023 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be used for
internal evaluation purposes or commercial use strictly subject to separate license agreement
between you and TomTom NV. If you are the licensee, you are only permitted to use this software in
accordance with the terms of your license agreement. If you are not the licensee, you are not
authorized to use this software in any manner and should immediately return or destroy it.
