package app.regate.data.dto.system

import kotlinx.serialization.Serializable

@Serializable
data class ReportData (
    val report_type:Int,
    val entity_id:Int,
    val detail:String = "",
    val profile_id:Long = 0,
  )

enum class ReportType{
    ESTABLECIMIENTO,
    PROFILE,
    GRUPO,
}