package com.android.data.repository

import com.android.data.local.BrochureDao
import com.android.data.model.domain.Brochure
import com.android.data.model.local.toDomain
import com.android.data.model.remote.toDomain
import com.android.data.model.remote.toLocal
import com.android.data.network.ApiService
import com.android.data.utils.Constants
import com.android.data.utils.Result.Failure
import com.android.data.utils.Result.Loading
import com.android.data.utils.Result.Success
import com.android.data.utils.Source
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Implementation of the [BrochureRepository] interface.
 *
 * This repository implementation fetches brochure data from both local and remote data sources.
 * It prioritizes local data for immediate display while synchronizing with remote data
 * and saving it locally for future use.
 *
 * @param brochureDao DAO for accessing local brochure data.
 * @param apiService API service for fetching remote brochure data.
 */
class BrochureRepositoryImpl @Inject constructor(
    private val brochureDao: BrochureDao,
    private val apiService: ApiService
) : BrochureRepository {

    /**
     * Fetches a list of brochures.
     *
     * The process includes:
     * 1. Emitting a loading state for local data.
     * 2. Fetching and emitting local data.
     * 3. Emitting a loading state for remote data.
     * 4. Fetching remote data, saving it locally, and emitting it.
     * 5. Handling errors gracefully and emitting a failure state if needed.
     *
     * @return A [Flow] emitting the current state of the data fetch process.
     */
    override fun fetchBrochures(): Flow<com.android.data.utils.Result<List<Brochure>>> = flow {
        emit(Loading(Source.LOCAL))
        val localBrochures = brochureDao.getAllBrochures().map { it.toDomain() }
        emit(Success(localBrochures, Source.LOCAL))

        try {

            emit(Loading(Source.REMOTE))
            val remoteBrochures = apiService.getBrochures().embedded.contents
                .map { it.content?.copy(contentType = it.contentType) }
                .filter { it?.contentType == Constants.BROCHURE_TYPE || it?.contentType == Constants.BROCHURE_PREMIUM_TYPE }
            emit(Success(remoteBrochures.mapNotNull { it?.toDomain() }, Source.REMOTE))

            val localEntities = remoteBrochures.mapNotNull { it?.toLocal() }
            brochureDao.insertBrochures(localEntities)
        } catch (e: Exception) {
            emit(Failure(e, Source.REMOTE))
        }
    }.catch { throwable ->
        val exception = if (throwable is Exception) throwable else Exception(throwable)
        emit(Failure(exception, Source.LOCAL))
    }
}
