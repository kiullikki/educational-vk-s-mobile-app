package ru.itmo.storemini.domain.appdetails

import ru.itmo.storemini.domain.catalog.AppSummary

interface AppDetailsRepository {
    suspend fun getAppById(id: String): AppDetails?
    suspend fun getShowcaseApps(excludeAppId: String): List<AppSummary>
}
