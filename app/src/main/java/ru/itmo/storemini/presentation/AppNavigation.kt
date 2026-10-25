package ru.itmo.storemini.presentation

import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import ru.itmo.storemini.R
import ru.itmo.storemini.di.AppModule
import ru.itmo.storemini.domain.appdetails.AppItem
import ru.itmo.storemini.presentation.appdetails.AppDetailsScreen
import ru.itmo.storemini.presentation.applist.AppListScreen
import ru.itmo.storemini.presentation.applist.AppListViewModel

@Serializable
object AppListRoute

@Serializable
data class AppDetailsRoute(val appId: Int)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val listViewModel: AppListViewModel = viewModel(factory = AppModule.appListViewModelFactory)

    NavHost(navController = navController, startDestination = AppListRoute) {
        composable<AppListRoute> {
            AppListScreen(
                viewModel = listViewModel,
                onAppClick = { navController.navigate(AppDetailsRoute(it.id)) },
            )
        }
        composable<AppDetailsRoute> { backStackEntry ->
            val appId = backStackEntry.toRoute<AppDetailsRoute>().appId
            var appRefresh by remember { mutableStateOf(0) }
            var showcaseRefresh by remember { mutableStateOf(0) }
            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            val appResult by produceState<Result<AppItem?>?>(null, appId, appRefresh) {
                value = runCatching { listViewModel.getAppById(appId) }
            }
            val showcaseApps by produceState<List<AppItem>>(
                emptyList(),
                appId, appRefresh, showcaseRefresh,
            ) {
                value = runCatching { listViewModel.getShowcaseApps(appId) }
                    .getOrDefault(emptyList())
            }

            val result = appResult
            when {
                result == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                result.isFailure -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(text = stringResource(R.string.load_error))
                        Button(onClick = { appRefresh++ }) {
                            Text(text = stringResource(R.string.retry))
                        }
                    }
                }

                else -> {
                    val currentApp = result.getOrNull()
                    if (currentApp == null) {
                        LaunchedEffect(Unit) { navController.popBackStack() }
                    } else {
                        AppDetailsScreen(
                            app = currentApp,
                        showcaseApps = showcaseApps,
                        onFavoriteClick = { id ->
                            scope.launch {
                                try {
                                    listViewModel.toggleFavorite(id)
                                    showcaseRefresh++
                                    appRefresh++
                                } catch (e: CancellationException) {
                                    throw e
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        R.string.load_error,
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                        },
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
}
