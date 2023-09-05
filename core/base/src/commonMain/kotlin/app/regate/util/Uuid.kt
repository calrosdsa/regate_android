package app.regate.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID


fun getLongUuid():Long = UUID.randomUUID().mostSignificantBits and Long.MAX_VALUE

fun now():Instant = Clock.System.now()