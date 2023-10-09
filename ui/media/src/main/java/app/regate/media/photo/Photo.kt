package app.regate.media.photo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.component.images.AsyncImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.viewModel
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import app.regate.common.resources.R
import app.regate.data.dto.system.ReportData
import app.regate.data.dto.system.ReportType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable


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
    val zoomState = rememberZoomState()
    Scaffold(
        topBar = {
//            IconButton(onClick = { navigateUp() }) {
//                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
//            }
            SimpleTopBar(navigateUp = navigateUp,
            actions = {
                IconButton(onClick = {
                    viewState.images
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
            .fillMaxSize()
        ){
            if(viewState.images.isNotEmpty()){
                AsyncImage(
                    model = viewState.images[0],
                    contentDescription =null,
                    modifier = Modifier.fillMaxSize()
                        .zoomable(
                            zoomState = zoomState,
                            onDoubleTap = { position ->
                                val targetScale = when {
                                    zoomState.scale < 2f -> 2f
                                    zoomState.scale < 4f -> 4f
                                    else -> 1f
                                }
                                zoomState.changeScale(targetScale, position)
                            }
                        ),
                    contentScale = ContentScale.Fit,
                )
            }

//            ZoomableComposable()
        }
    }
}

@Composable
internal fun ZoomableComposable() {
    // Reacting to state changes is the core behavior of Compose.
    // We use the state composable that is used for holding a
    // state value in this composable for representing the current
    // value scale(for zooming in the image)
    // & translation(for panning across the image).
    // Any composable that reads the value of counter will
    // be recomposed any time the value changes.
    var scale by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    // In the example below, we make the Column composable zoomable
    // by leveraging the Modifier.pointerInput modifier
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .pointerInput(Unit) {
                    awaitPointerEventScope {
                        awaitFirstDown()
                        do {
                            val event = awaitPointerEvent()
                            scale *= event.calculateZoom()
                            val offset = event.calculatePan()
                            offsetX += offset.x
                            offsetY += offset.y
                        } while (event.changes.any { it.pressed })
                    }
                }
    ) {
        // painterResource method loads an image resource asynchronously
        val imagepainter = painterResource(id = R.drawable.ic_launcher_background)
        // We use the graphicsLayer modifier to modify the scale & translation
        // of the image.
        // This is read from the state properties that we created above.
        Image(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offsetX,
                    translationY = offsetY
                ),
            painter = imagepainter,
            contentDescription = "androids launcher default launcher background image"
        )
    }
}



