package com.example.ivi.template.app.privacysettings

import com.tomtom.ivi.platform.framework.api.common.annotations.IviExperimental
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.ivi.platform.navigation.api.service.privacysettings.PrivacySettingsService
import com.tomtom.ivi.platform.navigation.api.service.privacysettings.PrivacySettingsServiceBase

/**
 * Implementation for the [PrivacySettingsService]. This service is implemented to
 * provide consent for data collection and online services.
 */
@OptIn(IviExperimental::class)
internal class ConfigureConsentPrivacySettingsService(
    iviServiceHostContext: IviServiceHostContext
) :
    PrivacySettingsServiceBase(iviServiceHostContext) {

    override fun onCreate() {
        super.onCreate()
        /*
         * Consent related to navigation online services.
         * Online navigation features using user private data other than his current location are
         * permitted. Using current location requires [PrivacySettingsService.ConsentType.LOCATION]
         * consent.
         *
         * NOTE: In this template application, the consent is not given by end-user but
         * hard-coded for demonstration of online navigation functionality.
         */
        consentTypes = setOf(
            PrivacySettingsService.ConsentType.CONNECTED_SERVICES,
        )
        serviceReady = true
    }

    companion object
}
