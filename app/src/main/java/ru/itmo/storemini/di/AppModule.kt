package ru.itmo.storemini.di

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ru.itmo.storemini.data.AppRepositoryImpl
import ru.itmo.storemini.presentation.applist.AppListViewModel

object AppModule {
    val appListViewModelFactory = viewModelFactory {
        initializer {
            AppListViewModel(repository = AppRepositoryImpl())
        }
    }
}
