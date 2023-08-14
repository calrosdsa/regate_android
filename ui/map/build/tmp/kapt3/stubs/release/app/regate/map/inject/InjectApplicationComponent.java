package app.regate.map.inject;

import android.app.Application;
import kotlin.reflect.KClass;
import me.tatarka.inject.internal.LazyMap;
import me.tatarka.inject.internal.ScopedComponent;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\n"}, d2 = {"Lapp/regate/map/inject/InjectApplicationComponent;", "Lapp/regate/map/inject/ApplicationComponent;", "Lme/tatarka/inject/internal/ScopedComponent;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "_scoped", "Lme/tatarka/inject/internal/LazyMap;", "get_scoped", "()Lme/tatarka/inject/internal/LazyMap;", "map_release"})
public final class InjectApplicationComponent extends app.regate.map.inject.ApplicationComponent implements me.tatarka.inject.internal.ScopedComponent {
    @org.jetbrains.annotations.NotNull
    private final me.tatarka.inject.internal.LazyMap _scoped = null;
    
    public InjectApplicationComponent(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public me.tatarka.inject.internal.LazyMap get_scoped() {
        return null;
    }
}