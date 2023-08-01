package app.regate.data.instalacion

import app.regate.api.ApiResult
import app.regate.data.dto.empresa.establecimiento.CupoInstaDto
import app.regate.data.dto.empresa.establecimiento.CuposRequest
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.data.dto.empresa.instalacion.InstalacionRequest
import app.regate.data.dto.empresa.instalacion.InstalacionesAvailables
import app.regate.data.dto.empresa.instalacion.PaginationInstalacionReponse
import app.regate.models.Instalacion

interface InstalacionDataSource {
    suspend fun getInstalaciones(id:Long): List<Instalacion>
    suspend fun getInstalacion(id:Long):Instalacion
    suspend fun getCupos(d:CuposRequest):List<CupoInstaDto>
    suspend fun getInstalacionesAvailables(d:InstalacionRequest):InstalacionesAvailables

    suspend fun filterInstalaciones(d:FilterInstalacionData,page:Int?):PaginationInstalacionReponse
}

