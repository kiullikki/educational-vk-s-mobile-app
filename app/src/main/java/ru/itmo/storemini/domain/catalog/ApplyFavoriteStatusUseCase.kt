package ru.itmo.storemini.domain.catalog

import javax.inject.Inject

class ApplyFavoriteStatusUseCase @Inject constructor() {
    operator fun invoke(
        apps: List<AppSummary>,
        favoriteIds: Set<String>,
        favoritesOnly: Boolean,
    ): List<AppSummary> {
        return apps
            .map { it.copy(isFavorite = it.id in favoriteIds) }
            .filter { !favoritesOnly || it.isFavorite }
    }
}
