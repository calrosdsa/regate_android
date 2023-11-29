package app.regate.common.composes.component.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog


@Composable
fun LoaderDialog (
    loading:Boolean,
    modifier:Modifier = Modifier
){
    if(loading){
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Box(modifier = modifier) {
        CircularProgressIndicator()
        }
    }
    }
}