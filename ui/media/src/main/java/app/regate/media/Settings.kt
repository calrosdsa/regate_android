package app.regate.media

import android.content.Intent
import android.content.pm.PackageManager.PackageInfoFlags
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.auth.AppAuthState
import app.regate.settings.AppPreferences

typealias Setting = @Composable (
    navigateUp:()->Unit
        ) -> Unit

@Inject
@Composable
fun Setting(
    viewModelFactory:()-> SettingViewModel,
    @Assisted navigateUp: () -> Unit
){
    Setting(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )
}

@Composable
internal fun Setting(
    viewModel: SettingViewModel,
    navigateUp: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    Setting(
        viewState = viewState,
        navigateUp = navigateUp,
        logout = viewModel::logout,
        onChangeTheme = viewModel::updateThemePreference,
        onChangeDynamicColors = viewModel::updateDynamicColors
    )
}

@Composable
internal fun Setting(
    viewState: SettingState,
    navigateUp: () -> Unit,
    logout:()->Unit,
    onChangeTheme: (AppPreferences.Theme) -> Unit,
    onChangeDynamicColors:(Boolean)->Unit
) {
    val isAuth by remember(viewState.authState) {
        derivedStateOf {
            viewState.authState == AppAuthState.LOGGED_IN
        }
    }
    val themeDialog =  remember {
        mutableStateOf(false)
    }
    val notificationDialog = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    val version = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) context.packageManager.getPackageInfo(context.packageName,PackageInfoFlags.of(0)).versionName else  @Suppress("DEPRECATION") context.packageManager.getPackageInfo(context.packageName,0).versionName
    if(themeDialog.value){
        Dialog(onDismissRequest = { themeDialog.value = false}) {
       ThemeDialog(theme = viewState.theme, onChangeTheme = onChangeTheme)
        }
    }
    if(notificationDialog.value){
        Dialog(onDismissRequest = { notificationDialog.value = false}) {
            NotificationsDialog(context = context)
        }
    }
    Scaffold(modifier = Modifier
        .fillMaxSize(),
        topBar = {
            Surface(color = MaterialTheme.colorScheme.inverseOnSurface) {
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")

                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(id = R.string.settings),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(id = R.string.notifications), modifier = Modifier
                        .clickable { notificationDialog.value = true }
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 10.dp)
                )
                Divider()
                Text(
                    text = stringResource(id = R.string.theme), modifier = Modifier
                        .clickable { themeDialog.value = !themeDialog.value }
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 10.dp)
                )
                Divider()
                Row(modifier = Modifier
                    .clickable { themeDialog.value = !themeDialog.value }
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = stringResource(id = R.string.dynamic_color))
                    Switch(checked = viewState.useDynamicColors, onCheckedChange = onChangeDynamicColors)
                }
                Divider()

                Text(text = stringResource(id = R.string.about_regate), color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,modifier = Modifier.padding(10.dp))

                Text(
                    text = stringResource(id = R.string.terms_of_services), modifier = Modifier
                        .clickable { }
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 10.dp)
                )
                Divider()
                Text(
                    text = stringResource(id = R.string.privacy_policy), modifier = Modifier
                        .clickable { }
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 10.dp)
                )
                Divider()
                Text(
                    text = stringResource(id = R.string.version,version), modifier = Modifier
                        .clickable { }
                        .fillMaxWidth()
                        .padding(vertical = 15.dp, horizontal = 10.dp)
                )
                Divider()
            }

            if (isAuth) {
                Column(modifier = Modifier.align(Alignment.BottomEnd)) {
                    Divider()
                    Surface(onClick = {logout()}) {
                        Text(
                            text = stringResource(id = R.string.logout),
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

