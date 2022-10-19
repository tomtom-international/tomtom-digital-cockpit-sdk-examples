/*
 * Copyright © 2021 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.activityview

import android.app.PendingIntent
import android.content.Intent
import com.example.ivi.example.activityview.databinding.ModalActivityFragmentBinding
import com.tomtom.ivi.platform.framework.api.common.uiembedding.TtiviActivityView
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ModalPanel
import com.tomtom.kotlin.traceevents.TraceEventListener
import com.tomtom.kotlin.traceevents.Tracer

/**
 * Fragment for the [ModalPanel] to show a [TtiviActivityView].
 */
internal class ModalActivityFragment :
    IviFragment<ModalActivityPanel, ModalActivityViewModel>(ModalActivityViewModel::class) {

    override val viewFactory =
        ViewFactory(ModalActivityFragmentBinding::inflate) { binding ->
            viewModel.pendingIntent.value =
                PendingIntent.getActivity(
                    context,
                    0,
                    ANDROID_INTENT,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

            with(binding.modalActivityFragmentActivityView) {
                onStartedCallback = {
                    requireActivity().runOnUiThread {
                        tracer.activityViewStarted()
                    }
                }
                // With this callback, when the embedded activity completes, the modal panel can
                // be automatically closed.
                onFinishedCallback = {
                    requireActivity().runOnUiThread {
                        tracer.activityViewFinished()
                        viewModel.finish()
                    }
                }
            }
        }

    private companion object {
        private val tracer = Tracer.Factory.create<ActivityViewEvents>(this)

        interface ActivityViewEvents : TraceEventListener {
            fun activityViewStarted()
            fun activityViewFinished()
        }

        val ANDROID_INTENT = Intent(Intent.ACTION_MAIN)
            .apply {
                setClassName("com.android.deskclock", "com.android.deskclock.DeskClock")
            }
    }
}
