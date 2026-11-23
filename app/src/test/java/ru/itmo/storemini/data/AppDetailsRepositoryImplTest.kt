package ru.itmo.storemini.data

import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import ru.itmo.storemini.appDetailsDto
import ru.itmo.storemini.catalogItemDto

class AppDetailsRepositoryImplTest {
    private val api = FakeStoreApi()
    private val favoritesDataSource = FavoritesLocalDataSource()
    private val repository = AppDetailsRepositoryImpl(api, favoritesDataSource)

    @Test
    fun `getAppById maps details and applies favorite status test`() = runTest {
        api.detailsById = mapOf("app-1" to appDetailsDto("app-1"))
        favoritesDataSource.toggleFavorite("app-1")

        val result = repository.getAppById("app-1")

        assertEquals("app-1", result?.id)
        assertEquals("Developer app-1", result?.developer)
        assertEquals(42.5f, result?.sizeMb)
        assertEquals(true, result?.isFavorite)
        assertEquals(listOf("app-1"), api.requestedDetailsIds)
    }

    @Test
    fun `getAppById returns null for missing app test`() = runTest {
        api.detailsError = httpException(404)

        val result = repository.getAppById("missing")

        assertNull(result)
    }

    @Test
    fun `getAppById propagates non 404 http errors test`() = runTest {
        api.detailsError = httpException(500)

        try {
            repository.getAppById("app-1")
            fail("Expected HttpException")
        } catch (e: HttpException) {
            assertEquals(500, e.code())
        }
    }

    @Test
    fun `getShowcaseApps excludes current app and marks favorites test`() = runTest {
        api.catalog = listOf(
            catalogItemDto("current"),
            catalogItemDto("app-1"),
            catalogItemDto("app-2"),
        )
        favoritesDataSource.toggleFavorite("app-2")

        val result = repository.getShowcaseApps(excludeAppId = "current")

        assertEquals(listOf("app-1", "app-2"), result.map { it.id })
        assertEquals(listOf(false, true), result.map { it.isFavorite })
    }

    @Test
    fun `getShowcaseApps returns at most nine apps test`() = runTest {
        api.catalog = (0..15).map { catalogItemDto("app-$it") }

        val result = repository.getShowcaseApps(excludeAppId = "missing")

        assertEquals(9, result.size)
        assertEquals((0..8).map { "app-$it" }, result.map { it.id })
    }

    private fun httpException(code: Int): HttpException {
        val response = Response.error<AppDetailsDto>(
            code,
            "error".toResponseBody("application/json".toMediaType()),
        )
        return HttpException(response)
    }
}
