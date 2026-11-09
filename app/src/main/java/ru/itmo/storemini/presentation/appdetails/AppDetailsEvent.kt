package ru.itmo.storemini.presentation.appdetails

sealed interface AppDetailsEvent {
    data object Error : AppDetailsEvent
}
