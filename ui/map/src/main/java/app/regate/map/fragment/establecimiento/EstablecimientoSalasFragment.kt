package app.regate.map.fragment.establecimiento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import app.regate.map.ComposeScreen

import app.regate.map.R
import me.tatarka.inject.annotations.Inject


@Inject
class EstablecimientoSalasFragment(private val composeScreens:ComposeScreen) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        savedInstanceState?.putLong("id",1)
        return inflater.inflate(R.layout.compose_layout, container, false).apply {
            findViewById<ComposeView>(R.id.compose_view).setContent {
                composeScreens.establecimientoSalas(navigateToSala={},crearSala={})
            }
        }
    }
}
