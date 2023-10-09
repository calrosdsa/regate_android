/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.regate.data.db

import androidx.room.TypeConverter
import app.regate.data.dto.empresa.establecimiento.HorarioInterval
import app.regate.data.dto.empresa.establecimiento.PaidType
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import app.regate.extensions.unsafeLazy
import app.regate.models.LabelType
import app.regate.models.TypeEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


object AppTypeConverters {
   private val imageTypeValues by unsafeLazy { LabelType.values() }
   private val typeEntityValues by unsafeLazy { TypeEntity.values() }
   private val grupoRequestEstado by unsafeLazy { GrupoRequestEstado.values() }

    @TypeConverter
    @JvmStatic
    fun fromListLong(value:List<Long>):String =Json.encodeToString(value)
    @TypeConverter
    @JvmStatic
    fun toListLong(value:String):List<Long> = Json.decodeFromString(value)
    @TypeConverter
    @JvmStatic
    fun fromListHorario(value:List<HorarioInterval>):String =Json.encodeToString(value)
    @TypeConverter
    @JvmStatic
    fun toListHorario(value:String):List<HorarioInterval> = Json.decodeFromString(value)
    @TypeConverter
    @JvmStatic
    fun fromPaidType(value:PaidType):String =Json.encodeToString(value)
    @TypeConverter
    @JvmStatic
    fun toPaidType(value:String):PaidType = Json.decodeFromString(value)

    @TypeConverter
    @JvmStatic
    fun fromLabelType(value: LabelType) = value.storageKey

    @TypeConverter
    @JvmStatic
    fun toLabelType(value: String?) = imageTypeValues.firstOrNull { it.storageKey == value }

    @TypeConverter
    @JvmStatic
    fun fromTypeEntity(value:TypeEntity) = value.ordinal

    @TypeConverter
    @JvmStatic
    fun toTypeEntity(value:Int?) = typeEntityValues.firstOrNull{ it.ordinal == value }
     @TypeConverter
     @JvmStatic
     fun fromGrupoRequestEstado(value:GrupoRequestEstado) = value.ordinal

    @TypeConverter
    @JvmStatic
    fun toGrupoRequestEstado(value:Int?) = grupoRequestEstado.firstOrNull{ it.ordinal == value }

    @TypeConverter
    @JvmStatic
    fun fromHorasSala(value:List<String>):String = Json.encodeToString(value)
    @TypeConverter
    @JvmStatic
    fun toHorasSala(value:String):List<String> = Json.decodeFromString(value)
}
