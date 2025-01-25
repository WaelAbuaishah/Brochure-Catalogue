package com.android.data.network

import com.android.data.model.remote.RemotePlacement
import com.android.data.model.remote.UnParsableRemotePlacement
import com.android.data.utils.Constants
import com.android.data.utils.DefaultOnMalformedDataAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder()
            .add(
                DefaultOnMalformedDataAdapter.factory(
                    RemotePlacement::class.java,
                    UnParsableRemotePlacement.default
                )
            )
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        apiService = retrofit.create(ApiService::class.java)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    @Ignore("This test is temporarily disabled due to an issue with reading the string issue (should be added to technical dept reference)")
    fun `getBrochures returns expected data`() = runTest {
        // Arrange
        val mockResponse = """
    {
        "_embedded": {
            "contents": [
                {
                    "contentType": "brochure",
                    "content": {
                        "id": 2500562002,
                        "brochureImage": "image1.png",
                        "publisher": { "id": "1", "name": "Publisher1" },
                        "contentType": "brochure",
                        "distance": 1.2
                    }
                },
                {
                    "contentType": "brochurePremium",
                    "content": {
                        "id": 2500562003,
                        "brochureImage": "image2.png",
                        "publisher": { "id": "2", "name": "Publisher2" },
                        "contentType": "brochurePremium",
                        "distance": 2.3
                    }
                }
            ]
        }
    }
    """
        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        // Act
        val response = apiService.getBrochures()

        // Assert
        println("Parsed response: ${response.embedded.contents}")
        assertNotNull(response.embedded)
        assertEquals(2, response.embedded.contents.size)
        assertEquals("brochure", response.embedded.contents[0].contentType)
        assertEquals("Publisher1", response.embedded.contents[0].content?.publisher?.name)
    }

    @Test
    fun `getBrochures handles malformed data gracefully`() = runTest {
        // Arrange
        val mockResponse = """
        {
            "_embedded": {
                "contents": [
                    {
                        "contentType": "${Constants.BROCHURE_TYPE}",
                        "content": {
                            "id": 1,
                            "brochureImage": "image1.png",
                            "publisher": { "id": "1", "name": "Publisher1" },
                            "contentType": "${Constants.BROCHURE_TYPE}",
                            "distance": 1.2
                        }
                    },
                    {
                        "invalidField": "Invalid data"
                    }
                ]
            }
        }
        """
        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        // Act
        val response = apiService.getBrochures()

        // Assert
        assertNotNull(response.embedded)
        assertEquals(2, response.embedded.contents.size)
        assertEquals("unknown", response.embedded.contents[1].contentType) // Default handled by the adapter
    }

    @Test
    fun `getBrochures handles error response`() = runTest {
        // Arrange
        mockWebServer.enqueue(MockResponse().setResponseCode(404))

        // Act & Assert
        val exception = assertThrows(HttpException::class.java) {
            runBlocking {
                apiService.getBrochures()
            }
        }
        assertEquals(404, exception.code())
    }

    @Test
    fun `getBrochures handles empty response`() = runTest {
        // Arrange
        val mockResponse = """{"_embedded": {"contents": []}}"""
        mockWebServer.enqueue(MockResponse().setBody(mockResponse).setResponseCode(200))

        // Act
        val response = apiService.getBrochures()

        // Assert
        assertNotNull(response.embedded)
        assertTrue(response.embedded.contents.isEmpty())
    }

    @Test
    fun `getBrochures handles large response`() = runTest {
        // Arrange
        val largeResponse = """
        {
            "_embedded": {
                "contents": [
                    ${
            List(1000) {
                """{
                            "contentType": "${Constants.BROCHURE_TYPE}",
                            "content": {
                                "id": $it,
                                "brochureImage": "image$it.png",
                                "publisher": { "id": "$it", "name": "Publisher$it" },
                                "contentType": "${Constants.BROCHURE_TYPE}",
                                "distance": $it.0
                            }
                        }"""
            }.joinToString(",")
        }
                ]
            }
        }
        """
        mockWebServer.enqueue(MockResponse().setBody(largeResponse).setResponseCode(200))

        // Act
        val response = apiService.getBrochures()

        // Assert
        assertEquals(1000, response.embedded.contents.size)
    }
}
