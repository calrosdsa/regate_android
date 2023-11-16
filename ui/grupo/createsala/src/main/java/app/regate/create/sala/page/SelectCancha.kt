package app.regate.create.sala.page

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import app.regate.common.composes.component.input.InputForm
import app.regate.common.resources.R
import app.regate.compoundmodels.InstalacionCupos
import app.regate.data.dto.empresa.grupo.GrupoVisibility
import app.regate.data.dto.empresa.salas.SalaVisibility
import kotlinx.datetime.Instant

@Composable
internal fun Page1(
    asunto:String,
    description:String,
    cupos:String,
    visibility:Int,
    onChangeAsunto:(v:String)->Unit,
    onChangeDescription:(v:String)->Unit,
    onChangeCupos:(v:String)->Unit,
    onChangeVisibility:(Int)->Unit,
    instalacionCupos: InstalacionCupos?,
    formatDate:(Instant)->String,
    formatShortTime:(Instant)->String,
//    max_cupos:Int,
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
            InstalacionSelected(
                instalacionCupos = instalacionCupos,
                formatDate = formatDate,
                formatShortTime = formatShortTime,)
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
                onValueChange = {if(it.length <= 255){onChangeDescription(it)}},
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
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.group_privacy),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(5.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .clickable { onChangeVisibility(SalaVisibility.PUBLIC.ordinal) }
                .padding(5.dp)) {
                RadioButton(
                    selected = visibility == SalaVisibility.PUBLIC.ordinal,
                    onClick = { onChangeVisibility(SalaVisibility.PUBLIC.ordinal) })
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = stringResource(id = R.string.publico))
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .clickable { onChangeVisibility(SalaVisibility.ONLY_GROUP.ordinal) }
                .padding(5.dp)) {
                RadioButton(
                    selected = visibility == SalaVisibility.ONLY_GROUP.ordinal,
                    onClick = { onChangeVisibility(SalaVisibility.ONLY_GROUP.ordinal) })
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Solo visible para el grupo")
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



