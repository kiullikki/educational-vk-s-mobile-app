package ru.itmo.storemini.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itmo.storemini.domain.applist.AppListRepository
import ru.itmo.storemini.domain.catalog.AppSummary
import ru.itmo.storemini.domain.favorites.FavoritesRepository

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val appListRepository: AppListRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<AppListState>(AppListState.Loading(favoritesOnly = false))
    val state = _state.asStateFlow()

    private val _events = Channel<AppListEvent>(BUFFERED)
    val events = _events.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        loadApps()
        observeFavoriteIds()
    }

    fun loadApps() {
        val favoritesOnly = _state.value.favoritesOnly
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = AppListState.Loading(favoritesOnly)
            try {
                val apps = getApps(favoritesOnly)
                _state.value = AppListState.Content(
                    apps = apps.withFavoriteStatus(favoritesOnly),
                    favoritesOnly = favoritesOnly,
                )
                applyFavoriteIds(favoritesRepository.favoriteIds.value)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = AppListState.Error(favoritesOnly)
            }
        }
    }

    fun toggleFavorite(appId: String) {
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
        if (_state.value.favoritesOnly == favoritesOnly && _state.value is AppListState.Content) return

        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = AppListState.Loading(favoritesOnly)
            try {
                val apps = getApps(favoritesOnly)
                _state.value = AppListState.Content(
                    apps = apps.withFavoriteStatus(favoritesOnly),
                    favoritesOnly = favoritesOnly,
                )
                applyFavoriteIds(favoritesRepository.favoriteIds.value)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = AppListState.Error(favoritesOnly)
            }
        }
    }

    fun onFavoriteClick(appId: String) {
        toggleFavorite(appId)
    }

    private fun observeFavoriteIds() {
        viewModelScope.launch {
            favoritesRepository.favoriteIds.collectLatest { favoriteIds ->
                try {
                    applyFavoriteIds(favoriteIds)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _events.send(AppListEvent.Error)
                }
            }
        }
    }

    private fun applyFavoriteIds(favoriteIds: Set<String>) {
        val content = _state.value as? AppListState.Content ?: return

        if (content.favoritesOnly && favoriteIds.any { id -> content.apps.none { it.id == id } }) {
            reloadFavorites()
            return
        }

        val apps = content.apps.withFavoriteStatus(content.favoritesOnly, favoriteIds)
        _state.update { currentState ->
            (currentState as? AppListState.Content)
                ?.takeIf { it.favoritesOnly == content.favoritesOnly }
                ?.copy(apps = apps)
                ?: currentState
        }
    }

    private fun reloadFavorites() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            try {
                val apps = favoritesRepository.getFavoriteApps()
                _state.update { currentState ->
                    (currentState as? AppListState.Content)
                        ?.takeIf { it.favoritesOnly }
                        ?.copy(apps = apps.withFavoriteStatus(favoritesOnly = true))
                        ?: currentState
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _events.send(AppListEvent.Error)
            }
        }
    }

    private suspend fun getApps(favoritesOnly: Boolean): List<AppSummary> {
        return if (favoritesOnly) {
            favoritesRepository.getFavoriteApps()
        } else {
            appListRepository.getApps()
        }
    }

    private fun List<AppSummary>.withFavoriteStatus(
        favoritesOnly: Boolean,
        favoriteIds: Set<String> = favoritesRepository.favoriteIds.value,
    ): List<AppSummary> {
        return map { it.copy(isFavorite = it.id in favoriteIds) }
            .filter { !favoritesOnly || it.isFavorite }
    }
}
