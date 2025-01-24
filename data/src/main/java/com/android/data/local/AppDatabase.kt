package com.android.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.data.model.local.LocalBrochure

@Database(entities = [LocalBrochure::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to the Brochure DAO.
     */
    abstract fun brochureDao(): BrochureDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Retrieves a singleton instance of the [AppDatabase].
         *
         * @param context The application context.
         * @return The [AppDatabase] instance.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
