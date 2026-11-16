package ru.itmo.storemini.domain.applist

import ru.itmo.storemini.domain.catalog.AppSummary

interface AppListRepository {
    suspend fun getApps(): List<AppSummary>
    suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float>
    suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float>
}
