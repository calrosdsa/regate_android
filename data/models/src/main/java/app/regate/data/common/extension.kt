package app.regate.data.common

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


inline fun <reified T>getDataEntityFromJson(value: String): T?{
    return try{
      Json.decodeFromString(value)
    }catch (e:Exception){
        null
    }
}