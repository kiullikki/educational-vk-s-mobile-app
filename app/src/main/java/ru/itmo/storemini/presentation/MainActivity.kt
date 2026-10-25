package ru.itmo.storemini.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.itmo.storemini.presentation.theme.AppTheme
import androidx.activity.enableEdgeToEdge
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                AppNavigation()
            }
        }
    }
}
