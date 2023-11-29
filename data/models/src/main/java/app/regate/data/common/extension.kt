package app.regate.data.common

import app.regate.data.app.MediaData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


inline fun <reified T>getDataEntityFromJson(value: String): T?{
    return try{
      Json.decodeFromString(value)
    }catch (e:Exception){
        null
    }
}

fun encodeMediaData(urls:List<String>,selectedIndex:Int?=null):MediaData{
    val mutableList = mutableListOf<String>()
    for(url in urls){
        val encodeUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
        mutableList.add(encodeUrl)
    }
    return MediaData(
        images = mutableList.toList(),
        selectedIndex = selectedIndex?:0
    )
}