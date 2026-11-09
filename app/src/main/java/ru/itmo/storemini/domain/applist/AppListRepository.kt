package ru.itmo.storemini.domain.applist

import ru.itmo.storemini.domain.catalog.AppSummary

interface AppListRepository {
    suspend fun getApps(): List<AppSummary>
}
