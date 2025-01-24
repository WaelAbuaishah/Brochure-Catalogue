package com.android.domain.usecase

import com.android.data.model.domain.Brochure
import com.android.data.utils.Result
import com.android.domain.utils.Constants.DEFAULT_MAX_INSTANCE_FILTER

interface GetBrochuresUseCase : BaseUseCase<GetBrochuresUseCase.Param, Result<List<Brochure>>> {
    data class Param(val filter: BrochureFilter)
}

data class BrochureFilter(
    val enableProximityFilter: Boolean = false,
    val maxDistance: Double = DEFAULT_MAX_INSTANCE_FILTER,
    /*Here we can add more criteria if want to have more filter policy*/
)
