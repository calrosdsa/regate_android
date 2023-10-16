package app.regate.data.dto.empresa.establecimiento

import kotlinx.serialization.Serializable

@Serializable
data class AttentionScheduleDto(
    val id:Int,
    val day_week:Int,
    val establecimiento_id:Int,
    val open:Boolean,
    val closed:Boolean,
    val schedule_interval:List<AttentionScheduleTimeDto> = emptyList()
)

@Serializable
data class AttentionScheduleTimeDto(
    val start_time:String="",
    val end_time:String="",
)
