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

package com.example.ivi.example.systemui.custompaneltype.systemui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.annotation.IdRes
import com.example.ivi.example.systemui.custompaneltype.R
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.ControlCenterPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PanelTransitionDestination
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PanelTransitionSource
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.MainProcessPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.isTransitioningFrom
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.TaskPanelSubContainer
import com.tomtom.ivi.platform.systemui.api.common.systemuihost.containercontrollers.TaskPanelSubContainerManager
import com.tomtom.ivi.platform.systemui.api.stock.systemuihost.view.TtiviDismissableContainer
import com.tomtom.tools.android.core.animation.LifecycleAwareAnimationController
import com.tomtom.tools.android.core.animation.extension.DefaultAnimationListener
import com.tomtom.tools.android.core.animation.view.BottomSideSpringAnimation
import com.tomtom.tools.android.core.animation.view.LeftSideSpringAnimation

/**
 * A sub container manager that adds and remove [TaskPanel] sub containers.
 *
 * __Note:__
 * This is currently the best way to implement this in TomTom Digital Cockpit. This is suboptimal,
 * and we are working to simplify this as part of pluggable system UI. We decided to provide this
 * example nonetheless, so it does not block anyone wanting to create a custom system UI.
 */
internal class ExampleTaskPanelSubContainerManager(
    private val animationController: LifecycleAwareAnimationController
) : TaskPanelSubContainerManager {
    private var subContainersBeingDismissedByDrag = mutableSetOf<ViewGroup>()
    private val panelTransitionSources =
        mutableMapOf<TaskPanelSubContainer, PanelTransitionSource?>()

    // [TaskPanel.Mode] will be removed. Please see [TaskPanel.MODE_DEPRECATION_MESSAGE] for
    // more information.
    @Suppress("DEPRECATION")
    override fun addSubContainer(
        parentContainer: ViewGroup,
        mode: TaskPanel.Mode,
        panelTransitionSource: PanelTransitionSource?,
        @IdRes subContainerTaskPanelId: Int?,
        @IdRes subContainerTaskProcessPanelId: Int?
    ): TaskPanelSubContainer =
        when (mode) {
            TaskPanel.Mode.SIDE_BAR -> R.layout.ttivi_systemui_taskpanel_subcontainer_sidebar
            TaskPanel.Mode.MAXIMIZED -> R.layout.ttivi_systemui_taskpanel_subcontainer_maximized
        }.let { layout ->
            LayoutInflater.from(parentContainer.context)
                .inflate(layout, parentContainer, false) as TtiviDismissableContainer
        }.run {
            // Update the ID of each container.
            val taskPanelContainer = findViewById<ViewGroup>(
                R.id.ttivi_systemui_taskpanel_subcontainer_taskpanels_stub_id
            )!!.apply { id = subContainerTaskPanelId ?: View.generateViewId() }

            val taskProcessPanelContainer = findViewById<ViewGroup>(
                R.id.ttivi_systemui_taskprocesspanel_subcontainer_taskprocesspanel_stub_id
            )!!.apply { id = subContainerTaskProcessPanelId ?: View.generateViewId() }

            parentContainer.addView(this)

            if (subContainerTaskPanelId == null) {
                animationController.runAnimation(
                    this,
                    createTaskPanelEnterAnimation(this, panelTransitionSource)
                )
            }

            if (panelTransitionSource.shouldHaveVerticalAnimation) {
                ttiviDismissalDirection = TtiviDismissableContainer.DismissalDirection.NONE
            }

            TaskPanelSubContainer(this, taskPanelContainer, taskProcessPanelContainer).also {
                panelTransitionSources[it] = panelTransitionSource
            }
        }

    override fun removeSubContainer(
        subContainer: TaskPanelSubContainer,
        panelTransitionDestination: PanelTransitionDestination?,
        onSubContainerRemoved: () -> Unit
    ) {
        val subContainerRoot = subContainer.root
        // Only create exit animation if the user didn't dismiss the task panel.
        if (!subContainersBeingDismissedByDrag.contains(subContainerRoot)) {
            // Run animation only if [taskPanelContainer] has children.
            if (subContainer.taskPanelSubContainer.childCount != 0) {
                val animation = createTaskPanelExitAnimation(
                    subContainerRoot,
                    panelTransitionSources[subContainer]
                )
                animationController.runAnimation(
                    subContainerRoot,
                    animation,
                    object : DefaultAnimationListener {
                        override fun onAnimationEnd(animation: Animation?) {
                            onSubContainerRemoved()
                        }
                    }
                )
            } else {
                onSubContainerRemoved()
            }
        }
        panelTransitionSources.remove(subContainer)
    }

    /**
     * @param panelView View of panel.
     * @param panelTransitionSource The source the [View] should transition from when animating into
     *   view.
     *
     * @return An animation used for putting a new task panel in the system UI.
     */
    private fun createTaskPanelEnterAnimation(
        panelView: View,
        panelTransitionSource: PanelTransitionSource?
    ): Animation = when {
        panelTransitionSource.shouldHaveVerticalAnimation ->
            BottomSideSpringAnimation(
                panelView,
                BottomSideSpringAnimation.TransitionType.ENTER,
                false
            )
        else -> LeftSideSpringAnimation(
            panelView,
            LeftSideSpringAnimation.TransitionType.ENTER,
            false
        )
    }

    /**
     * @param panelView View of panel.
     * @param panelTransitionSource The source the [View] should transition to when animating out
     *   of view.
     *
     * @return An animation used for removing a previous added task panel from the system UI.
     */
    private fun createTaskPanelExitAnimation(
        panelView: View,
        panelTransitionSource: PanelTransitionSource?
    ): Animation = when {
        panelTransitionSource.shouldHaveVerticalAnimation ->
            BottomSideSpringAnimation(
                panelView,
                BottomSideSpringAnimation.TransitionType.EXIT,
                panelView.context.resources
                    .getBoolean(R.bool.ttivi_systemui_taskpanel_fading_enabled)
            )
        else -> LeftSideSpringAnimation(
            panelView,
            LeftSideSpringAnimation.TransitionType.EXIT,
            panelView.context.resources
                .getBoolean(R.bool.ttivi_systemui_taskpanel_fading_enabled)
        )
    }

    private val PanelTransitionSource?.shouldHaveVerticalAnimation: Boolean
        get() = isTransitioningFrom<ControlCenterPanel>() || isTransitioningFrom<MainProcessPanel>()

    override fun setDismissalCallbacks(
        subContainer: ViewGroup,
        onDismissalStarted: () -> Unit,
        onDismissalCompleted: () -> Unit
    ) {
        require(subContainer is TtiviDismissableContainer)
        subContainer.onDismissalStarted = {
            subContainersBeingDismissedByDrag.add(subContainer)
            onDismissalStarted.invoke()
        }
        subContainer.onDismissalCompleted = {
            subContainersBeingDismissedByDrag.remove(subContainer)
            onDismissalCompleted.invoke()
        }
    }
}
