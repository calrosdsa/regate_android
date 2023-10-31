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
import app.regate.home.slideInVerticallyComposable
import com.google.accompanist.navigation.material.bottomSheet

@ExperimentalAnimationApi
internal fun NavGraphBuilder.AuthRoutes (
    composeScreens: ComposeScreens,
    navController: NavController,
//    navigateToMap:()->Unit
//    openSettings: () -> Unit,
) {
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
                navController.navigate(Route.HOME){
                    popUpTo(0)
                }
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
            },

        )
    }
    animatedComposable(route = Route.EMAIL_VERIFICATION){
        composeScreens.emailVerification(
            navigateUp = navController::navigateUp,
            navigateToHomeScreen = {
                navController.navigate(Route.HOME){
                    popUpTo(0)
                }
            },
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

}