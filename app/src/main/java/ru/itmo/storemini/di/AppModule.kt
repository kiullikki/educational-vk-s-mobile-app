package ru.itmo.storemini.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ru.itmo.storemini.data.AppDetailsRepositoryImpl
import ru.itmo.storemini.data.AppListRepositoryImpl
import ru.itmo.storemini.data.FavoritesRepositoryImpl
import ru.itmo.storemini.domain.appdetails.AppDetailsRepository
import ru.itmo.storemini.domain.applist.AppListRepository
import ru.itmo.storemini.domain.favorites.FavoritesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindAppListRepository(impl: AppListRepositoryImpl): AppListRepository

    @Binds
    @Singleton
    abstract fun bindAppDetailsRepository(impl: AppDetailsRepositoryImpl): AppDetailsRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
}
