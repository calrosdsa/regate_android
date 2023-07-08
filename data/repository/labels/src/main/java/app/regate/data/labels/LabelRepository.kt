package app.regate.data.labels

import app.regate.data.daos.LabelDao
import app.regate.data.mappers.AmenityToLabel
import app.regate.data.mappers.CategoryToLabel
import app.regate.data.mappers.RuleToLabel
import app.regate.data.mappers.SportsToLabel
import app.regate.inject.ApplicationScope
import app.regate.models.LabelType
import me.tatarka.inject.annotations.Inject
@ApplicationScope
@Inject
class LabelRepository(
  private val labelDao: LabelDao,
  private val sportsToLabel: SportsToLabel,
  private val categoryToLabel: CategoryToLabel,
  private val amenityToLabel: AmenityToLabel,
  private val labelDataSourceImpl: LabelDataSourceImpl,
  private val ruleToLabel: RuleToLabel
){
    suspend fun getAmenities(){
        val res = labelDataSourceImpl.getAmenities().map{ amenityToLabel.map(it) }
        labelDao.upsertAll(res)
    }
    suspend fun getCategories(){
        val res = labelDataSourceImpl.getCategories().map{ categoryToLabel.map(it) }
        labelDao.upsertAll(res)
    }
    suspend fun getSports(){
        val res = labelDataSourceImpl.getSports().map{ sportsToLabel.map(it) }
        labelDao.upsertAll(res)
    }

    suspend fun getRules(){
        val res = labelDataSourceImpl.getRules().map{ ruleToLabel.map(it) }
        labelDao.upsertAll(res)
    }
}