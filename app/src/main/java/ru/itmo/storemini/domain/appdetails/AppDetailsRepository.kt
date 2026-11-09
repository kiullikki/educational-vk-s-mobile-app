package ru.itmo.storemini.domain.appdetails

interface AppDetailsRepository {
    suspend fun getAppById(id: Int): AppItem?
    suspend fun getShowcaseApps(excludeAppId: Int): List<AppItem>
}
