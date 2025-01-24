package com.android.data.di

import android.content.Context
import androidx.room.Room
import com.android.data.local.AppDatabase
import com.android.data.local.BrochureDao
import com.android.data.utils.Constants.BROCHURE_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            BROCHURE_DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideUserDao(database: AppDatabase): BrochureDao = database.brochureDao()
}