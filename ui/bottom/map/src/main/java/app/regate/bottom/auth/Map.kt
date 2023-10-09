package app.regate.bottom.auth


import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import app.regate.common.composes.util.Layout
import app.regate.common.composes.viewModel
import app.regate.common.resources.R
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject


typealias Map = @Composable (
    navigateUp:()->Unit,

) -> Unit

@Inject
@Composable
fun Map(
    viewModelFactory:()-> MapViewModel,
    @Assisted navigateUp: () -> Unit
){
    Map(
        viewModel = viewModel(factory = viewModelFactory),
        navigateUp = navigateUp
    )
}


@Composable
internal fun Map(
    viewModel: MapViewModel,
    navigateUp: () -> Unit
){
    val state by viewModel.state.collectAsState()
//    val formatter = LocalAppDateFormatter.current
    Map(
        viewState = state,
//        navigateUp = navigateUp,
        onMessageShown = viewModel::clearMessage,
       navigateUp = navigateUp
    )
}

@SuppressLint("InflateParams")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Map(
    viewState: MapState,
    onMessageShown:(id:Long) -> Unit,
    navigateUp: () -> Unit
//    openAuthDialog: () -> Unit
) {
    val context = LocalContext.current
    val point = Point.fromLngLat(18.06, 59.31)
    val bitmap = BitmapFactory.decodeResource(context.resources, app.regate.common.resources.R.drawable.red_marker)
    val snackbarHostState = remember { SnackbarHostState() }
    val dismissSnackbarState = rememberDismissState(
        confirmValueChange = { value ->
            if (value != DismissValue.Default) {
                snackbarHostState.currentSnackbarData?.dismiss()
                true
            } else {
                false
            }
        },
    )


    viewState.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
            // Notify the view model that the message has been dismissed
            onMessageShown(message.id)
            navigateUp()
        }
    }

    Box() {
        SnackbarHost(hostState = snackbarHostState) { data ->
            SwipeToDismiss(
                state = dismissSnackbarState,
                background = {},
                dismissContent = { Snackbar(snackbarData = data) },
                modifier = Modifier
                    .padding(horizontal = Layout.bodyMargin)
                    .fillMaxWidth(),
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {context->
                    val viewAnnotationViews = mutableListOf<View>()
                    val view = LayoutInflater.from(context).inflate(R.layout.mapbox, null, false)
                    val mapView = view.findViewById<MapView>(R.id.mapView).apply {
                        getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS){
                        }
                    }
                    val annotationApi = mapView.annotations
                    val pointAnnotationManager = annotationApi.createPointAnnotationManager()
                    val viewAnnotationManager = mapView.viewAnnotationManager
                    val item = LayoutInflater.from(context).inflate(R.layout.item_callout_view,null,false)
                    val viewAnnotation = viewAnnotationManager.addViewAnnotation(
                        resId = R.layout.item_callout_view,
                        options = viewAnnotationOptions {
                            geometry(point)
                            allowOverlap(true)
//                            associatedFeatureId(pointAnnotation.featureIdentifier)
//                            anchor(ViewAnnotationAnchor.BOTTOM)
//                            offsetY((pointAnnotation.iconImageBitmap?.height!!).toInt())
                        }
                    )
                    item.findViewById<Button>(R.id.selectButton).setOnClickListener {
                        Log.d("DEBUG_XML","CLICKED")
//                        val button = b as Button
//                        val isSelected = button.text.toString().equals("SELECT", true)
//                        val pxDelta = if (isSelected) SELECTED_ADD_COEF_PX else -SELECTED_ADD_COEF_PX
//                        button.text = if (isSelected) "DESELECT" else "SELECT"
//                        viewAnnotationManager.updateViewAnnotation(
//                            viewAnnotation,
//                            viewAnnotationOptions {
//                                selected(isSelected)
//                            }
//                        )
//                        (button.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                            bottomMargin += pxDelta
//                            rightMargin += pxDelta
//                            leftMargin += pxDelta
//                        }
//                        button.requestLayout()
                    }
                    viewAnnotationViews.add(viewAnnotation)

                    val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage(bitmap)
                        .withIconSize(0.4)


                    pointAnnotationManager.create(pointAnnotationOptions)
                    view
                })
//
        }
    }
}


const val SELECTED_ADD_COEF_PX = 25
const val STARTUP_TEXT = "Click on a map to add a view annotation."
const val ADD_VIEW_ANNOTATION_TEXT = "Add view annotations to re-frame map camera"


