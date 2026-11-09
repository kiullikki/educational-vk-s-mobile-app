package ru.itmo.storemini.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itmo.storemini.domain.applist.AppListRepository
import ru.itmo.storemini.domain.appdetails.AppItem
import ru.itmo.storemini.domain.favorites.FavoritesRepository

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val appListRepository: AppListRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<AppListState>(AppListState.Loading)
    val state = _state.asStateFlow()

    private val _events = Channel<AppListEvent>(BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadApps()
        observeFavoriteChanges()
    }

    fun loadApps() {
        viewModelScope.launch {
            val favoritesOnly = (_state.value as? AppListState.Content)?.favoritesOnly ?: false
            _state.value = AppListState.Loading
            try {
                _state.value = AppListState.Content(
                    apps = getApps(favoritesOnly),
                    favoritesOnly = favoritesOnly,
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = AppListState.Error
            }
        }
    }

    fun toggleFavorite(appId: Int) {
        viewModelScope.launch {
            try {
                favoritesRepository.toggleFavorite(appId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _events.send(AppListEvent.Error)
            }
        }
    }

    fun onLogoClick() {
        viewModelScope.launch {
            _events.send(AppListEvent.UnderDevelopment)
        }
    }

    fun onTabSelected(favoritesOnly: Boolean) {
        viewModelScope.launch {
            try {
                val apps = getApps(favoritesOnly)
                _state.update { currentState ->
                    (currentState as? AppListState.Content)
                        ?.copy(apps = apps, favoritesOnly = favoritesOnly)
                        ?: currentState
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _events.send(AppListEvent.Error)
            }
        }
    }

    fun onFavoriteClick(appId: Int) {
        toggleFavorite(appId)
    }

    private fun observeFavoriteChanges() {
        viewModelScope.launch {
            favoritesRepository.favoriteChanges.collect {
                try {
                    reloadCurrentTab()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _events.send(AppListEvent.Error)
                }
            }
        }
    }

    private suspend fun reloadCurrentTab() {
        val favoritesOnly = (_state.value as? AppListState.Content)?.favoritesOnly ?: false
        val apps = getApps(favoritesOnly)
        _state.update { (it as? AppListState.Content)?.copy(apps = apps) ?: it }
    }

    private suspend fun getApps(favoritesOnly: Boolean): List<AppItem> {
        return if (favoritesOnly) {
            favoritesRepository.getFavoriteApps()
        } else {
            appListRepository.getApps()
        }
    }
}
