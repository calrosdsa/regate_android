package app.regate.home.nav

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
import app.regate.home.uri
import com.google.accompanist.navigation.material.bottomSheet

@ExperimentalAnimationApi
internal fun NavGraphBuilder.GrupoRoutes(
    composeScreens: ComposeScreens,
    navController: NavController,
//    navigateToMap:()->Unit
//    openSettings: () -> Unit,
) {
    animatedComposable(
        route = Route.CHAT_GRUPO  +"?id={id}&parentId={parentId}&data={data}&typeChat={typeChat}",
        arguments = listOf(
            navArgument("id") { type = NavType.LongType },
            navArgument("parentId") { type = NavType.LongType },
            navArgument("typeChat") { type = NavType.IntType },
            navArgument("data") {
                type = NavType.StringType
                defaultValue ="2312312" },
        ),
        deepLinks = listOf(navDeepLink { uriPattern = "$uri/${Route.CHAT_GRUPO}/{id}/{parentId}/{typeChat}" })
    ) {
        composeScreens.chatGrupo(
            navigateUp = navController::navigateUp,
            openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
//                navigateToCreateSala = {navController.navigate(Route.ESTABLECIMIENTO_FILTER id it)},
            navigateToGroup = { navController.navigate(Route.GRUPO id it)},
            navigateToInstalacionReserva = {instalacionId,establecimientoId ->
                navController.navigate(Route.RESERVAR id instalacionId id establecimientoId)
            },
            navigateToSala = { navController.navigate(Route.SALA id it)},
            navController = navController
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
            navigateToReport = {navController.navigate(Route.REPORT id it)},
            navController = navController
        )
    }
    animatedComposable(
        route = Route.INFO_GRUPO arg "id",
        arguments = listOf(
            navArgument("id") { type = NavType.LongType },
        )
    ) {
        composeScreens.infoGrupo(
            navigateUp = navController::navigateUp,
            navigateToPhoto = { navController.navigate(Route.PHOTO id it) },
            openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
            navigateToGroup = {navController.navigate(Route.GRUPO id it)}
        )
    }
    bottomSheet(
        route = Route.INVITATION_GRUPO + "?uuid={uuid}",
        arguments = listOf(
            navArgument("uuid") {
                defaultValue = ""
                type = NavType.StringType
            },
        ),
    ) {
        composeScreens.invitationGrupo(
            navigateUp = navController::navigateUp,
            navigateToPhoto = { navController.navigate(Route.PHOTO id it) },
            openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
            navigateToGroup = { navController.navigate(Route.GRUPO id it) }
        )
    }
    animatedComposable(
        route = Route.GRUPO_INVITATION_LINK arg "id",
        arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
            },
        ),
    ) {
        composeScreens.invitationLink(
            navigateUp = navController::navigateUp,
            openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)},
        )
    }

    animatedComposable(
        route = Route.GRUPO_INVITE_USER arg "id",
        arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
            },
        ),
    ) {
        composeScreens.inviteUser(
            navigateUp = navController::navigateUp,
            navigateToProfile = {navController.navigate(Route.PROFILE id it)}
        )
    }

    animatedComposable(
        route = Route.USER_INVITATIONS ,
    ) {
        composeScreens.userInvitations(
            navigateUp = navController::navigateUp,
        )
    }

    animatedComposable(
        route = Route.USER_PENDING_REQUESTS,
    ) {
        composeScreens.userGrupoRequests(
            navigateUp = navController::navigateUp,
        )
    }
    animatedComposable(
        route = Route.PENDING_REQUESTS arg "id",
        arguments = listOf(
            navArgument("id") { type = NavType.LongType },
        )
    ) {
        composeScreens.pendingRequests(
            navigateUp = navController::navigateUp,
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

    animatedComposable(
        route = Route.FILTER_GRUPOS,
    ){
        composeScreens.filterGroups(
            navigateUp = navController::navigateUp,
            navigateToGroup = {navController.navigate(Route.GRUPO id it)},
            navigateToInfoGrupo = {navController.navigate(Route.INFO_GRUPO id it)},
            openAuthBottomSheet = {navController.navigate(Route.AUTH_DIALOG)}
        )
    }


}