package app.regate.data.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import app.regate.models.LabelType
import app.regate.models.Labels
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RoomLabelDao:LabelDao,RoomEntityDao<Labels> {

    @Transaction
    @Query("select * from labels where type_label = :type")
    abstract override fun observeLabelByType(type: LabelType): Flow<List<Labels>>

    @Transaction
    @Query("select * from labels where type_label = :type")
    abstract override suspend fun getLabelsByType(type: LabelType): List<Labels>

    @Transaction
    @Query("select * from labels where type_label = :type and id in (:ids)")
    abstract override fun observeLabelByIdsAndType(type: LabelType, ids: List<Long>): Flow<List<Labels>>

}