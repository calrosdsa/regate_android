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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.components.text.DateTextWithIcon
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.models.Notification
import app.regate.models.TypeEntity
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias Notifications = @Composable (
    navigateUp:()->Unit,
    navigateToSala:(Long)->Unit,
    navigateToAccount:()->Unit,
        ) -> Unit

@Inject
@Composable
fun Notifications(
    viewModelFactory:()->NotificationViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToSala: (Long) -> Unit,
    @Assisted navigateToAccount:()->Unit,
){
    Notifications(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToSala = navigateToSala,
        navigateToAccount = navigateToAccount
    )
}

@Composable
internal fun Notifications(
  viewModel: NotificationViewModel,
  navigateUp: () -> Unit,
  navigateToSala: (Long) -> Unit,
  navigateToAccount: () -> Unit
){
    val state by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    Notifications(
        viewState = state,
        navigateUp = navigateUp,
        navigateToSala = navigateToSala,
        formatRelativeTime = formatter::formatShortRelativeTime,
        navigateToAccount = navigateToAccount
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Notifications(
    viewState:NotificationState,
    navigateUp: () -> Unit,
    navigateToSala: (Long) -> Unit,
    formatRelativeTime:(Instant)->String,
    navigateToAccount: () -> Unit
){
    Scaffold(
        topBar = {
            SimpleTopBar(navigateUp = navigateUp,
            title = stringResource(id = R.string.notifications))
        }
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
                        TypeEntity.SALA -> item.entityId?.let { navigateToSala(it) }
                        TypeEntity.BILLING -> item.entityId?.let { navigateToAccount() }
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
        .padding(top = 5.dp, bottom = 12.dp)
        .height(IntrinsicSize.Min)
        .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = Icons.Outlined.Notifications, contentDescription = null,
        modifier  = Modifier.rotate(30f))
        Spacer(modifier = Modifier.width(10.dp))
        Column() {
            if(item.title.isNotBlank()){
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            }
            Text(text = item.content ,
                style = MaterialTheme.typography.labelMedium)
        }
        }
            DateTextWithIcon(date = formatRelativeTime(item.created_at),
                modifier = Modifier.align(Alignment.BottomEnd).padding(top=9.dp))
    }
}