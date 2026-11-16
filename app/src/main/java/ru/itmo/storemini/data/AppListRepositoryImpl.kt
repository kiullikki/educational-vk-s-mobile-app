package ru.itmo.storemini.data

import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import ru.itmo.storemini.domain.applist.AppListRepository
import ru.itmo.storemini.domain.catalog.AppSummary

class AppListRepositoryImpl @Inject constructor(
    private val api: StoreApi,
    private val ratingApi: RatingApi,
    private val favoritesDataSource: FavoritesLocalDataSource,
) : AppListRepository {
    override suspend fun getApps(): List<AppSummary> {
        val catalog = api.getCatalog()
        val favoriteIds = favoritesDataSource.getFavoriteIds()
        return catalog.map { it.toDomain(it.id in favoriteIds) }
    }

    override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> {
        return buildMap {
            ids.forEach { id ->
                put(id, ratingApi.getRating(id))
            }
        }
    }

    override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = coroutineScope {
        ids.map { id ->
            async { id to ratingApi.getRating(id) }
        }.awaitAll().toMap()
    }
}
