package app.regate.map.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import app.regate.map.fragment.establecimiento.EstablecimientoActividadesFragment
import app.regate.map.fragment.establecimiento.EstablecimientoSalasFragment
import me.tatarka.inject.annotations.Inject

@Inject
class InjectFragmentFactory(
    private val mainFragment:()-> MainFragment,
    private val establecimientoFragment:()-> EstablecimientoFragment,
    private val episodesFragment:()-> EstablecimientosMapFragment,
    private val establecimientoActividadesFragment:()-> EstablecimientoActividadesFragment,
    private val establecimientoSalasFragment:()-> EstablecimientoSalasFragment

) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when (className) {
            name<MainFragment>() -> mainFragment()
            name<EstablecimientosMapFragment>() -> episodesFragment()
            name<EstablecimientoFragment>() -> establecimientoFragment()
            name<EstablecimientoActividadesFragment>() -> establecimientoActividadesFragment()
            name<EstablecimientoSalasFragment>()-> establecimientoSalasFragment()
            else -> super.instantiate(classLoader, className)
        }
    }

    private inline fun <reified C> name() = C::class.qualifiedName
}

