package app.regate.system.report

import app.regate.api.UiMessage

data class ReportState(
    val loading:Boolean = false,
    val message:UiMessage? = null,
){
    companion object{
        val Empty = ReportState()
    }
}


data class Option(
    val text:String,
    val id:Int,
)