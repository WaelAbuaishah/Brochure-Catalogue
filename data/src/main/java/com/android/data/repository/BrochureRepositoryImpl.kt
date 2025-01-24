package com.android.data.repository

import com.android.data.local.BrochureDao
import com.android.data.model.domain.Brochure
import com.android.data.model.local.toDomain
import com.android.data.model.remote.toDomain
import com.android.data.model.remote.toLocal
import com.android.data.network.ApiService
import com.android.data.utils.Constants.BROCHURE_PREMIUM_TYPE
import com.android.data.utils.Constants.BROCHURE_TYPE
import com.android.data.utils.Result.Failure
import com.android.data.utils.Result.Loading
import com.android.data.utils.Result.Success
import com.android.data.utils.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class BrochureRepositoryImpl(
    private val brochureDao: BrochureDao,
    private val apiService: ApiService
) : BrochureRepository {

    override fun fetchBrochures(): Flow<com.android.data.utils.Result<List<Brochure>>> = flow {
        emit(Loading(Source.LOCAL))
        val localBrochures = brochureDao.getAllBrochures().map { it.toDomain() }
        emit(Success(localBrochures, Source.LOCAL))

        emit(Loading(Source.REMOTE))
        try {
            val remoteBrochures = apiService.getBrochures().embedded.contents
                .filter { it.contentType == BROCHURE_TYPE || it.contentType == BROCHURE_PREMIUM_TYPE }

            emit(Success(remoteBrochures.mapNotNull { it.toDomain() }, Source.REMOTE))

            val localEntities = remoteBrochures.map { it.toLocal() }
            brochureDao.insertBrochures(localEntities)

        } catch (e: Exception) {
            emit(Failure(e, Source.REMOTE))
        }
    }.catch { e ->
        emit(Failure(Exception(e), Source.LOCAL))
    }
}
