package com.example.ivi.example.media.userflowpolicy

import com.tomtom.ivi.appsuite.media.api.common.core.SourceId
import com.tomtom.ivi.appsuite.media.api.common.frontend.MediaFrontendContext
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.BrowseSourceUserFlow
import com.tomtom.ivi.appsuite.media.api.common.frontend.policies.UserFlowResult

/**
 * An implementation of [BrowseSourceUserFlow] which opens a custom [ExampleMediaSourcePanel] to
 * browse the source.
 */
internal class ExampleMediaSourceBrowseUserFlow : BrowseSourceUserFlow {

    /**
     * Performs a browse source user flow. The new panel is created and added to the stack. Always
     * returns successful result.
     */
    override suspend fun perform(
        mediaFrontendContext: MediaFrontendContext,
        sourceId: SourceId,
    ): UserFlowResult {
        mediaFrontendContext.mediaFrontendNavigation.openMediaTaskPanel(
            ExampleMediaSourcePanel(mediaFrontendContext),
        )
        return UserFlowResult.Success
    }
}
