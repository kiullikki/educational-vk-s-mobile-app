package ru.itmo.storemini.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itmo.storemini.domain.appdetails.AppDetailsRepository
import ru.itmo.storemini.domain.favorites.FavoritesRepository

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appDetailsRepository: AppDetailsRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val appId: String = checkNotNull(savedStateHandle["appId"])

    private val _state = MutableStateFlow<AppDetailsState>(AppDetailsState.Loading)
    val state = _state.asStateFlow()

    private val _events = Channel<AppDetailsEvent>(BUFFERED)
    val events = _events.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        loadApp()
        observeFavoriteIds()
    }

    fun loadApp() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _state.value = AppDetailsState.Loading
            try {
                coroutineScope {
                    val appDeferred = async { appDetailsRepository.getAppById(appId) }
                    val showcaseDeferred = async { appDetailsRepository.getShowcaseApps(appId) }

                    val app = appDeferred.await()
                    if (app == null) {
                        showcaseDeferred.cancel()
                        _state.value = AppDetailsState.NotFound
                        return@coroutineScope
                    }

                    val showcaseApps = try {
                        showcaseDeferred.await()
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        null
                    }
                    _state.update {
                        val favoriteIds = favoritesRepository.favoriteIds.value
                        AppDetailsState.Content(
                            app = app.copy(isFavorite = app.id in favoriteIds),
                            showcaseApps = showcaseApps.orEmpty().map {
                                it.copy(isFavorite = it.id in favoriteIds)
                            },
                            showcaseError = showcaseApps == null,
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = AppDetailsState.Error
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
                _events.send(AppDetailsEvent.Error)
            }
        }
    }

    private fun observeFavoriteIds() {
        viewModelScope.launch {
            favoritesRepository.favoriteIds.collect(::applyFavoriteIds)
        }
    }

    private fun applyFavoriteIds(favoriteIds: Set<String>) {
        _state.update { currentState ->
            (currentState as? AppDetailsState.Content)?.let { content ->
                content.copy(
                    app = content.app.copy(isFavorite = content.app.id in favoriteIds),
                    showcaseApps = content.showcaseApps.map {
                        it.copy(isFavorite = it.id in favoriteIds)
                    },
                )
            } ?: currentState
        }
    }
}
