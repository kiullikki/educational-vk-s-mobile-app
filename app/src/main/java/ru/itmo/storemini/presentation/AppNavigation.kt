package ru.itmo.storemini.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import ru.itmo.storemini.R
import ru.itmo.storemini.presentation.appdetails.AppDetailsScreen
import ru.itmo.storemini.presentation.appdetails.AppDetailsState
import ru.itmo.storemini.presentation.appdetails.AppDetailsViewModel
import ru.itmo.storemini.presentation.applist.AppListScreen
import ru.itmo.storemini.presentation.applist.AppListViewModel

@Serializable
object AppListRoute

@Serializable
data class AppDetailsRoute(val appId: String)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val listViewModel: AppListViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = AppListRoute) {
        composable<AppListRoute> {
            AppListScreen(
                viewModel = listViewModel,
                onAppClick = { navController.navigate(AppDetailsRoute(it.id)) },
            )
        }
        composable<AppDetailsRoute> {
            val viewModel: AppDetailsViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            when (val currentState = state) {
                AppDetailsState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                AppDetailsState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = stringResource(R.string.load_error))
                        Button(onClick = viewModel::loadApp) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                }

                AppDetailsState.NotFound -> {
                    LaunchedEffect(Unit) { navController.popBackStack() }
                }

                is AppDetailsState.Content -> {
                    AppDetailsScreen(
                        app = currentState.app,
                        showcaseApps = currentState.showcaseApps,
                        showcaseError = currentState.showcaseError,
                        events = viewModel.events,
                        onFavoriteClick = viewModel::toggleFavorite,
                        onBackClick = { navController.popBackStack() },
                        onShowcaseAppClick = {
                            navController.navigate(AppDetailsRoute(it.id))
                        },
                    )
                }
            }
        }
    }
}
