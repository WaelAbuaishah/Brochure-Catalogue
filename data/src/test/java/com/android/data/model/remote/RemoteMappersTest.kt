package com.android.data.model.remote

import com.android.data.model.domain.Brochure
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteMappersTest {

    @Test
    fun `toDomain maps RemoteBrochure to StandardBrochure`() {
        // Arrange
        val remoteBrochure = RemoteBrochure(
            id = 1,
            brochureImage = "image1.png",
            publisher = RemotePublisher(id = "1", name = "Publisher1"),
            contentType = "brochure",
            distance = 1.2
        )

        // Act
        val domainBrochure = remoteBrochure.toDomain()

        // Assert
        assertEquals(Brochure.StandardBrochure::class, domainBrochure::class)
        assertEquals("1", domainBrochure.id)
        assertEquals("image1.png", domainBrochure.brochureImage)
        assertEquals("Publisher1", domainBrochure.publisher.name)
        assertEquals(1.2, domainBrochure.distance ?: 0.0, 0.0)
    }

    @Test
    fun `toDomain maps RemoteBrochure to PremiumBrochure`() {
        // Arrange
        val remoteBrochure = RemoteBrochure(
            id = 2,
            brochureImage = "image2.png",
            publisher = RemotePublisher(id = "2", name = "Publisher2"),
            contentType = "brochurePremium",
            distance = 2.3
        )

        // Act
        val domainBrochure = remoteBrochure.toDomain()

        // Assert
        assertEquals(Brochure.PremiumBrochure::class, domainBrochure::class)
        assertEquals("2", domainBrochure.id)
        assertEquals("image2.png", domainBrochure.brochureImage)
        assertEquals("Publisher2", domainBrochure.publisher.name)
        assertEquals(2.3, domainBrochure.distance ?: 0.0, 0.0)
    }

    @Test
    fun `toLocal maps RemoteBrochure to LocalBrochure`() {
        // Arrange
        val remoteBrochure = RemoteBrochure(
            id = 1,
            brochureImage = "image1.png",
            publisher = RemotePublisher(id = "1", name = "Publisher1"),
            contentType = "brochure",
            distance = 1.2
        )

        // Act
        val localBrochure = remoteBrochure.toLocal()

        // Assert
        assertEquals("1", localBrochure.id)
        assertEquals("brochure", localBrochure.contentType)
        assertEquals("image1.png", localBrochure.brochureImage)
        assertEquals("1", localBrochure.publisherId)
        assertEquals("Publisher1", localBrochure.publisherName)
        assertEquals(1.2, localBrochure.distance ?: 0.0, 0.0)
    }

    @Test
    fun `toLocal maps RemoteBrochure with null fields`() {
        // Arrange
        val remoteBrochure = RemoteBrochure(
            id = 1,
            brochureImage = null,
            publisher = RemotePublisher(id = "1", name = null),
            contentType = "brochure",
            distance = 1.0
        )

        // Act
        val localBrochure = remoteBrochure.toLocal()

        // Assert
        assertEquals("1", localBrochure.id)
        assertEquals("brochure", localBrochure.contentType)
        assertEquals(null, localBrochure.brochureImage)
        assertEquals("1", localBrochure.publisherId)
        assertEquals(null, localBrochure.publisherName)
        assertEquals(1.0, localBrochure.distance ?: 0.0, 0.0) // Default distance is 0.0
    }
}
