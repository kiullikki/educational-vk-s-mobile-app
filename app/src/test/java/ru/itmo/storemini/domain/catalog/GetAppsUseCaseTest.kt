package ru.itmo.storemini.domain.catalog

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.itmo.storemini.appSummary
import ru.itmo.storemini.domain.applist.AppListRepository
import ru.itmo.storemini.domain.favorites.FavoritesRepository

class GetAppsUseCaseTest {

    @Test
    fun `all apps mode loads apps from catalog repository test`() = runTest {
        val appListRepository = FakeAppListRepository(
            apps = listOf(appSummary("app-1"), appSummary("app-2")),
        )
        val favoritesRepository = FakeFavoritesRepository()
        val useCase = GetAppsUseCase(appListRepository, favoritesRepository)

        val result = useCase(favoritesOnly = false)

        assertEquals(listOf("app-1", "app-2"), result.map { it.id })
        assertEquals(1, appListRepository.getAppsCalls)
        assertEquals(0, favoritesRepository.getFavoriteAppsCalls)
    }

    @Test
    fun `favorites mode loads apps only from favorites repository test`() = runTest {
        val appListRepository = FakeAppListRepository(apps = listOf(appSummary("app-1")))
        val favoritesRepository = FakeFavoritesRepository(
            apps = listOf(appSummary("app-2", isFavorite = true)),
        )
        val useCase = GetAppsUseCase(appListRepository, favoritesRepository)

        val result = useCase(favoritesOnly = true)

        assertEquals(listOf("app-2"), result.map { it.id })
        assertEquals(0, appListRepository.getAppsCalls)
        assertEquals(1, favoritesRepository.getFavoriteAppsCalls)
    }

    private class FakeAppListRepository(
        private val apps: List<AppSummary>,
    ) : AppListRepository {
        var getAppsCalls = 0
            private set

        override suspend fun getApps(): List<AppSummary> {
            getAppsCalls++
            return apps
        }

        override suspend fun loadRatingsSequentially(ids: List<String>): Map<String, Float> = emptyMap()
        override suspend fun loadRatingsInParallel(ids: List<String>): Map<String, Float> = emptyMap()
    }

    private class FakeFavoritesRepository(
        private val apps: List<AppSummary> = emptyList(),
    ) : FavoritesRepository {
        var getFavoriteAppsCalls = 0
            private set
        override val favoriteIds: StateFlow<Set<String>> = MutableStateFlow(emptySet())

        override suspend fun getFavoriteApps(): List<AppSummary> {
            getFavoriteAppsCalls++
            return apps
        }

        override fun toggleFavorite(appId: String) = Unit
    }
}
