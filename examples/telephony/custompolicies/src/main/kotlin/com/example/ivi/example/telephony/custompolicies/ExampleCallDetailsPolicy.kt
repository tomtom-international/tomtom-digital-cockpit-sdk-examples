/*
 * Copyright © 2023 TomTom NV. All rights reserved.
 *
 * This software is the proprietary copyright of TomTom NV and its subsidiaries and may be
 * used for internal evaluation purposes or commercial use strictly subject to separate
 * license agreement between you and TomTom NV. If you are the licensee, you are only permitted
 * to use this software in accordance with the terms of your license agreement. If you are
 * not the licensee, you are not authorized to use this software in any manner and should
 * immediately return or destroy it.
 */

package com.example.ivi.example.telephony.custompolicies

import com.tomtom.ivi.appsuite.communications.api.common.frontend.CallDetails
import com.tomtom.ivi.appsuite.communications.api.common.frontend.CallDetailsPolicy
import com.tomtom.ivi.platform.contacts.api.common.model.Contact
import com.tomtom.ivi.platform.telecom.api.common.model.Call
import com.tomtom.tools.android.api.resourceresolution.drawable.ResourceDrawableResolver
import com.tomtom.tools.android.api.resourceresolution.string.StaticStringResolver
import com.tomtom.tools.android.api.resourceresolution.string.StringResolver
import com.tomtom.tools.android.api.uicontrols.imageview.ImageDescriptor

/**
 * This class shows an example of a [CallDetailsPolicy] implementation - the policy that helps you
 * to modify some of the call details: its title, state, image, etc. See [CallDetails] API for more
 * information.
 *
 * In this policy we specify that:
 * - title should contain the call's phone number or a fallback string.
 * - state is the call creation time or a fallback.
 * - description is the contact's company name and is shown only if it is not null.
 * - image is a static resource drawable.
 * - DTMF tones are not supported.
 * - Communications frontend should be opened only in case the call's contact is a favorite.
 */
internal class ExampleCallDetailsPolicy : CallDetailsPolicy {
    override fun invoke(call: Call?, contact: Contact?): CallDetails = CallDetails(
        title = getTitle(call),
        state = getState(call),
        description = getDescription(contact),
        image = getImage(),
        isDtmfSupported = true,
        shouldOpenFrontendOnContactTap = contact?.favorite ?: false
    )

    private fun getTitle(call: Call?): StringResolver =
        StaticStringResolver(call?.phoneNumber ?: "Example fallback title")

    private fun getState(call: Call?): StringResolver =
        StaticStringResolver(call?.creationTime ?: "Example fallback state")

    private fun getDescription(contact: Contact?): StringResolver? =
        contact?.let { StaticStringResolver(contact.companyName) }

    private fun getImage(): ImageDescriptor =
        ImageDescriptor(ResourceDrawableResolver(R.drawable.ic_example_call_image))
}
