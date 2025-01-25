package com.android.brochurecatalogue

import app.cash.turbine.test
import com.android.brochurecatalogue.brochureslist.BrochureViewModel
import com.android.data.model.domain.Brochure
import com.android.data.model.domain.Publisher
import com.android.data.utils.Result
import com.android.data.utils.Source
import com.android.domain.usecase.GetBrochuresUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BrochureViewModelTest {

    private lateinit var getBrochuresUseCase: GetBrochuresUseCase
    private val viewModel: BrochureViewModel by lazy { BrochureViewModel(getBrochuresUseCase) }
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getBrochuresUseCase = mockk()
        coEvery { getBrochuresUseCase.execute(any()) } returns flow {
            emit(Result.Success(emptyList(), Source.REMOTE))
        }
    }

    @Test
    fun `loadBrochures updates uiState with loading and success gradually`() = runTest {
        // Arrange
        val brochuresFlow = MutableSharedFlow<Result<List<Brochure>>>(replay = 1)
        coEvery { getBrochuresUseCase.execute(any()) } returns brochuresFlow

        val mockBrochures = listOf(
            Brochure.StandardBrochure("1", "image1.png", Publisher("1", "Publisher1"), 3.0),
            Brochure.PremiumBrochure("2", "image2.png", Publisher("2", "Publisher2"), 6.0)
        )

        // Act
        viewModel.loadBrochures()

        // Assert
        viewModel.uiState.test {
            brochuresFlow.emit(Result.Loading(Source.REMOTE))
            testScheduler.advanceUntilIdle()

            val loadingState = expectMostRecentItem()
            assertEquals(true, loadingState.isLoading)

            brochuresFlow.emit(Result.Success(mockBrochures, Source.REMOTE))

            val successState = awaitItem()
            assertEquals(false, successState.isLoading)
            assertEquals(mockBrochures, successState.brochures)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadBrochures updates uiState with failure`() = runTest(testDispatcher) {
        // Arrange
        val exceptionMessage = "Failed to load brochures"
        coEvery { getBrochuresUseCase.execute(any()) } returns flow {
            emit(Result.Failure(Exception(exceptionMessage), Source.REMOTE))
        }

        // Act
        viewModel.loadBrochures()

        // Assert
        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()

            val failureState = expectMostRecentItem()
            assertEquals(false, failureState.isLoading)
            assertEquals(exceptionMessage, failureState.errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateFilter reloads brochures with updated filter`() = runTest(testDispatcher) {
        // Arrange
        val mockBrochures = listOf(
            Brochure.StandardBrochure("1", "image1.png", Publisher("1", "Publisher1"), 3.0)
        )
        coEvery { getBrochuresUseCase.execute(any()) } returns flow {
            emit(Result.Success(mockBrochures, Source.REMOTE))
        }

        // Act
        viewModel.updateFilter(enableProximity = true, maxDistance = 3.0)

        // Assert
        viewModel.uiState.test {
            testScheduler.advanceUntilIdle()
            val updatedState = expectMostRecentItem()
            assertEquals(true, updatedState.filter.enableProximityFilter)
            assertEquals(3.0, updatedState.filter.maxDistance, 0.0)
            assertEquals(mockBrochures, updatedState.brochures)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateColumnsBasedOnOrientation updates column count`() = runTest(testDispatcher) {
        viewModel.updateColumnsBasedOnOrientation(android.content.res.Configuration.ORIENTATION_LANDSCAPE)
        viewModel.uiState.test {
            val landscapeState = awaitItem()
            assertEquals(3, landscapeState.numberOfColumns)

            viewModel.updateColumnsBasedOnOrientation(android.content.res.Configuration.ORIENTATION_PORTRAIT)

            val portraitState = awaitItem()
            assertEquals(2, portraitState.numberOfColumns)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
