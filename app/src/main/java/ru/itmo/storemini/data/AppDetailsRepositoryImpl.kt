package ru.itmo.storemini.data

import javax.inject.Inject
import retrofit2.HttpException
import ru.itmo.storemini.domain.appdetails.AppDetails
import ru.itmo.storemini.domain.appdetails.AppDetailsRepository
import ru.itmo.storemini.domain.catalog.AppSummary

class AppDetailsRepositoryImpl @Inject constructor(
    private val api: StoreApi,
    private val favoritesDataSource: FavoritesLocalDataSource,
) : AppDetailsRepository {
    override suspend fun getAppById(id: String): AppDetails? {
        val details = try {
            api.getAppDetails(id)
        } catch (e: HttpException) {
            if (e.code() == 404) return null else throw e
        }

        val favoriteIds = favoritesDataSource.getFavoriteIds()
        return details.toDomain(id in favoriteIds)
    }

    override suspend fun getShowcaseApps(excludeAppId: String): List<AppSummary> {
        val catalog = api.getCatalog()
        val favoriteIds = favoritesDataSource.getFavoriteIds()
        return catalog
            .filter { it.id != excludeAppId }
            .take(9)
            .map { it.toDomain(it.id in favoriteIds) }
    }
}
