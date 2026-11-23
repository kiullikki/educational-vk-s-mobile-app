package ru.itmo.storemini.domain.catalog

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.itmo.storemini.appSummary

class ApplyRatingsUseCaseTest {
    private val useCase = ApplyRatingsUseCase()

    @Test
    fun `applies ratings to apps loaded for current request test`() {
        val apps = listOf(appSummary("app-1"), appSummary("app-2"))

        val result = useCase(
            apps = apps,
            ratings = mapOf("app-1" to 3.9f, "app-2" to 4.8f),
            requestedIds = listOf("app-1", "app-2"),
            requestedFavoritesOnly = false,
            currentFavoritesOnly = false,
        )

        assertEquals(listOf(3.9f, 4.8f), result?.map { it.rating })
    }

    @Test
    fun `does not apply ratings after user switches list tab test`() {
        val apps = listOf(appSummary("app-1"), appSummary("app-2"))

        val result = useCase(
            apps = apps,
            ratings = mapOf("app-1" to 3.9f, "app-2" to 4.8f),
            requestedIds = listOf("app-1", "app-2"),
            requestedFavoritesOnly = false,
            currentFavoritesOnly = true,
        )

        assertNull(result)
    }

    @Test
    fun `does not apply ratings to changed all apps list test`() {
        val apps = listOf(appSummary("app-1"), appSummary("app-3"))

        val result = useCase(
            apps = apps,
            ratings = mapOf("app-1" to 3.9f, "app-2" to 4.8f),
            requestedIds = listOf("app-1", "app-2"),
            requestedFavoritesOnly = false,
            currentFavoritesOnly = false,
        )

        assertNull(result)
    }

    @Test
    fun `applies ratings to favorites subset after app removal test`() {
        val apps = listOf(appSummary("app-1"))

        val result = useCase(
            apps = apps,
            ratings = mapOf("app-1" to 3.9f, "app-2" to 4.8f),
            requestedIds = listOf("app-1", "app-2"),
            requestedFavoritesOnly = true,
            currentFavoritesOnly = true,
        )

        assertEquals(listOf(3.9f), result?.map { it.rating })
    }

    @Test
    fun `does not apply incomplete ratings to current list test`() {
        val apps = listOf(appSummary("app-1"), appSummary("app-3"))

        val result = useCase(
            apps = apps,
            ratings = mapOf("app-1" to 3.9f, "app-2" to 4.8f),
            requestedIds = listOf("app-1", "app-2"),
            requestedFavoritesOnly = true,
            currentFavoritesOnly = true,
        )

        assertNull(result)
    }
}
