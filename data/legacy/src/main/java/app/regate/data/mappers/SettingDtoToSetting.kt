package app.regate.data.mappers

import app.regate.data.dto.empresa.establecimiento.PaidType
import app.regate.data.dto.empresa.establecimiento.SettingEstablecimientoDto
import app.regate.models.Setting
import me.tatarka.inject.annotations.Inject

@Inject
class SettingDtoToSetting:Mapper<SettingEstablecimientoDto,Setting> {
    override suspend fun map(from: SettingEstablecimientoDto): Setting {
        return Setting(
            uuid = from.uuid,
            establecimiento_id = from.establecimiento_id,
            paid_type = PaidType(list = from.paid_type),
            payment_for_reservation = from.payment_for_reservation,
            horario_interval = from.horario_interval
        )
    }
}