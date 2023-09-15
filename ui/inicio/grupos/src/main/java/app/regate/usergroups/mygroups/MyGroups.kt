package app.regate.usergroups.mygroups

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.SavedStateHandle
import app.regate.common.compose.LocalAppDateFormatter
import app.regate.common.compose.ui.PosterCardImage
import app.regate.common.compose.ui.SimpleTopBar
import app.regate.common.compose.viewModel
import app.regate.data.dto.empresa.grupo.GrupoMessageData
import app.regate.data.dto.empresa.grupo.GrupoMessageInstalacion
import app.regate.data.dto.empresa.grupo.GrupoMessageType
import app.regate.models.Grupo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes
import app.regate.common.resources.R
typealias MyGroups = @Composable (
    navigateUp:()->Unit,
    navigateToChatGrupo:(Long,String)->Unit
)-> Unit

@Inject
@Composable
fun MyGroups(
    viewModelFactory:(SavedStateHandle)->MyGroupsViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToChatGrupo: (Long, String) -> Unit
){
    MyGroups(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToChatGrupo = navigateToChatGrupo
    )
}

@Composable
internal fun MyGroups(
    viewModel: MyGroupsViewModel,
    navigateUp: () -> Unit,
    navigateToChatGrupo: (Long, String) -> Unit
){
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    MyGroups(
        viewState = state,
        navigateToChat = {it1,it2->
            viewModel.navigateToGroupChat(it1,context,it2,navigateToChatGrupo,navigateUp)
        },
        navigateUp = navigateUp,
        sharedMessage = viewModel.getData(),
        selectGroup = viewModel::selectGroup
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
internal fun MyGroups(
    viewState:MyGroupsState,
    sharedMessage:String,
    navigateToChat: (id: Long,content:String) -> Unit,
    navigateUp: () -> Unit,
    selectGroup:(Long)->Unit
){
    var message by remember {
        mutableStateOf("")
    }
    Scaffold(
        topBar = {
            SimpleTopBar(navigateUp = navigateUp, title = if(viewState.selectedGroups.isNotEmpty()) stringResource(
                id = R.string.count_selected,viewState.selectedGroups.size
            ) else "")
        },
        bottomBar = {
            Surface(modifier = Modifier,
            shadowElevation = 10.dp) {
                Column(modifier = Modifier.fillMaxWidth()){
                SharedMessage(data = sharedMessage)
                 BasicTextField(
                     value = message, onValueChange = { message = it },
                     decorationBox = { innerTextField ->
                         Surface(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(10.dp)
                         ) {
                             if (message.isBlank()) {
                                 Text(text = stringResource(id = R.string.add_a_message))
                             }
                                 innerTextField()
                         }
                     },
                 )
                    Divider()
                  AnimatedVisibility(visible = viewState.selectedGroups.isNotEmpty()) {
                      Row(
                          modifier = Modifier.fillMaxWidth(),
                          verticalAlignment = Alignment.CenterVertically
                      ) {
                          FlowRow(modifier = Modifier.fillMaxWidth(0.85f)) {
                              viewState.selectedGroups.map { item ->
                                  Text(text = item.name + " ,",
                                  style = MaterialTheme.typography.labelLarge)
                              }
                          }
                          IconButton(
                              onClick = {
                              if(viewState.selectedGroups.isNotEmpty()){
                                  navigateToChat(viewState.selectedGroups[0].id,message)
                              } },
                              colors = IconButtonDefaults.iconButtonColors(
                                  containerColor = MaterialTheme.colorScheme.primary,
                                  contentColor = MaterialTheme.colorScheme.onPrimary
                              )
                          ) {
                              Icon(imageVector = Icons.Default.Send, contentDescription = "send")
                          }
                      }
                  }

                }
            }
        }
    ) {paddingValues->
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)){
        items(items = viewState.grupos, key = {it.id}){item->
            GrupoSelectedItem(grupo = item, selectGroup = selectGroup,
                isSelected =viewState.selectedGroups.map { it.id }.contains(item.id) )
//            GrupoItem(grupo = item, navigateToChatGrupo = selectGroup)
        }
    }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrupoSelectedItem(
    grupo:Grupo,
    selectGroup: (id: Long) -> Unit,
    isSelected:Boolean,
    modifier:Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { selectGroup(grupo.id) }
            .background(if (isSelected) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.surface)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier
            .size(70.dp)){
        if(isSelected){
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .zIndex(1f)
                    .padding(5.dp),
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Check, contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        PosterCardImage(
            model = grupo.photo, modifier = Modifier.fillMaxSize(),
            shape = CircleShape
        )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = grupo.name, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
internal fun SharedMessage(
    data:String,
) {
    val messageType = try {
        Json.decodeFromString<GrupoMessageData>(data)
    } catch (e: Exception) {
        null
    }
    messageType?.let { type ->
        when (type.type_data) {
            GrupoMessageType.INSTALACION.ordinal -> {
                val formatter = LocalAppDateFormatter.current
                val instalacion = try {
                    Json.decodeFromString<GrupoMessageInstalacion>(type.data)
                } catch (e: Exception) {
                    null
                }
                if (instalacion != null) {
                    Column(modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable {
//                        navigateToInstalacionReserva(instalacion.id.toLong(),instalacion.establecimiento_id.toLong(),instalacion.cupos)
                        }) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.4f)
                                    .height(90.dp)
                            ) {
                                PosterCardImage(
                                    model = instalacion.photo,
                                    modifier = Modifier
                                        .fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column() {

                            Text(
                                text = instalacion.name,
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "${stringResource(id = R.string.total_price)}: ${instalacion.total_price}",
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = stringResource(id = R.string.time_game_will_played),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = formatter.formatShortDate(instalacion.cupos.first().time) +
                                        " ${formatter.formatShortTime(instalacion.cupos.first().time)} a ${
                                            formatter.formatShortTime(
                                                instalacion.cupos.last().time.plus(30.minutes)
                                            )
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
}