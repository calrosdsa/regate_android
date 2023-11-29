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

package app.regate.models


interface AppEntity {
    val id: Long
}

enum class LabelType(val storageKey: String) {
    SPORTS("sports"),
    CATEGORIES("categories"),
    AMENITIES("amenities"),
    RULES("rules")

}
enum class UpdatedEntity(val value: Int) {
    NOTIFICATIONS(1),
    RESERVAS(2);

    companion object {
        fun fromInt(value: Int) =TypeEntity.values().first { it.value == value }
    }
}
enum class TypeEntity(val value: Int) {
    NONE(0),
    SALA(1),
    GRUPO(2),
    ACCOUNT(3),
    BILLING(4),
    RESERVA(5),
    ESTABLECIMIENTO(6),
    URI(7),
    SALA_COMPLETE(8),
    ENTITY_INVITATION(9);


    companion object {
        fun fromInt(value: Int) =TypeEntity.values().first { it.value == value }
    }
}