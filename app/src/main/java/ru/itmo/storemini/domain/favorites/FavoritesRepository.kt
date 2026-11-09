package ru.itmo.storemini.domain.favorites

import kotlinx.coroutines.flow.StateFlow
import ru.itmo.storemini.domain.catalog.AppSummary

interface FavoritesRepository {
    val favoriteIds: StateFlow<Set<String>>

    suspend fun getFavoriteApps(): List<AppSummary>
    fun toggleFavorite(appId: String)
}
