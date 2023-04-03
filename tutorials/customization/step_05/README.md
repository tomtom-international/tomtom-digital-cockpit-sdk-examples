# Step 5 - Create a ViewModel for the custom System UI Host

Implement a [CoreSystemUiViewModel](https://developer.tomtom.com/assets/downloads/tomtom-digital-cockpit/platform-api/1.0.5256/platform_systemui_api_common_systemuihost/com.tomtom.ivi.platform.systemui.api.common.systemuihost/-core-system-ui-view-model/index.html?query=class%20CoreSystemUiViewModel%20:%20LifecycleViewModel,%20IviInstanceIdProvider)

Create `CustomSystemUiViewModel` in `CustomSystemUiViewModel.kt`: 

```
/**
 * The view model of the [CustomSystemUiHost]. It's initialized in [CustomSystemUiHost.onCreate].
 *
 * Currently very small, because most of the logic is reused from the stock
 * [SystemUiHostExtension]s.
 */
@OptIn(IviExperimental::class)
internal class CustomSystemUiViewModel(coreViewModel: CoreSystemUiViewModel)
    : LifecycleViewModel() {
    init {
        // Hide the labels of the menu items.
        MenuService.createApi(this, coreViewModel.iviServiceProvider).apply {
            queueOrRunAction {
                setLabelVisibilityAsync(MenuService.MenuItemLabelVisibility.GONE)
            }
        }
    }
}
```

For now its responsibility is to hide the labels of the main menu.

Then initialize it inside `CustomSystemUiHost`'s `onCreate()` function:

```
ViewModelProvider(
    viewModelStoreOwner,
    FixedConstructorFactory(coreViewModel)
)[CustomSystemUiViewModel::class.java]
```

If, in the future, the `ViewModel` provides information required in the view, also bind it
in `bindSystemUiView`.

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.


