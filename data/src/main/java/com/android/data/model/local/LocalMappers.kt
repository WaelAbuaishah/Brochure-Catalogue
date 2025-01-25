package com.android.data.model.local

import com.android.data.model.domain.Brochure
import com.android.data.model.domain.Publisher
import com.android.data.utils.Constants.BROCHURE_PREMIUM_TYPE
import com.android.data.utils.Constants.BROCHURE_TYPE

fun LocalBrochure.toDomain(): Brochure {
    return when (contentType) {
        BROCHURE_TYPE -> Brochure.StandardBrochure(
            id = id,
            brochureImage = brochureImage,
            publisher = Publisher(
                id = publisherId,
                name = publisherName,
            ),
            distance = distance
        )
        BROCHURE_PREMIUM_TYPE -> Brochure.PremiumBrochure(
            id = id,
            brochureImage = brochureImage,
            publisher = Publisher(
                id = publisherId,
                name = publisherName,
            ),
            distance = distance
        )
        else -> throw IllegalArgumentException("Unknown contentType: $contentType")
    }
}


//import com.android.data.model.domain.Brochure
//import com.android.data.model.domain.ContentType
//import com.android.data.model.domain.Publisher
//
//fun LocalBrochure.toDomain(): Brochure {
//    val type = when (contentType) {
//        "brochure" -> ContentType.BROCHURE
//        "brochurePremium" -> ContentType.BROCHURE_PREMIUM
//        else -> ContentType.OTHER
//    }
//
//    return Brochure(
//        id = id,
//        title = title,
//        contentType = type,
//        brochureImage = brochureImage,
//        publisher = Publisher(
//            id = publisherId,
//            name = publisherName,
//            type = publisherType
//        ),
//        distance = distance
//    )
//}
