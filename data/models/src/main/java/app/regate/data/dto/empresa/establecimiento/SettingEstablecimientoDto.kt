package app.regate.data.dto.empresa.establecimiento

import kotlinx.serialization.Serializable

@Serializable
data class SettingEstablecimientoDto(
    val establecimiento_id:Long,
    val horario_interval: List<HorarioInterval>,
    val paid_type: List<Int>,
    val payment_for_reservation: Int,
    val uuid: String
)

@Serializable
data class PaidType(
    val list:List<Int>
)

enum class PaidTypeEnum {
    LOCAL,
    DEFERRED_PAYMENT,
    UPFRONT_PAYMENT,
}