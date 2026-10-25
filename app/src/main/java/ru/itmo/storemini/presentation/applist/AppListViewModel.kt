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
import ru.itmo.storemini.domain.applist.AppRepository

class AppListViewModel(
    private val repository: AppRepository,
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
                val apps = repository.getApps()
                _state.value = AppListState.Content(
                    apps = apps,
                    favoritesOnly = current?.favoritesOnly ?: false,
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = AppListState.Error
            }
        }
    }

    suspend fun getAppById(appId: Int) = repository.getAppById(appId)

    suspend fun getShowcaseApps(appId: Int) = repository.getShowcaseApps(appId)

    suspend fun toggleFavorite(appId: Int) {
        repository.toggleFavorite(appId)
        val favoritesOnly = (_state.value as? AppListState.Content)?.favoritesOnly ?: false
        val apps = if (favoritesOnly) repository.getFavoriteApps() else repository.getApps()
        _state.update { (it as? AppListState.Content)?.copy(apps = apps) ?: it }
    }

    fun onLogoClick() {
        viewModelScope.launch {
            _events.send(AppListEvent.UnderDevelopment)
        }
    }

    fun onTabSelected(favoritesOnly: Boolean) {
        viewModelScope.launch {
            try {
                val apps = if (favoritesOnly) repository.getFavoriteApps() else repository.getApps()
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
        viewModelScope.launch {
            try {
                toggleFavorite(appId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _events.send(AppListEvent.Error)
            }
        }
    }
}
