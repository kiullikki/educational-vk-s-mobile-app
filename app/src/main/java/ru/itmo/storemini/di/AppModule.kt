package ru.itmo.storemini.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.itmo.storemini.data.appdetails.FakeAppApi
import ru.itmo.storemini.presentation.applist.AppListViewModel

object AppModule {
    val appListViewModelFactory = viewModelFactory {
        initializer {
            // TODO: заменить FakeAppApi на репозиторий (сеть + Room-кэш + маппер DTO→AppItem)
            AppListViewModel(api = FakeAppApi())
        }
    }
}
