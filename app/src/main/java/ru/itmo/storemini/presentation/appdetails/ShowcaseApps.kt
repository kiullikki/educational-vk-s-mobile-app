package ru.itmo.storemini.presentation.appdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itmo.storemini.R
import ru.itmo.storemini.domain.catalog.AppSummary
import ru.itmo.storemini.presentation.applist.AppListItem

private val PAGE_HEIGHT = 200.dp

@Composable
fun ShowcaseApps(
    apps: List<AppSummary>,
    hasError: Boolean,
    onFavoriteClick: (String) -> Unit,
    onAppClick: (AppSummary) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showcasePages = remember(apps) { apps.chunked(3) }

    Column(modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.showcase_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
        if (hasError) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PAGE_HEIGHT),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.showcase_error),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            HorizontalPager(
                state = rememberPagerState { showcasePages.size },
                modifier = Modifier.height(PAGE_HEIGHT),
            ) { page ->
                Column {
                    showcasePages[page].forEach { item ->
                        AppListItem(
                            app = item,
                            isFavorite = item.isFavorite,
                            onClick = { onAppClick(item) },
                            onFavoriteClick = { onFavoriteClick(item.id) },
                        )
                    }
                }
            }
        }
    }
}
