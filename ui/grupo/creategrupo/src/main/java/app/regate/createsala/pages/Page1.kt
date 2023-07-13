package app.regate.createsala.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.input.CustomOutlinedTextInput

@Composable
internal fun Page1(
    asunto:String,
    description:String,
    cupos:String,
    onChangeAsunto:(v:String)->Unit,
    onChangeDescription:(v:String)->Unit,
    onChangeCupos:(v:String)->Unit,
    modifier: Modifier = Modifier
){
//    var asunto by remember{ mutableStateOf(asunto) }
//    var description by remember{ mutableStateOf(description) }
//    var cupos by remember{ mutableStateOf("") }
    Box(modifier = modifier.fillMaxSize()){

    Column() {
//        Text(text = "CreateSala ${viewState.loading}")
        Text(text = "Crea una sala y elije cuando quires jugar",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(10.dp))
        CustomOutlinedTextInput(value = asunto, onValueChange = {onChangeAsunto(it)},
            label = "Asunto"
        )
        Spacer(modifier = Modifier.height(10.dp))
        CustomOutlinedTextInput(value = description, onValueChange = {onChangeDescription(it)},
//                placeholder = "Descripcion de la creacion del grupo",
            label = "Descripcióm",
            modifier = Modifier.height(160.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        CustomOutlinedTextInput(value = cupos, onValueChange = {onChangeCupos(it)},
//                placeholder = "Descripcion de la creacion del grupo",
            label = "Cupos",
            modifier = Modifier.height(160.dp)
        )
//        Spacer(modifier = Modifier.height(10.dp))
//        Text(text = "Eljie don quieres jugar77 ${viewState.instalaciones.size}",style = MaterialTheme.typography.labelLarge)
//        Spacer(modifier = Modifier.height(10.dp))

    }
    }
}


