package ru.itmo.storemini.presentation.appdetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itmo.storemini.R
import ru.itmo.storemini.domain.appdetails.AppDetails
import ru.itmo.storemini.presentation.RemoteImage

private const val FALLBACK_SCREENSHOTS_COUNT = 3

@Composable
fun ScreenshotsList(app: AppDetails, modifier: Modifier = Modifier) {
    val screenshots = app.screenshotUrls

    Column(modifier) {
        Text(
            text = stringResource(R.string.app_details_screenshots),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(Modifier.height(8.dp))
        HorizontalPager(
            state = rememberPagerState {
                screenshots.size.takeIf { it > 0 } ?: FALLBACK_SCREENSHOTS_COUNT
            },
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 8.dp,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            if (screenshots.isEmpty()) {
                FakeScreenshot(app = app, variant = page)
            } else {
                val placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceContainerHighest)
                RemoteImage(
                    imageUrl = screenshots[page],
                    contentDescription = null,
                    placeholder = placeholder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp)),
                )
            }
        }
    }
}
