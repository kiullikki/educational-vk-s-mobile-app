package ru.itmo.storemini.presentation.appdetails

import androidx.lifecycle.SavedStateHandle
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
import kotlinx.coroutines.launch
import ru.itmo.storemini.domain.appdetails.AppDetailsRepository
import ru.itmo.storemini.domain.favorites.FavoritesRepository

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val appDetailsRepository: AppDetailsRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private val appId: Int = checkNotNull(savedStateHandle["appId"])

    private val _state = MutableStateFlow<AppDetailsState>(AppDetailsState.Loading)
    val state = _state.asStateFlow()

    private val _events = Channel<AppDetailsEvent>(BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadApp()
        observeFavoriteChanges()
    }

    fun loadApp() {
        viewModelScope.launch {
            _state.value = AppDetailsState.Loading
            try {
                reloadContent()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.value = AppDetailsState.Error
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
                _events.send(AppDetailsEvent.Error)
            }
        }
    }

    private fun observeFavoriteChanges() {
        viewModelScope.launch {
            favoritesRepository.favoriteChanges.collect {
                try {
                    reloadContent()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    _events.send(AppDetailsEvent.Error)
                }
            }
        }
    }

    private suspend fun reloadContent() {
        val app = appDetailsRepository.getAppById(appId)
        _state.value = if (app == null) {
            AppDetailsState.NotFound
        } else {
            AppDetailsState.Content(
                app = app,
                showcaseApps = appDetailsRepository.getShowcaseApps(appId),
            )
        }
    }
}
