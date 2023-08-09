package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.compoundmodels.InstalacionCategoryCount
import app.regate.models.Instalacion
import app.regate.models.LabelType
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomInstalacionDao: InstalacionDao,RoomEntityDao<Instalacion> {
    @Transaction
    @Query("SELECT * FROM instalaciones WHERE establecimiento_id = :id")
    abstract override fun observeInstalaciones(id:Long): Flow<List<Instalacion>>

    @Transaction
    @Query("SELECT * FROM instalaciones WHERE id = :id")
    abstract override fun observeInstalacion(id: Long): Flow<Instalacion>

    @Transaction
    @Query("SELECT * FROM instalaciones WHERE id  in (:ids)")
    abstract override fun observeInstalacionesAvailables(ids:List<Long>): Flow<List<Instalacion>>

    @Query("select l.name,i.category_id,i.count,l.thumbnail" +
            " from (select category_name,category_id,  count(category_id)  as count from instalaciones " +
            "where establecimiento_id = :id group by category_id,category_name) as i " +
            "inner join labels as l on l.id = i.category_id and type_label = :type;")
    abstract override fun observeGroupInstalacionByCategory(id:Long,type:LabelType): Flow<List<InstalacionCategoryCount>>

    @Query("DELETE from instalaciones where id = :id")
    abstract override suspend fun delete(id: Long)

    @Query("DELETE FROM instalaciones")
    abstract override suspend fun deleteAll()
}