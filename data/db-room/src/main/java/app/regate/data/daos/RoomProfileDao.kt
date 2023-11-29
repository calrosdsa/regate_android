package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import app.regate.models.LabelType
import app.regate.models.Labels
import app.regate.models.user.Profile
import app.regate.models.ProfileCategory
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomProfileDao:RoomEntityDao<Profile> ,ProfileDao{
    @Transaction
    @Query("select * from profiles where id = :id")
    abstract override fun observeProfile(id:Long):Flow<Profile>

    @Transaction
    @Query("""
        select l.* from profile_category as p 
        left join labels as l on l.id = p.category_id and l.type_label = :typeLabel
           where p.profile_id = :id
        """)
    abstract override fun observeProfileCategory(id: Long,typeLabel:LabelType): Flow<List<Labels>>

    @Transaction
    @Query("select * from profiles where id in (:ids)")
    abstract override fun observeProfileSalas(ids: List<Long>) :Flow<List<Profile>>

    @Query("select * from profiles where id = :id")
    abstract override suspend fun getProfile(id:Long):Profile

    @Query("delete from profile_category where profile_id = :id")
    abstract override suspend fun deleteAllProfileCategories(id: Long)

    @Upsert
    abstract override suspend fun insertProfileCategories(entities: List<ProfileCategory>)
}