package app.regate.data.labels

import app.regate.data.dto.empresa.labels.LabelDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import me.tatarka.inject.annotations.Inject

@Inject
class SearchDataSourceImpl(
    private val client:HttpClient,
//    private val authStore: AuthStore
): SearchDataSource {
}

