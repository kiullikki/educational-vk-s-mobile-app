package ru.itmo.storemini.domain.catalog

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.itmo.storemini.appSummary

class ApplyFavoriteStatusUseCaseTest {
    private val useCase = ApplyFavoriteStatusUseCase()

    @Test
    fun `marks apps by favorite ids and keeps catalog order test`() {
        val apps = listOf(appSummary("app-1"), appSummary("app-2"), appSummary("app-3"))

        val result = useCase(
            apps = apps,
            favoriteIds = setOf("app-3", "app-1"),
            favoritesOnly = false,
        )

        assertEquals(listOf("app-1", "app-2", "app-3"), result.map { it.id })
        assertEquals(listOf(true, false, true), result.map { it.isFavorite })
    }

    @Test
    fun `favorites only mode filters non favorite apps test`() {
        val apps = listOf(appSummary("app-1"), appSummary("app-2"), appSummary("app-3"))

        val result = useCase(
            apps = apps,
            favoriteIds = setOf("app-2"),
            favoritesOnly = true,
        )

        assertEquals(listOf(appSummary("app-2", isFavorite = true)), result)
    }
}
