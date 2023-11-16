package app.regate.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
//import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import app.regate.ComposeScreens
import app.regate.constant.AppUrl
import app.regate.constant.Route
import app.regate.constant.arg
import app.regate.constant.id
import app.regate.data.dto.chat.TypeChat
import app.regate.home.nav.AccountRoutes
import app.regate.home.nav.AddMainNav
import app.regate.home.nav.AuthRoutes
import app.regate.home.nav.EstablecimientoRoutes
import app.regate.home.nav.GrupoRoutes
import app.regate.home.nav.SalaRoutes
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

const val uri= AppUrl

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeEntry(
    composeScreens: ComposeScreens,
    establecimientoId:String?,
    startScreen:String,
    startRoute:String
//    navigateToMap:()->Unit
) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = true, block = {
        if (establecimientoId != null) {
            navController.navigate(Route.ESTABLECIMIENTO id establecimientoId.toLong() id 0)
        }
    })

    LaunchedEffect(key1 = navController, block = {
        Log.d("DEBUG_APP_DESTINATION",navController.currentDestination?.id.toString())
    })

    Scaffold(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigator,
                sheetShape = MaterialTheme.shapes.large.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp),
                ),
                sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                sheetContentColor = MaterialTheme.colorScheme.onSurface,
                scrimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.33f),
            ) {
                AppNavigation(
                    navController = navController,
                    composeScreens = composeScreens,
                    modifier = Modifier,
                    startScreen = startScreen,
                    startRoute = startRoute,
//                    finishActivity = establecimientoId != null,
//                    navigateToMap = navigateToMap
                )
            }
        }
    }
}


@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    composeScreens: ComposeScreens,
    startScreen:String,
    startRoute: String,
    modifier: Modifier = Modifier,
//    navigateToMap:()->Unit
) {
//    val context = LocalContext.current as Activity
    AnimatedNavHost(
        navController = navController,
        startDestination = startScreen,
        modifier = modifier
    ) {
        EstablecimientoRoutes(
            composeScreens = composeScreens,
            navController = navController
        )
        GrupoRoutes(
            composeScreens = composeScreens,
            navController = navController
        )
        SalaRoutes(
            composeScreens = composeScreens,
            navController = navController
        )
        AccountRoutes(
            composeScreens = composeScreens,
            navController = navController
        )
        AuthRoutes(
            composeScreens = composeScreens,
            navController = navController
        )

        animatedComposable(route = Route.WELCOME_PAGE){
            composeScreens.welcome(
//                navigateUp = navController::navigateUp
                  navigateToHome = { navController.navigate(Route.MAIN){
                      popUpTo(0)
//                      popUpTo(Route.MAIN)
                  } }
            )
        }
        animatedComposable(route = Route.REPORT arg  "data",
            arguments = listOf(
                navArgument("data"){ type = NavType.StringType },
            )){
            composeScreens.report(
            navigateUp = navController::navigateUp
            )
        }

        animatedComposable(route = Route.RESERVAR arg "id" arg "establecimientoId",
        arguments = listOf(
            navArgument("id"){ type = NavType.LongType },
            navArgument("establecimientoId"){ type = NavType.LongType }
        )
        ) {
            composeScreens.bottomReserva(
                openAuthDialog = {navController.navigate(Route.AUTH_DIALOG)},
                navigateUp = navController::navigateUp,
                navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)},
                navigateToConversation = {it1,it2->
                    navController.navigate(Route.CHAT_GRUPO + "?id=${it1}&parentId=${it2}&typeChat=${TypeChat.TYPE_CHAT_INBOX_ESTABLECIMIENTO.ordinal}" )
                },
                navigateToCreateSala = {navController.navigate(Route.CREAR_SALA id it id 0 id 1)},
                navigateToSelectGroup = {navController.navigate(Route.MY_CHATS + "?data=${it}")},
                navController = navController
            )
        }

        dialog(route = Route.MAP){
            composeScreens.map(
                navigateUp = navController::navigateUp,
            )
        }

        animatedComposable(route = Route.PHOTO arg "data",
            arguments = listOf(
                navArgument("data") { type = NavType.StringType }
            )){

            composeScreens.photo(
                navigateUp = navController::navigateUp,
                navigateToReport = {navController.navigate(Route.REPORT id it)}
            )
        }



        animatedComposableVariant(
            route = Route.INSTALACION arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            composeScreens.instalacion(
                navigateUp = navController::navigateUp,
//                navigateToReserva = { navController.navigate(Route.RESERVAR id it) }
            )
        }

        animatedComposable(
            route = Route.SETTING
        ){
            composeScreens.settings(
                navigateUp = navController::navigateUp
            )
        }

        animatedComposable(
            route = Route.RESERVA_DETAIL arg "id" arg "establecimiento_id" arg "instalacion_id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
                navArgument("establecimiento_id") { type = NavType.LongType },
                navArgument("instalacion_id") { type = NavType.LongType },
                )
        ){
            composeScreens.reserva(
                navigateUp = navController::navigateUp,
                navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)},
                navigateToConversation = {it1,it2->
                    navController.navigate(Route.CONVERSATION id it1 id it2 )
                }
            )
        }
        animatedComposable(
            route = Route.RESERVAS
        ){
            composeScreens.reservas(
                navigateUp = navController::navigateUp,
                navigateToReserva={reservaId,establecimientoId,instalacionId->
                    navController.navigate(Route.RESERVA_DETAIL id reservaId id establecimientoId id instalacionId)
                }
            )
        }
        animatedComposable(
            route = Route.INBOX,
        ){
            composeScreens.inbox(
                navigateUp = navController::navigateUp,
                navigateToConversation = {it1,it2->
                    navController.navigate(Route.CONVERSATION id it1 id it2 )
                }
            )
        }


        animatedComposable(route = Route.RECARGAR){
            composeScreens.recargar(
                navigateUp = navController::navigateUp,
                navigateToPay = {navController.navigate(Route.PAY id it)}
            )
        }
        animatedComposable(
            route = Route.PAY arg "qrRequest",
            arguments = listOf(navArgument("qrRequest") {
                type = NavType.StringType
            })
        ) {
            composeScreens.pay(
                navigateUp = navController::navigateUp,
//                navigateToReserva = { navController.navigate(Route.RESERVAR id it) }
            )
        }

        animatedComposable(
            route = Route.SEARCH + "?query={query}",
            arguments = listOf(navArgument("query") {
                defaultValue = ""
                type = NavType.StringType
            }),
        ) {navBackStackEntry->
            val query = navBackStackEntry.arguments?.getString("query")?:""
            composeScreens.search(
                navController = navController,
                queryArg = query,
                searchGrupos = { composeScreens.searchGrupos(
                    navigateToGroup = { navController.navigate(Route.GRUPO id it)},
                    navigateToInfoGrupo = {navController.navigate(Route.INFO_GRUPO id it)},
                    )},
                searchProfiles = { composeScreens.searchProfiles(
                    navigateToProfile = { navController.navigate(Route.PROFILE id it)},
                )},
                searchSalas = { composeScreens.searchSalas(
                    navigateToSala= { navController.navigate(Route.SALA id it)},
                )}
            )
        }
        animatedComposable(
            route = Route.HISTORY_SEARCH,
        ) {
            composeScreens.historySearch(
                navigateUp = navController::navigateUp,
                navigateToSearch = { navController.navigate(Route.SEARCH + "?query=${it}")}
            )
        }

        AddMainNav(
            composeScreens, navController,
            startRoute = startRoute
        )
    }
}
