package app.regate.data.dto.empresa.establecimiento

import app.regate.data.dto.account.user.ProfileDto
import kotlinx.serialization.Serializable

@Serializable
data class EstablecimientoReviewDto(
    val establecimiento_id:Long,
    val review:String,
    val score:Int,
    val profile:ProfileDto? = null
)

@Serializable
data class  EstablecimientoReviews(
    val page:Int,
    val average:Double,
    val count:Int,
    val results:List<EstablecimientoReviewDto>
)