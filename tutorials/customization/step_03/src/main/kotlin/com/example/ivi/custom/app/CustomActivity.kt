/*
 * Copyright © 2020 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

// Step 1: Rename the package.
package com.example.ivi.custom.app

// Step 2: Add dependencies for custom splash screen.
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ivi.custom.app.systemuihost.CustomSystemUiHost
import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.framework.api.common.iviinstance.IviInstanceId
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SplashScreenProvider


// Default imports
import com.tomtom.ivi.platform.framework.api.product.defaultactivity.DefaultActivity
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SystemUiHost

class CustomActivity : DefaultActivity() {

    // Step 2: Override the splash screen provider.
    override fun getSplashScreenProvider(): SplashScreenProvider {
        return object : SplashScreenProvider {
            override fun createSplashScreenView(splashScreenContainer: ViewGroup): View {
                return LayoutInflater
                    .from(splashScreenContainer.context)
                    .inflate(
                        // Step 2: Inflate the custom splash screen.
                        // Find this file in `res/layout/custom_splash_screen.xml`.
                        R.layout.custom_splash_screen,
                        splashScreenContainer,
                        false
                    )
            }
        }
    }

    @OptIn(IviExperimental::class)
    override fun createSystemUiHost(iviInstanceId: IviInstanceId): SystemUiHost =
        CustomSystemUiHost(getDefaultSystemUiHostContext(iviInstanceId))
}
