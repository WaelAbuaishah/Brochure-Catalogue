package com.android.data.network

import com.android.data.model.remote.BrochureApiResponse
import com.android.data.utils.Constants.BROCHURE_LIST
import retrofit2.http.GET

interface ApiService {

    @GET(BROCHURE_LIST)
    suspend fun getBrochures(): BrochureApiResponse

}