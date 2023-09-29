package app.regate.data.app

import kotlinx.serialization.Serializable


@Serializable
data class EmojisState(
    val emoticonos:List<Emoji> = emptyList(),
    val people:List<Emoji> = emptyList(),
    val flags:List<Emoji> = emptyList(),
    val objects:List<Emoji> = emptyList(),
    val symbols:List<Emoji> = emptyList(),
    val animals_nature:List<Emoji> = emptyList(),
    val activities:List<Emoji> = emptyList(),
    val travel_places:List<Emoji> = emptyList(),
    val food_drink:List<Emoji> = emptyList(),
    )


@Serializable
data class Emoji(
    val emoji:String,
    val description:String,
    val category:String,
    val aliases:List<String> = emptyList(),
    val tags:List<String> = emptyList(),
)