@file:Suppress("DEPRECATION")

package app.regate.profile.edit

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.components.dialog.LoaderDialog
import app.regate.common.composes.components.input.InputForm
import app.regate.common.composes.ui.UploadImageBitmap
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


typealias EditProfile = @Composable (
    navigateUp:()->Unit
) -> Unit

@Inject
@Composable
fun EditProfile(
    viewModelFactory:(SavedStateHandle)-> EditProfileViewModel,
    @Assisted navigateUp: () -> Unit,
) {
    EditProfile(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )
}

@Composable
internal fun EditProfile(
    viewModel: EditProfileViewModel,
    navigateUp: () -> Unit
){
    val state by viewModel.state.collectAsState()
    LoaderDialog(loading = state.loading)
//    val formatter = LocalAppDateFormatter.current
    EditProfile(
        viewState = state,
        navigateUp = navigateUp,
        uploadImage = viewModel::uploadImage,
        editProfile = viewModel::editProfile,
        clearMessage = viewModel::clearMessage
        )
}

@Composable
internal fun EditProfile(
    viewState: EditProfileState,
    navigateUp: () -> Unit,
    editProfile:(nombre:String,apellido:String)->Unit,
    uploadImage:(String,String,ByteArray)->Unit,
    clearMessage:(Long)->Unit
) {
    var nombre by remember(viewState.profile) {
        mutableStateOf(viewState.profile?.nombre?:"")
    }
    var apellido by remember(viewState.profile) {
        mutableStateOf(viewState.profile?.apellido?:"")
    }
    var description by remember(viewState.profile) {
        mutableStateOf("")
    }

    val context = LocalContext.current
    val bitmapImg =  remember {
        mutableStateOf<Bitmap?>(null)
    }
    val snackbarHostState = remember { SnackbarHostState() }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->


        uri?.let { returnUri ->
            context.contentResolver.query(returnUri, null, null, null, null)
        }?.use { cursor ->
            try{
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                cursor.moveToFirst()
                //file size 94414 = 94.4k
                val inputStream = context.contentResolver.openInputStream(uri)
                val imageByteArray = inputStream?.readBytes()
                val imgType = context.contentResolver.getType(uri).toString()
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
                inputStream?.close()
            }catch(e:Exception){
                //TODO()
            }
        }
    }
    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            clearMessage(message.id)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            IconButton(onClick = { navigateUp() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
            }
        },
        bottomBar = {
            BottomAppBar() {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)) {
                    Button(onClick = { editProfile(nombre,apellido) }, modifier = Modifier.align(Alignment.CenterEnd)) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }
        }
    ) {paddingValues ->  
        
    Box(modifier = Modifier
        .padding(paddingValues)
        .fillMaxSize()){

        Column(modifier = Modifier.padding(10.dp)) {
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

            Text(text = stringResource(id = R.string.edit),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(10.dp))
            InputForm(
                value = nombre, onValueChange = {
                    if (it.length <= 25) {
                        nombre = it
                    }
                },
                label = stringResource(id = R.string.name),
            ){
                Text(text = "${nombre.length}/25",style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp))
            }
            InputForm(value = apellido, onValueChange = {if(it.length <= 25){ apellido = it}},
                label =stringResource(id = R.string.family_name),
            ){
                Text(text = "${apellido.length}/25",style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(2.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            InputForm(value = description,
                onValueChange = {if(it.length < 255){ description = it}},
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
        }
    }
    }

}
