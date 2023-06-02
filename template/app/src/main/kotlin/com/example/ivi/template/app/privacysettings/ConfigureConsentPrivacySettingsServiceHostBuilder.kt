package com.example.ivi.template.app.privacysettings

import com.tomtom.ivi.platform.framework.api.ipc.iviservice.AnyIviServiceBase
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.SimpleIviServiceHostBuilder

public class ConfigureConsentPrivacySettingsServiceHostBuilder : SimpleIviServiceHostBuilder() {

    public override fun createIviServices(iviServiceHostContext: IviServiceHostContext):
        Collection<AnyIviServiceBase> =
        listOf(ConfigureConsentPrivacySettingsService(iviServiceHostContext))

    public companion object
}
