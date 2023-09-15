package app.regate.common.compose.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogConfirmation(
    open:Boolean,
    dismiss:()->Unit,
    confirm:()->Unit,
    descripcion:String = ""
){
    if(open){
        AlertDialog(onDismissRequest = { dismiss() }) {
            Column(modifier = Modifier
                .clip(ShapeDefaults.Medium)
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
            ) {
            Text(text = "Porfavor confirme para continuar", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = descripcion, style = MaterialTheme.typography.labelLarge)
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier) {
                    TextButton(onClick = { dismiss() }) {
                        Text(text = "Descartar")
                    }
                    TextButton(onClick = {
                        confirm()
                        dismiss()
                    }) {
                        Text(text = "Aceptar")
                    }
                }
            }
        }
    }
}