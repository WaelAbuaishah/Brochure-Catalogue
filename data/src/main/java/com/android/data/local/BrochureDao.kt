package com.android.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.data.local.model.LocalBrochure

/**
 * Data Access Object (DAO) interface for managing `LocalBrochure` operations in the Room database.
 */
@Dao
interface BrochureDao {

    /**
     * Retrieves all brochures from the database.
     *
     * @return A list of [LocalBrochure] objects representing all brochures stored in the database.
     */
    @Query("SELECT * FROM brochures")
    suspend fun getAllBrochures(): List<LocalBrochure>

    /**
     * Inserts a list of brochures into the database.
     *
     * If a brochure with the same primary key already exists, it will be replaced.
     *
     * @param brochures A list of [LocalBrochure] objects to be inserted into the database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrochures(brochures: List<LocalBrochure>)

    /**
     * Deletes all brochures from the database.
     */
    @Query("DELETE FROM brochures")
    suspend fun deleteAllBrochures()
}