# Step 4 - Move the Control Center


Time to make a real change: move the control center

The control center is currently at the bottom, lets move it to the top!

Edit the layout to make the change.

This means changing a lot of:
```
auto:layout_constraintBottom_toTopOf="@id/ttivi_control_center_container"
```
To
```
auto:layout_constraintBottom_toBottomOf="parent"
```

And changing
```
auto:layout_constraintTop_toTopOf="parent"
```
To
```
auto:layout_constraintTop_toBottomOf="@id/ttivi_control_center_container"
```

Adjust the `ttivi_homepanel_safearea_barrier_bottom` to
```
             <androidx.constraintlayout.widget.Barrier
                 android:id="@+id/ttivi_homepanel_safearea_barrier_bottom"
                 android:layout_width="wrap_content"
                 android:layout_height="wrap_content"
                 auto:barrierAllowsGoneWidgets="false"
                 auto:barrierDirection="top"
                 auto:constraint_referenced_ids="ttivi_control_center_container,ttivi_processpanel_placeholder" />
                 auto:constraint_referenced_ids="ttivi_processpanel_placeholder" />
```
The safe area is the area of a panel that is not covered by other panels.

Round the corners of the control center (inside of `custom_systemui.xml`).

```
            <!-- Step 4: add extra rounded corners: top left. -->
            <FrameLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                auto:elevationWithoutShadow="@{@dimen/ttivi_systemui_corner_elevation}"
                auto:layout_constraintLeft_toRightOf="@id/ttivi_main_menu_container"
                auto:layout_constraintTop_toBottomOf="@id/ttivi_control_center_container">
                <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:src="@drawable/ttivi_systemui_corner_topleft" />
            </FrameLayout>

            <!-- Step 4: add extra rounded corners: top right. -->
            <FrameLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                auto:elevationWithoutShadow="@{@dimen/ttivi_systemui_corner_elevation}"
                auto:layout_constraintRight_toRightOf="parent"
                auto:layout_constraintTop_toBottomOf="@id/ttivi_control_center_container">
                <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:src="@drawable/ttivi_systemui_corner_topright" />
            </FrameLayout>
            
            <!-- Step 4: add extra rounded corners: bottom left. -->
            <FrameLayout
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                auto:elevationWithoutShadow="@{@dimen/ttivi_systemui_corner_elevation}"
                auto:layout_constraintBottom_toBottomOf="parent"
                auto:layout_constraintLeft_toRightOf="@id/ttivi_main_menu_container">
                <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:src="@drawable/ttivi_systemui_corner_bottomleft" />
            </FrameLayout>
```

## Copyright

Copyright © 2022 TomTom NV. All rights reserved.

This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
used for internal evaluation purposes or commercial use strictly subject to separate
license agreement between you and TomTom NV. If you are the licensee, you are only permitted
to use this software in accordance with the terms of your license agreement. If you are
not the licensee, you are not authorized to use this software in any manner and should
immediately return or destroy it.


