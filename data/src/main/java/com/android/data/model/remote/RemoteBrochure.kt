package com.android.data.model.remote

import androidx.annotation.Keep
import com.android.data.utils.Constants.EMBEDDED
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BrochureApiResponse(
    @Json(name = EMBEDDED) val embedded: EmbeddedBrochures
)

@JsonClass(generateAdapter = true)
data class EmbeddedBrochures(
    val contents: List<RemotePlacement>
)

@Keep
@JsonClass(generateAdapter = true)
data class RemotePlacement(
    val contentType: String?,
    val content: RemoteBrochure?
)

@JsonClass(generateAdapter = true)
data class RemoteBrochure(
    val id: Long,
    val brochureImage: String?,
    val publisher: Publisher,
    val contentType: String?,
    val distance: Double
)

@JsonClass(generateAdapter = true)
data class Publisher(
    val id: String,
    val name: String?,
)

object UnParsableRemotePlacement {
    val default = RemotePlacement(contentType = "unknown", content = null)
}