package com.android.domain.usecase

import com.android.data.model.domain.Brochure
import com.android.data.repository.BrochureRepository
import com.android.data.utils.Result
import com.android.domain.utils.Constants.DEFAULT_PLACE_HOLDER
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
                    val filteredBrochures = result.data
                        .filter { brochure ->
                            !param.filter.enableProximityFilter ||
                                (brochure.distance != null && brochure.distance!! <= param.filter.maxDistance)
                        }
                        .map { brochure ->
                            brochure.copy(brochureImage = brochure.brochureImage ?: DEFAULT_PLACE_HOLDER)
                        }
                    Result.Success(filteredBrochures, result.source)
                }

                else -> result
            }
        }
}