package app.regate.common.compose.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import app.regate.settings.AppPreferences

@Composable
fun AppPreferences.shouldUseDarkColors(): Boolean {
    val themePreference = remember { observeTheme() }.collectAsState(initial = theme)
    return when (themePreference.value) {
        AppPreferences.Theme.LIGHT -> false
        AppPreferences.Theme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}

@Composable
fun AppPreferences.shouldUseDynamicColors(): Boolean {
    return remember { observeUseDynamicColors() }
        .collectAsState(initial = useDynamicColors)
        .value
}