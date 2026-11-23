package ru.itmo.storemini.domain.catalog

import javax.inject.Inject

class ShouldReloadFavoritesUseCase @Inject constructor() {
    operator fun invoke(
        apps: List<AppSummary>,
        favoriteIds: Set<String>,
        requestedFavoriteIds: Set<String>,
        favoritesOnly: Boolean,
    ): Boolean {
        return favoritesOnly && favoriteIds.any { id ->
            id !in requestedFavoriteIds && apps.none { it.id == id }
        }
    }
}
