package ru.itmo.storemini.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itmo.storemini.domain.appdetails.AppApi

// TODO: заменить AppApi на AppRepository, когда появятся сеть/БД/маппинг DTO
class AppListViewModel(
    private val api: AppApi,
) : ViewModel() {

    private val _state = MutableStateFlow<AppListState>(AppListState.Loading)
    val state = _state.asStateFlow()

    private val _events = Channel<AppListEvent>(BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadApps()
    }

    fun loadApps() {
        viewModelScope.launch {
            val current = _state.value as? AppListState.Content
            _state.value = AppListState.Loading
            try {
                val apps = api.getApps()
                _state.value = AppListState.Content(
                    apps = apps,
                    favoriteIds = current?.favoriteIds
                        ?: apps.filter { it.isFavorite }.map { it.id }.toSet(),
                    favoritesOnly = current?.favoritesOnly ?: false,
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = AppListState.Error
            }
        }
    }

    fun onLogoClick() {
        viewModelScope.launch {
            _events.send(AppListEvent.UnderDevelopment)
        }
    }

    fun onTabSelected(favoritesOnly: Boolean) {
        _state.update { currentState ->
            if (currentState is AppListState.Content) {
                currentState.copy(favoritesOnly = favoritesOnly)
            } else {
                currentState
            }
        }
    }

    fun onFavoriteClick(appId: Int) {
        _state.update { currentState ->
            if (currentState is AppListState.Content) {
                val favoriteIds = currentState.favoriteIds
                currentState.copy(
                    favoriteIds = if (appId in favoriteIds) favoriteIds - appId
                    else favoriteIds + appId
                )
            } else {
                currentState
            }
        }
    }
}
