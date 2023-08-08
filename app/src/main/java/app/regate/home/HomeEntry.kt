package app.regate.home

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
//import androidx.navigation.compose.composable
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import app.regate.ComposeScreens
import app.regate.constant.Route
import app.regate.constant.arg
import app.regate.constant.id
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeEntry(
    composeScreens: ComposeScreens,
    establecimientoId:String?,
    startScreen:String,
    navigateToMap:()->Unit
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
                    finishActivity = establecimientoId != null,
                    navigateToMap = navigateToMap
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
    finishActivity:Boolean,
    startScreen:String,
    modifier: Modifier = Modifier,
    navigateToMap:()->Unit
) {
    val uri = "https://example.com"
    val context = LocalContext.current as Activity
    AnimatedNavHost(
        navController = navController,
        startDestination = startScreen,
        modifier = modifier
    ) {
        animatedComposable(route = Route.WELCOME_PAGE){
            composeScreens.welcome(
//                navigateUp = navController::navigateUp
                  navigateToHome = { navController.navigate(Route.MAIN){
                      popUpTo(0)
//                      popUpTo(Route.MAIN)
                  } }
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
                navigateToConversation = {navController.navigate(Route.CONVERSATION id it)}
            )
        }

        dialog(route = Route.MAP){
            composeScreens.map(
                navigateUp = navController::navigateUp,
            )
        }
        bottomSheet(route = Route.AUTH_DIALOG){
            composeScreens.bottomAuth(
                navigateUp = navController::navigateUp,
                navigateToLogin = {navController.navigate(Route.LOGIN_PAGE)},
                navigateToSignUp = {navController.navigate(Route.SIGNUP_SCREEN)},
            )
        }
        animatedComposable(route = Route.FILTER){
            composeScreens.filter(
                navigateUp = navController::navigateUp
            )
        }

        animatedComposable(route = Route.LOGIN_PAGE) {
            composeScreens.login(
                navigateToSignUpScreen = { navController.navigate(Route.SIGNUP_SCREEN) },
                navigateToHomeScreen = {
                    navController.navigate(Route.HOME)
                })
        }

        animatedComposable(route = Route.SIGNUP_SCREEN) {
            composeScreens.signUp(onBack = { navController.navigate(Route.HOME) })
        }
        animatedComposable(route = Route.PHOTO arg "url",
        arguments = listOf(
            navArgument("url") { type = NavType.StringType }
        )){
            val url = it.arguments?.getString("url")?:""
            composeScreens.photo(
                navigateUp = navController::navigateUp,
                url = url
            )
        }
        animatedComposableVariant(
            route = Route.ESTABLECIMIENTO arg "id" arg "page",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
                navArgument("page") { type = NavType.LongType },
                ),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/establecimiento/id={id}/page={page}" })
        ) { it ->
            val page = it.arguments?.getLong("page")?:0
            composeScreens.establecimiento(
                navigateUp = {
                    if(finishActivity){ context.finish() }else{ navController.navigateUp() }
                },
//                navigateToInstalacion = { navController.navigate(Route.INSTALACION id it) },
                actividades = {},
                reservar = {category->
                    composeScreens.establecimientoReserva(
                        navigateToReservaDetail = {instalacionId,establecimientoId ->
                            navController.navigate(Route.RESERVAR id instalacionId id establecimientoId)
                        },
                        category = category,
                    )
                },
                salas = {
                    composeScreens.establecimientoSalas(
                        navigateToSala = { navController.navigate(Route.SALA id it ) },
                        crearSala = { navController.navigate(Route.CREAR_SALA id it) }
                    )
                },
                currentPage = page.toInt(),
                navigateToPhoto = {
                    val url =  URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                    navController.navigate(Route.PHOTO id url)
                },
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
            route = Route.SALA arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/sala_id={id}" })
        ) {
            composeScreens.sala(
                navigateUp = navController::navigateUp,
                navigateToChat = {navController.navigate(Route.CHAT_SALA id it)},
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                navigateToInstalacion = {navController.navigate(Route.INSTALACION id it)},
                navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)}
            )
        }
        animatedComposable(
            route = Route.CREAR_SALA arg "id" arg "grupo_id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
                navArgument("grupo_id") { type = NavType.LongType },
                )
        ) {
            composeScreens.createSala(
                navigateUp = navController::navigateUp,
                reservarInstalacion = {
                    composeScreens.establecimientoReserva(
                        navigateToReservaDetail = { _, _ ->},
                        category = 0
                    )
                }
            )
        }

        animatedComposable(
            route = Route.ESTABLECIMIENTO_FILTER arg "grupo_id",
            arguments = listOf(
                navArgument("grupo_id") { type = NavType.LongType },
            )
        ){
            composeScreens.establecimientoFilter(
                navigateUp = navController::navigateUp,
                navigateToCreateSala = {id,grupoId->
                    navController.navigate(Route.CREAR_SALA id id id grupoId)
                },
                navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)}
            )
        }

        animatedComposable(
            route = Route.CHAT_SALA arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/grupo_id={id}" })
        ) {
            composeScreens.chatSala(
                navigateUp = navController::navigateUp,
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                navigateToCreateSala = {navController.navigate(Route.ESTABLECIMIENTO_FILTER id it)},
                navigateToGroup = { navController.navigate(Route.GRUPO id it)}
                )
        }

        animatedComposable(
            route = Route.GRUPO arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            composeScreens.grupo(
                navigateUp = navController::navigateUp,
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
//                navigateToChat = {navController.navigate(Route.CHAT_SALA id it)},
                createSala = { navController.navigate(Route.ESTABLECIMIENTO_FILTER id it)},
                navigateToSala = { navController.navigate(Route.SALA id it)},
                editGroup = {navController.navigate(Route.CREATE_GROUP + "?id=${it}" )},
                navigateToProfile = { navController.navigate(Route.PROFILE id it)},
                navigateToSalas = { navController.navigate(Route.GRUPO_SALAS id it)}
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
            route = Route.PROFILE arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ){
            composeScreens.profile(
                navigateUp = navController::navigateUp,
                navigateToEditProfile = {navController.navigate(Route.EDIT_PROFILE id it)}
            )
        }
        animatedComposable(
            route = Route.EDIT_PROFILE arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ){
            composeScreens.editProfile(
                navigateUp = navController::navigateUp
            )
        }
        animatedComposable(
            route = Route.RESERVA_DETAIL arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ){
            composeScreens.reserva(
                navigateUp = navController::navigateUp,
                navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)},
                navigateToConversation = {navController.navigate(Route.CONVERSATION id it)}
            )
        }
        animatedComposable(
            route = Route.RESERVAS
        ){
            composeScreens.reservas(
                navigateUp = navController::navigateUp,
                navigateToReserva={
                    navController.navigate(Route.RESERVA_DETAIL id it)
                }
            )
        }
        animatedComposable(
            route = Route.FAVORITES
        ){
            composeScreens.favorites(
                navigateUp = navController::navigateUp,
                navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)}
            )
        }
        animatedComposable(
            route = Route.CREATE_GROUP + "?id={id}",
            arguments = listOf(navArgument("id") {
                defaultValue = 0
                type = NavType.LongType
            })
        ){
            composeScreens.createGroup(
                navigateUp = navController::navigateUp,
                navigateToGroup = { navController.navigate(Route.GRUPO id it){
                    popUpTo(Route.GRUPOS)
                } },
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)}
//                groupId = backStackEntry.arguments?.getLong("id")?:0
            )
        }
        animatedComposable(
            route = Route.INBOX,
        ){
            composeScreens.inbox(
                navigateUp = navController::navigateUp,
                navigateToConversation = { navController.navigate(Route.CONVERSATION id it) }
            )
        }
        animatedComposable(
            route = Route.CONVERSATION arg "id",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
            })
        ){
            composeScreens.conversation(
                navigateUp = navController::navigateUp,
            )
        }
        animatedComposable(
            route = Route.GRUPO_SALAS arg "id",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
            })
        ){
            composeScreens.grupoSalas(
                navigateUp = navController::navigateUp,
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                navigateToSala = {navController.navigate(Route.SALA id it)},
                navigateToSelectEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO_FILTER id it)}
            )
        }

        animatedComposable(route = Route.RECARGAR){
            composeScreens.recargar(
                navigateUp = navController::navigateUp,
                navigateToPay = {navController.navigate(Route.PAY id it)}
            )
        }

        animatedComposable(route = Route.FILTER_SALAS){
            composeScreens.filterSalas(
                navigateUp = navController::navigateUp,
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                navigateToSala = {navController.navigate(Route.SALA id it)},
                navigateToSelectEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO_FILTER id 0)}
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

        AddMainNav(composeScreens, navController,navigateToMap)
    }
}

