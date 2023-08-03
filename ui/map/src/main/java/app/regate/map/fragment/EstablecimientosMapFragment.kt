@file:Suppress("DEPRECATION")

package app.regate.map.fragment

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto
import app.regate.map.databinding.EstablecimientoMapFragmentBinding
import app.regate.map.databinding.ItemCalloutViewBinding
import app.regate.map.viewmodel.EstablecimientosMapState
import app.regate.map.viewmodel.EstablecimientosMapViewModel
import app.regate.map.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import app.regate.map.R as A


@Inject
class EstablecimientosMapFragment (viewModel:()-> EstablecimientosMapViewModel) : Fragment(app.regate.map.R.layout.establecimiento_map_fragment){
    private val viewModel by viewModels(viewModel)
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var bottomSheet:ConstraintLayout
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var mapView:MapView
    private lateinit var state: EstablecimientosMapState
    private lateinit var establecimientoMapBinding:EstablecimientoMapFragmentBinding
    private var viewAnnotationList:MutableList<View> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        establecimientoMapBinding = DataBindingUtil
            .inflate(inflater,A.layout.establecimiento_map_fragment,container,false)
        return establecimientoMapBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        view.findNavController().navigate(A.id.establecimientoFragment)
        viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach {
            state = it
            it.establecimientos.map {establecimiento->
                prepareViewAnnotation(
                    Point.fromLngLat(
                        establecimiento.longitud?.toDouble()?:0.0,
                        establecimiento.latitud?.toDouble()?:0.0,
                    ), establecimiento)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.state
//                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
//                .collect{
//                }
//        }

        mapView =  view.findViewById(A.id.mapView)
        bottomSheet = view.findViewById(A.id.bottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        val closeButton = view.findViewById<ImageButton>(A.id.closeButton)
        closeButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }
        })
        bottomSheet.setOnClickListener{
            Log.d("DEBUG_APP","HELLO")
        }

    viewAnnotationManager = mapView.viewAnnotationManager
    mapView.getMapboxMap().apply {
        setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-63.17524817752105,-17.804381048746333))
                .zoom(12.0)
                .build()
        )
    }.loadStyleUri(Style.MAPBOX_STREETS) {
  }
    }
    private fun bottomSheetToggle(){
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//
        }
    }

@SuppressLint("SuspiciousIndentation")
private fun prepareViewAnnotation(point: Point, item:EstablecimientoDto) {
  val viewAnnotation = viewAnnotationManager.addViewAnnotation(
          resId = A.layout.item_callout_view,
          options = viewAnnotationOptions {
              geometry(point)
              visible(true)
              height(130)
              width(130)
              anchor(ViewAnnotationAnchor.BOTTOM)
          }
      )
    val binding: ItemCalloutViewBinding? =  DataBindingUtil.bind(viewAnnotation)
    binding?.establecimiento =item
    item.photo?.let { viewModel.loadImageFromUrl(it,binding?.img) }
    binding?.img?.setOnClickListener {
        establecimientoMapBinding.let {
        it.establecimiento = item
            it.navToDetail.setOnClickListener {
                val activityToStart = "app.regate.home.MainActivity"
                try {
                    val c = Class.forName(activityToStart)
                    val intent = Intent(activity,c)
                    Log.d("DEBUG_APP",item.id.toString())
                    intent.putExtra("establecimientoId", item.id.toString())
                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

                    startActivity(intent)
                } catch (ignored: Exception) {
                    //TODO()
                }
//                navigateToComplejo(item.id)
            }
            item.photo?.let { it1 -> viewModel.loadImageFromUrl(it1,it.img) }
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
    viewAnnotationList.add(viewAnnotation)
}

//private fun navigateToComplejo(id:Int){
//    try{
//
//    val activityToStart = "app.regate.home.MainActivity"
//    val c = Class.forName(activityToStart)
//    val taskDetailIntent = Intent(
//        Intent.ACTION_VIEW,
//        "https://example.com/establecimiento/id=${id}/page=0".toUri(),
//        context,
//        c
//    )
//    val taskBuilder = context?.let { TaskStackBuilder.create(it) }
//    if (taskBuilder != null) {
//        taskBuilder.addNextIntentWithParentStack(taskDetailIntent)
//        taskBuilder.getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
//    }
//    }catch(e:Exception){
//        //TODO
//    }
//}





private companion object {
  const val SELECTED_ADD_COEF_PX = 25
  val POINT: Point = Point.fromLngLat(-63.17524817752105,-17.804381048746333)
  val POINT2: Point = Point.fromLngLat(-63.17524817752105,-17.104381048746333)
  val POINT3: Point = Point.fromLngLat(-63.17524817752105,-17.404381048746334)
  val listPoints = listOf<Point>(POINT,POINT2)
}
}