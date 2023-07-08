package app.regate.data.mappers

import app.regate.data.dto.account.reserva.CupoDto
import app.regate.models.Cupo
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.tatarka.inject.annotations.Inject

@Inject
class CupoToCupoDto:Mapper<Cupo,CupoDto>{
    override suspend fun map(from: Cupo): CupoDto = CupoDto(
        start_date = from.time.toString(),
        precio = from.price.toInt(),
        instalacion_id = from.instalacion_id,
    )
}