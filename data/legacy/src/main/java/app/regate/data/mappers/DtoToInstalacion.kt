package app.regate.data.mappers

import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.models.Instalacion

fun InstalacionDto.toInstalacion()= Instalacion(
    id = id,
    name = name,
    cantidad_personas = cantidad_personas,
    establecimiento_id = establecimiento_id,
    precio_hora = precio_hora,
    description = description,
    category_name = category_name,
    category_id = category_id,
    portada = portada
)