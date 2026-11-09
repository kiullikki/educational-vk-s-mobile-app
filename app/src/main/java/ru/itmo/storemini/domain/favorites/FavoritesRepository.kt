package ru.itmo.storemini.domain.favorites

import kotlinx.coroutines.flow.Flow
import ru.itmo.storemini.domain.appdetails.AppItem

interface FavoritesRepository {
    val favoriteChanges: Flow<Unit>

    suspend fun getFavoriteApps(): List<AppItem>
    fun toggleFavorite(appId: Int)
}
