package app.regate.data.establecimiento

import app.regate.api.ApiResult
import app.regate.data.daos.EstablecimientoDao
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import app.regate.data.dto.empresa.establecimiento.CuposEstablecimientoRequest
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDetailDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.mappers.EstablecimientoDtoToEstablecimiento
import app.regate.data.mappers.SettingDtoToSetting
import app.regate.inject.ApplicationScope
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class EstablecimientoRepository(
    private val establecimientoDataSourceImpl: EstablecimientoDataSourceImpl,
    private val establecimientoDao: EstablecimientoDao,
    private val establecimientoMapper: EstablecimientoDtoToEstablecimiento,
    private val settingsDtoToSetting: SettingDtoToSetting,
){
    fun observeEstablecimiento(id:Long): Flow<app.regate.models.Establecimiento> {
        return establecimientoDao.observeEstablecimiento(id)
    }

    suspend fun getEstablecimientoCupos(d:CuposEstablecimientoRequest):List<CupoEstablecimiento>{
        return establecimientoDataSourceImpl.getEstablecimientoCupos(d)
    }
    suspend fun getEstablecimientos():List<EstablecimientoDto> {
        val res = establecimientoDataSourceImpl.getEstablecimientos()
//        establecimientoDao.upsertAll(res.map{ establecimientoMapper.map(it)})
        return res
    }

    suspend fun  getEstablecimiento(id:Long): EstablecimientoDetailDto {
        return establecimientoDataSourceImpl.getEstablecimiento(id)
    }

    suspend fun updateEstablecimiento(id:Long){
            val res = getEstablecimiento(id)
            establecimientoDao.upsert(establecimientoMapper.map(res.establecimiento))
            establecimientoDao.insertSettingEstablecimiento(settingsDtoToSetting.map(res.setting_establecimiento))
    }
}