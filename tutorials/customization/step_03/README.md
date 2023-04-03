# Step 3 - Custom System UI Host

In this step we will add the custom System UI host.

In this step we'll copy the layout of the stock system UI, so we can customize it later.
First we'll download a copy of the existing layout:
Access the `ttivi_systemui_medium.xml`:

- Open `build-logic/libraries.versions.toml`
- Copy the `iviPlatform` version
- Go to https://repo.tomtom.com/#browse/browse:ivi:com%2Ftomtom%2Fivi%2Fplatform%2Fplatform_systemui_api_stock_systemuihost
- Sign in with your Nexus repository credentials
- Open the folder of the correct TTDC version
- Download the `.sources.jar` file (`platform_systemui_api_stock_systemuihost-<TTDC version>-sources.jar`)
- Extract the archive.
- Copy `layout/ttivi_systemui_medium.xml` to the template app in `res/layout/custom_systemui.xml`.
Then we'll make our custom system UI use it:
- Add `tomtomToolsApiDatabinding` to `build-logic/libraries.versions.toml`
- Add `iviPlatformSystemuiApiCommonDebug` to `build-logic/libraries.versions.toml`
- Depend on both in `template/app/build.gradle.kts`
- Alter the `viewFactory` of `CustomSystemUiHost`

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.


