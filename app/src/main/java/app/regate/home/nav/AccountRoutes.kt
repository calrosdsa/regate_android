package app.regate.home.nav

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import app.regate.ComposeScreens
import app.regate.constant.Route
import app.regate.constant.arg
import app.regate.constant.id
import app.regate.home.animatedComposable

@ExperimentalAnimationApi
internal fun NavGraphBuilder.AccountRoutes(
    composeScreens: ComposeScreens,
    navController: NavController,
//    navigateToMap:()->Unit
//    openSettings: () -> Unit,
) {
    animatedComposable(
        route = Route.PROFILE arg "id",
        arguments = listOf(
            navArgument("id") { type = NavType.LongType },
        )
    ){
        composeScreens.profile(
            navigateUp = navController::navigateUp,
            navigateToEditProfile = {navController.navigate(Route.EDIT_PROFILE id it)},
            navigateToReport = {navController.navigate(Route.REPORT id it)},
            navController = navController
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
        route = Route.PROFILE_CATEGORIES arg "id",
        arguments = listOf(
            navArgument("id") { type = NavType.LongType },
        )
    ){
        composeScreens.profileCategories(
            navigateUp = navController::navigateUp
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
            )},
            montoRetenido = { composeScreens.montoRetenido(
                navigateToSala = {navController.navigate(Route.SALA id it)},
            )}
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
}