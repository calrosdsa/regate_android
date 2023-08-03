package app.regate.data.dto.empresa.instalacion

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FilterInstalacionData(
    val max_price:Int? = null,
    val currentDate:Long = Clock.System.now().toEpochMilliseconds(),
//    val currentTime:LocalTime = Clock.System.now().toLocalDateTime(TimeZone.UTC).time,
    val currentTime:LocalTime = LocalTime.parse("21:00"),
    val date:List<String> = emptyList(),
    val time:List<String> = emptyList(),
    val day_week:Int = 0,
    val category_id:Long = 0,
    val amenities:List<Long> = emptyList(),
    val longitud:Double? = null,
    val latitud:Double? = null,
    val interval:Long = 60,
    val isInit:Boolean = false
)