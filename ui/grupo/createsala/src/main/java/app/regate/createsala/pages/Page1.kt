package app.regate.createsala.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import app.regate.common.composes.components.input.CustomOutlinedTextInput
import app.regate.common.composes.components.input.InputForm
import app.regate.data.dto.empresa.grupo.GroupVisibility
import app.regate.common.resources.R
import app.regate.createsala.NumberPicker

@Composable
internal fun Page1(
    asunto:String,
    description:String,
    cupos:String,
    onChangeAsunto:(v:String)->Unit,
    onChangeDescription:(v:String)->Unit,
    onChangeCupos:(v:String)->Unit,
    max_cupos:Int,
    modifier: Modifier = Modifier
) {
//    var asunto by remember{ mutableStateOf(asunto) }
//    var description by remember{ mutableStateOf(description) }
//    var cupos by remember{ mutableStateOf("") }
//    val numberPicker = remember {
//        mutableStateOf(false)
//    }
    Box(modifier = modifier.fillMaxSize()) {

        Column {
//        Text(text = "CreateSala ${viewState.loading}")

            Spacer(modifier = Modifier.height(10.dp))
            InputForm(value = asunto, onValueChange = {if(it.length <= 25){ onChangeAsunto(it)}},
                label = "Asunto",
            ){
                Text(text = "${asunto.length}/25",style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            InputForm(value = description,
                onValueChange = {if(it.length < 255){onChangeDescription(it)}},
//                placeholder = "Descripcion de la creacion del grupo",
                label = "Descripcióm",
                modifier = Modifier.height(160.dp),
                maxLines = 5,
//                maxCharacters = 255,
//                currentCharacters = description.length
            ){
                Text(text = "${description.length}/255",style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))

            InputForm(value = cupos, onValueChange = {onChangeCupos(it)},
                label = "Cupos",
                keyboardType = KeyboardType.Number
            ){
                Text(text = "Max: $max_cupos",style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp))
            }


        }

//        CustomOutlinedTextInput(value = cupos, onValueChange = {onChangeCupos(it)},
////                placeholder = "Descripcion de la creacion del grupo",
//            label = "Cupos",
//            modifier = Modifier.height(160.dp)
//        )
//        Spacer(modifier = Modifier.height(10.dp))
//        Text(text = "Eljie don quieres jugar77 ${viewState.instalaciones.size}",style = MaterialTheme.typography.labelLarge)
//        Spacer(modifier = Modifier.height(10.dp))

    }
}



