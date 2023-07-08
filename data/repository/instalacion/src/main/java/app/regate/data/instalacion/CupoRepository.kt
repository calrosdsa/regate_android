package app.regate.data.instalacion

import app.regate.data.daos.CupoDao
import app.regate.inject.ApplicationScope
import app.regate.models.Cupo
import app.regate.util.AppCoroutineDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class CupoRepository (
    private val cupoDao: CupoDao,
    private val dispatchers: AppCoroutineDispatchers
    ){
    fun observeCupos(id:Long):Flow<List<Cupo>>{
        return cupoDao.observeCupos(id)
    }

    suspend fun insertCupos(cupos:List<Cupo>){
        cupoDao.deleteCupos()
        cupoDao.upsertAll(cupos)
    }
    suspend fun insertCuposReserva(dates:List<Instant>,id:Long,price:Double){
        withContext(dispatchers.computation){

        cupoDao.deleteCupos()
        dates.map {
            val cupoD = Cupo(
                time = it,
                instalacion_id =id,
                price = price
            )
            cupoDao.upsert(cupoD)
        }
        }
    }

}