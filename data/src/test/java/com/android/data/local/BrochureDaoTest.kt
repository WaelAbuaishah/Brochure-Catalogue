package com.android.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.android.data.model.local.LocalBrochure
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BrochureDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var brochureDao: BrochureDao

    /**
     * Sets up an in-memory Room database for testing.
     */
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        brochureDao = database.brochureDao()
    }

    /**
     * Closes the database after each test to ensure a clean slate.
     */
    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `insert and retrieve brochures`() = runBlocking {
        val brochures = listOf(
            LocalBrochure("1", "brochure", "image1.png", "1", "Publisher1", 1.2),
            LocalBrochure("2", "brochurePremium", "image2.png", "2", "Publisher2", 2.3)
        )
        brochureDao.insertBrochures(brochures)
        val retrievedBrochures = brochureDao.getAllBrochures()
        assertEquals(brochures, retrievedBrochures)
    }

    @Test
    fun `insert duplicate brochures replaces the old ones`() = runBlocking {
        val initialBrochure = LocalBrochure("1", "brochure", "image1.png", "1", "Publisher1", 1.2)
        val updatedBrochure = LocalBrochure("1", "brochurePremium", "imageUpdated.png", "1", "PublisherUpdated", 3.4)
        brochureDao.insertBrochures(listOf(initialBrochure))
        brochureDao.insertBrochures(listOf(updatedBrochure))
        val retrievedBrochures = brochureDao.getAllBrochures()
        assertEquals(listOf(updatedBrochure), retrievedBrochures)
    }

    @Test
    fun `getAllBrochures returns empty list when no brochures are present`() = runBlocking {
        val retrievedBrochures = brochureDao.getAllBrochures()
        assertEquals(emptyList<LocalBrochure>(), retrievedBrochures)
    }

    @Test
    fun `insert and retrieve large list of brochures`() = runBlocking {
        val brochures = (1..1000).map {
            LocalBrochure(it.toString(), "brochure", "image$it.png", it.toString(), "Publisher$it", it.toDouble())
        }
        brochureDao.insertBrochures(brochures)
        val retrievedBrochures = brochureDao.getAllBrochures()
        assertEquals(brochures, retrievedBrochures)
    }

    @Test
    fun `delete all brochures`() = runBlocking {
        val brochures = listOf(
            LocalBrochure("1", "brochure", "image1.png", "1", "Publisher1", 1.2),
            LocalBrochure("2", "brochurePremium", "image2.png", "2", "Publisher2", 2.3)
        )
        brochureDao.insertBrochures(brochures)
        brochureDao.deleteAllBrochures()
        val retrievedBrochures = brochureDao.getAllBrochures()
        assertEquals(emptyList<LocalBrochure>(), retrievedBrochures)
    }

    @Test
    fun `insert brochures with special characters`() = runBlocking {
        val brochures = listOf(
            LocalBrochure("1", "brochure", "image1@!#.png", "1", "Publisher1-Updated", 1.2),
            LocalBrochure("2", "brochurePremium", "image2.png", "2", "Publisher2+Special", 2.3)
        )
        brochureDao.insertBrochures(brochures)
        val retrievedBrochures = brochureDao.getAllBrochures()
        assertEquals(brochures, retrievedBrochures)
    }

    @Test
    fun `insert empty list does nothing`() = runBlocking {
        brochureDao.insertBrochures(emptyList())
        val retrievedBrochures = brochureDao.getAllBrochures()
        assertEquals(emptyList<LocalBrochure>(), retrievedBrochures)
    }

    @Test
    fun `insert brochures in batches and retrieve all`() = runBlocking {
        val batch1 = listOf(
            LocalBrochure("1", "brochure", "image1.png", "1", "Publisher1", 1.2)
        )
        val batch2 = listOf(
            LocalBrochure("2", "brochurePremium", "image2.png", "2", "Publisher2", 2.3)
        )
        brochureDao.insertBrochures(batch1)
        brochureDao.insertBrochures(batch2)

        val retrievedBrochures = brochureDao.getAllBrochures()
        assertEquals(batch1 + batch2, retrievedBrochures)
    }
}
