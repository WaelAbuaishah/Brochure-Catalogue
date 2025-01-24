package com.android.domain.usecase

import com.android.data.model.domain.Brochure
import com.android.data.repository.BrochureRepository
import com.android.data.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBrochuresUseCaseImpl @Inject constructor(
    private val repository: BrochureRepository
) : GetBrochuresUseCase {

    override fun execute(param: GetBrochuresUseCase.Param): Flow<Result<List<Brochure>>> =
        repository.fetchBrochures().map { result ->
            when (result) {
                is Result.Success -> {
                    val filteredBrochures = result.data.filter { brochure ->
                        if (param.filter.enableProximityFilter) {
                            (brochure.distance != null && brochure.distance!! <= param.filter.maxDistance)
                        }
                        true
                    }
                    Result.Success(filteredBrochures, result.source)
                }

                is Result.Loading -> result
                is Result.Failure -> result
            }
        }
}