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

@kotlin.Metadata(mv = {1, 8, 0}, k = 2, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0018\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"create", "Lapp/regate/map/MapActivityComponent;", "Lkotlin/reflect/KClass;", "parent", "Lapp/regate/map/inject/ApplicationComponent;", "map_debug"})
public final class InjectMapActivityComponentKt {
    
    @org.jetbrains.annotations.NotNull
    public static final app.regate.map.MapActivityComponent create(@org.jetbrains.annotations.NotNull
    kotlin.reflect.KClass<app.regate.map.MapActivityComponent> $this$create, @org.jetbrains.annotations.NotNull
    app.regate.map.inject.ApplicationComponent parent) {
        return null;
    }
}