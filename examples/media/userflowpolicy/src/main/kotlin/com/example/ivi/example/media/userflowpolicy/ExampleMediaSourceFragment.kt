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

package com.example.ivi.example.media.userflowpolicy

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ivi.example.media.userflowpolicy.databinding.ExamplesMediaUserflowpolicyPanelBinding
import com.tomtom.ivi.platform.frontend.api.common.frontend.IviFragment

/**
 * An [IviFragment] responsible for creating a layout and linking it to a ViewModel. This connects
 * the [ExampleMediaSourceViewModel] to the XML layout, represented by
 * [ExamplesMediaUserflowpolicyPanelBinding].
 */
internal class ExampleMediaSourceFragment :
    IviFragment<ExampleMediaSourcePanel, ExampleMediaSourceViewModel>(
        ExampleMediaSourceViewModel::class,
    ) {

    override val viewFactory =
        ViewFactory(ExamplesMediaUserflowpolicyPanelBinding::inflate) { binding ->
            binding.viewModel = viewModel

            val categoriesAdapter = ExampleMediaSourceItemAdapter()
            binding.exampleMediaUserflowpolicyCategories.adapter = categoriesAdapter
            binding.exampleMediaUserflowpolicyCategories.layoutManager =
                LinearLayoutManager(requireContext())
            val contentAdapter = ExampleMediaSourceItemAdapter()
            binding.exampleMediaUserflowpolicyContents.adapter = contentAdapter
            binding.exampleMediaUserflowpolicyContents.layoutManager =
                LinearLayoutManager(requireContext())

            viewModel.categories.observe(viewLifecycleOwner) {
                categoriesAdapter.submitList(it)
            }

            viewModel.contents.observe(viewLifecycleOwner) {
                contentAdapter.submitList(it)
            }
        }
}
