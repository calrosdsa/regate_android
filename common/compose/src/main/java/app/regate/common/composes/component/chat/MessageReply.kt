package app.regate.common.composes.component.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import app.regate.compoundmodels.MessageProfile
import app.regate.compoundmodels.UserProfileGrupoAndSala
import app.regate.data.dto.chat.TypeChat
import app.regate.data.dto.empresa.grupo.CupoInstalacion
import kotlinx.datetime.Instant
import app.regate.common.resources.R

@Composable
fun MessageReply (
    item: MessageProfile,
    scrollToItem:()->Unit,
    getUserProfileGrupoAndSala: (id:Long)-> UserProfileGrupoAndSala?,
    navigateToInstalacionReserva: (Long, Long,List<CupoInstalacion>) -> Unit,
    navigateToSala: (Int) -> Unit,
    formatShortDate: (Instant) -> String,
    formatShortTime: (Instant) -> String,
    formatShortDateFromString: (String) -> String,
    formatShortTimeFromString: (String,Int) -> String,
    typeOfChat:TypeChat,
    modifier: Modifier = Modifier
){
    val profile = item.reply?.let { getUserProfileGrupoAndSala(it.profile_id) }
    Surface(
        border = BorderStroke(1.dp, Color.LightGray),
        shape = MaterialTheme.shapes.medium,
        onClick = {
            scrollToItem()

        }
    ) {

        Column(modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
        ) {
            if(typeOfChat != TypeChat.TYPE_CHAT_INBOX_ESTABLECIMIENTO){
            Text(
                text = "${profile?.nombre?:""} ${profile?.apellido ?: ""}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            }
            if(item.reply?.is_deleted == true){
              DeleteMessage()
            }else{
            item.reply?.data?.let { data->
                MessageContent1(
                    content = data,
                    messageType = item.reply?.type_message?:0,
                    navigateToInstalacionReserva = navigateToInstalacionReserva,
                    formatShortDate = formatShortDate,
                    formatShortTime = formatShortTime,
                    formatShortDateFromString = formatShortDateFromString,
                    formatShortTimeFromString = formatShortTimeFromString,
                    navigateToSala = navigateToSala
                )
            }
            Text(
                text = item.reply?.content ?:"",
                style = MaterialTheme.typography.bodySmall,
            )
        }
            }
    }
}

