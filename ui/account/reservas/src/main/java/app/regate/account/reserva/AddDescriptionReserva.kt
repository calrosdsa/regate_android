package app.regate.account.reserva

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import app.regate.common.composes.component.input.InputForm
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.PeekHeight
import com.dokar.sheets.m3.BottomSheet
import app.regate.common.resources.R

@Composable
internal fun AddDescriptionBottomSheen(
    state: BottomSheetState,
    description:String,
    save:(String)->Unit,
){
    var value by remember {
        mutableStateOf(description)
    }
    BottomSheet(
        state = state,
//        peekHeight = PeekHeight.dp(400),
        // Set to true you don't want the peeked state.
        skipPeeked = false,
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            InputForm(value = value, onValueChange = {if(it.length <= 500){ value = it}},
                modifier = Modifier.height(200.dp),
                label = "Descripción",
            ){
                Text(text = "${value.length}/500",style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))

                Box(modifier = Modifier.fillMaxWidth()){
                    Button(onClick = { save(value) },modifier = Modifier.align(Alignment.BottomEnd)) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }

        }


    }
}