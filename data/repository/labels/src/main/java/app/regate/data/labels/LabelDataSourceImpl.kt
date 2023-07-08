package app.regate.data.labels

import app.regate.data.dto.empresa.labels.LabelDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import me.tatarka.inject.annotations.Inject

@Inject
class LabelDataSourceImpl(
    private val client:HttpClient,
//    private val authStore: AuthStore
): LabelsDataSource {
    override suspend fun getAmenities(): List<LabelDto> {
        return client.get("/v1/label/amenities/").body()
    }

    override suspend fun getCategories(): List<LabelDto> {
        return client.get("/v1/label/categories/").body()
    }

    override suspend fun getSports(): List<LabelDto> {
        return client.get("/v1/label/sports/").body()
    }

    override suspend fun getRules(): List<LabelDto> {
        return client.get("v1/label/rules/").body()
    }

}

