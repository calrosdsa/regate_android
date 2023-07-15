package app.regate.data.dto.empresa.grupo

import app.regate.data.dto.FileData

data class GroupRequest(
    val description:String= "",
    val name :String = "",
    val visibility:Int = 0,
    val fileData:FileData? = null
)