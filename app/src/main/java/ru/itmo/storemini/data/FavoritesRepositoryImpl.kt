package ru.itmo.storemini.data

import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import ru.itmo.storemini.domain.catalog.AppSummary
import ru.itmo.storemini.domain.favorites.FavoritesRepository

class FavoritesRepositoryImpl @Inject constructor(
    private val api: StoreApi,
    private val dataSource: FavoritesLocalDataSource,
) : FavoritesRepository {
    override val favoriteIds: StateFlow<Set<String>> = dataSource.favoriteIds

    override suspend fun getFavoriteApps(): List<AppSummary> {
        if (dataSource.getFavoriteIds().isEmpty()) return emptyList()

        val catalog = api.getCatalog()
        val favoriteIds = dataSource.getFavoriteIds()
        return catalog
            .filter { it.id in favoriteIds }
            .map { it.toDomain(isFavorite = true) }
    }

    override fun toggleFavorite(appId: String) {
        dataSource.toggleFavorite(appId)
    }
}
