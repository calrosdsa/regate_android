package app.regate.data.daos

import app.regate.models.AttentionSchedule
import app.regate.models.Establecimiento
import app.regate.models.LabelType
import app.regate.models.Labels
import app.regate.models.Setting
import kotlinx.coroutines.flow.Flow

interface EstablecimientoDao:EntityDao<Establecimiento> {
    fun getEstablecimiento(id:Long):Flow<Establecimiento>
    fun getEstablecimientos():Flow<List<Establecimiento>>
    fun observeEstablecimiento(id:Long):Flow<Establecimiento>
//    fun observeEstablecimientoCategories(id:Long,type:LabelType):Flow<List<Labels>>
    fun observeEstablecimientoSetting(id:Long):Flow<Setting>
    fun observeAttentionScheduleTime(id:Long,dayWeek:Int):Flow<AttentionSchedule>
    fun observeAttentionScheduleWeek(id:Long):Flow<List<AttentionSchedule>>


    suspend fun insertSettingEstablecimiento(entity:Setting)
    suspend fun insertAttentionScheduleTime(entity: AttentionSchedule)

    suspend fun delete(id:Long)
    suspend fun deleteAll()

}