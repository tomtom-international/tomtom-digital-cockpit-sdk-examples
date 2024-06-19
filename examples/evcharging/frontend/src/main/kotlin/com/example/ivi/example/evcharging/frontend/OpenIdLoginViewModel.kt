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

package com.example.ivi.example.evcharging.frontend

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.EmspAuthenticationStatus
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.FailureReason
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.OpenIdAuthenticationResult
import com.tomtom.ivi.platform.evcharging.api.common.evcharging.OpenIdAuthenticationResultInfo
import com.tomtom.ivi.platform.evcharging.api.service.evchargingmediator.EvChargingMediatorService
import com.tomtom.ivi.platform.evcharging.api.service.evchargingmediator.createApi
import com.tomtom.ivi.platform.framework.api.common.uiembedding.TtiviActivityViewStateHolder
import com.tomtom.ivi.platform.frontend.api.common.frontend.viewmodels.FrontendViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class OpenIdLoginViewModel(
    panel: OpenIdLoginPanel,
) : FrontendViewModel<OpenIdLoginPanel>(panel) {
    private val evChargingMediatorService =
        EvChargingMediatorService.createApi(this, frontendContext.iviServiceProvider)

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()
    val activityViewStateHolder = TtiviActivityViewStateHolder()

    init {
        evChargingMediatorService.emspAuthenticationStatus.observe(this) { statuses ->
            statuses?.values?.firstOrNull { status ->
                when (status) {
                    is EmspAuthenticationStatus.AuthenticationRequested ->
                        status.openIdAuthenticationRequestInfo?.authorizationRequestIntent != null
                    else -> false
                }
            }.let { status ->
                when (status) {
                    is EmspAuthenticationStatus.AuthenticationRequested -> {
                        status.openIdAuthenticationRequestInfo?.authorizationRequestIntent
                            ?.let { authIntent ->
                                _viewState.update { viewState ->
                                    val resultIntent =
                                        Intent(
                                            AuthorizationStubActivity
                                                .AUTHORIZATION_ACTIVITY_RESULT_INTENT_FILTER,
                                        )
                                    viewState.copy(
                                        pendingIntentProvider =
                                        AuthorizationStubActivity.createPendingIntentProvider(
                                            authIntent,
                                            resultIntent,
                                        ),
                                    )
                                }
                            }
                    }
                    else -> Unit // Do nothing.
                }
            }
        }
    }

    fun pendingIntentLaunched() {
        _viewState.update {
            it.copy(pendingIntentProvider = null)
        }
    }

    fun authorizationResultReceived(responseIntent: Intent) {
        evChargingMediatorService.provideOpenIdAuthenticationResultAsync(
            OpenIdAuthenticationResult.Success(
                OpenIdAuthenticationResultInfo(responseIntent),
            ),
        )
    }

    fun authorizationCanceled() {
        evChargingMediatorService.provideOpenIdAuthenticationResultAsync(
            OpenIdAuthenticationResult.Failure(FailureReason.LOGIN_CANCELLED_ERROR),
        )
    }

    fun dismiss() {
        viewModelScope.launch { panel.dismiss() }
    }

    override fun onCleared() {
        super.onCleared()
        activityViewStateHolder.release()
    }

    data class ViewState(
        val pendingIntentProvider: AuthorizationStubActivity.PendingIntentProvider? = null,
    )
}
