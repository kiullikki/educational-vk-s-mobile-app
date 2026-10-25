package ru.itmo.storemini.presentation.applist

sealed interface AppListEvent {
    data object UnderDevelopment : AppListEvent
}
