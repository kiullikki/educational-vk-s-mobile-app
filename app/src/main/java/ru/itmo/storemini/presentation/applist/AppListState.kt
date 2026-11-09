package ru.itmo.storemini.presentation.applist

import androidx.compose.runtime.Immutable
import ru.itmo.storemini.domain.catalog.AppSummary

@Immutable
sealed interface AppListState {
    val favoritesOnly: Boolean

    data class Loading(
        override val favoritesOnly: Boolean,
    ) : AppListState

    data class Error(
        override val favoritesOnly: Boolean,
    ) : AppListState

    data class Content(
        val apps: List<AppSummary>,
        override val favoritesOnly: Boolean,
    ) : AppListState
}
