# Module examples_media_fallbackpolicy

## Example media fallback policy

This is an example demonstrating how to customize the browsing experience for media sources for
which a specific customization is not provided by the TomTom Digital Cockpit stock media player UI.

Every media source installed in the system has its data and information provided to the stock
media player UI via a `PolicyProvider`. When a product hardware platform already has all necessary
media sources pre-installed, it is advantageous to customize them. But if the user is allowed to
install their own media sources via an app store, then it is hard to keep track of which media
sources are available in the store to customize them.

The stock media player UI contains a predefined fallback `PolicyProvider` which is used to display
generic contents for non-manually customized media sources. However, it can be desirable to replace
this with a different one.

The example demonstrates concepts also explained in the
[Customize a media source](https://developer.tomtom.com/tomtom-digital-cockpit/tutorials/domains/media/customize-a-media-source)
guide in the
[TomTom Digital Cockpit developer portal](https://developer.tomtom.com/tomtom-digital-cockpit/developers/introduction).

To be able to try the customizations in this example, other than installing it on a device, it is
also necessary to compile and install the example module `examples_media_source`, as that contains
the media source customized by the current example. Trying another example or the template app will
clarify the differences.

It can be helpful to also install another non customized Android Automotive media source: the
fallback policy's customizations will be then applied to both.

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.
