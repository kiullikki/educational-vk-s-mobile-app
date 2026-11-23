package ru.itmo.storemini.data

import org.junit.Assert.assertEquals
import org.junit.Test

class FavoritesLocalDataSourceTest {

    @Test
    fun `toggleFavorite removes selected id and keeps other favorites test`() {
        val dataSource = FavoritesLocalDataSource()
        dataSource.toggleFavorite("app-1")
        dataSource.toggleFavorite("app-2")

        dataSource.toggleFavorite("app-1")

        assertEquals(setOf("app-2"), dataSource.favoriteIds.value)
    }
}
