package app.regate.common.composes.components.dialog

import android.annotation.SuppressLint
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog


@SuppressLint("ComposeModifierMissing")
@Composable
fun LoaderDialog(
    loading:Boolean,
){
    if(loading){
    Dialog(onDismissRequest = { /*TODO*/ }) {
        CircularProgressIndicator()
    }
    }
}