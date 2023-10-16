@file:Suppress("DEPRECATION")
package app.regate.profile.edit

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.widget.Toast
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.component.dialog.LoaderDialog
import app.regate.common.composes.component.input.InputForm
import app.regate.common.composes.ui.UploadImageBitmap
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

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

@OptIn(ExperimentalMaterial3Api::class)
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

    val sheetState = rememberModalBottomSheetState()


    val file = context.createImageFile()
    val uriF = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
         "app.regate.provider", file
    )

//    var capturedImageUri by remember {
//        mutableStateOf<Uri>(Uri.EMPTY)
//    }

//    val isTream = context.contentResolver.openInputStream(uriF)?.use {
//        it.buffered().readBytes()
//    }


    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            context.setImage(uriF,{bitmapImg.value = it},uploadImage)
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uriF)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

//    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        context.setImage(uri,{bitmapImg.value = it},uploadImage)
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

//        if (capturedImageUri.path?.isNotEmpty() == true) {
//            Image(
//                modifier = Modifier
//                    .padding(16.dp, 8.dp),
//                painter = rememberAsyncImagePainter(capturedImageUri),
//                contentDescription = null
//            )
//        }

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
                    IconButton(onClick = {
                        showBottomSheet = true},
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

        if (showBottomSheet)      {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        launcher.launch("image/*")
                    }
                    .padding(vertical = 15.dp, horizontal = 15.dp)) {
                    Icon(imageVector = Icons.Default.Image, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                Text(text = stringResource(id = R.string.choose_from_gallery),style =MaterialTheme.typography.titleMedium)
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uriF)
                        } else {
                            // Request a permission
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                    .padding(vertical = 15.dp, horizontal = 15.dp)) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = stringResource(id = R.string.take_a_picture),style =MaterialTheme.typography.titleMedium)
                }


                // Sheet content
//                Button(onClick = {
//                    scope.launch { sheetState.hide() }.invokeOnCompletion {
//                        if (!sheetState.isVisible) {
//                            showBottomSheet = false
//                        }
//                    }
//                }) {
//                    Text("Hide bottom sheet")
//                }
                Spacer(modifier = Modifier.height(10.dp))

            }
        }

    }
    }

}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

fun Context.setImage(uri:Uri?,setBitmap:(Bitmap)->Unit,
uploadImage: (String, String, ByteArray) -> Unit){
    uri?.let { returnUri ->
        contentResolver.query(returnUri, null, null, null, null)
    }?.use { cursor ->
        try{
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            //file size 94414 = 94.4k
            val inputStream = contentResolver.openInputStream(uri)
            val imageByteArray = inputStream?.readBytes()
            val imgType = contentResolver.getType(uri).toString()
            if (imageByteArray != null) {
                uploadImage(imgType,cursor.getString(nameIndex),imageByteArray)
            }
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
               setBitmap(bitmap)

            } else {
                val source = ImageDecoder
                    .createSource(contentResolver,uri)
                val bitmap = ImageDecoder.decodeBitmap(source)
                setBitmap(bitmap)
            }
            inputStream?.close()
        }catch(e:Exception){
            //TODO()
        }
    }
}