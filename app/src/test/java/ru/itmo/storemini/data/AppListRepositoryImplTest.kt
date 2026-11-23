package ru.itmo.storemini.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.itmo.storemini.catalogItemDto

@OptIn(ExperimentalCoroutinesApi::class)
class AppListRepositoryImplTest {
    private val api = FakeStoreApi()
    private val favoritesDataSource = FavoritesLocalDataSource()

    @Test
    fun `getApps returns catalog mapped to domain and applies favorite ids test`() = runTest {
        api.catalog = listOf(catalogItemDto("app-1"), catalogItemDto("app-2"))
        favoritesDataSource.toggleFavorite("app-2")
        val repository = AppListRepositoryImpl(api, FakeRatingApi(), favoritesDataSource)

        val result = repository.getApps()

        assertEquals(listOf("app-1", "app-2"), result.map { it.id })
        assertEquals(listOf(false, true), result.map { it.isFavorite })
        assertEquals("App app-1", result.first().name)
        assertEquals(1, api.catalogCalls)
    }

    @Test
    fun `loadRatingsSequentially requests ratings in input order test`() = runTest {
        val ratingApi = FakeRatingApi(
            delayMillis = 1000,
            ratings = mapOf("app-1" to 4.1f, "app-2" to 4.2f, "app-3" to 4.3f),
        )
        val repository = AppListRepositoryImpl(api, ratingApi, favoritesDataSource)

        val result = repository.loadRatingsSequentially(listOf("app-2", "app-1", "app-3"))

        assertEquals(listOf("app-2", "app-1", "app-3"), ratingApi.calls)
        assertEquals(
            mapOf("app-2" to 4.2f, "app-1" to 4.1f, "app-3" to 4.3f),
            result,
        )
        assertEquals(3000, testScheduler.currentTime)
    }

    @Test
    fun `loadRatingsInParallel starts all requests and returns ratings by id test`() = runTest {
        val ratingApi = FakeRatingApi(
            delayMillis = 1000,
            ratings = mapOf("app-1" to 4.1f, "app-2" to 4.2f, "app-3" to 4.3f),
        )
        val repository = AppListRepositoryImpl(api, ratingApi, favoritesDataSource)

        val result = repository.loadRatingsInParallel(listOf("app-1", "app-2", "app-3"))

        assertEquals(setOf("app-1", "app-2", "app-3"), ratingApi.calls.toSet())
        assertEquals(
            mapOf("app-1" to 4.1f, "app-2" to 4.2f, "app-3" to 4.3f),
            result,
        )
        assertEquals(1000, testScheduler.currentTime)
    }
}
