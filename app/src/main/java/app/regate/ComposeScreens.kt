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

package app.regate

import app.regate.account.Account
import app.regate.actividades.Actividades
import app.regate.home.Home
import app.regate.account.reservas.Reservas
import app.regate.servicios.Servicios
import app.regate.auth.Login
import app.regate.bottom.auth.BottomAuth
import app.regate.bottom.auth.Map
import app.regate.bottom.reserva.BottomReserva
import app.regate.entidad.salas.Salas
import app.regate.establecimiento.Establecimiento
import app.regate.instalacion.InstalacionDetail
import app.regate.reservar.EstablecimientoReserva
import app.regate.chat.grupo.ChatSala
import app.regate.creategroup.CreateGroup
import app.regate.createsala.CreateSala
import app.regate.createsala.establecimiento.EstablecimientoFilter
import app.regate.discover.DiscoverScreen
import app.regate.discover.filter.Filter
import app.regate.entidad.actividades.ActividadesEstablecimiento
import app.regate.grupo.Grupo
import app.regate.grupos.FilterGroups
import app.regate.grupos.Grupos
import app.regate.profile.Profile
import app.regate.profile.edit.EditProfile
import app.regate.sala.Sala
import app.regate.settings.Setting
import app.regate.signup.SignUp
import me.tatarka.inject.annotations.Inject

//@ActivityScope
@Inject
class ComposeScreens(
    val filter:Filter,
    val login: Login,
    val signUp: SignUp,
    val home: Home,
    val grupos:Grupos,
    val servicios: Servicios,
    val discover:DiscoverScreen,
    val actividades: Actividades,
    val establecimiento: Establecimiento,
    val instalacion: InstalacionDetail,
    val bottomReserva:BottomReserva,
    val bottomAuth:BottomAuth,

    val map:Map,
    val account:Account,
    val profile:Profile,
    val editProfile:EditProfile,
    val reservas: Reservas,

    val actividadesEstablecimiento: ActividadesEstablecimiento,
    val establecimientoReserva:EstablecimientoReserva,
    val establecimientoSalas: Salas,
    val sala: Sala,
    val chatSala: ChatSala,
    val createSala:CreateSala,
    val settings:Setting,

    val grupo:Grupo,
    val filterGroups:FilterGroups,
    val createGroup: CreateGroup,

    val establecimientoFilter:EstablecimientoFilter

    )
