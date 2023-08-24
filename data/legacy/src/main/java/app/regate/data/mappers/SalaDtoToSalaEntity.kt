package app.regate.data.mappers

import app.regate.data.dto.empresa.salas.SalaDto
import app.regate.models.SalaEntity
import me.tatarka.inject.annotations.Inject

@Inject
class SalaDtoToSalaEntity:Mapper<SalaDto,SalaEntity> {
    override suspend fun map(from: SalaDto): SalaEntity {
        return SalaEntity(
            id = from.id,
            instalacion_id = from.instalacion_id,
            establecimiento_id = from.establecimiento_id,
            cupos = from.cupos,
            descripcion = from.descripcion,
            titulo = from.titulo,
            precio = from.precio,
            horas = from.horas,
            created_at = from.created_at,
            category_id = from.category_id,
            users = from.users,
            grupo_id = from.grupo_id
        )
    }
}