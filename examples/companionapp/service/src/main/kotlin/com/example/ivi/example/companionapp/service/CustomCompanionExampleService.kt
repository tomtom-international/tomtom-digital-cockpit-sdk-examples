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

package com.example.ivi.example.companionapp.service

import com.example.ivi.example.companionapp.serviceapi.CompanionExampleServiceBase
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.IviServiceHostContext
import com.tomtom.ivi.platform.framework.api.ipc.iviservice.mirrormap.MutableMirrorableMap
import com.tomtom.ivi.sdk.communications.client.CommunicationsClient
import com.tomtom.ivi.sdk.communications.client.CommunicationsClientContext
import com.tomtom.ivi.sdk.communications.client.CommunicationsClientListener
import com.tomtom.ivi.sdk.communications.common.ServiceProviderId
import com.tomtom.ivi.sdk.communications.exampleservice.ExampleId
import com.tomtom.ivi.sdk.communications.exampleservice.ExampleMessageRequest
import com.tomtom.ivi.sdk.communications.exampleservice.ExampleService
import com.tomtom.ivi.sdk.communications.serviceframework.CommunicationsServiceBase

/**
 * A custom companion service that illustrates how to proxy the communications sdk ExampleService
 * over an Ivi service.
 */
internal class CustomCompanionExampleService(iviServiceHostContext: IviServiceHostContext) :
    CompanionExampleServiceBase(iviServiceHostContext) {

    /**
     * Map of all connected companion apps. Every connected companion app provides its own proxy.
     */
    private val companionAppProxies = mutableMapOf<ServiceProviderId, ExampleService>()

    private val mutableTestMap = MutableMirrorableMap<Int, String>().also { testMap = it }

    /**
     * The communications client context that indicates what service we want to connect to (in this
     * case the ExampleService).
     */
    private val communicationsClientContext = CommunicationsClientContext(
        iviServiceHostContext.context,
        this,
        ExampleService.ClientFactory(),
    )

    private val communicationsServiceClientListener = object : CommunicationsClientListener {
        /**
         * This is called when a companion app that provides the ExampleService is connected .
         * Each service provided by a companion app has a unique service provider ID.
         */
        override fun onServiceConnected(
            serviceProviderId: ServiceProviderId,
            client: CommunicationsServiceBase,
        ) {
            companionAppProxies[serviceProviderId] = client as ExampleService
            client.testLiveDataProperty.observe(this@CustomCompanionExampleService) {
                testProperty = it?.stuff ?: ""
            }
            client.testLiveDataMapProperty
                .observe(this@CustomCompanionExampleService) { sourceMap ->
                    mutableTestMap.keys.retainAll(sourceMap.keys.map { it.value })
                    mutableTestMap.putAll(sourceMap.map { it.key.value to (it.value?.stuff ?: "") })
                }
        }

        /**
         * This is called when a connection is lost to a previously connected service.
         */
        override fun onServiceDisconnected(serviceProviderId: ServiceProviderId) {
            companionAppProxies.remove(serviceProviderId)
        }
    }

    /**
     * This starts listening to available [ExampleService] services on any connected companion app.
     */
    val communicationsClient = CommunicationsClient(
        communicationsClientContext,
        communicationsServiceClientListener,
    )

    init {
        testProperty = "default value"
        serviceReady = true
    }

    override suspend fun testFunctionCall(bar: String): String? {
        /**
         * Proxy a service api function call to the first connected companion app.
         */
        return companionAppProxies.values.firstOrNull()?.testFunctionCall(
            ExampleMessageRequest.newBuilder()
                .setBar(bar)
                .setId(ExampleId.getDefaultInstance())
                .build(),
        )?.stuff
    }
}
