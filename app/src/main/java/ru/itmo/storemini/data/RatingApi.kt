package ru.itmo.storemini.data

import javax.inject.Inject
import kotlinx.coroutines.delay

interface RatingApi {
    suspend fun getRating(id: String): Float
}

class RatingApiImpl @Inject constructor() : RatingApi {
    override suspend fun getRating(id: String): Float {
        delay(1500)
        return (30..50).random() / 10f
    }
}
