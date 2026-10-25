package ru.itmo.storemini.presentation.appdetails

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itmo.storemini.presentation.applist.AppListItem
import ru.itmo.storemini.R
import ru.itmo.storemini.domain.appdetails.AppItem

private val PAGE_HEIGHT = 200.dp

@Composable
fun ShowcaseApps(
    apps: List<AppItem>,
    onFavoriteClick: (Int) -> Unit,
    onAppClick: (AppItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showcasePages = remember(apps) { apps.chunked(3) }

    Column(modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.showcase_title),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )
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
