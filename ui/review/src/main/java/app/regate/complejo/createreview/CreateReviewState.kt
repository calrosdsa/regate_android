package app.regate.complejo.createreview

import app.regate.api.UiMessage
import app.regate.data.auth.AppAuthState
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReviewDto

data class CreateReviewState(
    val loading: Boolean = false,
    val message: UiMessage? = null,
    val authState: AppAuthState = AppAuthState.LOGGED_OUT,
    val review: EstablecimientoReviewDto? = null
) {
    companion object {
        val Empty = CreateReviewState()
    }
}