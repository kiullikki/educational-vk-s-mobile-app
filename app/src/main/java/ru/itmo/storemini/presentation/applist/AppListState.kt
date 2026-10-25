package ru.itmo.storemini.presentation.applist

import androidx.compose.runtime.Immutable
import ru.itmo.storemini.domain.appdetails.AppItem

@Immutable
sealed interface AppListState {
    data object Loading : AppListState
    data object Error : AppListState

    data class Content(
        val apps: List<AppItem>,
        val favoriteIds: Set<Int>,
        val favoritesOnly: Boolean = false,
    ) : AppListState {
        val visibleApps: List<AppItem>
            get() = if (favoritesOnly) apps.filter { it.id in favoriteIds } else apps
    }
}
