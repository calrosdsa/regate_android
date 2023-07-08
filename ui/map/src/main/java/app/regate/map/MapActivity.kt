

@file:Suppress("DEPRECATION")

package app.regate.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import app.regate.inject.ActivityScope
//import app.regate.map.databinding.MainActivityBinding
import app.regate.map.fragment.InjectFragmentFactory
import app.regate.map.fragment.MainFragment
import app.regate.map.inject.ApplicationComponent
import app.regate.util.AppDateFormatter
import me.tatarka.inject.annotations.Component
import java.util.Arrays


    class MapActivity : FragmentActivity() {


        private lateinit var component: MapActivityComponent
        @SuppressLint("SetTextI18n")
        override fun onCreate(savedInstanceState: Bundle?) {
            component = MapActivityComponent::class.create(ApplicationComponent.getInstance(this))
            supportFragmentManager.fragmentFactory = component.fragmentFactory
            super.onCreate(savedInstanceState)
            setContentView(R.layout.main_activity)
//            val navOptions = NavOptions.Builder()
//                .setPopUpTo(R.id.main,true)
//                .build()
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
//                ?.findNavController()?.navigate(R.id.establecimientoFragment,savedInstanceState,navOptions)

        }

        fun getAllRunningActivities(context: Context): List<ActivityInfo>? {
            return try {
                val pi = context.packageManager.getPackageInfo(
                    context.packageName, PackageManager.GET_ACTIVITIES
                )
                listOf(*pi.activities)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                null
            }
        }


    }
@ActivityScope
@Component
abstract class MapActivityComponent(@Component val parent: ApplicationComponent) {
        abstract val fragmentFactory: InjectFragmentFactory
//        abstract val appDateFormatter: AppDateFormatter

//        abstract val composeScreen:ComposeScreen
    }
