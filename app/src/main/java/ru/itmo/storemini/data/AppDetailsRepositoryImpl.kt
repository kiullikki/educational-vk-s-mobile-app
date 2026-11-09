package ru.itmo.storemini.data

import javax.inject.Inject
import ru.itmo.storemini.domain.appdetails.AppDetailsRepository
import ru.itmo.storemini.domain.appdetails.AppItem

class AppDetailsRepositoryImpl @Inject constructor(
    private val dataSource: AppsLocalDataSource,
) : AppDetailsRepository {
    override suspend fun getAppById(id: Int): AppItem? {
        return dataSource.getAppById(id)?.toDomain()
    }

    override suspend fun getShowcaseApps(excludeAppId: Int): List<AppItem> {
        return dataSource.getShowcaseApps(excludeAppId).map { it.toDomain() }
    }
}
