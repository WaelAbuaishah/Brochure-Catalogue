package com.android.data.repository

import com.android.data.model.domain.Brochure
import com.android.data.utils.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing brochures.
 *
 * This abstraction defines the contract for fetching brochure data from
 * both local and remote data sources, while ensuring consistency and
 * handling error cases.
 */
interface BrochureRepository {

    /**
     * Fetches a list of brochures.
     *
     * The repository emits a flow of [Result] objects, representing the state of the data fetch process:
     * - [Result.Loading]: Indicates that the data is being loaded, either from local or remote sources.
     * - [Result.Success]: Contains the successfully fetched data and its source (local or remote).
     * - [Result.Failure]: Contains the exception in case of an error.
     *
     * @return A [Flow] emitting the current state of the data fetch process.
     */
    fun fetchBrochures(): Flow<Result<List<Brochure>>>
}
