package ru.itmo.storemini.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import ru.itmo.storemini.R

@Composable
fun RemoteImage(
    imageUrl: String,
    contentDescription: String?,
    placeholder: Painter,
    error: Painter = placeholder,
    fallback: Painter = placeholder,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        placeholder = placeholder,
        error = error,
        fallback = fallback,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Composable
fun AppIcon(
    iconUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val placeholder = painterResource(R.drawable.ic_tech_android)
    RemoteImage(
        imageUrl = iconUrl,
        contentDescription = contentDescription,
        placeholder = placeholder,
        modifier = modifier,
    )
}
