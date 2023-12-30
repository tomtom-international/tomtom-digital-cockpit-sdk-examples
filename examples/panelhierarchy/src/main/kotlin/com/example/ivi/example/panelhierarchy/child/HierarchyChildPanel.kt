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

package com.example.ivi.example.panelhierarchy.child

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.ivi.example.panelhierarchy.Node
import com.example.ivi.example.panelhierarchy.leaf.HierarchyLeafPanel
import com.example.ivi.example.panelhierarchy.root.AddPanel
import com.tomtom.ivi.platform.frontend.api.common.frontend.FrontendContext
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.PanelDescriptor
import com.tomtom.ivi.platform.frontend.api.common.frontend.panels.TaskPanel
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver

/**
 * Displays [node], which is not at the root of the tree, but has at least one child.
 */
internal class HierarchyChildPanel(
    frontendContext: FrontendContext,
    val node: Node,
    private val addPanel: AddPanel,
) : TaskPanel(frontendContext) {

    override val descriptor: LiveData<PanelDescriptor>
        get() = super.descriptor.map { it.copy(label = StaticStringResolver(node.name)) }

    override fun createInitialFragmentInitializer() =
        IviFragment.Initializer(HierarchyChildFragment(), this)

    fun openChild(child: Node) = addPanel(HierarchyLeafPanel(frontendContext, child))
}
