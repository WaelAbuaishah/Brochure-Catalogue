package com.android.data.repository

import com.android.data.local.BrochureDao
import com.android.data.model.local.LocalBrochure
import com.android.data.model.remote.BrochureApiResponse
import com.android.data.model.remote.EmbeddedBrochures
import com.android.data.model.remote.RemoteBrochure
import com.android.data.model.remote.RemotePlacement
import com.android.data.model.remote.RemotePublisher
import com.android.data.network.ApiService
import com.android.data.utils.Constants
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before

class BrochureRepositoryImplTest {
    private lateinit var brochureRepository: BrochureRepositoryImpl
    private val apiService: ApiService = mockk()
    private val brochureDao: BrochureDao = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private val localBrochure = listOf(
        LocalBrochure("1", Constants.BROCHURE_TYPE, "some image link", "3", "Lidl", 1.234),
        LocalBrochure("2", Constants.BROCHURE_PREMIUM_TYPE, "some image link", "4", "Kaufland", 6.234)
    )

    private val remoteBrochureResponse = BrochureApiResponse(
        EmbeddedBrochures(
            listOf(
                RemotePlacement(
                    Constants.BROCHURE_TYPE, RemoteBrochure(
                        id = 1,
                        brochureImage = "some image link",
                        publisher = RemotePublisher("3", "Lidl"),
                        contentType = Constants.BROCHURE_TYPE,
                        distance = 1.234
                    )
                ),
                RemotePlacement(
                    Constants.BROCHURE_PREMIUM_TYPE, RemoteBrochure(
                        id = 2,
                        brochureImage = "some image link",
                        publisher = RemotePublisher("4", "Kaufland"),
                        contentType = Constants.BROCHURE_PREMIUM_TYPE,
                        distance = 1.234
                    )
                )
            )
        )
    )

    @Before
    fun setUp() {
        brochureRepository = BrochureRepositoryImpl(brochureDao, apiService)
    }


}