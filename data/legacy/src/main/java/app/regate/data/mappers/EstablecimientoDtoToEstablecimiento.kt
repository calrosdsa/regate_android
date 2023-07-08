package app.regate.data.mappers

import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.models.Establecimiento
import me.tatarka.inject.annotations.Inject

@Inject
class EstablecimientoDtoToEstablecimiento:Mapper<EstablecimientoDto, Establecimiento> {
    override suspend fun map(from: EstablecimientoDto): Establecimiento =
       Establecimiento(
            id = from.id.toLong(),
            name = from.name,
            photo = from.photo,
            portada = from.portada,
            created_at = from.created_at,
            address = from.address,
            phone_number = from.phone_number,
            empresa_id = from.empresa_id,
            email = from.email,
            latidud = from.latitud,
            longitud = from.longitud,
            description = from.description,
            amenities = from.amenities,
            rules = from.rules
        )

}

//fun EstablecimientoDto.toEstablecimiento() = Establecimiento(
//    id = from.id.toLong(),
//    name = from.name,
//    photo = from.photo,
//    portada = from.portada,
//    created_at = from.created_at,
//    address = from.address,
//    phone_number = from.phone_number,
//    empresa_id = from.empresa_id,
//    email = from.email,
//    latidud = from.latidud,
//    longitud = from.longitud
//)