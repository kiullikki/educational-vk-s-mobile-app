package ru.itmo.storemini.domain.appdetails

interface AppApi {
    suspend fun getApps(): List<AppItem>
}
