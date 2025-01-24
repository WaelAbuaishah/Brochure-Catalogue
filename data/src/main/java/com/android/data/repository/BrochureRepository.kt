package com.android.data.repository

import com.android.data.model.domain.Brochure
import com.android.data.utils.Result
import kotlinx.coroutines.flow.Flow

interface BrochureRepository {

    fun fetchBrochures(): Flow<Result<List<Brochure>>>

}