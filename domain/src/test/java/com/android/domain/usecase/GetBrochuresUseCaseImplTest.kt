package com.android.domain.usecase

import com.android.data.model.domain.Brochure
import com.android.data.model.domain.Publisher
import com.android.data.repository.BrochureRepository
import com.android.data.utils.Result
import com.android.domain.utils.Constants
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetBrochuresUseCaseImplTest {

    private lateinit var repository: BrochureRepository
    private lateinit var useCase: GetBrochuresUseCaseImpl

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetBrochuresUseCaseImpl(repository)
    }

    @Test
    fun `execute filters and maps brochures correctly`() = runTest {
        // Arrange
        val brochures = listOf(
            Brochure.StandardBrochure(
                id = "1",
                brochureImage = null,
                publisher = Publisher(id = "1", name = "Publisher1"),
                distance = 3.0
            ),
            Brochure.PremiumBrochure(
                id = "2",
                brochureImage = "image2.png",
                publisher = Publisher(id = "2", name = "Publisher2"),
                distance = 6.0
            )
        )
        val param = GetBrochuresUseCase.Param(
            filter = BrochureFilter(enableProximityFilter = true, maxDistance = 5.0)
        )
        coEvery { repository.fetchBrochures() } returns flowOf(Result.Success(brochures, source = com.android.data.utils.Source.REMOTE))

        // Act
        val result = useCase.execute(param)

        // Assert
        result.collect { actualResult ->
            assertEquals(Result.Success::class, actualResult::class)
            val filtered = (actualResult as Result.Success).data
            assertEquals(1, filtered.size) // Only one brochure matches the filter
            assertEquals(Constants.DEFAULT_PLACE_HOLDER, filtered[0].brochureImage) // Default placeholder applied
        }
        coVerify(exactly = 1) { repository.fetchBrochures() }
    }

    @Test
    fun `execute includes all brochures when proximity filter is disabled`() = runTest {
        // Arrange
        val brochures = listOf(
            Brochure.StandardBrochure(
                id = "1",
                brochureImage = null,
                publisher = Publisher(id = "1", name = "Publisher1"),
                distance = 10.0
            ),
            Brochure.PremiumBrochure(
                id = "2",
                brochureImage = "image2.png",
                publisher = Publisher(id = "2", name = "Publisher2"),
                distance = 15.0
            )
        )
        val param = GetBrochuresUseCase.Param(
            filter = BrochureFilter(enableProximityFilter = false)
        )
        coEvery { repository.fetchBrochures() } returns flowOf(Result.Success(brochures, source = com.android.data.utils.Source.REMOTE))

        // Act
        val result = useCase.execute(param)

        // Assert
        result.collect { actualResult ->
            assertEquals(Result.Success::class, actualResult::class)
            val filtered = (actualResult as Result.Success).data
            assertEquals(2, filtered.size) // Both brochures included
            assertEquals(Constants.DEFAULT_PLACE_HOLDER, filtered[0].brochureImage) // Default placeholder applied to the first brochure
            assertEquals("image2.png", filtered[1].brochureImage) // Second brochure retains its image
        }
        coVerify(exactly = 1) { repository.fetchBrochures() }
    }

    @Test
    fun `execute handles empty response`() = runTest {
        // Arrange
        val param = GetBrochuresUseCase.Param(
            filter = BrochureFilter(enableProximityFilter = true, maxDistance = 5.0)
        )
        coEvery { repository.fetchBrochures() } returns flowOf(Result.Success(emptyList(), source = com.android.data.utils.Source.REMOTE))

        // Act
        val result = useCase.execute(param)

        // Assert
        result.collect { actualResult ->
            assertEquals(Result.Success::class, actualResult::class)
            val filtered = (actualResult as Result.Success).data
            assertEquals(0, filtered.size) // No brochures in response
        }
        coVerify(exactly = 1) { repository.fetchBrochures() }
    }

    @Test
    fun `execute handles repository failure`() = runTest {
        // Arrange
        val param = GetBrochuresUseCase.Param(
            filter = BrochureFilter(enableProximityFilter = true, maxDistance = 5.0)
        )
        val exception = Exception("Repository error")
        coEvery { repository.fetchBrochures() } returns flowOf(Result.Failure(exception, source = com.android.data.utils.Source.REMOTE))

        // Act
        val result = useCase.execute(param)

        // Assert
        result.collect { actualResult ->
            assertEquals(Result.Failure::class, actualResult::class)
            val failure = actualResult as Result.Failure
            assertEquals(exception, failure.exception)
        }
        coVerify(exactly = 1) { repository.fetchBrochures() }
    }
}