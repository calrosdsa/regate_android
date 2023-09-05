package app.regate.home

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
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
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

const val uri = "https://example.com"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeEntry(
    composeScreens: ComposeScreens,
    establecimientoId:String?,
    startScreen:String,
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
    modifier: Modifier = Modifier,
//    navigateToMap:()->Unit
) {
//    val context = LocalContext.current as Activity
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
        animatedComposable(route = Route.REPORT arg  "data",
            arguments = listOf(
                navArgument("data"){ type = NavType.StringType },
            )){
            composeScreens.report(
            navigateUp = navController::navigateUp
            )
        }
        animatedComposable(route = Route.NOTIFICATIONS) {
            composeScreens.notifications(
                navigateUp = navController::navigateUp,
                navigateToSala = { navController.navigate(Route.SALA id it) },
                navigateToAccount = { navController.navigate(Route.BILLING) }
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
                    navController.navigate(Route.CONVERSATION id it1 id it2 )
                },
                navigateToCreateSala = {navController.navigate(Route.CREAR_SALA id it id 0 id 1)},
                navigateToSelectGroup = {navController.navigate(Route.MY_GROUPS + "?data=${it}")}
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
        slideInVerticallyComposable(route = Route.FILTER){
            composeScreens.filter(
                navigateUp = navController::navigateUp
            )
        }

        animatedComposable(route = Route.LOGIN_PAGE) {
            composeScreens.login(
                navigateToSignUpScreen = { navController.navigate(Route.SIGNUP_SCREEN) },
                navigateToHomeScreen = {
                    navController.navigate(Route.HOME)
                },
//                navigateToVerificationEmail = {
//                    navController.navigate(Route.EMAIL_VERIFICATION)
//                }
            )
        }

        animatedComposable(route = Route.SIGNUP_SCREEN) {
            composeScreens.signUp(
                onBack = navController::navigateUp,
            navigateToVerificationEmail = {
                navController.navigate(Route.EMAIL_VERIFICATION)
            })
        }
        animatedComposable(route = Route.EMAIL_VERIFICATION){
            composeScreens.emailVerification(
                navigateUp = navController::navigateUp)
        }
        animatedComposable(route = Route.PHOTO arg "url",
        arguments = listOf(
            navArgument("url") { type = NavType.StringType }
        )){
            val url = it.arguments?.getString("url")?:""
            composeScreens.photo(
                navigateUp = navController::navigateUp,
                url = url,
                navigateToReport = {navController.navigate(Route.REPORT id it)}
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
                navigateUp = { navController.navigateUp() },
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
                        crearSala = { navController.navigate(Route.CREAR_SALA id it id 0 id 0) }
                    )
                },
                currentPage = page.toInt(),
                navigateToPhoto = {
                    val url =  URLEncoder.encode(it, StandardCharsets.UTF_8.toString())
                    navController.navigate(Route.PHOTO id url)
                },
                navigateToProfile = { navController.navigate(Route.PROFILE id it)},
                navigateToReviews = { navController.navigate(Route.REVIEWS id it)},
                navigateToCreateReview = { navController.navigate(Route.CREATE_REVIEW id it)},
//                openAuthBottomSheet = { navController.navigate(Route.AUTH_DIALOG) },
                )
        }

        animatedComposable(
            route = Route.REVIEWS arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            composeScreens.reviews(
                navigateUp = navController::navigateUp,
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                navigateToProfile = { navController.navigate(Route.PROFILE id it)},
                navigateToCreateReview = { navController.navigate(Route.CREATE_REVIEW id it)}
//                navigateToReserva = { navController.navigate(Route.RESERVAR id it) }
            )
        }

        slideInVerticallyComposable(
            route = Route.CREATE_REVIEW arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            composeScreens.createReview(
                navigateUp = navController::navigateUp,
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
        animatedComposableVariant(
            route = Route.SALA_COMPLETE arg "id",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
            )
        ) {
            composeScreens.salaComplete(
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
                navigateToChat = {it1,it2->
                    navController.navigate(Route.CHAT_SALA id it1 id it2) },
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                navigateToInstalacion = {navController.navigate(Route.INSTALACION id it)},
                navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)},
                navigateToComplete = {navController.navigate(Route.SALA_COMPLETE id it)}
            )
        }
        animatedComposable(
            route = Route.CREAR_SALA arg "id" arg "grupo_id" arg "page",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
                navArgument("grupo_id") { type = NavType.LongType },
              navArgument("page") { type = NavType.IntType })
        ) {
            composeScreens.createSala(
                navigateUp = navController::navigateUp,
                reservarInstalacion = {
                    composeScreens.establecimientoReserva(
                        navigateToReservaDetail = { _, _ ->},
                        category = 0
                    )
                },
                navigateToCreateGroup = { navController.navigate(Route.CREATE_GROUP)},
                navigateToGroup = {navController.navigate(Route.GRUPO id it){
                    popUpTo(navController.graph.findStartDestination().id)
                }
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
                    navController.navigate(Route.CREAR_SALA id id id grupoId id 0)
                },
                navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)}
            )
        }

        animatedComposable(
            route = Route.CHAT_GRUPO  +"?id={id}&data={data}",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
                navArgument("data") {
                    type = NavType.StringType
                    defaultValue ="2312312" },
                ),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/chat-grupo/grupo_id={id}" })
        ) {
            composeScreens.chatGrupo(
                navigateUp = navController::navigateUp,
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                navigateToCreateSala = {navController.navigate(Route.ESTABLECIMIENTO_FILTER id it)},
                navigateToGroup = { navController.navigate(Route.GRUPO id it)},
                navigateToInstalacionReserva = {instalacionId,establecimientoId ->
                    navController.navigate(Route.RESERVAR id instalacionId id establecimientoId)
                },
                )
        }
        animatedComposable(
            route = Route.MY_GROUPS + "?data={data}",
            arguments = listOf(
                navArgument("data") { type = NavType.StringType;defaultValue ="" },
            ),
        ) {
            composeScreens.myGroups(
                navigateUp = navController::navigateUp,
                navigateToChatGrupo= {it1,it2->
                    navController.navigate(Route.CHAT_GRUPO + "?id=$it1&data=$it2")
                }
                )
        }
        animatedComposable(
            route = Route.CHAT_SALA arg "id" arg "title",
            arguments = listOf(
                navArgument("id") { type = NavType.LongType },
                navArgument("title") { type = NavType.StringType }
            ),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/chat-sala/sala_id={id}/title={title}" })
        ) {
            composeScreens.chatSala(
                navigateUp = navController::navigateUp,
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                navigateToSala = { navController.navigate(Route.SALA id it)}
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
                navigateToSalas = { navController.navigate(Route.GRUPO_SALAS id it)},
                navigateToReport = {navController.navigate(Route.REPORT id it)}
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
                navigateToEditProfile = {navController.navigate(Route.EDIT_PROFILE id it)},
                navigateToReport = {navController.navigate(Route.REPORT id it)}
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
//                navigateToGroup = { navController.navigate(Route.GRUPO id it){
//                    popUpTo(Route.GRUPOS)
//                } },
                openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)}
