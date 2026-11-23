package ru.itmo.storemini.data

import kotlinx.coroutines.delay

class FakeStoreApi : StoreApi {
    var catalog: List<AppCatalogItemDto> = emptyList()
    var detailsById: Map<String, AppDetailsDto> = emptyMap()
    var catalogError: Throwable? = null
    var detailsError: Throwable? = null
    var catalogCalls = 0
        private set
    val requestedDetailsIds = mutableListOf<String>()

    override suspend fun getCatalog(): List<AppCatalogItemDto> {
        catalogCalls++
        catalogError?.let { throw it }
        return catalog
    }

    override suspend fun getAppDetails(id: String): AppDetailsDto {
        requestedDetailsIds += id
        detailsError?.let { throw it }
        return detailsById.getValue(id)
    }
}

class FakeRatingApi(
    private val delayMillis: Long = 0,
    private val ratings: Map<String, Float> = emptyMap(),
) : RatingApi {
    val calls = mutableListOf<String>()

    override suspend fun getRating(id: String): Float {
        calls += id
        if (delayMillis > 0) delay(delayMillis)
        return ratings[id] ?: 4.2f
    }
}
