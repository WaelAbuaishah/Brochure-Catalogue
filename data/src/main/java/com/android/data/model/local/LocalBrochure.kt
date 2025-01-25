package com.android.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.data.utils.Constants.BROCHURES_TABLE_NAME

@Entity(tableName = BROCHURES_TABLE_NAME)
data class LocalBrochure(
    @PrimaryKey val id: String,
    val contentType: String,
    val brochureImage: String?,
    val publisherId: String,
    val publisherName: String?,
    val distance: Double?
)