package app.regate.common.composes.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import app.regate.constant.Route


@Composable
fun BottomBar (
    navController:NavController,
    modifier: Modifier = Modifier
){
    val currentSelectedItem by navController.currentScreenAsState()
    HomeNavigationBar(
                    selectedNavigation = currentSelectedItem,
                    onNavigationSelected = { selected ->
                        navController.navigate(selected) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                    },
                    modifier = modifier.fillMaxWidth(),
                )
}


@Stable
@Composable
fun NavController.currentScreenAsState(): State<String> {
    val selectedItem = remember { mutableStateOf(Route.HOME ) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Route.HOME } -> {
                    selectedItem.value = Route.HOME
                }
                destination.hierarchy.any { it.route == Route.NOTIFICATIONS} -> {
                    selectedItem.value = Route.NOTIFICATIONS
                }
                destination.hierarchy.any { it.route == Route.DISCOVER + "?data={data}" } -> {
                    selectedItem.value = Route.DISCOVER
                }
                destination.hierarchy.any { it.route == Route.GRUPOS + "?uuid={uuid}"} -> {
                    selectedItem.value = Route.GRUPOS
                }
                destination.hierarchy.any { it.route == Route.ACCOUNT} -> {
                    selectedItem.value = Route.ACCOUNT
                }
//                destination.hierarchy.any { it.route == Route.DISCOVER } -> {
//                    selectedItem.value = Route.DISCOVER
//                }
//                destination.hierarchy.any { it.route == Route.ACTIVITIES } -> {
//                    selectedItem.value = Route.ACTIVITIES
//                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

@Composable
internal fun HomeNavigationBar(
    selectedNavigation: String,
    onNavigationSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.inverseOnSurface
    ) {

    Row(modifier = modifier
        .padding(5.dp)
        .height(50.dp)) {
        for (item in HomeNavigationItems) {
            NavigationBarItem(
                icon = {
//                       Icon(imageVector = item., contentDescription =null )
                    HomeNavigationItemIcon(
                        item = item,
                        selected = selectedNavigation == item.screen,
                    )
                },
//                label = { Text(text = item.labelResId, maxLines = 1) },
                selected = false,
                onClick = { onNavigationSelected(item.screen) },
            )
        }
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


private sealed class HomeNavigationItem(
    val screen: String,
    val labelResId: String,
    val contentDescriptionResId: String,
) {
//    class ResourceIcon(
//        screen: Route,
//        @StringRes labelResId: Int,
//        @StringRes contentDescriptionResId: Int,
//        @DrawableRes val iconResId: Int,
//        @DrawableRes val selectedIconResId: Int? = null,
//    ) : HomeNavigationItem(screen, labelResId, contentDescriptionResId)

    class ImageVectorIcon(
        screen: String,
//        @StringRes labelResId: Int,
//        @StringRes contentDescriptionResId: Int,
        labelResId: String,
        contentDescriptionResId: String,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null,
    ) : HomeNavigationItem(screen, labelResId, contentDescriptionResId)
}

private val HomeNavigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        screen = Route.HOME,
        labelResId = "Home",
        contentDescriptionResId ="home",
        iconImageVector = Icons.Outlined.Home,
        selectedImageVector = Icons.Filled.Home,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Route.DISCOVER,
        labelResId = "Reservas",
        contentDescriptionResId = "reservas",
        iconImageVector = Icons.Outlined.CalendarToday,
        selectedImageVector = Icons.Filled.CalendarToday,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Route.NOTIFICATIONS,
        labelResId = "Notificaciones",
        contentDescriptionResId = "notificaciones",
        iconImageVector = Icons.Outlined.Notifications,
        selectedImageVector = Icons.Filled.Notifications,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Route.GRUPOS,
        labelResId = "Chats",
        contentDescriptionResId = "chats",
        iconImageVector = Icons.Outlined.Chat,
        selectedImageVector = Icons.Filled.Chat,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Route.ACCOUNT,
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
