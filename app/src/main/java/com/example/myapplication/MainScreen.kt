package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myapplication.appdetails.AppDetailsScreen
import com.example.myapplication.data.mockAllApps
import com.example.myapplication.data.mockFavoriteApps
import kotlinx.serialization.Serializable

@Serializable
object AppListRoute

@Serializable
data class AppDetailsRoute(val appId: Int)

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    var favoriteIds by rememberSaveable {
        mutableStateOf(mockFavoriteApps.map { it.id }.toSet())
    }
    val toggleFavorite = remember {
        { id: Int ->
            favoriteIds = if (id in favoriteIds) favoriteIds - id else favoriteIds + id
        }
    }

    NavHost(navController = navController, startDestination = AppListRoute) {
        composable<AppListRoute> {
            AppListScreen(
                isFavorite = favoriteIds::contains,
                onFavoriteClick = toggleFavorite,
                onAppClick = { navController.navigate(AppDetailsRoute(it.id)) },
            )
        }
        composable<AppDetailsRoute> { backStackEntry ->
            val appId = backStackEntry.toRoute<AppDetailsRoute>().appId
            val app = mockAllApps.find { it.id == appId }
            if (app != null) {
                AppDetailsScreen(
                    app = app,
                    isFavorite = favoriteIds::contains,
                    onFavoriteClick = toggleFavorite,
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
