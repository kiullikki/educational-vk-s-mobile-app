package ru.itmo.storemini.domain.applist

import ru.itmo.storemini.domain.appdetails.AppItem

interface AppRepository {
    suspend fun getApps(): List<AppItem>
    suspend fun getFavoriteApps(): List<AppItem>
    suspend fun getShowcaseApps(excludeAppId: Int): List<AppItem>
    suspend fun getAppById(id: Int): AppItem?
    suspend fun toggleFavorite(appId: Int)
}
