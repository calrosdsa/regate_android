package app.regate.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.images.ProfileImage
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.auth.AppAuthState

typealias Account = @Composable (
    navigateToSettings:()->Unit,
    closeDrawer:()->Unit,
    navigateToReservas:()->Unit,
    openAuthBottomSheet:()->Unit
        ) -> Unit

@Inject
@Composable
fun Account(
    viewModelFactory:()->AccountViewModel,
    @Assisted navigateToSettings: () -> Unit,
    @Assisted closeDrawer: () -> Unit,
    @Assisted navigateToReservas: () -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit
) {
    Account(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToSettings = navigateToSettings,
        closeDrawer = closeDrawer,
        navigateToReservas = navigateToReservas,
        openAuthBottomSheet = openAuthBottomSheet
    )
}

@Composable
internal fun Account(
    viewModel: AccountViewModel,
    navigateToSettings: () -> Unit,
    closeDrawer: () -> Unit,
    navigateToReservas: () -> Unit,
    openAuthBottomSheet: () -> Unit
){
    val viewState by viewModel.state.collectAsState()
    Account(
        viewState = viewState,
        navigateToSettings ={navigateToSettings();closeDrawer()},
        logout = {viewModel.logout();closeDrawer()},
        navigateToReservas = {
            closeDrawer()
            navigateToReservas()
        },
        openAuthBottomSheet = {
            closeDrawer()
            openAuthBottomSheet()
        }
    )
}

@Composable
internal fun Account(
    viewState:AccountState,
    navigateToSettings: () -> Unit,
    logout:()->Unit,
    navigateToReservas: () -> Unit,
    openAuthBottomSheet: () -> Unit
) {
    val settings = stringResource(id = R.string.settings)
    val isAuth by remember(viewState.authState) { derivedStateOf{
        viewState.authState == AppAuthState.LOGGED_IN
    }}
    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(0.7f)
        .padding(20.dp)) {
        Column(
            modifier = Modifier
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            viewState.user?.let { user ->
//        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                ProfileImage(
                    profileImage = user.profile_photo,
                    contentDescription = user.nombre, modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${user.nombre} ${user.apellido ?: ""}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1
                )
                Text(
                    text = user.email, style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
                viewState.addressDevice?.let {
                    Surface(onClick = {}) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.city ?: "", style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.inverseSurface,
                                textDecoration = TextDecoration.Underline
                            )
                            Icon(
                                imageVector = Icons.Outlined.Map,
                                contentDescription = "place_direction"
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.padding(bottom = 10.dp))
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = (user.coins ?: 0).toString(),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(imageVector = Icons.Default.CurrencyBitcoin, contentDescription = "coins")
                }
            }
                RowIconOption(icon = Icons.Outlined.Settings, text = settings,
                modifier = Modifier
                    .clickable { navigateToSettings() }
                    .fillMaxWidth()
                    .padding(10.dp))
            if(isAuth){
                RowIconOption(icon = Icons.Outlined.CollectionsBookmark, text = stringResource(id = R.string.inbox),
                modifier = Modifier
                    .clickable { navigateToReservas() }
                    .fillMaxWidth()
                    .padding(10.dp))
                RowIconOption(icon = Icons.Outlined.Notifications, text = stringResource(id = R.string.notifications),
                    modifier = Modifier
                        .clickable { navigateToReservas() }
                        .fillMaxWidth()
                        .padding(10.dp))

                RowIconOption(icon = Icons.Outlined.CollectionsBookmark, text = stringResource(id = R.string.bookings),
                    modifier = Modifier
                        .clickable { navigateToReservas() }
                        .fillMaxWidth()
                        .padding(10.dp))


            }
            RowIconOption(icon = Icons.Outlined.Bookmark, text = stringResource(id = R.string.favorites),
                modifier = Modifier
                    .clickable { navigateToSettings() }
                    .fillMaxWidth()
                    .padding(10.dp))
        }

                Column(modifier = Modifier.align(Alignment.BottomCenter)) {

                    if(isAuth){
                    OutlinedButton(onClick = { logout() },modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(id = R.string.logout),style  = MaterialTheme.typography.labelLarge,
                        textAlign = TextAlign.Center)
                    }
                    }else{
                        OutlinedButton(onClick = { openAuthBottomSheet() },modifier = Modifier.fillMaxWidth()) {
                            Text(text = stringResource(id = R.string.login),style  = MaterialTheme.typography.labelLarge,
                                textAlign = TextAlign.Center)
                        }
                    }
                }
    }
}

@Composable
internal fun RowIconOption(
    icon: ImageVector,
    text:String,
    modifier : Modifier = Modifier
){
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically){
        Icon(imageVector = icon, contentDescription = text)
        Spacer(modifier = Modifier.width(15.dp))
        Text(text = text,style = MaterialTheme.typography.labelLarge)
    }
}
