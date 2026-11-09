package ru.itmo.storemini.data

import retrofit2.http.GET
import retrofit2.http.Path

interface StoreApi {
    @GET("catalog")
    suspend fun getCatalog(): List<AppCatalogItemDto>

    @GET("catalog/{id}")
    suspend fun getAppDetails(@Path("id") id: String): AppDetailsDto
}
