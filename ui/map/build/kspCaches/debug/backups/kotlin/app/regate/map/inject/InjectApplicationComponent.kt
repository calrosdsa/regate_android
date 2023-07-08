package app.regate.map.inject

import android.app.Application
import kotlin.reflect.KClass
import me.tatarka.inject.`internal`.LazyMap
import me.tatarka.inject.`internal`.ScopedComponent

public fun KClass<ApplicationComponent>.create(application: Application): ApplicationComponent =
    InjectApplicationComponent(application)

public class InjectApplicationComponent(
  application: Application
) : ApplicationComponent(application), ScopedComponent {
  public override val _scoped: LazyMap = LazyMap()
}
