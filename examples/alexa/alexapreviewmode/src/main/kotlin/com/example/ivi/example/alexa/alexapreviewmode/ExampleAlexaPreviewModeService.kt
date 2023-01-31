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

package com.example.ivi.example.alexa.alexapreviewmode

import com.tomtom.ivi.platform.alexa.api.service.alexapreviewmode.AlexaPreviewModeServiceBase
import com.tomtom.ivi.platform.alexa.api.service.alexapreviewmode.PreviewModeResult
import com.tomtom.ivi.platform.alexa.api.service.alexapreviewmode.PreviewModeToken
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.tools.android.core.geography.CountryId
import java.time.Instant

internal class ExampleAlexaPreviewModeService(
    iviServiceHostContext: IviServiceHostContext
) :
    AlexaPreviewModeServiceBase(iviServiceHostContext) {

    override suspend fun registerDevice(
        deviceSerialNumber: String,
        countryId: CountryId
    ): PreviewModeResult {
        // TODO: Requests the OEM's backend to register this device for Alexa Preview Mode using the
        //  AVS `InstantDeviceRegistration` API.

        return PreviewModeResult.Success(
            PreviewModeToken(EXAMPLE_ACCESS_TOKEN, EXAMPLE_EXPIRATION_TIME)
        )
    }

    override suspend fun deregisterDevice(deviceSerialNumber: String): PreviewModeResult {
        // TODO: Requests the OEM's backend to unregister this device for Alexa Preview Mode using
        //  the AVS `InstantDeviceRegistration` API.

        return PreviewModeResult.Success()
    }

    override suspend fun refreshToken(
        deviceSerialNumber: String,
        countryId: CountryId
    ): PreviewModeResult {
        // TODO: Requests the OEM's backend to retrieve a new access token using the AVS
        //  `InstantDeviceRegistration` API.

        return PreviewModeResult.Success(
            PreviewModeToken(EXAMPLE_ACCESS_TOKEN, EXAMPLE_EXPIRATION_TIME)
        )
    }

    private companion object {
        private const val EXAMPLE_ACCESS_TOKEN = "123"
        private val EXAMPLE_EXPIRATION_TIME = Instant.now().plusSeconds(3600)
    }
}
