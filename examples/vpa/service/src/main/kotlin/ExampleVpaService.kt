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

package com.example.ivi.example.vpa.service

import com.tomtom.ivi.platform.bluetoothconnectivity.api.common.model.BluetoothDeviceAddress
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviDiscoverableServiceIdProvider
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.ivi.platform.vpa.api.common.vpa.AuthenticationState
import com.tomtom.ivi.platform.vpa.api.common.vpa.AuthenticationType
import com.tomtom.ivi.platform.vpa.api.common.vpa.DialogueState
import com.tomtom.ivi.platform.vpa.api.common.vpa.PersonalDataType
import com.tomtom.ivi.platform.vpa.api.common.vpa.VpaAuthenticationStatus
import com.tomtom.ivi.platform.vpa.api.common.vpa.VpaAvailabilityState
import com.tomtom.ivi.platform.vpa.api.common.vpa.VpaEarconType
import com.tomtom.ivi.platform.vpa.api.common.vpa.VpaIdentifier
import com.tomtom.ivi.platform.vpa.api.common.vpa.VpaProperties
import com.tomtom.ivi.platform.vpa.api.common.vpa.VpaSettings
import com.tomtom.ivi.platform.vpa.api.service.vpaadaptation.VpaAdaptationServiceBase
import java.util.EnumSet
import java.util.Locale

internal class ExampleVpaService(
    iviServiceHostContext: IviServiceHostContext,
    serviceIdProvider: IviDiscoverableServiceIdProvider
) : VpaAdaptationServiceBase(iviServiceHostContext, serviceIdProvider) {

    override fun onCreate() {
        super.onCreate()

        vpaProperties = VpaProperties(
            vpaIdentifier = VPA_IDENTIFIER,
            supportedLocales = SUPPORTED_LOCALES,
            supportedCountries = SUPPORTED_COUNTRIES
        )

        vpaAuthenticationStatus = VpaAuthenticationStatus(AuthenticationState.NOT_AUTHENTICATED)

        vpaAvailabilityState = VpaAvailabilityState.AVAILABLE

        dialogueState = DialogueState.IDLE

        alerts = emptyList()

        hasPendingNotifications = false

        vpaSettings = VpaSettings(
            wakeUpWordEnabled = false,
            activeLocales = emptyList(),
            enabledEarcons = EnumSet.noneOf(VpaEarconType::class.java),
            doNotDisturbModeEnabled = false,
            locationSharingEnabled = false,
            syncVehicleNavigationFavoritesEnabled = false,
            allowedPersonalData = emptyMap()
        )

        serviceReady = true
    }

    override suspend fun logIn(authenticationType: AuthenticationType?) {
        // TODO: Log in to the VPA.

        // If the login is successful - update the status.
        vpaAuthenticationStatus = VpaAuthenticationStatus(AuthenticationState.AUTHENTICATED)
    }

    override suspend fun logOut() {
        // TODO: Log out of the VPA.

        // If the logout is successful - update the status.
        vpaAuthenticationStatus = VpaAuthenticationStatus(AuthenticationState.NOT_AUTHENTICATED)
    }

    override suspend fun enableWakeUpWord(enable: Boolean) {
        // TODO: Switch listening for the wake-up word.
    }

    override suspend fun updateActiveLocales(locales: List<Locale>) {
        // TODO: Update the VPA's active locales.
    }

    override suspend fun enableEarcon(earconType: VpaEarconType, enable: Boolean) {
        // TODO: Enable or disable playing an earcon of a certain type.
    }

    override suspend fun enableDoNotDisturbMode(enable: Boolean) {
        // TODO: Enable or disable do not disturb mode.
    }

    override suspend fun enableLocationSharing(enable: Boolean) {
        // TODO: Enable or disable location sharing.
    }

    override suspend fun enableSyncVehicleNavigationFavorites(enable: Boolean) {
        // TODO: Enable or disable favorites synchronization.
    }

    override suspend fun updateAllowedPersonalData(
        bluetoothDeviceAddress: BluetoothDeviceAddress,
        allowedPersonalData: EnumSet<PersonalDataType>
    ) {
        // TODO: Update what personal data is allowed to be sent to the VPA.
    }

    override suspend fun startListening() {
        // TODO: Handle push-to-talk activation.
    }

    override suspend fun enablePrivacyMode(enable: Boolean) {
        // TODO: Enable or disable the privacy mode.
    }

    private companion object ExampleVpaStub {
        val VPA_IDENTIFIER = VpaIdentifier(name = "Example VPA")

        val SUPPORTED_LOCALES: List<Locale> = listOf(Locale.US)
        val SUPPORTED_COUNTRIES: List<String> = listOf("USA")
    }
}
