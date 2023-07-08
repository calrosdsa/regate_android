package app.regate.map.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.SavedStateHandle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import app.regate.entidad.actividades.ActividadEstablecimientoViewModel
import app.regate.map.R
import app.regate.map.fragment.establecimiento.EstablecimientoActividadesFragment
import app.regate.map.fragment.establecimiento.EstablecimientoReservaFragment
import app.regate.map.fragment.establecimiento.EstablecimientoSalasFragment
import app.regate.map.viewmodel.ActiviViewModel
import app.regate.map.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.tatarka.inject.annotations.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

@Inject
class EstablecimientoFragment() : Fragment(R.layout.fragment_establecimiento) {
    private var tabLayout: TabLayout? = null
    //    private var viewPager: ViewPager? = null
    private var fragments = mutableListOf<Fragment>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_establecimiento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        populateFragmentList()
        val adapter = activity?.let { FragmentsAdapter(it) }
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = adapter
        viewPager.currentItem = 0
        tabLayout = view.findViewById(R.id.content_tabs)
        TabLayoutMediator(tabLayout!!, viewPager) { tab, position ->
            tab.text ="TAB $position"
        }.attach()

        hideSystemUI()

    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        activity?.window?.decorView?.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
    }

    private fun populateFragmentList() {
        fragments.clear()
        fragments.add(parentFragmentManager.fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(),
            EstablecimientoActividadesFragment::class.java.name))
        fragments.add(parentFragmentManager.fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(),
            EstablecimientoSalasFragment::class.java.name))
        fragments.add(EstablecimientoReservaFragment())
    }

    internal inner class FragmentsAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
        fun addFragment(fragment: Fragment) {
            fragments.add(fragment)
//            fragments.add(title)
        }

    }


}