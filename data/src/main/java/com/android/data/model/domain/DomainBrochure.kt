package com.android.data.model.domain

data class Brochure(
    val id: String,
    val title: String?,
    val contentType: ContentType,
    val brochureImage: String?,
    val publisher: Publisher,
    val distance: Double?
)

data class Publisher(
    val id: String,
    val name: String?,
    val type: String?
)

enum class ContentType {
    BROCHURE,
    BROCHURE_PREMIUM,
    OTHER
}
