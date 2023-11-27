package app.regate.data.daos

import app.regate.models.LastUpdatedEntity
import app.regate.models.UpdatedEntity

interface LastUpdatedEntityDao:EntityDao<LastUpdatedEntity> {
    suspend fun getLastUpdatedEntity(entity: UpdatedEntity):LastUpdatedEntity?
}