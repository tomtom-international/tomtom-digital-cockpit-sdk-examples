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

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ivi.example.evcharging.frontend.databinding.TtiviEvchargingloginFragmentBinding
import com.tomtom.ivi.platform.framework.api.common.uiembedding.TtiviActivityView
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.tools.android.api.util.getParcelableCompat
import kotlinx.coroutines.launch

internal class OpenIdLoginFragment :
    IviFragment<OpenIdLoginPanel, OpenIdLoginViewModel>(OpenIdLoginViewModel::class) {

    private lateinit var activityView: TtiviActivityView
    private val authorizationActivityResultReceiver = AuthorizationActivityResultReceiver()

    override val viewFactory =
        ViewFactory(TtiviEvchargingloginFragmentBinding::inflate) { binding ->
            with(binding.ttiviEvchargingloginActivityView) {
                activityView = this
                onFinishedCallback = { viewModel.dismiss() }
            }
        }

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                OpenIdLoginViewModel(panel)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ContextCompat.registerReceiver(
            requireContext(),
            authorizationActivityResultReceiver,
            IntentFilter(AuthorizationStubActivity.AUTHORIZATION_ACTIVITY_RESULT_INTENT_FILTER),
            AuthorizationStubActivity.BROADCAST_PERMISSION,
            null,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    it.pendingIntentProvider?.let { provider ->
                        activityView.pendingIntent = provider.get(requireContext())
                        viewModel.pendingIntentLaunched()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().unregisterReceiver(authorizationActivityResultReceiver)
    }

    private inner class AuthorizationActivityResultReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val result = intent.extras?.let {
                getActivityResult(it)
            }

            result?.let {
                when (result.resultCode) {
                    Activity.RESULT_OK -> {
                        result.data?.let {
                            viewModel.authorizationResultReceived(it)
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        viewModel.authorizationCanceled()
                    }
                    else -> {
                        // Do something with result.resultCode
                    }
                }
            }
        }
    }

    private companion object {
        fun getActivityResult(extras: Bundle): ActivityResult? {
            val key = AuthorizationStubActivity.AUTHORIZATION_ACTIVITY_RESULT_EXTRA
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.getParcelable(key, ActivityResult::class.java)
            } else {
                extras.getParcelableCompat(key)
            }
        }
    }
}
