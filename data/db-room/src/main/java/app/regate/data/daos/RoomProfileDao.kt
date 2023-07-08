package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.Profile
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomProfileDao:RoomEntityDao<Profile> ,ProfileDao{
    @Transaction
    @Query("select * from profiles where id = :id")
    abstract override fun observeProfile(id:Long):Flow<Profile>

    @Transaction
    @Query("select * from profiles where id in (:ids)")
    abstract override fun observeProfileSalas(ids: List<Long>) :Flow<List<Profile>>
}