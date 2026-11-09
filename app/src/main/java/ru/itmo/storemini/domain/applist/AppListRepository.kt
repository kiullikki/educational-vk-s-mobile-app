package ru.itmo.storemini.domain.applist

import ru.itmo.storemini.domain.appdetails.AppItem

interface AppListRepository {
    suspend fun getApps(): List<AppItem>
}
