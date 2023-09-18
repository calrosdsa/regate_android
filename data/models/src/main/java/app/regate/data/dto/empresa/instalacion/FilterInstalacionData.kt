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
//    val currentTime:LocalTime = LocalTime.parse("21:00"),
    val currentTime:LocalTime = getMostNearTime(),
    val date:List<String> = emptyList(),
    val time:List<String> = emptyList(),
    val day_week:Int = 0,
    val category_id:Long = 0,
    val amenities:List<Long> = emptyList(),
    val longitud:Double? = null,
    val latitud:Double? = null,
    val interval:Long = 60,
    val near_me:Boolean = false,
    val isInit:Boolean = false
)

fun getMostNearTime():LocalTime{
    return  when(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time.hour){
        0 -> LocalTime.parse("01:00")
        1 -> LocalTime.parse("02:00")
        2 -> LocalTime.parse("03:00")
        3 -> LocalTime.parse("04:00")
        4 -> LocalTime.parse("05:00")
        5 -> LocalTime.parse("06:00")
        6 -> LocalTime.parse("07:00")
        7 -> LocalTime.parse("08:00")
        8 -> LocalTime.parse("09:00")
        9 -> LocalTime.parse("10:00")
        10 -> LocalTime.parse("11:00")
        11 -> LocalTime.parse("12:00")
        12 -> LocalTime.parse("13:00")
        13 -> LocalTime.parse("14:00")
        14 -> LocalTime.parse("15:00")
        15 -> LocalTime.parse("16:00")
        16 -> LocalTime.parse("17:00")
        17 -> LocalTime.parse("18:00")
        18 -> LocalTime.parse("19:00")
        19 -> LocalTime.parse("20:00")
        20 -> LocalTime.parse("21:00")
        21 -> LocalTime.parse("22:00")
        22 -> LocalTime.parse("23:00")
        else -> LocalTime.parse("18:00")
    }
}