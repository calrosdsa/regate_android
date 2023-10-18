package app.regate.grupo.invitationlink

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.component.dialog.LoaderDialog
import app.regate.common.composes.ui.Loader
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.constant.AppUrl
import app.regate.constant.Route
import app.regate.data.auth.AppAuthState
import app.regate.data.common.encodeMediaData
import app.regate.data.dto.empresa.grupo.GrupoRequestEstado
import app.regate.data.dto.empresa.grupo.GrupoVisibility
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias InvitationLink = @Composable (
    navigateUp:()->Unit,
    openAuthBottomSheet: () -> Unit,
    ) -> Unit

@Inject
@Composable
fun InvitationLink(
    viewModelFactory:(SavedStateHandle)-> InvitationLinkViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted openAuthBottomSheet:()->Unit,
    ){
    InvitationLink(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        openAuthBottomSheet = openAuthBottomSheet,
    )
}

@Composable
internal fun InvitationLink(
    viewModel: InvitationLinkViewModel,
    navigateUp: () -> Unit,
    openAuthBottomSheet: () -> Unit,
){
    val state by viewModel.state.collectAsState()
    InvitationLink(
        viewState = state,
        navigateUp = navigateUp,
//        cancelRequest = viewModel::cancelRequest,
        openAuthBottomSheet = openAuthBottomSheet,
        resetInvitationLink = viewModel::resetInvitationLink,
        clearMessage = viewModel::clearMessage
    )
        LoaderDialog(loading = state.loading)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InvitationLink(
    viewState: InvitationLinkState,
    resetInvitationLink:()->Unit,
    navigateUp: () -> Unit,
    openAuthBottomSheet: () -> Unit,
    clearMessage:(Long)->Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val openResetDialog = remember {
        mutableStateOf(false)
    }
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    viewState.message?.let {uiMessage ->
        LaunchedEffect(key1 = Unit, block = {
            snackbarHostState.showSnackbar(uiMessage.message)
            clearMessage(uiMessage.id)
        })
    }
    fun shareIntent() {
        if (viewState.authState == AppAuthState.LOGGED_IN) {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        } else {
            openAuthBottomSheet()
        }
    }

    DialogConfirmation(open = openResetDialog.value,
        dismiss = { openResetDialog.value = false },
        confirm = {
                  resetInvitationLink()
            openResetDialog.value = false
        },
//        descripcion = "Si deseas continuar se restablecera el enlace del grupo"
    )
    Scaffold(modifier = Modifier,
        topBar = {
            SimpleTopBar(
                navigateUp = { navigateUp() },
                title = stringResource(id = R.string.invitation_link)
            )
        },
        snackbarHost ={ SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(
                text = stringResource(id = R.string.invitation_link_info),
                modifier = Modifier.padding(10.dp), style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

                viewState.invitationLink?.let { invitationLink ->
            Row(modifier = Modifier
                .clickable {
                    shareIntent()
                }
                .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                    FloatingActionButton(
                        onClick = { shareIntent() },
                        shape = CircleShape
                    ) {
                        Icon(imageVector = Icons.Filled.Link, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "${AppUrl}/${Route.GRUPO}/${invitationLink.id_link}")
                }
            Spacer(modifier = Modifier.height(15.dp))

            ShareRow(
                text = stringResource(id = R.string.share_link),
                icon = Icons.Default.Share,
                action = { shareIntent() }
            )
            ShareRow(text = stringResource(id = R.string.copy_link),
                icon = Icons.Default.ContentCopy,
                action = { clipboardManager.setText(AnnotatedString(("${AppUrl}/${Route.GRUPO}/${invitationLink.id_link}"))) })

                    ShareRow(
                        text = stringResource(id = R.string.reset_link),
                        icon = Icons.Default.Refresh,
                        action = { openResetDialog.value = true }
                    )
            if (viewState.authState == AppAuthState.LOGGED_IN) {
                Text(text = "")
            }
            }
        }
    }
}

@Composable
internal fun ShareRow(
    text:String,
    icon:ImageVector,
    action:()->Unit,
){
    Row(modifier = Modifier
        .clickable { action() }
        .padding(horizontal = 32.dp, vertical = 20.dp)
        .fillMaxWidth()
        ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}

//@Composable
//internal fun GrupoInfo(
//    grupo:GrupoDto,
//){
//
//}
