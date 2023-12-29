/*
 * Copyright © 2022 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.splashscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tomtom.ivi.platform.framework.api.product.defaultactivity.DefaultActivity
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.SplashScreenProvider

class SplashScreenActivity : DefaultActivity() {

    override fun getSplashScreenProvider(): SplashScreenProvider {
        return object : SplashScreenProvider {
            override fun createSplashScreenView(splashScreenContainer: ViewGroup): View {
                return LayoutInflater
                    .from(splashScreenContainer.context)
                    .inflate(
                        R.layout.custom_splash_screen,
                        splashScreenContainer,
                        false,
                    )
            }
        }
    }
}
