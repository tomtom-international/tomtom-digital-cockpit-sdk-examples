# Replacing the media dashboard panel's fragment with a custom fragment

This is an example demonstrating how to customize the media dashboard experience in 
the TomTom Digital Cockpit stock media player UI.

Every panel is able to create their own initial fragment. It's possible to adjust the look and
behavior of a panel by customizing this fragment. A system UI host can create custom fragment rules
that replace the normal fragment of a panel with a custom fragment.

This directory contains an example system UI that customizes the fragment of 
`MediaDashboardPanelBase`. It's possible to see this customization in action by following these 
steps:
1. Clone this repository.
2. Open this in Android Studio.
3. Run the `examples_media_customdashboard` configuration.
4. Open the media frontend.

The media dashboard is now opened. It differs from the stock one: it contains custom header and 
footer, and also a clickable button which increments text label on the right. The media sources are
still shown in the dashboard. The `SourcePickerView` provides the implementation of listing and 
browsing sources.

## Copyright

Copyright © 2023 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.
