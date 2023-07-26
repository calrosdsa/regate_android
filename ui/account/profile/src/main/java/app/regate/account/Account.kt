package app.regate.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.regate.common.composes.components.images.ProfileImage
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.data.auth.AppAuthState

typealias Account = @Composable (
    navController:NavController,
    navigateToSettings:()->Unit,
    closeDrawer:()->Unit,
    navigateToReservas:()->Unit,
    openAuthBottomSheet:()->Unit,
    navigateToRecargaCoins:()->Unit
        ) -> Unit

@Inject
@Composable
fun Account(
    viewModelFactory:()->AccountViewModel,
    @Assisted navController: NavController,
    @Assisted navigateToSettings: () -> Unit,
    @Assisted closeDrawer: () -> Unit,
    @Assisted navigateToReservas: () -> Unit,
    @Assisted openAuthBottomSheet: () -> Unit,
    @Assisted navigateToRecargaCoins: () -> Unit
) {
    Account(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToSettings = navigateToSettings,
        closeDrawer = closeDrawer,
        navigateToReservas = navigateToReservas,
        openAuthBottomSheet = openAuthBottomSheet,
        navController = navController,
        navigateToRecargaCoins = navigateToRecargaCoins
    )
}

@Composable
internal fun Account(
    viewModel: AccountViewModel,
    navigateToSettings: () -> Unit,
    closeDrawer: () -> Unit,
    navigateToReservas: () -> Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToRecargaCoins: () -> Unit,
    navController:NavController
) {
    val viewState by viewModel.state.collectAsState()
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        },
    ) {paddingValues->
        Account(
            viewState = viewState,
            navigateToSettings = { navigateToSettings();closeDrawer() },
            logout = { viewModel.logout();closeDrawer() },
            navigateToReservas = {
                closeDrawer()
                navigateToReservas()
            },
            openAuthBottomSheet = {
                closeDrawer()
                openAuthBottomSheet()
            },
            navigateToProfile = { navController.navigate(Route.PROFILE id it)},
            navigateToFavorites = { navController.navigate(Route.FAVORITES)},
            modifier = Modifier.padding(paddingValues),
            navigateToRecargaCoins = navigateToRecargaCoins,
            navigateToInbox = {navController.navigate(Route.INBOX)}
        )
    }
}

@Composable
internal fun Account(
    viewState:AccountState,
    navigateToSettings: () -> Unit,
    logout:()->Unit,
    navigateToReservas: () -> Unit,
    openAuthBottomSheet: () -> Unit,
    navigateToFavorites: ()-> Unit,
    navigateToProfile:(Long)->Unit,
    navigateToRecargaCoins: () -> Unit,
    navigateToInbox:()->Unit,
    modifier:Modifier = Modifier
) {
    val settings = stringResource(id = R.string.settings)
    val isAuth by remember(viewState.authState) { derivedStateOf{
        viewState.authState == AppAuthState.LOGGED_IN
    }}
    Box(modifier = modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .padding(20.dp)) {
        Column( 
            modifier = Modifier
        ) {
            viewState.user?.let { user ->
        Surface(color = MaterialTheme.colorScheme.inverseOnSurface,
        shape = MaterialTheme.shapes.medium, onClick = { navigateToProfile(viewState.user.profile_id)}) {
        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()) {
            ProfileImage(
                profileImage = user.profile_photo,
                contentDescription = user.nombre, modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column() {

            Text(
                text = "${user.nombre} ${user.apellido ?: ""}",
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
            Text(
                text = user.email, style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.inverseSurface
            )
            }
        }
        }
                Spacer(modifier = Modifier.height(10.dp))
                if(viewState.authState == AppAuthState.LOGGED_IN){
                Surface(color = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = MaterialTheme.shapes.medium) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically){
                            Image(painter = painterResource(id = R.drawable.coin), contentDescription = null,
                            modifier = Modifier.size(25.dp))
                                Spacer(modifier = Modifier.width(5.dp))
                        Column() {
                        Text(text = stringResource(id = R.string.balance_coins),style = MaterialTheme.typography.labelMedium)
                            Text(text = user.coins.toString(),style = MaterialTheme.typography.labelLarge)
                            }
                        }
                        Button(onClick = { navigateToRecargaCoins() }) {
                            Text(text = stringResource(id = R.string.purchase))
                        }
                    }
                }
                }
                Spacer(modifier = Modifier.height(10.dp))

                Surface(color = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()) {
                    Text(text = stringResource(id = R.string.new_offers),style = MaterialTheme.typography.labelLarge)
                    Text(text = stringResource(id = R.string.discount),color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    style=  MaterialTheme.typography.labelMedium)
                    }
                }
//                viewState.addressDevice?.let {
//                    Surface(onClick = {}) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(10.dp),
//                            horizontalArrangement = Arrangement.SpaceBetween,
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Text(
//                                text = it.city ?: "", style = MaterialTheme.typography.labelMedium,
//                                color = MaterialTheme.colorScheme.inverseSurface,
//                                textDecoration = TextDecoration.Underline
//                            )
//                            Icon(
//                                imageVector = Icons.Outlined.Map,
//                                contentDescription = "place_direction"
//                            )
//                        }
//                    }
//                }
//                Divider(modifier = Modifier.padding(bottom = 10.dp))


            }
            Spacer(modifier = Modifier.height(10.dp))
            Surface(color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = MaterialTheme.shapes.medium) {
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                RowIconOption(icon = Icons.Outlined.Settings, text = settings,
                modifier = Modifier
                    .clickable { navigateToSettings() }
                    .fillMaxWidth()
                    .padding(10.dp))
            if(isAuth){
                RowIconOption(icon = Icons.Outlined.CollectionsBookmark, text = stringResource(id = R.string.inbox),
                modifier = Modifier
                    .clickable { navigateToInbox() }
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
                    .clickable { navigateToFavorites() }
                    .fillMaxWidth()
                    .padding(10.dp))
        }
                }
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
