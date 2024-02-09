/*
 * Copyright © 2024 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.evcharging.service

import com.tomtom.ivi.platform.evcharging.api.common.evcharging.AccountInfo
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.ChargingPointId
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.ConnectorId
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.EmspAuthenticationStatus
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.EmspAuthenticationStatus.NotAuthenticated
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.EmspIdentifier
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.EmspServiceInfo
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.EvChargingResult
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.OpenIdAuthenticationResult
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.UserId
import com.tomtom.ivi.platform.evcharging.api.service.evcharging.EvChargingService.ChargingStationTariffsResult
import com.tomtom.ivi.platform.evcharging.api.service.evcharging.EvChargingServiceBase
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.ivi.platform.navigation.api.common.model.ChargingStationId
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver
import com.tomtom.tools.android.core.geography.CountryId

/**
 * Example implementation of an EV charging service.
 */
internal class ExampleEvChargingService(
    iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider,
) : EvChargingServiceBase(iviServiceHostContext, serviceIdProvider) {

    override fun onCreate() {
        super.onCreate()

        emspServiceInfo = EMSP_INFO
        emspAuthenticationStatus = NotAuthenticated
        serviceReady = true
    }

    override fun onDestroy() {
        // Put here cleaning code if necessary.
        super.onDestroy()
    }

    override suspend fun logIn(): EvChargingResult {
        emspAuthenticationStatus = EmspAuthenticationStatus.Authenticated(
            accountInfo = AccountInfo(
                emailAddress = EMSP_ACCOUNT_EMAIL_ADDRESS,
                userId = UserId(EMSP_ACCOUNT_USER_ID),
            ),
        )
        return EvChargingResult.Success
    }

    override suspend fun provideOpenIdAuthenticationResult(result: OpenIdAuthenticationResult) {
    }

    override suspend fun logOut() {
        emspAuthenticationStatus = NotAuthenticated
    }

    override suspend fun getChargingStationTariffs(
        chargingStationId: ChargingStationId,
    ): ChargingStationTariffsResult {
        // Add here an implementation to get the charging station tariffs.
        return ChargingStationTariffsResult.Success(emptyList())
    }

    override suspend fun startCharging(
        chargingStationId: ChargingStationId,
        chargingPointId: ChargingPointId,
        connectorId: ConnectorId,
    ): EvChargingResult {
        // Add here an implementation to start the charging session.
        return EvChargingResult.Success
    }

    override suspend fun stopCharging(): EvChargingResult {
        // Add here an implementation to stop the charging session.
        return EvChargingResult.Success
    }

    override suspend fun resetCharging(): EvChargingResult {
        // Add here an implementation to reset the charging session.
        return EvChargingResult.Success
    }

    internal companion object {
        private const val SERVICE_NAME = "Example EvCharging Service"
        private const val EMSP_NAME = "Example eMSP"
        const val EMSP_ACCOUNT_EMAIL_ADDRESS = "test@emsp.com"
        const val EMSP_ACCOUNT_USER_ID = "12345abc"

        val EMSP_INFO = EmspServiceInfo(
            emspIdentifier = EmspIdentifier(EMSP_NAME),
            localizedServiceName = StaticStringResolver(SERVICE_NAME),
            icon = null,
            countriesOfOperation = CountryId.values().toSet(),
        )
    }
}
