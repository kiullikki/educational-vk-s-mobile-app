package ru.itmo.storemini.presentation.appdetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.itmo.storemini.R

@Composable
fun InstallButton(
    installState: InstallState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        enabled = installState !is InstallState.Installed,
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(
                when (installState) {
                    InstallState.NotInstalled -> R.string.install
                    is InstallState.Installing -> R.string.cancel
                    InstallState.Installed -> R.string.installed
                }
            )
        )
    }
}
