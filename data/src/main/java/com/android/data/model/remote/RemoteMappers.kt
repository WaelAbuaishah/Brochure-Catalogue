package com.android.data.model.remote

import com.android.data.model.domain.Brochure
import com.android.data.model.domain.Publisher
import com.android.data.model.local.LocalBrochure
import com.android.data.utils.Constants
import com.android.data.utils.Constants.BROCHURE_TYPE

typealias RemotePublisher = com.android.data.model.remote.Publisher

fun RemoteBrochure.toDomain() =
    if (this.contentType.equals(Constants.BROCHURE_PREMIUM_TYPE, ignoreCase = true)) {
        Brochure.PremiumBrochure(
            id = id.toString(),
            brochureImage = brochureImage,
            publisher = publisher.toDomain(),
            distance = distance
        )
    } else {
        Brochure.StandardBrochure(
            id = id.toString(),
            brochureImage = brochureImage,
            publisher = publisher.toDomain(),
            distance = distance
        )
    }

fun RemotePublisher.toDomain() =
    Publisher(
        id = id,
        name = name
    )

fun RemoteBrochure.toLocal() =
    LocalBrochure(
        id = id.toString(),
        contentType = this.contentType ?: BROCHURE_TYPE,
        brochureImage = brochureImage,
        publisherId = publisher.id,
        publisherName = publisher.name,
        distance = distance
    )
