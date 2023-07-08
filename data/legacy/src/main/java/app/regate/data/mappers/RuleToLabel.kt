package app.regate.data.mappers

import app.regate.data.dto.empresa.labels.LabelDto
import app.regate.models.LabelType
import app.regate.models.Labels
import me.tatarka.inject.annotations.Inject

@Inject
class RuleToLabel:Mapper<LabelDto,Labels> {
    override suspend fun map(from: LabelDto): Labels {
        return Labels(
        id = from.id,
        name = from.name,
        type_label = LabelType.RULES
        )
    }
}