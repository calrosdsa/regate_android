package app.regate.map

import app.regate.map.inject.ApplicationComponent
import kotlin.reflect.KClass
import me.tatarka.inject.`internal`.LazyMap
import me.tatarka.inject.`internal`.ScopedComponent

public fun KClass<MapActivityComponent>.create(parent: ApplicationComponent): MapActivityComponent =
    InjectMapActivityComponent(parent)

public class InjectMapActivityComponent(
  parent: ApplicationComponent
) : MapActivityComponent(parent), ScopedComponent {
  public override val _scoped: LazyMap = LazyMap()
}
