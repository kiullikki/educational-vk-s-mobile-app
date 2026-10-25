package ru.itmo.storemini.presentation.appdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import ru.itmo.storemini.presentation.theme.AppTheme
import ru.itmo.storemini.R
import android.content.ActivityNotFoundException
import android.content.Intent
import ru.itmo.storemini.domain.appdetails.AppItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsScreen(
    app: AppItem,
    showcaseApps: List<AppItem>,
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

    BottomSheetScaffold(
        scaffoldState = rememberBottomSheetScaffoldState(),
        sheetPeekHeight = 32.dp,
        sheetContainerColor = MaterialTheme.colorScheme.surface,
        sheetTonalElevation = 8.dp,
        sheetContent = {
            ShowcaseApps(
                apps = showcaseApps,
                onFavoriteClick = onFavoriteClick,
                onAppClick = onShowcaseAppClick,
            )
        },
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
        ) {
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
                    AppDetailsHeader(
                        app = app,
                        onFavoriteClick = { onFavoriteClick(app.id) },
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
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
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppDetailsScreenPreview() {
    AppTheme {
        AppDetailsScreen(
            app = AppItem(
                iconRes = R.drawable.ic_tech_kotlin,
                name = "Kotlin",
                description = "Основной язык Android-разработки",
                fullDescription = "Статически типизирован, полностью совместим с Java.",
                category = "Языки",
                id = 1,
                isFavorite = true,
                developer = "JetBrains",
                ageRating = 0,
                sizeMb = 95.4f,
            ),
            showcaseApps = emptyList(),
            onFavoriteClick = {},
            onBackClick = {},
            onShowcaseAppClick = {},
        )
    }
}