@ExperimentalAnimationApi
//@Composable
private fun NavGraphBuilder.AddMainNav(
    composeScreens: ComposeScreens,
    navController:NavController,
    navigateToMap:()->Unit
//    openSettings: () -> Unit,
) {

    navigation(
        route = Route.MAIN,
        startDestination = Route.HOME,
    ) {
        composable(route= Route.HOME) {
            composeScreens.home(
                navigateToComplejo = {
                    navController.navigate(Route.ESTABLECIMIENTO id it id 0)
                },
                navController = navController,
                navigateToMap = navigateToMap
            )
        }
        composable(route= Route.DISCOVER) {
            composeScreens.discover(navController = navController)
        }
        composable(route = Route.ACCOUNT){
            composeScreens.account(
                navigateToSettings = { navController.navigate(Route.SETTING) },
                closeDrawer = { },
                navigateToReservas = { navController.navigate(Route.RESERVAS)},
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                navController = navController,
                navigateToRecargaCoins = { navController.navigate(Route.RECARGAR)}
            )
        }
//        composable(route= Route.SERVICIOS){
//            composeScreens.servicios(navController = navController)
//        }
        composable(route= Route.GRUPOS){
            composeScreens.grupos(navController = navController,filterGroups={
                composeScreens.filterGroups(
                    navigateToGroup = {navController.navigate(Route.GRUPO id it)},
                )
            }
        )}

//        composable(route= Route.ACTIVITIES){
//            composeScreens.actividades(navController = navController)
//        }
    }
}
