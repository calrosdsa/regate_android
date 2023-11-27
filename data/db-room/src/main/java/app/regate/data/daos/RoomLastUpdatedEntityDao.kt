package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import app.regate.models.LastUpdatedEntity
import app.regate.models.UpdatedEntity

@Dao
abstract class RoomLastUpdatedEntityDao:RoomEntityDao<LastUpdatedEntity>,LastUpdatedEntityDao {
    @Query("select * from last_updated_entity where entity_id = :entity")
    abstract override suspend fun getLastUpdatedEntity(entity: UpdatedEntity): LastUpdatedEntity?
}