//                groupId = backStackEntry.arguments?.getLong("id")?:0
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
        animatedComposable(
            route = Route.BILLING,
        ) {
            composeScreens.billing(
                navigateUp = navController::navigateUp,
                navigateToRecargaCoins = { navController.navigate(Route.RECARGAR)},
                deposits = composeScreens.deposits,
                consume = { composeScreens.consume(
                    navigateToSala = {navController.navigate(Route.SALA id it)},
                    navigateToReserva = { navController.navigate(Route.RESERVAS) }
                )}
            )
        }

            animatedComposable(
            route = Route.CONVERSATION arg "id" arg "establecimientoId",
            arguments = listOf(navArgument("id") {
                type = NavType.LongType
            },navArgument("establecimientoId") {
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

        AddMainNav(composeScreens, navController)
    }
}

@ExperimentalAnimationApi
//@Composable
private fun NavGraphBuilder.AddMainNav(
    composeScreens: ComposeScreens,
    navController:NavController,
//    navigateToMap:()->Unit
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
//                navigateToMap = navigateToMap
            )
        }
        composable(route= Route.DISCOVER+ "?data={data}",
            arguments = listOf(navArgument("data") {
                defaultValue = ""
                type = NavType.StringType
            }),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/discover/data={data}" })) {
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
                )},
                userSalas = { composeScreens.userSalas(
                    navigateToSala = { it1,it2->
                        navController.navigate(Route.CHAT_SALA id it1 id it2)},
                )
            }
        )}

//        composable(route= Route.ACTIVITIES){
//            composeScreens.actividades(navController = navController)
//        }
    }
}
