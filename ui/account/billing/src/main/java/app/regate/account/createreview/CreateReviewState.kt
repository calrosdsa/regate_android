package app.regate.account.createreview

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReview

data class CreateReviewState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT,
    val review: EstablecimientoReview? = null
) {
    companion object {
        val Empty = CreateReviewState()
    }
}