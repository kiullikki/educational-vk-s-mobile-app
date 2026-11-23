package ru.itmo.storemini.presentation.applist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itmo.storemini.domain.applist.AppListRepository
import ru.itmo.storemini.domain.catalog.AppSummary
import ru.itmo.storemini.domain.catalog.ApplyFavoriteStatusUseCase
import ru.itmo.storemini.domain.catalog.ApplyRatingsUseCase
import ru.itmo.storemini.domain.catalog.GetAppsUseCase
import ru.itmo.storemini.domain.catalog.ShouldReloadFavoritesUseCase
import ru.itmo.storemini.domain.favorites.FavoritesRepository

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val appListRepository: AppListRepository,
    private val favoritesRepository: FavoritesRepository,
    private val getAppsUseCase: GetAppsUseCase,
    private val applyFavoriteStatusUseCase: ApplyFavoriteStatusUseCase,
    private val applyRatingsUseCase: ApplyRatingsUseCase,
    private val shouldReloadFavoritesUseCase: ShouldReloadFavoritesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<AppListState>(AppListState.Loading(favoritesOnly = false))
    val state = _state.asStateFlow()

    private val _events = Channel<AppListEvent>(BUFFERED)
    val events = _events.receiveAsFlow()

    private var loadJob: Job? = null
    private var requestedFavoriteIds: Set<String> = emptySet()

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
                if (favoritesOnly) requestedFavoriteIds = favoritesRepository.favoriteIds.value
                val apps = getAppsUseCase(favoritesOnly)
                val filteredApps = applyFavoriteStatusUseCase(
                    apps = apps,
                    favoriteIds = favoritesRepository.favoriteIds.value,
                    favoritesOnly = favoritesOnly,
                )
                _state.value = AppListState.Content(
                    apps = filteredApps,
                    favoritesOnly = favoritesOnly,
                )
                applyFavoriteIds(favoritesRepository.favoriteIds.value)
                loadRatings(filteredApps, favoritesOnly)
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
                if (favoritesOnly) requestedFavoriteIds = favoritesRepository.favoriteIds.value
                val apps = getAppsUseCase(favoritesOnly)
                val filteredApps = applyFavoriteStatusUseCase(
                    apps = apps,
                    favoriteIds = favoritesRepository.favoriteIds.value,
                    favoritesOnly = favoritesOnly,
                )
                _state.value = AppListState.Content(
                    apps = filteredApps,
                    favoritesOnly = favoritesOnly,
                )
                applyFavoriteIds(favoritesRepository.favoriteIds.value)
                loadRatings(filteredApps, favoritesOnly)
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

    private suspend fun loadRatings(apps: List<AppSummary>, favoritesOnly: Boolean) {
        val ids = apps.map { it.id }
        if (ids.isEmpty()) return

        var sequentialRatings = emptyMap<String, Float>()
        val sequentialTime = measureTimeMillis {
            sequentialRatings = appListRepository.loadRatingsSequentially(ids)
        }
        coroutineContext.ensureActive()
        applyRatings(sequentialRatings, ids, favoritesOnly) { it.copy(sequentialMs = sequentialTime) }

        var parallelRatings = emptyMap<String, Float>()
        val parallelTime = measureTimeMillis {
            parallelRatings = appListRepository.loadRatingsInParallel(ids)
        }
        coroutineContext.ensureActive()
        applyRatings(parallelRatings, ids, favoritesOnly) { it.copy(parallelMs = parallelTime) }
    }

    private fun applyRatings(
        ratings: Map<String, Float>,
        ids: List<String>,
        favoritesOnly: Boolean,
        updateTimes: (RatingLoadTimes) -> RatingLoadTimes,
    ) {
        _state.update { currentState ->
            (currentState as? AppListState.Content)
                ?.let { content ->
                    applyRatingsUseCase(
                        apps = content.apps,
                        ratings = ratings,
                        requestedIds = ids,
                        requestedFavoritesOnly = favoritesOnly,
                        currentFavoritesOnly = content.favoritesOnly,
                    )?.let { ratedApps ->
                        content.copy(
                            apps = ratedApps,
                            ratingLoadTimes = updateTimes(content.ratingLoadTimes),
                        )
                    } ?: content
                }
                ?: currentState
        }
    }

    private fun applyFavoriteIds(favoriteIds: Set<String>) {
        val content = _state.value as? AppListState.Content ?: return

        if (shouldReloadFavoritesUseCase(
                apps = content.apps,
                favoriteIds = favoriteIds,
                requestedFavoriteIds = requestedFavoriteIds,
                favoritesOnly = content.favoritesOnly,
            )
        ) {
            reloadFavorites()
            return
        }

        _state.update { currentState ->
            (currentState as? AppListState.Content)
                ?.takeIf { it.favoritesOnly == content.favoritesOnly }
                ?.let { latestContent ->
                    latestContent.copy(
                        apps = applyFavoriteStatusUseCase(
                            apps = latestContent.apps,
                            favoriteIds = favoriteIds,
                            favoritesOnly = latestContent.favoritesOnly,
                        )
                    )
                }
                ?: currentState
        }
    }

    private fun reloadFavorites() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            try {
                requestedFavoriteIds = favoritesRepository.favoriteIds.value
                val apps = getAppsUseCase(favoritesOnly = true)
                val filteredApps = applyFavoriteStatusUseCase(
                    apps = apps,
                    favoriteIds = favoritesRepository.favoriteIds.value,
                    favoritesOnly = true,
                )
                _state.update { currentState ->
                    (currentState as? AppListState.Content)
                        ?.takeIf { it.favoritesOnly }
                        ?.copy(apps = filteredApps)
                        ?: currentState
                }
                loadRatings(filteredApps, favoritesOnly = true)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _events.send(AppListEvent.Error)
            }
        }
    }
}
