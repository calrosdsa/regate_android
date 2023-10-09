package app.regate.common.composes.component.input.emoji

import kotlinx.serialization.Serializable


@Serializable
data class EmojisState(
    val emoticonos:List<Emoji> = emptyList()
)


@Serializable
data class Emoji(
    val emoji:String,
    val description:String,
    val category:String,
    val aliases:List<String> = emptyList(),
    val tags:List<String> = emptyList(),
)