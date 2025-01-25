package com.android.data.repository

import app.cash.turbine.test
import com.android.data.local.BrochureDao
import com.android.data.model.domain.Brochure
import com.android.data.model.local.LocalBrochure
import com.android.data.model.local.toDomain
import com.android.data.model.remote.BrochureApiResponse
import com.android.data.model.remote.EmbeddedBrochures
import com.android.data.model.remote.RemoteBrochure
import com.android.data.model.remote.RemotePlacement
import com.android.data.model.remote.RemotePublisher
import com.android.data.model.remote.toDomain
import com.android.data.model.remote.toLocal
import com.android.data.network.ApiService
import com.android.data.utils.Constants
import com.android.data.utils.Result
import com.android.data.utils.Source
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BrochureRepositoryImplTest {
    private lateinit var brochureRepository: BrochureRepositoryImpl
    private val apiService: ApiService = mockk()
    private val brochureDao: BrochureDao = mockk()
    private val testDispatcher = StandardTestDispatcher()

    private val localBrochures = listOf(
        LocalBrochure("1", Constants.BROCHURE_TYPE, "some image link", "3", "Lidl", 1.234),
        LocalBrochure("2", Constants.BROCHURE_PREMIUM_TYPE, "some image link", "4", "Kaufland", 6.234)
    )

    private val remoteBrochures = listOf(
        RemotePlacement(Constants.BROCHURE_TYPE, RemoteBrochure(1, "image1.png", RemotePublisher("1", "Publisher1"), Constants.BROCHURE_TYPE, 1.2)),
        RemotePlacement(
            Constants.BROCHURE_PREMIUM_TYPE,
            RemoteBrochure(2, "image2.png", RemotePublisher("2", "Publisher2"), Constants.BROCHURE_PREMIUM_TYPE, 2.3)
        )
    )

    private val remoteApiResponse = BrochureApiResponse(EmbeddedBrochures(remoteBrochures))

    @Before
    fun setUp() {
        brochureRepository = BrochureRepositoryImpl(brochureDao, apiService)
    }

    private fun mockLocalData(localData: List<LocalBrochure> = localBrochures) {
        coEvery { brochureDao.getAllBrochures() } returns localData
    }

    private fun mockRemoteData(remoteData: BrochureApiResponse = remoteApiResponse) {
        coEvery { apiService.getBrochures() } returns remoteData
    }

    private fun mockInsertBrochures() {
        coEvery { brochureDao.insertBrochures(any()) } returns Unit
    }

    @Test
    fun `fetchBrochures emits local data followed by remote data on success`() = runTest(testDispatcher) {
        // Arrange
        mockLocalData()
        mockRemoteData()
        mockInsertBrochures()

        // Act & Assert
        brochureRepository.fetchBrochures().test {
            assertEquals(Result.Loading(Source.LOCAL), awaitItem())
            assertEquals(Result.Success(localBrochures.map { it.toDomain() }, Source.LOCAL), awaitItem())
            assertEquals(Result.Loading(Source.REMOTE), awaitItem())
            assertEquals(Result.Success(remoteBrochures.mapNotNull { it.content?.toDomain() }, Source.REMOTE), awaitItem())
            awaitComplete()
        }

        // Verify
        coVerify { brochureDao.getAllBrochures() }
        coVerify { apiService.getBrochures() }
        coVerify { brochureDao.insertBrochures(remoteBrochures.mapNotNull { it.content?.toLocal() }) }
    }

    @Test
    fun `fetchBrochures emits local data and handles remote failure`() = runTest(testDispatcher) {
        // Arrange
        val exception = RuntimeException("Network error")
        mockLocalData()
        coEvery { apiService.getBrochures() } throws exception

        // Act & Assert
        brochureRepository.fetchBrochures().test {
            assertEquals(Result.Loading(Source.LOCAL), awaitItem())
            assertEquals(Result.Success(localBrochures.map { it.toDomain() }, Source.LOCAL), awaitItem())
            assertEquals(Result.Loading(Source.REMOTE), awaitItem())
            assertEquals(Result.Failure(exception, Source.REMOTE), awaitItem())
            awaitComplete()
        }

        // Verify
        coVerify { brochureDao.getAllBrochures() }
        coVerify { apiService.getBrochures() }
    }

    @Test
    fun `fetchBrochures handles empty local data`() = runTest(testDispatcher) {
        // Arrange
        mockLocalData(emptyList())
        mockRemoteData()
        mockInsertBrochures()

        // Act & Assert
        brochureRepository.fetchBrochures().test {
            assertEquals(Result.Loading(Source.LOCAL), awaitItem())
            assertEquals(Result.Success(emptyList<Brochure>(), Source.LOCAL), awaitItem())
            assertEquals(Result.Loading(Source.REMOTE), awaitItem())
            assertEquals(Result.Success(remoteBrochures.mapNotNull { it.content?.toDomain() }, Source.REMOTE), awaitItem())
            awaitComplete()
        }

        // Verify
        coVerify { brochureDao.getAllBrochures() }
        coVerify { apiService.getBrochures() }
        coVerify { brochureDao.insertBrochures(remoteBrochures.mapNotNull { it.content?.toLocal() }) }
    }

    @Test
    fun `fetchBrochures filters brochures by valid content types`() = runTest(testDispatcher) {
        // Arrange
        val mixedRemoteBrochures = listOf(
            RemotePlacement(
                Constants.BROCHURE_TYPE,
                RemoteBrochure(1, "image1.png", RemotePublisher("1", "Publisher1"), Constants.BROCHURE_TYPE, 1.2)
            ), // Valid
            RemotePlacement(
                Constants.BROCHURE_PREMIUM_TYPE,
                RemoteBrochure(2, "image2.png", RemotePublisher("2", "Publisher2"), Constants.BROCHURE_PREMIUM_TYPE, 2.3)
            ), // Valid
            RemotePlacement("INVALID_TYPE", RemoteBrochure(3, "image3.png", RemotePublisher("3", "Publisher3"), "INVALID_TYPE", 3.4)), // Invalid
            RemotePlacement(null, RemoteBrochure(4, "image4.png", RemotePublisher("4", "Publisher4"), null, 4.5)), // Invalid
            RemotePlacement(Constants.BROCHURE_TYPE, null) // Null content, invalid
        )
        val responseWithMixedData = BrochureApiResponse(EmbeddedBrochures(mixedRemoteBrochures))
        mockLocalData(emptyList())
        mockRemoteData(responseWithMixedData)
        mockInsertBrochures()

        // Act & Assert
        brochureRepository.fetchBrochures().test {
            assertEquals(Result.Loading(Source.LOCAL), awaitItem())
            assertEquals(Result.Success(emptyList<Brochure>(), Source.LOCAL), awaitItem())
            assertEquals(Result.Loading(Source.REMOTE), awaitItem())
            assertEquals(
                Result.Success(
                    listOf(
                        RemoteBrochure(
                            1,
                            "image1.png",
                            RemotePublisher("1", "Publisher1"),
                            Constants.BROCHURE_TYPE, 1.2
                        ).toDomain(),
                        RemoteBrochure(
                            2,
                            "image2.png",
                            RemotePublisher("2", "Publisher2"),
                            Constants.BROCHURE_PREMIUM_TYPE, 2.3
                        ).toDomain()
                    ),
                    Source.REMOTE
                ),
                awaitItem()
            )
            awaitComplete()
        }

        // Verify
        coVerify { brochureDao.getAllBrochures() }
        coVerify { apiService.getBrochures() }
        coVerify { brochureDao.insertBrochures(any()) }
    }
}