package app.regate.system.notification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import app.regate.common.resources.R
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.component.text.DateTextWithIcon
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.models.Notification
import app.regate.models.TypeEntity
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Notifications= @Composable (
    navigateToSala:(Long)->Unit,
    navigateToAccount:()->Unit,
    navigateToNoticationSetting:()->Unit,
    navController:NavController
        ) -> Unit

@Inject
@Composable
fun Notifications(
    viewModelFactory:()->NotificationViewModel,
    @Assisted navigateToSala: (Long) -> Unit,
    @Assisted navigateToAccount:()->Unit,
    @Assisted navigateToNoticationSetting: () -> Unit,
    @Assisted navController:NavController,
){
    Notifications(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToSala = navigateToSala,
        navigateToAccount = navigateToAccount,
        navigateToNoticationSetting = navigateToNoticationSetting,
        navController = navController
    )
}

@Composable
internal fun Notifications(
  viewModel: NotificationViewModel,
  navigateToSala: (Long) -> Unit,
  navigateToAccount: () -> Unit,
  navigateToNoticationSetting: () -> Unit,
  navController:NavController
){
    val state by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    Notifications(
        viewState = state,
        navigateToSala = navigateToSala,
        formatRelativeTime = formatter::formatShortRelativeTime,
        navigateToAccount = navigateToAccount,
        navigateToNoticationSetting = navigateToNoticationSetting,
        navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Notifications(
    viewState:NotificationState,
    navigateToSala: (Long) -> Unit,
    formatRelativeTime:(Instant)->String,
    navigateToAccount: () -> Unit,
    navigateToNoticationSetting: () -> Unit,
    navigateToEstablecimiento:(Long) -> Unit
){
    Scaffold(
       topBar = { TopAppBar(
           title = { Text(text = stringResource(id = R.string.notifications))},
           actions = {
               IconButton(onClick = { navigateToNoticationSetting() }) {
                   Icon(imageVector = Icons.Default.Settings, contentDescription = null)
               }
           }
       ) },
    ) {paddingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 10.dp)) {

        LazyColumn(
            modifier = Modifier,
            content = {
            items(
                items = viewState.notifications
            ){item->
                NotificationItem(item = item,formatRelativeTime = formatRelativeTime,
                navigate = {
                    when(item.typeEntity){
                        TypeEntity.NONE -> item.typeEntity?.let { navigateToAccount() }
                        TypeEntity.SALA -> item.entityId?.let { navigateToSala(it) }
                        TypeEntity.BILLING -> item.entityId?.let { navigateToAccount() }
                        TypeEntity.ESTABLECIMIENTO -> item.entityId?.let{ navigateToEstablecimiento(it)}
                        else -> {}
                    }
                })
                Divider(modifier = Modifier.padding(vertical = 5.dp))
            }

        })
        }
    }
}


@Composable
internal fun NotificationItem(
    item:Notification,
    navigate:()->Unit,
    formatRelativeTime: (Instant) -> String
){
        Box(modifier = Modifier.fillMaxWidth()){
    Row(modifier = Modifier
        .clickable { navigate() }
        .padding(top = 5.dp, bottom = 5.dp)
        .height(IntrinsicSize.Min)
        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        PosterCardImage(model = item.image,
        modifier = Modifier.size(50.dp),
            shape = CircleShape
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column() {
            if(item.title?.isNotBlank() == true){
            Text(text = item.title!!, style = MaterialTheme.typography.labelLarge)
            }
            Text(text = item.content ,
                style = MaterialTheme.typography.labelMedium)
            DateTextWithIcon(date = formatRelativeTime(item.created_at))
        }
        }
    }
}