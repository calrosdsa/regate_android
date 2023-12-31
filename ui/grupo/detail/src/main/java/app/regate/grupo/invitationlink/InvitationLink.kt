package app.regate.grupo.invitationlink

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.LocalAppUtil
import app.regate.common.composes.component.dialog.DialogConfirmation
import app.regate.common.composes.component.dialog.LoaderDialog
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import app.regate.constant.AppUrl1
import app.regate.constant.MainPages
import app.regate.constant.Route
import app.regate.data.auth.AppAuthState
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

typealias InvitationLink= @Composable (
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
    val appUtil = LocalAppUtil.current
    viewState.message?.let {uiMessage ->
        LaunchedEffect(key1 = Unit, block = {
            snackbarHostState.showSnackbar(uiMessage.message)
            clearMessage(uiMessage.id)
        })
    }
    fun shareIntent() {
        if (viewState.authState == AppAuthState.LOGGED_IN) {
            val data = "${AppUrl1}/${Route.GRUPOS}/${viewState.invitationLink?.id_link}/${MainPages.Chat}"
            appUtil.shareTextIntent(context,data)
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
                        shape = CircleShape,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(imageVector = Icons.Filled.Link, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "${AppUrl1}/${Route.GRUPOS}/${invitationLink.id_link}/${MainPages.Chat}")
                }
            Spacer(modifier = Modifier.height(15.dp))

            ShareRow(
                text = stringResource(id = R.string.share_link),
                icon = Icons.Default.Share,
                action = { shareIntent() }
            )
            ShareRow(text = stringResource(id = R.string.copy_link),
                icon = Icons.Default.ContentCopy,
                action = { clipboardManager.setText(AnnotatedString(("${AppUrl1}/${Route.GRUPOS}/${invitationLink.id_link}/${MainPages.Chat}"))) })

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
