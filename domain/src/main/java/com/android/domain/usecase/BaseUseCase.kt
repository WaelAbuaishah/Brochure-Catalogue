package com.android.domain.usecase

import kotlinx.coroutines.flow.Flow

interface BaseUseCase<in Param, out Type> {

    fun execute(param: Param): Flow<Type>
}