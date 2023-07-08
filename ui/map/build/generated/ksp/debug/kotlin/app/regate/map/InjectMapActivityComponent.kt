package app.regate.map

import app.regate.`data`.db.AppDatabase
import app.regate.`data`.mappers.DtoToProfile
import app.regate.`data`.mappers.MessageDtoToMessage
import app.regate.`data`.sala.SalaDataSourceImpl
import app.regate.`data`.sala.SalaRepository
import app.regate.entidad.actividades.ActividadesEstablecimiento
import app.regate.entidad.salas.Salas
import app.regate.entidad.salas.SalasViewModel
import app.regate.map.fragment.EstablecimientoFragment
import app.regate.map.fragment.EstablecimientosMapFragment
import app.regate.map.fragment.InjectFragmentFactory
import app.regate.map.fragment.MainFragment
import app.regate.map.fragment.establecimiento.EstablecimientoActividadesFragment
import app.regate.map.fragment.establecimiento.EstablecimientoSalasFragment
import app.regate.map.inject.ApplicationComponent
import app.regate.map.viewmodel.ActiviViewModel
import app.regate.map.viewmodel.EstablecimientosMapViewModel
import app.regate.settings.AppPreferencesImpl
import app.regate.settings.PreferencesAuthStore
import app.regate.settings.store.AppAuthStore
import app.regate.settings.store.BlockStoreAuthStore
import io.ktor.client.HttpClient
import kotlin.reflect.KClass
import me.tatarka.inject.`internal`.LazyMap
import me.tatarka.inject.`internal`.ScopedComponent

public fun KClass<MapActivityComponent>.create(parent: ApplicationComponent): MapActivityComponent =
    InjectMapActivityComponent(parent)

public class InjectMapActivityComponent(
  parent: ApplicationComponent
) : MapActivityComponent(parent), ScopedComponent {
  public override val _scoped: LazyMap = LazyMap()

  private val httpClient: HttpClient
    get() {
      require(parent is ScopedComponent)
      return parent._scoped.get("io.ktor.client.HttpClient") {
        parent.provideKtorClient(
          client = parent._scoped.get("okhttp3.OkHttpClient") {
            parent.provideOkHttpClient(
              context = parent.application
            )
          },
          preferences = parent._scoped.get("app.regate.settings.AppPreferences") {
            parent.providePreferences(
              bind = AppPreferencesImpl(
                context = parent.application,
                sharedPreferences = parent._scoped.get("app.regate.settings.AppSharedPreferences") {
                  parent.provideAppPreferences(
                    context = parent.application
                  )
                }
              )
            )
          }
        )
      }
    }

  private val composeScreen: ComposeScreen
    get() {
      require(parent is ScopedComponent)
      return ComposeScreen(
        actividadesEstablecimiento = {
          ActividadesEstablecimiento()
        },
        establecimientoSalas = { arg0_, arg1 ->
          Salas(
            viewModelFactory = { arg0__ ->
              SalasViewModel(
                savedStateHandle = arg0__,
                salaRepository = parent._scoped.get("app.regate.`data`.sala.SalaRepository") {
                  SalaRepository(
                    salaDataSourceImpl = SalaDataSourceImpl(
                      client = httpClient,
                      authStore = parent._scoped.get("app.regate.`data`.auth.store.AuthStore") {
                        parent.provideAuthStore(
                          manager = AppAuthStore(
                            preferencesAuthStore = PreferencesAuthStore(
                              authPrefs = lazy {
                                parent._scoped.get("app.regate.settings.AuthSharedPreferences") {
                                  parent.provideAuthSharedPrefs(
                                    application = parent.application
                                  )
                                }
                              }
                            ),
                            blockStoreAuthStore = BlockStoreAuthStore(
                              context = parent.application
                            )
                          )
                        )
                      }
                    ),
                    profileDao = parent.provideProfileDao(
                      db = appDatabase
                    ),
                    messageProfileDao = parent.provideMessageProfileDao(
                      db = appDatabase
                    ),
                    messageMapper = MessageDtoToMessage(),
                    profileMapper = DtoToProfile(),
                    userDao = parent.provideAppUserDao(
                      db = appDatabase
                    )
                  )
                }
              )
            },
            navigateToSala = arg0_,
            crearSala = arg1
          )
        }
      )
    }

  private val appDatabase: AppDatabase
    get() {
      require(parent is ScopedComponent)
      return parent.provideAppDatabase(
        bind = parent._scoped.get("app.regate.`data`.AppRoomDatabase") {
          parent.provideAppRoomDatabase(
            application = parent.application
          )
        }
      )
    }

  public override val fragmentFactory: InjectFragmentFactory
    get() = InjectFragmentFactory(
      mainFragment = {
        MainFragment()
      },
      establecimientoFragment = {
        EstablecimientoFragment()
      },
      episodesFragment = {
        EstablecimientosMapFragment(
          viewModel = {
            EstablecimientosMapViewModel(
              client = httpClient
            )
          }
        )
      },
      establecimientoActividadesFragment = {
        EstablecimientoActividadesFragment(
          viewModel = { arg0 ->
            ActiviViewModel(
              handle = arg0
            )
          },
          composeScreens = composeScreen
        )
      },
      establecimientoSalasFragment = {
        EstablecimientoSalasFragment(
          composeScreens = composeScreen
        )
      }
    )
}
