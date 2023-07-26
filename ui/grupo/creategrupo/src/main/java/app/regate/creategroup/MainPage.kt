@file:Suppress("DEPRECATION")

package app.regate.creategroup

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.regate.common.composes.components.input.InputForm
import app.regate.common.composes.ui.UploadImageBitmap
import app.regate.common.resources.R
import app.regate.data.dto.empresa.grupo.GroupVisibility

//@RequiresApi(Build.VERSION_CODES.P)
@Composable
internal fun MainPage(
    asunto:String,
    description:String,
    visibility:Int,
    onChangeVisibility:(v: Int)->Unit,
    onChangeAsunto:(v:String)->Unit,
    onChangeDescription:(v:String)->Unit,
//    group:Grupo?,
    uploadImage:(type:String,name:String,byteArray:ByteArray)->Unit,
    modifier: Modifier = Modifier
){

    val context = LocalContext.current
    val bitmapImg =  remember {
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
                     if (Build.VERSION.SDK_INT < 28) {
                         val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver,uri)
                         bitmapImg.value = bitmap

                     } else {
                         val source = ImageDecoder
                             .createSource(context.contentResolver,uri)
                         val bitmap = ImageDecoder.decodeBitmap(source)
                         bitmapImg.value = bitmap
                     }
                 Log.d("DEBUG_APP",imageByteArray.toString())
                 inputStream?.close()
             }catch(e:Exception){
                 Log.d("DEBUG_APP",e.localizedMessage?:"")
             }
        }
    }
    Box(modifier = modifier.fillMaxSize()){

    Column() {
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                if(group?.photo != null){
//                    PosterCardImage(model = group.photo,
//                    modifier = Modifier.size(100.dp),
//                    shape = CircleShape)
//                }else{

        UploadImageBitmap(bitmap = bitmapImg.value,
            modifier = Modifier
                .size(100.dp)
//            uploadImage = { launcher.launch("image/*") }
        )

//                }
            IconButton(onClick = { launcher.launch("image/*") },
                colors= IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            modifier = Modifier.offset(y=-(30).dp)) {
                Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "camera")
            }
            }
        }

        Text(text = "Crea una sala y elije cuando quires jugar",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,color = MaterialTheme.colorScheme.primary)
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



        Text(text = stringResource(id = R.string.group_visibility),
            style = MaterialTheme.typography.titleSmall,color = MaterialTheme.colorScheme.primary,
            modifier= Modifier.padding(5.dp))

        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxWidth()
            .clickable { onChangeVisibility(GroupVisibility.PUBLIC.ordinal) }.padding(5.dp)) {
            RadioButton(selected = visibility == GroupVisibility.PUBLIC.ordinal, onClick = { onChangeVisibility(GroupVisibility.PUBLIC.ordinal)})
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(id = R.string.publico))
        }

        Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxWidth()
            .clickable { onChangeVisibility(GroupVisibility.PRIVATE.ordinal) }.padding(5.dp)) {
            RadioButton(selected = visibility == GroupVisibility.PRIVATE.ordinal, onClick = { onChangeVisibility(GroupVisibility.PRIVATE.ordinal)})
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(id = R.string.privado))
        }
//        Spacer(modifier = Modifier.height(10.dp))
//        Text(text = "Eljie don quieres jugar77 ${viewState.instalaciones.size}",style = MaterialTheme.typography.labelLarge)
//        Spacer(modifier = Modifier.height(10.dp))

    }
    }
}


