package com.android.data.model.local

import com.android.data.model.domain.Brochure
import com.android.data.model.domain.ContentType
import com.android.data.model.domain.Publisher

fun LocalBrochure.toDomain(): Brochure {
    val type = when (contentType) {
        "brochure" -> ContentType.BROCHURE
        "brochurePremium" -> ContentType.BROCHURE_PREMIUM
        else -> ContentType.OTHER
    }

    return Brochure(
        id = id,
        title = title,
        contentType = type,
        brochureImage = brochureImage,
        publisher = Publisher(
            id = publisherId,
            name = publisherName,
            type = publisherType
        ),
        distance = distance
    )
}
