package ru.itmo.storemini.presentation.appdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.itmo.storemini.domain.appdetails.AppItem

@Composable
fun FakeScreenshot(app: AppItem, variant: Int) {
    val (background, foreground) = with(MaterialTheme.colorScheme) {
        listOf(
            surfaceContainerHighest to onSurfaceVariant,
            primaryContainer to onPrimaryContainer,
            inverseSurface to inverseOnSurface,
        )[variant]
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(app.iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(8.dp))
            SkeletonBar(width = 96.dp, height = 8.dp, color = foreground)
        }

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            if (variant != 1) {
                Icon(
                    painter = painterResource(app.iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(if (variant == 2) 56.dp else 40.dp),
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                SkeletonBar(width = 140.dp, height = 10.dp, color = foreground)
                SkeletonBar(width = 180.dp, height = 8.dp, color = foreground)
                SkeletonBar(width = 110.dp, height = 8.dp, color = foreground)
            }
        }

        Spacer(Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(3) {
                Box(
                    Modifier
                        .size(width = 56.dp, height = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(foreground.copy(alpha = 0.25f)),
                )
            }
        }
    }
}

@Composable
private fun SkeletonBar(width: Dp, height: Dp, color: Color) {
    Box(
        Modifier
            .size(width = width, height = height)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.4f)),
    )
}
