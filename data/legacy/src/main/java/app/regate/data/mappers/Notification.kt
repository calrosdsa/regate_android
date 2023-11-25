package app.regate.data.mappers

import app.regate.data.dto.system.NotificationDto
import app.regate.models.Notification
import app.regate.models.TypeEntity

fun NotificationDto.toNotification():Notification{
    return Notification(
        id = id,
        title = title,
        content = content,
        created_at = created_at,
        typeEntity = type_entity.let { it1 -> TypeEntity.fromInt(it1) },
        entityId = entity_id,
        image = image,
        read = false
    )
}