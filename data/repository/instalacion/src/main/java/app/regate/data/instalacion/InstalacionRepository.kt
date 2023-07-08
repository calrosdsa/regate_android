package app.regate.data.instalacion

import app.regate.api.ApiResult
import app.regate.data.daos.InstalacionDao
import app.regate.data.daos.LabelDao
import app.regate.data.dto.empresa.establecimiento.CupoInstaDto
import app.regate.data.dto.empresa.establecimiento.CuposRequest
import app.regate.data.dto.empresa.instalacion.FilterInstalacionData
import app.regate.data.dto.empresa.instalacion.InstalacionDto
import app.regate.data.dto.empresa.instalacion.InstalacionRequest
import app.regate.data.dto.empresa.instalacion.InstalacionesAvailables
import app.regate.data.mappers.toInstalacion
import app.regate.inject.ApplicationScope
import app.regate.models.Instalacion
import app.regate.models.LabelType
import app.regate.models.Labels
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class InstalacionRepository(
    private val instalacionDataSourceImpl: InstalacionDataSourceImpl,
    private val instalacionDao: InstalacionDao,
    private val labelDao:LabelDao,
    private val dispatchers: AppCoroutineDispatchers
){
    suspend fun filterInstacion(d:FilterInstalacionData,page:Int?): List<Pair<InstalacionDto, List<Labels>>>{
        val res =  instalacionDataSourceImpl.filterInstalaciones(d,page)
//        withContext(dispatchers.computation){
//        val instalaciones = res.map { it.toInstalacion() }
//        instalacionDao.upsertAll(instalaciones)
//        }
        val amenitiesInstalacion = labelDao.getLabelsByType(LabelType.AMENITIES)
        val amenitiesInsta = res.map {instalacion->
             instalacion.amenities.map {amenityId->
                amenitiesInstalacion.first {label->
                    label.id == amenityId }
            }
        }
        return res zip amenitiesInsta
    }
    fun observeInstalacionesAvailables(ids:List<Long>):Flow<List<Instalacion>>{
        return instalacionDao.observeInstalacionesAvailables(ids)
    }
    suspend fun getInstalacionesAvailables(d:InstalacionRequest):InstalacionesAvailables{
        return instalacionDataSourceImpl.getInstalacionesAvailables(d)
    }
    fun observeInstalaciones(id:Long): Flow<List<Instalacion>>{
        return instalacionDao.observeInstalaciones(id)
    }

    fun observeInstalacion(id:Long):Flow<Instalacion>{
        return instalacionDao.observeInstalacion(id)
    }
    suspend fun getCupos(d:CuposRequest):List<CupoInstaDto>{
        return instalacionDataSourceImpl.getCupos(d)
    }
    suspend fun getInstalaciones(id:Long):List<Instalacion>{
        return instalacionDataSourceImpl.getInstalaciones(id).also{
            instalacionDao.upsertAll(it)
        }
    }

    suspend fun getInstalacion(id:Long):Instalacion{
        return instalacionDataSourceImpl.getInstalacion(id).also {
            instalacionDao.upsert(it)
        }
    }
}