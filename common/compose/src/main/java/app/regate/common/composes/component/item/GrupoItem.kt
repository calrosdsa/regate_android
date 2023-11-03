package app.regate.common.composes.component.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import app.regate.common.resources.R
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage
import app.regate.data.dto.empresa.grupo.GrupoDto
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import app.regate.data.dto.empresa.grupo.GrupoVisibility

@Composable
fun GrupoItem (
    grupo:GrupoDto,
    navigate:(Long)->Unit,
    joinToGroup:(Long,Int)->Unit,
    modifier: Modifier = Modifier,
    navigateToInfoGrupo:(Long)->Unit={}
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                when(grupo.grupo_request_estado){
                    GrupoRequestEstado.NONE.ordinal -> {
                        navigateToInfoGrupo(grupo.id)
                    }
                    GrupoRequestEstado.PENDING.ordinal ->{
                        navigateToInfoGrupo(grupo.id)
                    }
                    else -> {
                        navigate(grupo.id)
                    }
                }
            }
            .padding(10.dp),
    ) {
        PosterCardImage(
            model = grupo.photo, modifier = Modifier
                .size(70.dp), shape = CircleShape,
            onClick = {
                when(grupo.grupo_request_estado){
                    GrupoRequestEstado.NONE.ordinal -> {
                        navigateToInfoGrupo(grupo.id)
                    }
                    GrupoRequestEstado.PENDING.ordinal ->{
                        navigateToInfoGrupo(grupo.id)
                    }
                    else -> {
                        navigate(grupo.id)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.width(10.dp))

        Column() {
            Text(text = grupo.name, style = MaterialTheme.typography.titleMedium)
            
            Row() {
            Text(text = if(grupo.visibility == GrupoVisibility.PUBLIC.ordinal) stringResource(id = R.string.publico) else stringResource(
                id = R.string.privado), style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = stringResource(id = R.string.members,grupo.members),
                style = MaterialTheme.typography.titleSmall)
            }
            Spacer(modifier = Modifier.height(1.dp))
            Button(
                onClick = {
                    when(grupo.grupo_request_estado){
                        GrupoRequestEstado.NONE.ordinal -> {
                            joinToGroup(grupo.id,grupo.visibility)
                        }
                        GrupoRequestEstado.PENDING.ordinal ->{
                            navigateToInfoGrupo(grupo.id)
                        }
                        else -> {
                            navigate(grupo.id)
                        }
                    }
                          },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                when(grupo.grupo_request_estado){
                    GrupoRequestEstado.JOINED.ordinal -> {
                Text(text = stringResource(id = R.string.visit))
                    }
                    GrupoRequestEstado.PENDING.ordinal -> {
                        Text(text = stringResource(id = R.string.pending_request))
                    }
                    GrupoRequestEstado.NONE.ordinal-> {
                        Text(text = stringResource(id = R.string.join_a_group))
                    }
                }
            }
        }
    }
}