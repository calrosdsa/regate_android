package app.regate.media.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.compose.ui.PosterCardImage
import app.regate.common.compose.ui.SimpleTopBar
import app.regate.common.compose.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.dto.system.ReportData
import app.regate.data.dto.system.ReportType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


typealias Photo = @Composable (
    navigateUp:()->Unit,
    navigateToReport:(String)->Unit
) -> Unit


@Inject
@Composable
fun Photo(
    viewModelFactory:(SavedStateHandle)->PhotoViewModel,
    @Assisted navigateUp: () -> Unit,
    @Assisted navigateToReport:(String)->Unit
){
    Photo(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp,
        navigateToReport = navigateToReport)
}

@Composable
internal fun Photo(
    viewModel: PhotoViewModel,
    navigateUp:()->Unit,
    navigateToReport:(String)->Unit
){
    val state by viewModel.state.collectAsState()
    Photo(viewState = state,
        navigateUp = navigateUp,
        navigateToReport = navigateToReport)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Photo(
    viewState:PhotoState,
    navigateUp:()->Unit,
    navigateToReport:(String)->Unit
) {
    Scaffold(
        topBar = {
//            IconButton(onClick = { navigateUp() }) {
//                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
//            }
            SimpleTopBar(navigateUp = navigateUp,
            actions = {
                IconButton(onClick = {
                    val report = ReportData(
                        report_type = ReportType.ESTABLECIMIENTO.ordinal,
                        entity_id = 1,
                    )
                    navigateToReport(Json.encodeToString(report)) }) {
                    Icon(imageVector = Icons.Outlined.Flag,
                        contentDescription = stringResource(id = R.string.report))
                }
            })
        }
    ) {paddingValues ->
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .fillMaxSize()){
            Text(text = viewState.images[0])
            PosterCardImage(model = viewState.images[0],
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.Fit
            )
        }
    }
}