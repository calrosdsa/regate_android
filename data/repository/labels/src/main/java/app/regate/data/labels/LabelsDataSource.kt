package app.regate.data.labels

import app.regate.data.dto.empresa.labels.LabelDto

interface LabelsDataSource {
   suspend fun getAmenities():List<LabelDto>
   suspend fun getCategories():List<LabelDto>
   suspend fun getSports():List<LabelDto>
   suspend fun getRules():List<LabelDto>
}

