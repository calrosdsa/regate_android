package app.regate.data.app

import kotlinx.serialization.Serializable


@Serializable
data class EmojisState(
    val emoticonos:List<EmojiDto> = emptyList(),
    val people:List<EmojiDto> = emptyList(),
    val flags:List<EmojiDto> = emptyList(),
    val objects:List<EmojiDto> = emptyList(),
    val symbols:List<EmojiDto> = emptyList(),
    val animals_nature:List<EmojiDto> = emptyList(),
    val activities:List<EmojiDto> = emptyList(),
    val travel_places:List<EmojiDto> = emptyList(),
    val food_drink:List<EmojiDto> = emptyList(),
    )


@Serializable
data class EmojiDto(
    val id:Int = 0,
    val emoji:String,
    val description:String,
    val category:String,
//    val aliases:List<String> = emptyList(),
//    val tags:List<String> = emptyList(),
)

object EmojiCategory {
    const val Emoticonos = "Emoticonos"
    const val Gente = "Gente"
    const val Animales = "animales"
    const val Alimentos = "Alimento"
    const val Viajar = "Viajar"
    const val Actividades = "Actividades"
    const val Objetos = "Objetos"
    const val Simbolos = "Símbolos"
    const val Banderas = "Banderas"

}