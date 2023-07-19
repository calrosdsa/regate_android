package app.regate.profile

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.account.AccountViewModel
import app.regate.common.composes.LocalAppDateFormatter
import app.regate.common.composes.ui.CommonTopBar
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.viewModel
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R

typealias Profile = @Composable (
    navigateUp:()->Unit,
    navigateToEditProfile:(Long)->Unit
) -> Unit

@Inject
@Composable
fun Profile(
    viewModelFactory:(SavedStateHandle)-> ProfileViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToEditProfile: (Long) -> Unit
) {
    Profile(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToEditProfile = navigateToEditProfile
    )
}

@Composable
internal fun Profile(
    viewModel:ProfileViewModel,
    navigateUp: () -> Unit,
    navigateToEditProfile: (Long) -> Unit
){
    val state by viewModel.state.collectAsState()
    val formatter = LocalAppDateFormatter.current
    Profile(
        viewState = state,
        navigateUp = navigateUp,
        formatterDate = formatter::formatMediumDate,
        navigateToEditProfile = navigateToEditProfile
    )
}

@Composable
internal fun Profile(
    viewState:ProfileState,
    navigateUp: () -> Unit,
    formatterDate:(Instant)->String,
    navigateToEditProfile: (Long) -> Unit
) {
    val scrollState = rememberScrollState()
    val like = remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            Column() {
                Row(horizontalArrangement = Arrangement.SpaceBetween,modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { navigateUp() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
                    if(viewState.profile?.id == viewState.user?.profile_id){
                    TextButton(onClick = { viewState.profile?.id?.let { navigateToEditProfile(it) } }) {
                        Text(text = stringResource(id = R.string.edit),style = MaterialTheme.typography.labelLarge,
                        textDecoration = TextDecoration.Underline)
                    }
                    }
                }
                Divider()
            }
        },
        modifier = Modifier.padding(horizontal =  10.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(vertical = 10.dp)
                .verticalScroll(scrollState)
        ) {
            viewState.profile?.let {profile->
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(vertical = 15.dp).fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically,modifier = Modifier.fillMaxWidth(0.8f)) {
                    PosterCardImage(
                        model = profile.profile_photo, modifier = Modifier
                            .size(60.dp), shape = CircleShape
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column() {
                        Text(
                            text = "${profile.nombre} ${profile.apellido ?: ""}",
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                        profile.created_at?.let { formatterDate(it)}?.let {
                        Text(
//                            text = "Usuario desde  ${profile.created_at?.let { formatterDate(it) }}",
                            text = stringResource(R.string.user_since,it),
                            style = MaterialTheme.typography.labelSmall,
                        )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Place, contentDescription = null,modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Santa Cruz de la Sierra",style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    }
                    Crossfade(targetState = like.value) {
                        if(it){
                            IconButton(onClick = { like.value = !like.value }) {
                                Icon(imageVector = Icons.Default.ThumbUp, contentDescription = "like")
                            }
                        }else{
                            IconButton(onClick = { like.value = !like.value  }) {
                                Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = "dis_like")
                            }
                        }
                    }

                }
                Text(text = "An Administrator has access to the entire system, including the Account Setup tab." +
                        "They can update and run the model, create needs and work orders, add new users, and run reports." +
                        " You can have as",
                style = MaterialTheme.typography.bodySmall,modifier = Modifier.padding(vertical = 15.dp))


                Surface(modifier = Modifier.padding(15.dp),
                shadowElevation = 10.dp, shape = MaterialTheme.shapes.medium) {
                    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "3",style = MaterialTheme.typography.labelLarge)
                            Text(text = "Partidos jugados",style = MaterialTheme.typography.labelSmall)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "2",style = MaterialTheme.typography.labelLarge)
                            Text(text = "Recomendaciones",style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}


