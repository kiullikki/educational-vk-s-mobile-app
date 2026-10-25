package ru.itmo.storemini.presentation.appdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itmo.storemini.R
import ru.itmo.storemini.domain.appdetails.AppItem
import kotlin.math.roundToInt

@Composable
fun AppDetailsHeader(app: AppItem, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(128.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerHighest),
        ) {
            Icon(
                painter = painterResource(app.iconRes),
                contentDescription = app.name,
                tint = Color.Unspecified,
                modifier = Modifier.size(80.dp),
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = app.category,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 12.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = app.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(Modifier.height(4.dp))
            Text(text = app.developer, fontSize = 12.sp)
            Spacer(Modifier.height(4.dp))
            Row {
                Column(Modifier.width(IntrinsicSize.Max)) {
                    Text(
                        text = "${app.ageRating}+",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(text = stringResource(R.string.app_details_age))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(text = "${app.sizeMb.roundToInt()} MB")
                    Spacer(Modifier.height(4.dp))
                    Text(text = stringResource(R.string.app_details_size))
                }
            }
        }
    }
}
