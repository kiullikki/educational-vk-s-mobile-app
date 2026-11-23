package ru.itmo.storemini.domain.catalog

import javax.inject.Inject

class ApplyRatingsUseCase @Inject constructor() {
    operator fun invoke(
        apps: List<AppSummary>,
        ratings: Map<String, Float>,
        requestedIds: List<String>,
        requestedFavoritesOnly: Boolean,
        currentFavoritesOnly: Boolean,
    ): List<AppSummary>? {
        if (currentFavoritesOnly != requestedFavoritesOnly) return null
        if (apps.any { it.id !in ratings }) return null
        if (!requestedFavoritesOnly && apps.map { it.id } != requestedIds) return null

        return apps.map { app ->
            app.copy(rating = ratings.getValue(app.id))
        }
    }
}
