package app.regate.home.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
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
import app.regate.home.slideInVerticallyComposable
import app.regate.home.uri


@ExperimentalAnimationApi
internal fun NavGraphBuilder.EstablecimientoRoutes(
    composeScreens: ComposeScreens,
    navController: NavController,
//    navigateToMap:()->Unit
//    openSettings: () -> Unit,
) {
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
        route = Route.ESTABLECIMIENTO arg "id" arg "page",
        arguments = listOf(
            navArgument("id") { type = NavType.LongType },
            navArgument("page") { type = NavType.LongType },
        ),
        deepLinks = listOf(navDeepLink { uriPattern = "$uri/${Route.ESTABLECIMIENTO}/{id}/{page}" })
    ) { it ->
        val page = it.arguments?.getLong("page")?:0
        composeScreens.establecimiento(
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
            establecimientoPhotos = { composeScreens.establecimientoPhotos(
                navigateToPhoto = { navController.navigate(Route.PHOTO id it)}
            )},
            currentPage = page.toInt(),
            navController = navController
//                openAuthBottomSheet = { navController.navigate(Route.AUTH_DIALOG) },
        )
    }

}