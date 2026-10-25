package ru.itmo.storemini.presentation.applist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.itmo.storemini.R
import ru.itmo.storemini.data.AppRepositoryImpl
import ru.itmo.storemini.domain.appdetails.AppItem
import ru.itmo.storemini.presentation.theme.AppTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun AppListScreen(
    viewModel: AppListViewModel,
    onAppClick: (AppItem) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveEvents(viewModel.events, snackbarHostState)

    Box(Modifier.fillMaxSize().safeDrawingPadding()) {
        when (val currentState = state) {
            AppListState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            AppListState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(R.string.load_error),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Button(onClick = viewModel::loadApps) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }

            is AppListState.Content -> {
                Column(Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(horizontal = 32.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = viewModel::onLogoClick) {
                            Icon(
                                imageVector = Icons.Outlined.Storefront,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = stringResource(R.string.apps_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    PrimaryTabRow(
                        selectedTabIndex = if (currentState.favoritesOnly) 1 else 0
                    ) {
                        Tab(
                            selected = !currentState.favoritesOnly,
                            onClick = { viewModel.onTabSelected(false) },
                            text = { Text(stringResource(R.string.tab_all_apps)) }
                        )
                        Tab(
                            selected = currentState.favoritesOnly,
                            onClick = { viewModel.onTabSelected(true) },
                            text = { Text(stringResource(R.string.favorites)) }
                        )
                    }

                    if (currentState.apps.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.favorites_empty),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 24.dp),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(currentState.apps, key = { it.id }) { app ->
                                AppListItem(
                                    app = app,
                                    isFavorite = app.isFavorite,
                                    onClick = { onAppClick(app) },
                                    onFavoriteClick = { viewModel.onFavoriteClick(app.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun ObserveEvents(
    events: Flow<AppListEvent>,
    snackbarHostState: SnackbarHostState,
) {
    val underDevelopment = stringResource(R.string.under_development)
    val loadError = stringResource(R.string.load_error)
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                AppListEvent.UnderDevelopment -> snackbarHostState.showSnackbar(underDevelopment)
                AppListEvent.Error -> snackbarHostState.showSnackbar(loadError)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppListScreenPreview() {
    AppTheme {
        AppListScreen(
            viewModel = AppListViewModel(
                AppRepositoryImpl()
            ),
            onAppClick = {},
        )
    }
}
