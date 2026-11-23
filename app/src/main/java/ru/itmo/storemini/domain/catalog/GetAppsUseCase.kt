package ru.itmo.storemini.domain.catalog

import javax.inject.Inject
import ru.itmo.storemini.domain.applist.AppListRepository
import ru.itmo.storemini.domain.favorites.FavoritesRepository

class GetAppsUseCase @Inject constructor(
    private val appListRepository: AppListRepository,
    private val favoritesRepository: FavoritesRepository,
) {
    suspend operator fun invoke(favoritesOnly: Boolean): List<AppSummary> {
        return if (favoritesOnly) {
            favoritesRepository.getFavoriteApps()
        } else {
            appListRepository.getApps()
        }
    }
}
