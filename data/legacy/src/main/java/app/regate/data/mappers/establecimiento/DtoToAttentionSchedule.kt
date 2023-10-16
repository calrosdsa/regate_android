package app.regate.data.mappers.establecimiento

import app.regate.data.dto.empresa.establecimiento.AttentionScheduleDto
import app.regate.data.mappers.Mapper
import app.regate.models.AttentionSchedule
import me.tatarka.inject.annotations.Inject

@Inject
class DtoToAttentionSchedule:Mapper<AttentionScheduleDto,AttentionSchedule> {
    override suspend fun map(from: AttentionScheduleDto): AttentionSchedule {
        return AttentionSchedule(
            id = from.id.toLong(),
            closed = from.closed,
            open = from.open,
            establecimiento_id = from.establecimiento_id.toLong(),
            schedule_interval = from.schedule_interval,
            day_week = from.day_week
        )
    }
}