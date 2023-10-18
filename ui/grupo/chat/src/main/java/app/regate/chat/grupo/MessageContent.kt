package app.regate.chat.grupo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.resources.R
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import app.regate.data.dto.empresa.grupo.GrupoMessageType
import app.regate.data.dto.empresa.grupo.MessageInstalacionPayload
import app.regate.data.dto.empresa.grupo.MessageSalaPayload
import kotlinx.datetime.Instant
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.minutes


@Composable
internal fun MessageContent1(
    messageType:Int,
    content:String,
    navigateToInstalacionReserva: (Long, Long,List<CupoInstalacion>) -> Unit,
    navigateToSala:(Int)->Unit,
    formatShortDate: (Instant) -> String,
    formatShortTime: (Instant) -> String,
    formatShortDateFromString: (String) -> String,
    formatShortTimeFromString: (String,Int) -> String,
    modifier: Modifier = Modifier
){
    when(messageType){
        GrupoMessageType.MESSAGE.ordinal ->{
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                modifier = modifier
            )
        }
        GrupoMessageType.INSTALACION.ordinal ->{
            val instalacion = try{ Json.decodeFromString<MessageInstalacionPayload>(content) }catch (e:Exception){
                null
            }
            if(instalacion != null){
                Column(modifier = Modifier
                    .padding(vertical = 5.dp)
                    .clickable {
                        navigateToInstalacionReserva(instalacion.id.toLong(),instalacion.establecimiento_id.toLong(),instalacion.cupos)
                    }) {
                    PosterCardImage(model = instalacion.photo,modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                        onClick = {
                            navigateToInstalacionReserva(instalacion.id.toLong(),instalacion.establecimiento_id.toLong(),instalacion.cupos)
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = instalacion.name,style = MaterialTheme.typography.labelLarge)
                    Text(text = "${stringResource(id = R.string.total_price)}: ${instalacion.total_price}",
                        style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = stringResource(id = R.string.time_game_will_played),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = formatShortDate(instalacion.cupos.first().time) +
                                " ${formatShortTime(instalacion.cupos.first().time)} a ${
                                    formatShortTime(
                                        instalacion.cupos.last().time.plus(30.minutes)
                                    )
                                }",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        GrupoMessageType.SALA.ordinal ->{
            val sala = try {
                Json.decodeFromString<MessageSalaPayload>(content)
            } catch (e: Exception) {
                null
            }

            if (sala != null) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navigateToSala(sala.id)
//                        navigateToInstalacionReserva(instalacion.id.toLong(),instalacion.establecimiento_id.toLong(),instalacion.cupos)
                    }
                    .padding(5.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {

                        Column() {

                            Text(
                                text = sala.titulo,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "${stringResource(id = R.string.total_price)}: ${sala.precio}",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = "${stringResource(id = R.string.precio_per_person)}: ${sala.precio_cupo}",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = stringResource(id = R.string.time_game_will_played),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = formatShortDateFromString(sala.start) +
                                        " ${formatShortTimeFromString(sala.start,0)} a ${
                                            formatShortTimeFromString(sala.end,30)
                                        }",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                    Divider()
                }
            }

        }

    }
}