package ru.itmo.storemini.data

import javax.inject.Inject
import ru.itmo.storemini.domain.applist.AppListRepository
import ru.itmo.storemini.domain.catalog.AppSummary

class AppListRepositoryImpl @Inject constructor(
    private val api: StoreApi,
    private val favoritesDataSource: FavoritesLocalDataSource,
) : AppListRepository {
    override suspend fun getApps(): List<AppSummary> {
        val catalog = api.getCatalog()
        val favoriteIds = favoritesDataSource.getFavoriteIds()
        return catalog.map { it.toDomain(it.id in favoriteIds) }
    }
}
