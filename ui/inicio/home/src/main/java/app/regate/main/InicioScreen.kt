package app.regate.main

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.regate.common.composes.viewModel
import app.regate.constant.Route
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias InicioScreen = @Composable (
    home:@Composable () ->Unit,
    reservas:@Composable () ->Unit,
    notifications:@Composable () ->Unit,
    chats:@Composable () ->Unit,
    account:@Composable () ->Unit,
//    bottomNav:@Composable () ->Unit,
    currentPage:Int,
    ) ->Unit

@Inject
@Composable
fun InicioScreen(
    viewModelFactory:()->InicioViewModel,
    @Assisted home: @Composable () ->Unit,
    @Assisted reservas: @Composable () ->Unit,
    @Assisted notifications: @Composable () ->Unit,
    @Assisted chats: @Composable () ->Unit,
    @Assisted account: @Composable () ->Unit,
//    @Assisted bottomNav: @Composable () ->Unit,
    @Assisted currentPage: Int,
    ){
    InicioScreen(
        viewModel = viewModel(factory = viewModelFactory) ,
        home = home,
        reservas = reservas,
        notifications = notifications,
        chats = chats,
        account = account,
        currentPage = currentPage
    )
}

@Composable
internal fun InicioScreen(
    viewModel:InicioViewModel,
    home: @Composable () ->Unit,
    reservas: @Composable () ->Unit,
    notifications: @Composable () ->Unit,
    chats: @Composable () ->Unit,
    account: @Composable () ->Unit,
//    @Assisted bottomNav: @Composable () ->Unit,
    currentPage: Int,
){
    val state by viewModel.state.collectAsState()
    InicioScreen(
        viewState = state,
        home = home,
        reservas = reservas,
        notifications = notifications,
        chats = chats,
        account = account,
        currentPage = currentPage,
        updateInitPage = viewModel::updateInitPage
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun InicioScreen(
    viewState:InicioState,
    home: @Composable () ->Unit,
    reservas: @Composable () ->Unit,
    notifications: @Composable () ->Unit,
    chats: @Composable () ->Unit,
    account: @Composable () ->Unit,
    updateInitPage:(Int)->Unit,
//    @Assisted bottomNav: @Composable () ->Unit,
    currentPage: Int,
){
    val pagerState = rememberPagerState(initialPage = currentPage)
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = pagerState.currentPage, block = {
        updateInitPage(pagerState.currentPage)
    })
    Scaffold(
        bottomBar = {
            InicioTabLayout(
                current = pagerState.currentPage,
                viewState = viewState,
                navigateToPage = {
                    coroutineScope.launch {
                    pagerState.scrollToPage(it)
                    }
                }
            )
        }
    ) {paddingValues->
    HorizontalPager(
        pageCount = 5,
        state = pagerState,
        modifier = Modifier.padding(paddingValues),
        userScrollEnabled = false
    ) {page ->
        when(page){
            0 -> home()
            1 -> reservas()
            2 -> notifications()
            3 -> chats()
            4 -> account()
        }
    }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InicioTabLayout(
    current:Int,
    viewState:InicioState,
    navigateToPage:(Int)->Unit
) {
    TabRow(selectedTabIndex = current,
    indicator = {tabPositions ->
            if (current < tabPositions.size) {
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffsetInv(tabPositions[current])
                )
            }
    },
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        for (item in HomeNavigationItems) {
            Tab(selected = current == item.page,
                modifier = Modifier.size(55.dp).padding(0.dp),
                onClick = { navigateToPage(item.page) },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.primary,
                icon = {
                    BadgedBox(badge = {
                        when (item.page) {
                            2 -> {
                                if (viewState.notificationCount > 0) {
                                    Badge() {
                                        Text(text = viewState.notificationCount.toString())
                                    }
                                }
                            }

                            3 -> {
                                if (viewState.messagesCount > 0) {
                                    Badge() {
                                        Text(text = viewState.messagesCount.toString())
                                    }
                                }
                            }
                        }
                    }){
                       HomeNavigationItemIcon(
                            item = item,
                            selected = current == item.page,
                        )
                    }
        })
        }
    }
}

@Composable
private fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val painter = when (item) {
//        is HomeNavigationItem.ResourceIcon -> painterResource(item.iconResId)
        is HomeNavigationItem.ImageVectorIcon -> rememberVectorPainter(item.iconImageVector)
    }
    val selectedPainter = item.selectedImageVector?.let { rememberVectorPainter(it) }
//        is HomeNavigationItem.ResourceIcon -> item.selectedIconResId?.let { painterResource(it) }


    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                painter = if (it) selectedPainter else painter,
                contentDescription = item.contentDescriptionResId,
            )
        }
    } else {
        Icon(
            painter = painter,
            contentDescription = item.contentDescriptionResId,

        )
    }
}





private fun Modifier.tabIndicatorOffsetInv(
    currentTabPosition: TabPosition
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val currentTabWidth by animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.TopStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}



private sealed class HomeNavigationItem(
    val page: Int,
    val labelResId: String,
    val contentDescriptionResId: String,
) {

    class ImageVectorIcon(
        page: Int,
//        @StringRes labelResId: Int,
//        @StringRes contentDescriptionResId: Int,
        labelResId: String,
        contentDescriptionResId: String,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null,
    ) : HomeNavigationItem(page, labelResId, contentDescriptionResId)
}

private val HomeNavigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        page = 0,
        labelResId = "Home",
        contentDescriptionResId ="home",
        iconImageVector = Icons.Outlined.Home,
        selectedImageVector = Icons.Filled.Home,
    ),
    HomeNavigationItem.ImageVectorIcon(
        page = 1,
        labelResId = "Reservas",
        contentDescriptionResId = "reservas",
        iconImageVector = Icons.Outlined.CalendarToday,
        selectedImageVector = Icons.Filled.CalendarToday,
    ),
    HomeNavigationItem.ImageVectorIcon(
        page = 2,
        labelResId = "Notificaciones",
        contentDescriptionResId = "notificaciones",
        iconImageVector = Icons.Outlined.Notifications,
        selectedImageVector = Icons.Filled.Notifications,
    ),
    HomeNavigationItem.ImageVectorIcon(
        page = 3,
        labelResId = "Chats",
        contentDescriptionResId = "chats",
        iconImageVector = Icons.Outlined.Chat,
        selectedImageVector = Icons.Filled.Chat,
    ),
    HomeNavigationItem.ImageVectorIcon(
        page = 4,
        labelResId = "Account",
        contentDescriptionResId = "account",
        iconImageVector = Icons.Outlined.AccountCircle,
        selectedImageVector = Icons.Filled.AccountCircle,
    ),
//    HomeNavigationItem.ImageVectorIcon(
//        screen = Route.SERVICIOS,
//        labelResId = "Servicios",
//        contentDescriptionResId = "home",
//        iconImageVector = Icons.Outlined.DesignServices,
//        selectedImageVector = Icons.Default.DesignServices,
//    ),
//    HomeNavigationItem.ImageVectorIcon(
//        screen = Route.ACTIVITIES,
//        labelResId = "Actividades",
//        contentDescriptionResId = "activities",
//        iconImageVector = Icons.Outlined.Schedule,
//        selectedImageVector = Icons.Default.Schedule,
//    ),
)
