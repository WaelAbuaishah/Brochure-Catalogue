package com.android.data.model.local

import com.android.data.model.domain.Brochure
import com.android.data.model.remote.RemoteBrochure
import com.android.data.model.remote.RemotePublisher
import com.android.data.model.remote.toDomain
import com.android.data.model.remote.toLocal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class LocalMappersTest {
    @Test
    fun `toDomain maps LocalBrochure to StandardBrochure`() {
        // Arrange
        val localBrochure = LocalBrochure(
            id = "1",
            contentType = "brochure",
            brochureImage = "image1.png",
            publisherId = "1",
            publisherName = "Publisher1",
            distance = 1.2
        )

        // Act
        val domainBrochure = localBrochure.toDomain()

        // Assert
        assertEquals(Brochure.StandardBrochure::class, domainBrochure::class)
        assertEquals("1", domainBrochure.id)
        assertEquals("image1.png", domainBrochure.brochureImage)
        assertEquals("Publisher1", domainBrochure.publisher.name)
        assertEquals(1.2, domainBrochure.distance ?: 0.0, 0.0)
    }

    @Test
    fun `toDomain maps LocalBrochure to PremiumBrochure`() {
        // Arrange
        val localBrochure = LocalBrochure(
            id = "2",
            contentType = "brochurePremium",
            brochureImage = "image2.png",
            publisherId = "2",
            publisherName = "Publisher2",
            distance = 2.3
        )

        // Act
        val domainBrochure = localBrochure.toDomain()

        // Assert
        assertEquals(Brochure.PremiumBrochure::class, domainBrochure::class)
        assertEquals("2", domainBrochure.id)
        assertEquals("image2.png", domainBrochure.brochureImage)
        assertEquals("Publisher2", domainBrochure.publisher.name)
        assertEquals(2.3, domainBrochure.distance ?: 0.0, 0.0)
    }

    @Test
    fun `toDomain throws exception for unknown contentType`() {
        // Arrange
        val localBrochure = LocalBrochure(
            id = "3",
            contentType = "unknownType",
            brochureImage = "image3.png",
            publisherId = "3",
            publisherName = "Publisher3",
            distance = 3.4
        )

        // Act & Assert
        assertThrows(IllegalArgumentException::class.java) {
            localBrochure.toDomain()
        }
    }
}

class RemoteBrochureMapperTest {

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
}