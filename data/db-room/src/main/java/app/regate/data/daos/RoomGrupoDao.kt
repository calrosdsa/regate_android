package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.Grupo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomGrupoDao:GrupoDao,RoomEntityDao<Grupo>{
    @Transaction
    @Query("select * from grupos where id = :id")
    abstract override fun observeGrupo(id: Long): Flow<Grupo>

    @Transaction
    @Query("select * from grupos")
    abstract override fun observeGrupos(): Flow<List<Grupo>>
    @Query("delete from grupos")
    abstract override fun deleteAll()

    @Query("select * from grupos where id = :id")
    abstract override fun getGrupo(id: Long): Grupo
}