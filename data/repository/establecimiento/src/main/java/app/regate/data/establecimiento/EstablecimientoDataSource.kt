package app.regate.data.establecimiento

import app.regate.api.ApiResult
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import app.regate.data.dto.empresa.establecimiento.CuposEstablecimientoRequest
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDetailDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.models.Establecimiento

interface EstablecimientoDataSource {
    suspend fun getEstablecimientos():List<EstablecimientoDto>
    suspend fun getEstablecimiento(id:Long): EstablecimientoDetailDto
    suspend fun getEstablecimientoCupos(d:CuposEstablecimientoRequest):List<CupoEstablecimiento>
    suspend fun getEstablecimientoFavoritos():List<EstablecimientoDto>
    suspend fun likeEstablecimiento(id:Long)
    suspend fun removeLikeEstablecimiento(id:Long)
}

