package com.example.myapplication.appdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.AppTheme
import com.example.myapplication.R
import android.content.ActivityNotFoundException
import android.content.Intent
import com.example.myapplication.data.AppItem
import com.example.myapplication.data.mockAllApps
import kotlinx.coroutines.launch

@Composable
fun AppDetailsScreen(
    app: AppItem,
    isFavorite: (Int) -> Boolean,
    onFavoriteClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    onShowcaseAppClick: (AppItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val underDevelopment = stringResource(R.string.under_development)
    val showUnderDevelopment: () -> Unit = {
        scope.launch { snackbarHostState.showSnackbar(underDevelopment) }
    }

    val context = LocalContext.current
    val shareText = stringResource(R.string.share_app_text, app.name, app.description)
    val shareFailed = stringResource(R.string.share_failed)
    val shareApp: () -> Unit = {
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            context.startActivity(Intent.createChooser(intent, null))
        } catch (e: ActivityNotFoundException) {
            scope.launch { snackbarHostState.showSnackbar(shareFailed) }
        }
    }

    Box(modifier.fillMaxSize().safeDrawingPadding()) {
        Column(Modifier.fillMaxSize()) {
            Toolbar(
                onBackClick = onBackClick,
                onShareClick = shareApp,
            )
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(8.dp))
                AppDetailsHeader(app, Modifier.padding(horizontal = 16.dp))
                Spacer(Modifier.height(16.dp))
                InstallButton(
                    onClick = showUnderDevelopment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(12.dp))
                ScreenshotsList(app)
                Spacer(Modifier.height(12.dp))
                AppDescription(
                    description = app.fullDescription,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Spacer(Modifier.height(12.dp))
                Developer(
                    name = app.developer,
                    onClick = showUnderDevelopment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                )
                Spacer(Modifier.height(12.dp))
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            ShowcaseApps(
                currentApp = app,
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick,
                onAppClick = onShowcaseAppClick,
            )
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppDetailsScreenPreview() {
    AppTheme {
        AppDetailsScreen(
            app = mockAllApps.first(),
            isFavorite = { true },
            onFavoriteClick = {},
            onBackClick = {},
            onShowcaseAppClick = {},
        )
    }
}
