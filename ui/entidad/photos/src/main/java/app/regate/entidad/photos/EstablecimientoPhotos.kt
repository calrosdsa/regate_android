package app.regate.entidad.photos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import app.regate.common.composes.ui.PosterCardImage
import app.regate.common.composes.ui.SimpleTopBar
import app.regate.common.composes.util.Layout
import app.regate.common.composes.viewModel
import app.regate.data.common.encodeMediaData
import app.regate.data.dto.empresa.establecimiento.CupoInstaDto
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


typealias EstablecimientoPhotos = @Composable (
    navigateToPhoto:(String)->Unit
) -> Unit

@Inject
@Composable
fun EstablecimientoPhotos(
    viewModelFactory:(SavedStateHandle)-> EstablecimientoPhotosViewModel,
    @Assisted navigateToPhoto: (String) -> Unit
    ){
    EstablecimientoPhotos(
        viewModel = viewModel(factory = viewModelFactory),
        navigateToPhoto = navigateToPhoto,
    )
}


@Composable
internal fun EstablecimientoPhotos(
    viewModel: EstablecimientoPhotosViewModel,
    navigateToPhoto: (String) -> Unit

    ){
    val state by viewModel.state.collectAsState()
//    val formatter = LocalAppDateFormatter.current
    EstablecimientoPhotos(
        viewState = state,
        navigateToPhoto = navigateToPhoto
//        formatDate = { formatter.formatShortDateTime(it) },
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
internal fun EstablecimientoPhotos(
    viewState: EstablecimientoPhotoState,
    navigateToPhoto: (String) -> Unit
//    formatDate:(date:Instant) -> String,

) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {paddingValues->

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 120.dp),
        verticalArrangement = Arrangement.spacedBy(space = 1.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 1.dp),
        contentPadding = PaddingValues(all = 5.dp),
                modifier = Modifier.padding(paddingValues)
    ) {
        itemsIndexed(
            items = viewState.photos
        ) { index, photo ->
            PosterCardImage(model = photo.url,
            modifier= Modifier.fillMaxSize().height(130.dp),
            shape = RoundedCornerShape(0.dp),
                onClick = {
                    val payload = Json.encodeToString(encodeMediaData(viewState.photos.map { it.url },index))
                    navigateToPhoto(payload)
                }
            )
        }
    }
    }
}



