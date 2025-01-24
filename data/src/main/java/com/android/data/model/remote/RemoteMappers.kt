package com.android.data.model.remote

import com.android.data.model.domain.Brochure
import com.android.data.model.domain.ContentType
import com.android.data.model.domain.Publisher
import com.android.data.model.local.LocalBrochure
import com.android.data.utils.Constants.UNKNOWN_BROCHURE_TYPE

fun RemoteBrochure.toDomain(): Brochure? {
    val type = when (contentType) {
        "brochure" -> ContentType.BROCHURE
        "brochurePremium" -> ContentType.BROCHURE_PREMIUM
        else -> ContentType.OTHER
    }

    return if (type != ContentType.OTHER) {
        Brochure(
            id = id,
            title = title,
            contentType = type,
            brochureImage = brochureImage,
            publisher = publisher?.toDomain() ?: return null,
            distance = distance
        )
    } else null
}

fun RemotePublisher.toDomain(): Publisher {
    return Publisher(
        id = id.orEmpty(),
        name = name,
        type = type
    )
}

fun RemoteBrochure.toLocal(): LocalBrochure {
    return LocalBrochure(
        id = id,
        title = title,
        contentType = contentType ?: UNKNOWN_BROCHURE_TYPE,
        brochureImage = brochureImage,
        publisherId = publisher?.id.orEmpty(),
        publisherName = publisher?.name,
        publisherType = publisher?.type,
        distance = distance
    )
}
