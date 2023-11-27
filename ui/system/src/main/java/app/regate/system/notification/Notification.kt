package app.regate.system.notification

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.component.text.DateTextWithIcon
import app.regate.common.composes.ui.BottomBar
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.PosterCardImageWithoutClick
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.models.Notification
import app.regate.models.TypeEntity
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.rememberBottomSheetState
import kotlinx.coroutines.launch
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
        navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)},
        updateUnreadNotifications = viewModel::updateUnreadNotifications,
        deleteNotification = viewModel::deleteNotification
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Notifications(
    viewState:NotificationState,
    navigateToSala: (Long) -> Unit,
    formatRelativeTime:(Instant)->String,
    updateUnreadNotifications:()->Unit,
    deleteNotification:(Long)->Unit,
    navigateToAccount: () -> Unit,
    navigateToNoticationSetting: () -> Unit,
    navigateToEstablecimiento:(Long) -> Unit,
){
    val sheetState = rememberBottomSheetState()
    var selectedNotification by remember {
        mutableStateOf(0L)
    }
    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
    LaunchedEffect(key1 = Unit, block = {
        updateUnreadNotifications()
    })

    BottomSheet(
        state = sheetState,
//        peekHeight = PeekHeight.dp(400),
        // Set to true you don't want the peeked state.
        skipPeeked = false,
    ) {
        Column(modifier = Modifier.padding(bottom= 10.dp)) {
            DropdownMenuItem(text = { Text(text = stringResource(id = R.string.delete_notification)) },
                onClick = {
                    deleteNotification(selectedNotification)
                    coroutineScope.launch {
                        sheetState.collapse()
                    } },
                leadingIcon = { Icon(imageVector = Icons.Filled.Delete, contentDescription = stringResource(
                    id = R.string.delete_notification
                ))},
                contentPadding = PaddingValues(8.dp)
            )
        }
    }

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
            .padding(paddingValues)) {
            if(viewState.notifications.isEmpty()) {
                Column(modifier =  Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.empty_inbox),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .fillMaxHeight(0.3f),
                        contentScale = ContentScale.Fit,
                    )
                    Text(text = stringResource(id = R.string.no_new_notifications),
                        textAlign = TextAlign.Center,style =MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight(350)
                        ))
                }
            }
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
//                        TypeEntity.URI -> {
//                            try{
//
//                            val urlIntent = Intent(
//                                Intent.ACTION_VIEW,
//                                Uri.parse()
//                            )
//                            startActivity(urlIntent)
//                            }catch (e:Exception){
//                                //TODO()
//                            }
//                        }
                        else -> {}
                    }
                },
                    onLongClick = {
                        selectedNotification = item.id
                        coroutineScope.launch {
                            sheetState.expand()
                        }
                    }
                )
            }

        })
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun NotificationItem(
    item:Notification,
    navigate:()->Unit,
    onLongClick:()->Unit,
    formatRelativeTime: (Instant) -> String
){

        Column(modifier = Modifier
            .combinedClickable(onLongClick = onLongClick) { navigate() }
            .padding(vertical = 5.dp, horizontal = 10.dp)
            .height(IntrinsicSize.Min)
            .fillMaxWidth()) {
            Row(modifier = Modifier
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                PosterCardImageWithoutClick(
                    model = item.image?:"https://cdn-icons-png.flaticon.com/128/4239/4239989.png",
                    modifier = Modifier.size(45.dp),
                    shape = CircleShape
                )
                Spacer(modifier = Modifier.width(10.dp))
                if (item.title?.isNotBlank() == true) {
                    Column() {
                    Text(text = item.title!!, style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.sp,
                        lineHeight = 15.sp
                    ),
                    maxLines = 2, overflow = TextOverflow.Ellipsis)
                        Text(text = formatRelativeTime(item.created_at),
                            style= MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp
                            ),
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    }

                }
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = item.content ,
                style = MaterialTheme.typography.bodySmall)
//            DateTextWithIcon(date = formatRelativeTime(item.created_at))
        }

}