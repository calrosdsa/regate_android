package app.regate.settings

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.regate.common.resources.R


@Composable
fun ThemeDialog(
    theme:AppPreferences.Theme,
    onChangeTheme:(AppPreferences.Theme)->Unit,
    modifier: Modifier = Modifier,
){
    Column(modifier = modifier
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.background)
        .padding(10.dp)) {
        Text(text = "Theme", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        Row(modifier = Modifier
            .clickable { onChangeTheme(AppPreferences.Theme.LIGHT) }
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = AppPreferences.Theme.LIGHT == theme, onClick = { onChangeTheme(AppPreferences.Theme.LIGHT)  })
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Light")
        }
        Row(modifier = Modifier
            .clickable { onChangeTheme(AppPreferences.Theme.DARK) }
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = AppPreferences.Theme.DARK == theme, onClick = { onChangeTheme(AppPreferences.Theme.DARK)  })
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Dark")
        }
        Row(modifier = Modifier
            .clickable { onChangeTheme(AppPreferences.Theme.SYSTEM) }
            .fillMaxWidth()
            .padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = AppPreferences.Theme.SYSTEM == theme, onClick = { onChangeTheme(AppPreferences.Theme.SYSTEM)  })
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = "Seguir configuarion del sistema")
        }
    }
}

@Composable
fun NotificationsDialog(
    context: Context,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.background)
        .padding(10.dp)) {
        Text(text = stringResource(id = R.string.notifications), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold))
        Text(
            text = stringResource(id = R.string.chat_message_channel_name), modifier = Modifier
                .clickable {
                    try {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                            val intent =
                                Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                    putExtra(
                                        Settings.EXTRA_CHANNEL_ID,
                                        context.getString(R.string.chat_message_channel_id)
                                    )
                                }
                            context.startActivity(intent)
                        }

                    } catch (e: Exception) {
                        Log.d("DEBUG_APP_SETTINGS", e.localizedMessage ?: "")
                    }
                }
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 10.dp)
        )

        Text(
            text = stringResource(id = R.string.chat_group_channel_name), modifier = Modifier
                .clickable {
                    try {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                            val intent =
                                Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                    putExtra(
                                        Settings.EXTRA_CHANNEL_ID,
                                        context.getString(R.string.chat_group_channel_id)
                                    )
                                }
                            context.startActivity(intent)
                        }

                    } catch (e: Exception) {
                        Log.d("DEBUG_APP_SETTINGS", e.localizedMessage ?: "")
                    }
                }
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 10.dp)
        )

        Text(
            text = stringResource(id = R.string.notification_sala), modifier = Modifier
                .clickable {
                    try {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                            val intent =
                                Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
                                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                                    putExtra(
                                        Settings.EXTRA_CHANNEL_ID,
                                        context.getString(R.string.notification_sala_channel)
                                    )
                                }
                            context.startActivity(intent)
                        }

                    } catch (e: Exception) {
                        Log.d("DEBUG_APP_SETTINGS", e.localizedMessage ?: "")
                    }
                }
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 10.dp)
        )
    }
}
