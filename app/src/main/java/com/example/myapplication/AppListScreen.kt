package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.AppItem
import com.example.myapplication.data.mockAllApps

@Composable
fun AppListScreen(
    isFavorite: (Int) -> Boolean,
    onFavoriteClick: (Int) -> Unit,
    onAppClick: (AppItem) -> Unit,
) {
    var favoritesOnly by rememberSaveable { mutableStateOf(false) }
    val apps = remember(favoritesOnly, isFavorite) {
        if (favoritesOnly) mockAllApps.filter { isFavorite(it.id) } else mockAllApps
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = 32.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Storefront,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.apps_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )

        }

        PrimaryTabRow(
            selectedTabIndex = if (favoritesOnly) 1 else 0
        ) {
            Tab(
                selected = !favoritesOnly,
                onClick = { favoritesOnly = false },
                text = { Text(stringResource(R.string.tab_all_apps)) }
            )
            Tab(
                selected = favoritesOnly,
                onClick = { favoritesOnly = true },
                text = { Text(stringResource(R.string.favorites)) }
            )
        }

        if (apps.isEmpty()) {
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
                items(apps, key = { it.id }) { app ->
                    AppListItem(
                        app = app,
                        isFavorite = isFavorite(app.id),
                        onClick = { onAppClick(app) },
                        onFavoriteClick = { onFavoriteClick(app.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppListScreenPreview() {
    AppTheme {
        AppListScreen(
            isFavorite = { true },
            onFavoriteClick = {},
            onAppClick = {},
        )
    }
}
