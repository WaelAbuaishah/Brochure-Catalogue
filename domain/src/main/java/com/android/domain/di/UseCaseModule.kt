package com.android.domain.di

import com.android.data.repository.BrochureRepository
import com.android.domain.usecase.GetBrochuresUseCase
import com.android.domain.usecase.GetBrochuresUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun bindGetBrochuresUseCase(
        repository: BrochureRepository
    ): GetBrochuresUseCase {
        return GetBrochuresUseCaseImpl(repository)
    }

}