package ru.itmo.storemini.presentation.applist

import androidx.compose.runtime.Immutable
import ru.itmo.storemini.domain.appdetails.AppItem

@Immutable
sealed interface AppListState {
    data object Loading : AppListState
    data object Error : AppListState

    data class Content(
        val apps: List<AppItem>,
        val favoritesOnly: Boolean = false,
    ) : AppListState
}
