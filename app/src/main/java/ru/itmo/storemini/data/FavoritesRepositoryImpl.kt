package ru.itmo.storemini.data

import javax.inject.Inject
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.itmo.storemini.domain.appdetails.AppItem
import ru.itmo.storemini.domain.favorites.FavoritesRepository

class FavoritesRepositoryImpl @Inject constructor(
    private val dataSource: AppsLocalDataSource,
) : FavoritesRepository {
    private val _favoriteChanges = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val favoriteChanges: Flow<Unit> = _favoriteChanges.asSharedFlow()

    override suspend fun getFavoriteApps(): List<AppItem> {
        return dataSource.getFavoriteApps().map { it.toDomain() }
    }

    override fun toggleFavorite(appId: Int) {
        dataSource.toggleFavorite(appId)
        _favoriteChanges.tryEmit(Unit)
    }
}
