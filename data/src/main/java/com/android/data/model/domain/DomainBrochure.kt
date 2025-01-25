package com.android.data.model.domain

sealed class Brochure(
    open val id: String,
    open val brochureImage: String?,
    open val publisher: Publisher,
    open val distance: Double?
) {
    data class StandardBrochure(
        override val id: String,
        override val brochureImage: String?,
        override val publisher: Publisher,
        override val distance: Double?
    ) : Brochure(id, brochureImage, publisher, distance)

    data class PremiumBrochure(
        override val id: String,
        override val brochureImage: String?,
        override val publisher: Publisher,
        override val distance: Double?
    ) : Brochure(id, brochureImage, publisher, distance)
}

fun Brochure.copyWith(
    brochureImage: String? = this.brochureImage
) = when (this) {
    is Brochure.StandardBrochure -> this.copy(
        brochureImage = brochureImage,
    )

    is Brochure.PremiumBrochure -> this.copy(
        brochureImage = brochureImage,
    )
}

data class Publisher(
    val id: String,
    val name: String?,
)

//data class Brochure(
//    val id: String,
//    val title: String?,
//    val contentType: ContentType,
//    val brochureImage: String?,
//    val publisher: Publisher,
//    val distance: Double?
//)
//
//data class Publisher(
//    val id: String,
//    val name: String?,
//    val type: String?
//)
//
//enum class ContentType {
//    BROCHURE,
//    BROCHURE_PREMIUM,
//    OTHER
//}
