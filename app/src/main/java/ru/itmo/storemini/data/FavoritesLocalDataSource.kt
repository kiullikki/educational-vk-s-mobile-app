package ru.itmo.storemini.data

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class FavoritesLocalDataSource @Inject constructor() {
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    fun getFavoriteIds(): Set<String> = _favoriteIds.value

    fun toggleFavorite(appId: String) {
        _favoriteIds.update { ids ->
            if (appId in ids) ids - appId else ids + appId
        }
    }
}
