
package app.regate.creategroup

import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.input.CustomOutlinedTextInput
import app.regate.common.composes.ui.UploadImageBitmap

//@RequiresApi(Build.VERSION_CODES.P)
@Composable
internal fun Page1(
    asunto:String,
    description:String,
    cupos:String,
    onChangeAsunto:(v:String)->Unit,
    onChangeDescription:(v:String)->Unit,
    onChangeCupos:(v:String)->Unit,
    uploadImage:(type:String,name:String,byteArray:ByteArray)->Unit,
    modifier: Modifier = Modifier
){
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val context = LocalContext.current
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
         uri?.let { returnUri ->
            context.contentResolver.query(returnUri, null, null, null, null)
        }?.use { cursor ->
             try{
                 val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                 val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                 cursor.moveToFirst()
                 //file size 94414 = 94.4k
                 Log.d("DEBUG_APP",cursor.getString(nameIndex))
                 Log.d("DEBUG_APP",cursor.getLong(sizeIndex).toString())
                 val inputStream = context.contentResolver.openInputStream(uri)
                 val imageByteArray = inputStream?.readBytes()
                 val imgType = context.contentResolver.getType(uri).toString()
                 Log.d("DEBUG_APP","uploading image ")
                 if (imageByteArray != null) {
                     uploadImage(imgType,cursor.getString(nameIndex),imageByteArray)
                 }
                 Log.d("DEBUG_APP",imageByteArray.toString())
                 inputStream?.close()
             }catch(e:Exception){
                 Log.d("DEBUG_APP",e.localizedMessage?:"")
             }
        }
        imageUri = uri

    }
    Box(modifier = modifier.fillMaxSize()){

    Column() {
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
        UploadImageBitmap(bitmap = bitmap.value,
            setBitmap = {bitmap.value = it},
            context = context,
            uri = imageUri,
            modifier = Modifier
                .size(100.dp)
//            uploadImage = { launcher.launch("image/*") }
        )
            Spacer(modifier = Modifier.height(5.dp))
            IconButton(onClick = { launcher.launch("image/*") }) {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "camera")
            }
            }
        }

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


