package app.regate.home.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import app.regate.ComposeScreens
import app.regate.constant.Route
import app.regate.constant.id
import app.regate.home.uri1
import com.google.accompanist.navigation.animation.composable


@ExperimentalAnimationApi
//@Composable
fun NavGraphBuilder.AddMainNav (
    composeScreens: ComposeScreens,
    navController: NavController,
    startPage:Int,
//    navigateToMap:()->Unit
//    openSettings: () -> Unit,
) {
    navigation(startDestination = Route.MAIN, route = Route.INICIO){

    composable(
        route = Route.MAIN + "?data={data}&uuid={uuid}&page={page}",
        arguments = listOf(navArgument("data") {
            defaultValue = ""
            type = NavType.StringType
        },
        navArgument("uuid") {
            defaultValue = ""
            type = NavType.StringType
        },
            navArgument("page") {
                defaultValue = 0
                type = NavType.IntType
            }
        ),
        deepLinks = listOf(
            navDeepLink { uriPattern = "$uri1/${Route.GRUPOS}/{uuid}/{page}" },
            navDeepLink { uriPattern = "$uri1/${Route.DISCOVER}/{data}/{page}" },
            navDeepLink { uriPattern = "$uri1/${Route.NOTIFICATIONS}/{page}" }
        )
    ) { navArguments ->
        val uuid = navArguments.arguments?.getString("uuid") ?: ""
        val page = navArguments.arguments?.getInt("page") ?: 0

        composeScreens.inicio(
            home = {
                composeScreens.home(
                    navigateToComplejo = {
                        navController.navigate(Route.ESTABLECIMIENTO id it id 0)
                    },
                    navController = navController,
//                navigateToMap = navigateToMap
                )
            },
            reservas = {
                composeScreens.discover(navController = navController)
            },
            notifications = {
                composeScreens.notifications(
                    navigateToSala = { navController.navigate(Route.SALA id it) },
                    navigateToAccount = { navController.navigate(Route.BILLING) },
                    navigateToNoticationSetting = {navController.navigate(Route.SETTING)},
                    navController = navController
                )
            },
            chats = {
                composeScreens.grupos(navController = navController,
                    userSalas = { composeScreens.userSalas(
                        navigateToSala = { navController.navigate(Route.SALA id it)},
                    )
                    },
                    uuid = uuid
                )
            },
            account = {
                composeScreens.account(
                    navigateToSettings = { navController.navigate(Route.SETTING) },
                    closeDrawer = { },
                    navigateToReservas = { navController.navigate(Route.RESERVAS)},
                    openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
                    navController = navController,
                    navigateToRecargaCoins = { navController.navigate(Route.RECARGAR)}
                )
            },
            currentPage =  if(page !=0) page else startPage
        )
    }




        composable(
            route = Route.MY_CHATS + "?data={data}",
            arguments = listOf(
                navArgument("data") { type = NavType.StringType;defaultValue ="" },
            ),
        ) {
            composeScreens.chats(
                navController = navController
            )
        }




    }

    }

