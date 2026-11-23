package ru.itmo.storemini.data

import java.io.IOException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import ru.itmo.storemini.catalogItemDto

class FavoritesRepositoryImplTest {
    private val api = FakeStoreApi()
    private val favoritesDataSource = FavoritesLocalDataSource()
    private val repository = FavoritesRepositoryImpl(api, favoritesDataSource)

    @Test
    fun `getFavoriteApps does not request catalog when favorites are empty test`() = runTest {
        val result = repository.getFavoriteApps()

        assertTrue(result.isEmpty())
        assertEquals(0, api.catalogCalls)
    }

    @Test
    fun `getFavoriteApps returns only local favorite apps marked as favorite test`() = runTest {
        api.catalog = listOf(
            catalogItemDto("app-1"),
            catalogItemDto("app-2"),
            catalogItemDto("app-3"),
        )
        repository.toggleFavorite("app-1")
        repository.toggleFavorite("app-3")

        val result = repository.getFavoriteApps()

        assertEquals(listOf("app-1", "app-3"), result.map { it.id })
        assertEquals(listOf(true, true), result.map { it.isFavorite })
        assertEquals(1, api.catalogCalls)
    }

    @Test
    fun `getFavoriteApps propagates catalog loading errors test`() = runTest {
        repository.toggleFavorite("app-1")
        val expectedError = IOException("catalog is unavailable")
        api.catalogError = expectedError

        try {
            repository.getFavoriteApps()
            fail("Expected IOException")
        } catch (e: IOException) {
            assertSame(expectedError, e)
        }
    }
}
