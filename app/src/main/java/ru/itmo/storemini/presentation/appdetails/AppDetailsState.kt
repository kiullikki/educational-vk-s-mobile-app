package ru.itmo.storemini.presentation.appdetails

import androidx.compose.runtime.Immutable
import ru.itmo.storemini.domain.appdetails.AppItem

@Immutable
sealed interface AppDetailsState {
    data object Loading : AppDetailsState
    data object Error : AppDetailsState
    data object NotFound : AppDetailsState

    data class Content(
        val app: AppItem,
        val showcaseApps: List<AppItem>,
    ) : AppDetailsState
}
