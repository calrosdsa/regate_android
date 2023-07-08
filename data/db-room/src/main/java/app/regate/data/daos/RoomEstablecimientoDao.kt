package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.Establecimiento
import app.regate.models.LabelType
import app.regate.models.Labels
import app.regate.models.Setting
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomEstablecimientoDao : EstablecimientoDao, RoomEntityDao<Establecimiento>{

    @Transaction
    @Query("SELECT * FROM establecimientos")
    abstract override fun getEstablecimientos(): Flow<List<Establecimiento>>

//    @Transaction
//    @Query("select l.id,l.name,l.thumbnail,l.type_label" +
//            " from (select category_id  from instalaciones " +
//            "where establecimiento_id = :id group by category_id) as i " +
//            "inner join labels as l on l.id = i.category_id and type_label = :type;")
//    abstract override fun observeEstablecimientoCategories(id: Long,type:LabelType): Flow<List<Labels>>

    @Transaction
    @Query("SELECT * FROM establecimientos WHERE  id = :id")
    abstract override fun observeEstablecimiento(id:Long): Flow<Establecimiento>

    @Transaction
    @Query("SELECT * FROM settings WHERE establecimiento_id = :id")
    abstract override fun observeEstablecimientoSetting(id:Long):Flow<Setting>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract override suspend fun insertSettingEstablecimiento(entity: Setting)
}