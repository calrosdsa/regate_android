package app.regate.map;

import app.regate.data.db.AppDatabase;
import app.regate.data.mappers.DtoToProfile;
import app.regate.data.mappers.MessageDtoToMessage;
import app.regate.data.sala.SalaDataSourceImpl;
import app.regate.data.sala.SalaRepository;
import app.regate.entidad.salas.SalasViewModel;
import app.regate.map.fragment.EstablecimientoFragment;
import app.regate.map.fragment.EstablecimientosMapFragment;
import app.regate.map.fragment.InjectFragmentFactory;
import app.regate.map.fragment.MainFragment;
import app.regate.map.fragment.establecimiento.EstablecimientoActividadesFragment;
import app.regate.map.fragment.establecimiento.EstablecimientoSalasFragment;
import app.regate.map.inject.ApplicationComponent;
import app.regate.map.viewmodel.ActiviViewModel;
import app.regate.map.viewmodel.EstablecimientosMapViewModel;
import app.regate.settings.AppPreferencesImpl;
import app.regate.settings.PreferencesAuthStore;
import app.regate.settings.store.AppAuthStore;
import app.regate.settings.store.BlockStoreAuthStore;
import kotlin.reflect.KClass;
import me.tatarka.inject.internal.LazyMap;
import me.tatarka.inject.internal.ScopedComponent;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000b8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00138VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u00178BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0019\u00a8\u0006\u001a"}, d2 = {"Lapp/regate/map/InjectMapActivityComponent;", "Lapp/regate/map/MapActivityComponent;", "Lme/tatarka/inject/internal/ScopedComponent;", "parent", "Lapp/regate/map/inject/ApplicationComponent;", "(Lapp/regate/map/inject/ApplicationComponent;)V", "_scoped", "Lme/tatarka/inject/internal/LazyMap;", "get_scoped", "()Lme/tatarka/inject/internal/LazyMap;", "appDatabase", "Lapp/regate/data/db/AppDatabase;", "getAppDatabase", "()Lapp/regate/data/db/AppDatabase;", "composeScreen", "Lapp/regate/map/ComposeScreen;", "getComposeScreen", "()Lapp/regate/map/ComposeScreen;", "fragmentFactory", "Lapp/regate/map/fragment/InjectFragmentFactory;", "getFragmentFactory", "()Lapp/regate/map/fragment/InjectFragmentFactory;", "httpClient", "Lio/ktor/client/HttpClient;", "getHttpClient", "()Lio/ktor/client/HttpClient;", "map_release"})
public final class InjectMapActivityComponent extends app.regate.map.MapActivityComponent implements me.tatarka.inject.internal.ScopedComponent {
    @org.jetbrains.annotations.NotNull
    private final me.tatarka.inject.internal.LazyMap _scoped = null;
    
    public InjectMapActivityComponent(@org.jetbrains.annotations.NotNull
    app.regate.map.inject.ApplicationComponent parent) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public me.tatarka.inject.internal.LazyMap get_scoped() {
        return null;
    }
    
    private final io.ktor.client.HttpClient getHttpClient() {
        return null;
    }
    
    private final app.regate.map.ComposeScreen getComposeScreen() {
        return null;
    }
    
    private final app.regate.data.db.AppDatabase getAppDatabase() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public app.regate.map.fragment.InjectFragmentFactory getFragmentFactory() {
        return null;
    }
}