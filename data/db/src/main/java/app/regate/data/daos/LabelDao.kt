package app.regate.data.daos

import app.regate.models.LabelType
import app.regate.models.Labels
import kotlinx.coroutines.flow.Flow

interface LabelDao:EntityDao<Labels> {
    fun observeLabelByType(type:LabelType):Flow<List<Labels>>
    suspend fun getLabelsByType(type: LabelType):List<Labels>
    fun observeLabelByIdsAndType(type: LabelType,ids:List<Long>):Flow<List<Labels>>
}