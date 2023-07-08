package app.regate.map

import app.regate.entidad.actividades.ActividadesEstablecimiento
import app.regate.entidad.salas.Salas
import app.regate.reservar.EstablecimientoReserva
import me.tatarka.inject.annotations.Inject

@Inject
class ComposeScreen (
    val actividadesEstablecimiento: ActividadesEstablecimiento,
//    val establecimientoReserva:EstablecimientoReserva,
    val establecimientoSalas: Salas,
)