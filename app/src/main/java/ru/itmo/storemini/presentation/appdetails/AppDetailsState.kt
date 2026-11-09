package ru.itmo.storemini.presentation.appdetails

import androidx.compose.runtime.Immutable
import ru.itmo.storemini.domain.appdetails.AppDetails
import ru.itmo.storemini.domain.catalog.AppSummary

@Immutable
sealed interface AppDetailsState {
    data object Loading : AppDetailsState
    data object Error : AppDetailsState
    data object NotFound : AppDetailsState

    data class Content(
        val app: AppDetails,
        val showcaseApps: List<AppSummary>,
        val showcaseError: Boolean = false,
    ) : AppDetailsState
}
