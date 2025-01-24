package com.android.data.network.model

import com.android.data.utils.Constants.EMBEDDED
import com.google.gson.annotations.SerializedName

data class RemoteBrochure(
    val id: String,
    val title: String?,
    val contentType: String?,
    val brochureImage: String?,
    val publisher: RemotePublisher?,
    val distance: Double?
)

data class RemotePublisher(
    val id: String?,
    val name: String?,
    val type: String?
)

data class BrochureApiResponse(
    @SerializedName(EMBEDDED) val embedded: EmbeddedBrochures
)

data class EmbeddedBrochures(
    val contents: List<RemoteBrochure>
)