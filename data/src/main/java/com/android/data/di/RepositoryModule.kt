package com.android.data.di

import com.android.data.local.BrochureDao
import com.android.data.network.ApiService
import com.android.data.repository.BrochureRepository
import com.android.data.repository.BrochureRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideBrochureRepository(
        brochureDao: BrochureDao,
        apiService: ApiService
    ): BrochureRepository {
        return BrochureRepositoryImpl(brochureDao, apiService)
    }
}
