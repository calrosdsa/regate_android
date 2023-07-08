package app.regate.data.mappers

import app.regate.data.dto.empresa.establecimiento.CupoInstaDto
import app.regate.models.Cupo
import me.tatarka.inject.annotations.Inject

@Inject
class DtoToCupo:Mapper<CupoInstaDto,Cupo>{
    override suspend fun map(from: CupoInstaDto): Cupo = Cupo(
        price = from.price.toDouble(),
        time = from.time,
        instalacion_id = from.instalacion_id,
    )
}