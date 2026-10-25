package ru.itmo.storemini.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.itmo.storemini.di.AppModule
import ru.itmo.storemini.presentation.appdetails.AppDetailsScreen
import ru.itmo.storemini.presentation.applist.AppListScreen
import ru.itmo.storemini.presentation.applist.AppListState
import ru.itmo.storemini.presentation.applist.AppListViewModel
import kotlinx.serialization.Serializable

@Serializable
object AppListRoute

@Serializable
data class AppDetailsRoute(val appId: Int)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val listViewModel: AppListViewModel = viewModel(factory = AppModule.appListViewModelFactory)
    val listState by listViewModel.state.collectAsStateWithLifecycle()
    var lastFavoriteIds by remember { mutableStateOf(emptySet<Int>()) }
    (listState as? AppListState.Content)?.let { lastFavoriteIds = it.favoriteIds }

    NavHost(navController = navController, startDestination = AppListRoute) {
        composable<AppListRoute> {
            AppListScreen(
                viewModel = listViewModel,
                onAppClick = { navController.navigate(AppDetailsRoute(it.id)) },
            )
        }
        composable<AppDetailsRoute> { backStackEntry ->
            val appId = backStackEntry.toRoute<AppDetailsRoute>().appId
            val app = (listState as? AppListState.Content)?.apps?.find { it.id == appId }
            if (app != null) {
                AppDetailsScreen(
                    app = app,
                    isFavorite = lastFavoriteIds::contains,
                    onFavoriteClick = listViewModel::onFavoriteClick,
                    onBackClick = { navController.popBackStack() },
                    onShowcaseAppClick = {
                        navController.navigate(AppDetailsRoute(it.id))
                    },
                )
            } else {
                LaunchedEffect(Unit) { navController.popBackStack() }
            }
        }
    }
}
