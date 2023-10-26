package app.regate.home.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import app.regate.ComposeScreens
import app.regate.constant.Route
import app.regate.constant.arg
import app.regate.constant.id
import app.regate.home.animatedComposable
import app.regate.home.animatedComposableVariant
import app.regate.home.uri

@ExperimentalAnimationApi
internal fun NavGraphBuilder.SalaRoutes(
    composeScreens: ComposeScreens,
    navController: NavController,
//    navigateToMap:()->Unit
//    openSettings: () -> Unit,
) {
    animatedComposable(route = Route.FILTER_SALAS){
        composeScreens.filterSalas(
            navigateUp = navController::navigateUp,
            openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
            navigateToSala = {navController.navigate(Route.SALA id it)},
            navigateToSelectEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO_FILTER id 0)}
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
        deepLinks = listOf(navDeepLink { uriPattern = "$uri/${Route.SALA}/{id}" })
    ) {
        composeScreens.sala(
            navigateUp = navController::navigateUp,
            navigateToChat = {it1,it2->
                navController.navigate(Route.CHAT_SALA id it1 id it2) },
            openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
            navigateToInstalacion = {navController.navigate(Route.INSTALACION id it)},
            navigateToEstablecimiento = {navController.navigate(Route.ESTABLECIMIENTO id it id 0)},
            navigateToComplete = {navController.navigate(Route.SALA_COMPLETE id it)},
            navigateToSelectGroup = {navController.navigate(Route.MY_GROUPS + "?data=${it}")},
            navController = navController
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
                popUpTo(Route.CREAR_SALA arg "id" arg "grupo_id" arg "page"){
                    inclusive = true
                }
//                popUpTo(Route.ESTABLECIMIENTO_FILTER arg "grupo_id"){
//                    inclusive = true
//                }
            }
            },
            navigateToRecargaScreen = {navController.navigate(Route.RECARGAR)}
        )
    }


}