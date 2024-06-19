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

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.tomtom.tools.android.api.util.getParcelableCompat

/**
 * This class supports loading the UI to perform Open ID authentication. It expects
 * an Open ID authorization [Intent] to be in the intent [Bundle] at key
 * [AUTHORIZATION_REQUEST_INTENT_EXTRA] and launches that intent. @see
 * [net.openid.appauth.AuthorizationService.getAuthorizationRequestIntent] for how to obtain
 * an authorization [Intent].
 *
 * The actual end-user login process is then delegated to the webpage loaded from that [Intent].
 *
 * After the user logs in (or abandons the login process), the activity result holds the login
 * result and this stub activity finishes with an [Intent] containing an extra with that
 * result under the key [AUTHORIZATION_ACTIVITY_RESULT_EXTRA].
 */
internal class AuthorizationStubActivity : AppCompatActivity() {

    private fun createActivityLauncher(extras: Bundle) = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result ->
        val activityResultPendingIntent =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                extras.getParcelable(
                    AUTHORIZATION_REQUEST_RESULT_BROADCAST_INTENT,
                    PendingIntent::class.java,
                )
            } else {
                extras.getParcelableCompat(
                    AUTHORIZATION_REQUEST_RESULT_BROADCAST_INTENT,
                ) as? PendingIntent
            }

        val resultIntent = Intent().putExtra(AUTHORIZATION_ACTIVITY_RESULT_EXTRA, result)
        activityResultPendingIntent?.send(
            this,
            0,
            resultIntent,
            null /* onFinished */,
            null /* handler */,
            BROADCAST_PERMISSION,
        )
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.ttivi_evcharginglogin_authorization_stub_activity_layout)

        check(
            intent.extras != null &&
                intent.extras!!.containsKey(AUTHORIZATION_REQUEST_INTENT_EXTRA) &&
                intent.extras!!.containsKey(AUTHORIZATION_REQUEST_RESULT_BROADCAST_INTENT),
        )

        intent.extras?.let {
            val activityToLaunchIntent =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    it.getParcelable(AUTHORIZATION_REQUEST_INTENT_EXTRA, Intent::class.java)
                } else {
                    it.getParcelableCompat(AUTHORIZATION_REQUEST_INTENT_EXTRA) as? Intent
                }

            createActivityLauncher(it).launch(activityToLaunchIntent)
        }
    }

    class PendingIntentProvider internal constructor(
        private val authorizationRequestIntent: Intent,
        private val activityResultBroadcastIntent: Intent,
    ) {
        fun get(context: Context): PendingIntent? = PendingIntent.getActivity(
            context,
            0 /* No result code */,
            createStubActivityIntent(context),
            getPendingIntentFlags(),
        )

        private fun createStubActivityIntent(context: Context) =
            Intent(Intent.ACTION_MAIN)
                .setComponent(
                    ComponentName(
                        context.packageName,
                        AuthorizationStubActivity::class.qualifiedName!!,
                    ),
                )
                .putExtra(AUTHORIZATION_REQUEST_INTENT_EXTRA, authorizationRequestIntent)
                .putExtra(
                    AUTHORIZATION_REQUEST_RESULT_BROADCAST_INTENT,
                    createActivityResultBroadcastPendingIntent(context),
                )

        private fun createActivityResultBroadcastPendingIntent(context: Context) =
            PendingIntent.getBroadcast(
                context,
                0,
                activityResultBroadcastIntent,
                getPendingIntentFlags(),
            )

        private fun getPendingIntentFlags() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
    }

    companion object {
        private const val PACKAGE_PREFIX =
            "com.tomtom.ivi.appsuite.evcharging.plugin.frontend.evcharginglogin"

        const val AUTHORIZATION_REQUEST_INTENT_EXTRA =
            "$PACKAGE_PREFIX.AuthorizationRequestIntentExtra"

        const val AUTHORIZATION_REQUEST_RESULT_BROADCAST_INTENT =
            "$PACKAGE_PREFIX.AuthorizationRequestResultBroadcastIntentExtra"

        const val AUTHORIZATION_ACTIVITY_RESULT_EXTRA =
            "$PACKAGE_PREFIX.AuthorizationActivityResultExtra"

        const val AUTHORIZATION_ACTIVITY_RESULT_INTENT_FILTER =
            "$PACKAGE_PREFIX.AuthorizationActivityResult"

        const val BROADCAST_PERMISSION = "$PACKAGE_PREFIX.permission.RECEIVE_LOGIN_ATTEMPT_RESULT"

        fun createPendingIntentProvider(
            authorizationRequestIntent: Intent,
            activityResultBroadcastIntent: Intent,
        ) = PendingIntentProvider(authorizationRequestIntent, activityResultBroadcastIntent)
    }
}
