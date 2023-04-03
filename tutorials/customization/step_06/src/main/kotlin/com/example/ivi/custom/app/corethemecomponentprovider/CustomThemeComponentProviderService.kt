/*
 * Copyright © 2023 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.custom.app.corethemecomponentprovider

import com.example.ivi.custom.app.R
import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.ivi.platform.theming.api.common.attributes.IviThemeComponent
import com.tomtom.ivi.platform.theming.api.common.attributes.TtiviThemeCategoryPreset
import com.tomtom.ivi.platform.theming.api.service.themecomponentprovider.ThemeComponentProviderServiceBase

@OptIn(IviExperimental::class)
internal class CustomThemeComponentProviderService(
    iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider,
) : ThemeComponentProviderServiceBase(iviServiceHostContext, serviceIdProvider) {

    init {
        availableThemeComponents = listOf(
            with(TtiviThemeCategoryPreset.COLOR) {
                "Custom".let {
                    IviThemeComponent(
                        "com.example.ivi.custom.app.corethemecomponentprovider.color.$it.dark",
                        category,
                        R.style.CustomThemeColor,
                        "Bad Mobile"
                    )
                }
            },
        )
    }

    @IviExperimental
    override fun onRequiredPropertiesInitialized() {
        serviceReady = true
    }
}
