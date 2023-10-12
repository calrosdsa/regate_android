package app.regate.data.establecimiento

import app.regate.data.daos.EstablecimientoDao
import app.regate.data.daos.FavoriteEstablecimientoDao
import app.regate.data.dto.SearchFilterRequest
import app.regate.data.dto.empresa.establecimiento.CupoEstablecimiento
import app.regate.data.dto.empresa.establecimiento.CuposEstablecimientoRequest
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDetailDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReview
import app.regate.data.dto.empresa.establecimiento.EstablecimientoReviews
import app.regate.data.dto.empresa.establecimiento.InitialData
import app.regate.data.dto.empresa.establecimiento.InitialDataFilter
import app.regate.data.dto.empresa.establecimiento.PaginationEstablecimientoResponse
import app.regate.data.dto.empresa.establecimiento.PhotoDto
import app.regate.data.mappers.EstablecimientoDtoToEstablecimiento
import app.regate.data.mappers.SettingDtoToSetting
import app.regate.inject.ApplicationScope
import app.regate.models.FavoriteEstablecimiento
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class EstablecimientoRepository(
    private val establecimientoDataSourceImpl: EstablecimientoDataSourceImpl,
    private val establecimientoDao: EstablecimientoDao,
    private val favoriteEstablecimientoDao: FavoriteEstablecimientoDao,
    private val establecimientoMapper: EstablecimientoDtoToEstablecimiento,
    private val settingsDtoToSetting: SettingDtoToSetting,
    private val dispatchers: AppCoroutineDispatchers,
){
    suspend fun getEstablecimientoPhotos(id:Long):List<PhotoDto>{
        return establecimientoDataSourceImpl.getEstablecimentoPhotos(id)
    }
    suspend fun searcEstablecimientos(d: SearchFilterRequest, page:Int, size: Int):PaginationEstablecimientoResponse{
        return establecimientoDataSourceImpl.searcEstablecimientos(d,page,size)
    }
    suspend fun getReviewUser(establecimientoId:Long):EstablecimientoReview{
        return establecimientoDataSourceImpl.getReviewUser(establecimientoId)
    }
    suspend fun createEstablecimientoReview(d:EstablecimientoReview):EstablecimientoReview{
        return establecimientoDataSourceImpl.createEstablecimientoReview(d)
    }
    suspend fun getEstablecimientoReviews(id: Long,page: Int,size:Int):EstablecimientoReviews{
        return establecimientoDataSourceImpl.getEstablecimientoReview(id,page,size)
    }
    suspend fun getRecommendedEstablecimientos(d:InitialDataFilter,page:Int):PaginationEstablecimientoResponse{
        return establecimientoDataSourceImpl.getRecommendedEstablecimientos(d,page)
    }
    fun checkIsFavorite():Flow<List<Long>>{
        return favoriteEstablecimientoDao.observeFavoriteEstablecimientosIds()
    }
    suspend fun likeEstablecimiento(id:Long){
        withContext(dispatchers.computation){
            establecimientoDataSourceImpl.likeEstablecimiento(id).also {
                favoriteEstablecimientoDao.upsert(FavoriteEstablecimiento(establecimiento_id = id))
            }
        }
    }
    suspend fun removeLikeEstablecimiento(id:Long){
        withContext(dispatchers.computation){
            establecimientoDataSourceImpl.removeLikeEstablecimiento(id).also {
                favoriteEstablecimientoDao.removeFavoriteEstablecimiento(id)
            }
        }
    }
    suspend fun getFavoritosEstablecimiento() {
        withContext(dispatchers.computation) {
            establecimientoDataSourceImpl.getEstablecimientoFavoritos().map {
                FavoriteEstablecimiento(establecimiento_id = it.id.toLong())
            }.also {
            favoriteEstablecimientoDao.removeAll()
            favoriteEstablecimientoDao.upsertAll(it)
            }
        }
    }
    fun observeEstablecimiento(id:Long): Flow<app.regate.models.Establecimiento> {
        return establecimientoDao.observeEstablecimiento(id)
    }

    suspend fun getEstablecimientoCupos(d:CuposEstablecimientoRequest):List<CupoEstablecimiento>{
        return establecimientoDataSourceImpl.getEstablecimientoCupos(d)
    }
    suspend fun getEstablecimientos(d:InitialDataFilter):InitialData {
        val res = establecimientoDataSourceImpl.getEstablecimientos(d)
//        establecimientoDao.upsertAll(res.map{ establecimientoMapper.map(it)})
        return res
    }

    suspend fun getNearEstablecimientos(lng:String,lat:String):List<EstablecimientoDto> {
        val res = establecimientoDataSourceImpl.getNearEstablecimientos(lng,lat)
//        establecimientoDao.upsertAll(res.map{ establecimientoMapper.map(it)})
        return res
    }

    suspend fun  getEstablecimientoDetail(id:Long): EstablecimientoDetailDto {
        return establecimientoDataSourceImpl.getEstablecimientoDetail(id)
    }
    suspend fun  getEstablecimiento(id:Long): EstablecimientoDto {
        return establecimientoDataSourceImpl.getEstablecimiento(id)
    }

    suspend fun updateEstablecimiento(id:Long){
        withContext(dispatchers.computation){
            try{
                val res = getEstablecimiento(id)
                establecimientoDao.upsert(establecimientoMapper.map(res))
            }catch (e:Exception){
                //TODO()
            }
        }
    }

    suspend fun updateEstablecimientoDetail(id:Long){
        withContext(dispatchers.computation){
        try{
            val res = getEstablecimientoDetail(id)
            establecimientoDao.upsert(establecimientoMapper.map(res.establecimiento))
            establecimientoDao.insertSettingEstablecimiento(settingsDtoToSetting.map(res.setting_establecimiento))
        }catch (e:Exception){
            //TODO()
        }
        }
    }
}