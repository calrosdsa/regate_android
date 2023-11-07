package app.regate.home.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import app.regate.ComposeScreens
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.home.animatedComposable
import app.regate.home.uri
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation


@ExperimentalAnimationApi
//@Composable
fun NavGraphBuilder.AddMainNav (
    composeScreens: ComposeScreens,
    navController: NavController,
//    navigateToMap:()->Unit
//    openSettings: () -> Unit,
) {

    navigation(
        route = Route.MAIN,
        startDestination = Route.ACCOUNT,
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
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/${Route.DISCOVER}/{data}" })
        ) {
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
        composable(
            route= Route.GRUPOS + "?uuid={uuid}",
            arguments = listOf(navArgument("uuid") {
                defaultValue = ""
                type = NavType.StringType
            }),
            deepLinks = listOf(navDeepLink { uriPattern = "$uri/${Route.GRUPOS}/{uuid}" })
        ){navArguments->
            val uuid = navArguments.arguments?.getString("uuid")?:""
            composeScreens.grupos(navController = navController,filterGroups={
                composeScreens.filterGroups(
                    navigateUp = navController::navigateUp,
                    navigateToGroup = {navController.navigate(Route.GRUPO id it)},
                    navigateToInfoGrupo = {navController.navigate(Route.INFO_GRUPO id it)},
                    openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)}
                )},
                userSalas = { composeScreens.userSalas(
                    navigateToSala = { it1,it2->
                        navController.navigate(Route.CHAT_SALA id it1 id it2)},
                )
                },
                uuid = uuid
            )}


        animatedComposable(
            route = Route.MY_CHATS + "?data={data}",
            arguments = listOf(
                navArgument("data") { type = NavType.StringType;defaultValue ="" },
            ),
        ) {
            composeScreens.chats(
                navController = navController
            )
        }


//        composable(route= Route.ACTIVITIES){
//            composeScreens.actividades(navController = navController)
//        }
    }
}
