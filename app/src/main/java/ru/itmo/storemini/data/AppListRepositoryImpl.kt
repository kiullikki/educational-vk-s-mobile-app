package ru.itmo.storemini.data

import javax.inject.Inject
import ru.itmo.storemini.domain.appdetails.AppItem
import ru.itmo.storemini.domain.applist.AppListRepository

class AppListRepositoryImpl @Inject constructor(
    private val dataSource: AppsLocalDataSource,
) : AppListRepository {
    override suspend fun getApps(): List<AppItem> {
        return dataSource.getApps().map { it.toDomain() }
    }
}
