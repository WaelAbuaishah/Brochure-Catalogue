package com.android.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.data.utils.Constants.BROCHURES_TABLE_NAME

@Entity(tableName = BROCHURES_TABLE_NAME)
data class LocalBrochure(
    @PrimaryKey val id: String,
    val title: String?,
    val contentType: String,
    val brochureImage: String?,
    val publisherId: String,
    val publisherName: String?,
    val publisherType: String?,
    val distance: Double?
)