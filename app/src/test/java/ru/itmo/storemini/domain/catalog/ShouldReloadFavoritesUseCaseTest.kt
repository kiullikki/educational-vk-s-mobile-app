package ru.itmo.storemini.domain.catalog

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.itmo.storemini.appSummary

class ShouldReloadFavoritesUseCaseTest {
    private val useCase = ShouldReloadFavoritesUseCase()

    @Test
    fun `reloads when new favorite id appeared after previous request test`() {
        val result = useCase(
            apps = listOf(appSummary("app-1")),
            favoriteIds = setOf("app-1", "app-2"),
            requestedFavoriteIds = setOf("app-1"),
            favoritesOnly = true,
        )

        assertTrue(result)
    }

    @Test
    fun `does not reload when requested favorite is absent from catalog test`() {
        val result = useCase(
            apps = listOf(appSummary("app-1")),
            favoriteIds = setOf("app-1", "app-2"),
            requestedFavoriteIds = setOf("app-1", "app-2"),
            favoritesOnly = true,
        )

        assertFalse(result)
    }

    @Test
    fun `does not reload outside favorites tab test`() {
        val result = useCase(
            apps = listOf(appSummary("app-1")),
            favoriteIds = setOf("app-1", "app-2"),
            requestedFavoriteIds = setOf("app-1"),
            favoritesOnly = false,
        )

        assertFalse(result)
    }

    @Test
    fun `does not reload when new favorite is already in list test`() {
        val result = useCase(
            apps = listOf(appSummary("app-1"), appSummary("app-2")),
            favoriteIds = setOf("app-1", "app-2"),
            requestedFavoriteIds = setOf("app-1"),
            favoritesOnly = true,
        )

        assertFalse(result)
    }
}
