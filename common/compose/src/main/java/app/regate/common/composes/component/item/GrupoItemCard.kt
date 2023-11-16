package app.regate.common.composes.component.item

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.regate.common.composes.ui.PosterCardImage
import app.regate.models.grupo.Grupo

@Composable
fun GrupoItemCard(
    grupo: Grupo,
    modifier:Modifier = Modifier,
    navigateToGrupoInfo:(Long)->Unit,
) {
    Surface(onClick = { navigateToGrupoInfo(grupo.id) },
        shape = MaterialTheme.shapes.small
    ) {
        Box(modifier = modifier
            .padding(5.dp)
            .size(110.dp),
        ) {
            PosterCardImage(
                model = grupo.photo, darkerImage = true,
                onClick = {navigateToGrupoInfo(grupo.id)},
                modifier = Modifier.fillMaxSize(), shape = MaterialTheme.shapes.small
            )
            Text(text = grupo.name,style = MaterialTheme.typography.labelMedium, maxLines = 2,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(5.dp),color = Color.White)
        }
    }
}