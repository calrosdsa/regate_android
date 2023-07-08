package app.regate.map.fragment.establecimiento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import app.regate.entidad.actividades.ActividadEstablecimientoViewModel
import app.regate.map.ComposeScreen

import app.regate.map.R
import app.regate.map.viewModels
import app.regate.map.viewmodel.ActiviViewModel
import me.tatarka.inject.annotations.Inject

@Inject
class EstablecimientoActividadesFragment(
    viewModel: (SavedStateHandle) -> ActiviViewModel,
    private val composeScreens:ComposeScreen) : Fragment() {
    private val viewModel by viewModels(viewModel)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel.showMessage()
        return inflater.inflate(R.layout.compose_layout, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).setContent {

                composeScreens.actividadesEstablecimiento()
            }
            }
        }
}
