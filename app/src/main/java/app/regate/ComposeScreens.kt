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
import app.regate.account.billing.Billing
import app.regate.account.billing.consume.Consume
import app.regate.account.billing.deposits.Deposits
import app.regate.account.reserva.Reserva
import app.regate.actividades.Actividades
import app.regate.home.Home
import app.regate.account.reservas.Reservas
import app.regate.servicios.Servicios
import app.regate.auth.signin.Login
import app.regate.bottom.auth.BottomAuth
import app.regate.bottom.auth.Map
import app.regate.bottom.reserva.Reservar
import app.regate.entidad.salas.Salas
import app.regate.establecimiento.Establecimiento
import app.regate.instalacion.InstalacionDetail
import app.regate.reservar.EstablecimientoReserva
import app.regate.chat.grupo.ChatGrupo
import app.regate.coin.paid.Pay
import app.regate.coin.recargar.Recargar
import app.regate.inbox.Conversations
import app.regate.creategroup.CreateGroup
import app.regate.create.sala.establecimiento.EstablecimientoFilter
import app.regate.discover.DiscoverScreen
import app.regate.discover.filter.Filter
import app.regate.entidad.actividades.ActividadesEstablecimiento
import app.regate.favorites.Favorites
import app.regate.grupo.Grupo
import app.regate.grupos.FilterGroups
import app.regate.grupos.Grupos
import app.regate.conversation.Conversation
import app.regate.media.photo.Photo
import app.regate.profile.Profile
import app.regate.profile.edit.EditProfile
import app.regate.sala.Sala
import app.regate.settings.Setting
import app.regate.auth.signup.SignUp
import app.regate.auth.signup.emailverification.EmailVerification
import app.regate.chat.sala.ChatSala
import app.regate.create.sala.CreateSala
import app.regate.complejo.createreview.CreateReview
import app.regate.complejo.reviews.Reviews
import app.regate.filterSalas.FilterSalas
import app.regate.sala.grupo.GrupoSalas
import app.regate.system.notification.Notifications
import app.regate.system.report.Report
import app.regate.welcome.Welcome
import me.tatarka.inject.annotations.Inject

//@ActivityScope
@Inject
class ComposeScreens(
    val welcome:Welcome,
    val report:Report,
    val notifications:Notifications,
    val filter:Filter,
    val login: Login,
    val signUp: SignUp,
    val emailVerification:EmailVerification,
    val home: Home,
    val grupos:Grupos,
    val servicios: Servicios,
    val discover:DiscoverScreen,
    val actividades: Actividades,
    val establecimiento: Establecimiento,
    val instalacion: InstalacionDetail,
    val bottomReserva:Reservar,
    val bottomAuth:BottomAuth,

    val map:Map,
    val account:Account,
    val profile:Profile,
    val editProfile:EditProfile,
    val reservas: Reservas,
    val reserva:Reserva,

    val actividadesEstablecimiento: ActividadesEstablecimiento,
    val establecimientoReserva:EstablecimientoReserva,
    val establecimientoSalas: Salas,
    val sala: Sala,
    val filterSalas:FilterSalas,
    val chatGrupo: ChatGrupo,
    val chatSala:ChatSala,
    val createSala: CreateSala,
    val settings:Setting,
    val favorites:Favorites,
    val conversation:Conversation,
    val inbox:Conversations,
    val billing:Billing,
    val deposits: Deposits,
    val consume:Consume,
    val recargar:Recargar,
    val pay:Pay,

    val photo:Photo,

    val grupo:Grupo,
    val grupoSalas:GrupoSalas,
    val filterGroups:FilterGroups,
    val createGroup: CreateGroup,

    val establecimientoFilter: EstablecimientoFilter,

    val reviews: Reviews,
    val createReview: CreateReview,
    )
