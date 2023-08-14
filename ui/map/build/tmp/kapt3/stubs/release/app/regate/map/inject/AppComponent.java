package app.regate.map.inject;

import android.app.Application;
import app.regate.app.ApplicationInfo;
import app.regate.appinitializers.AppInitializer;
import app.regate.util.AppCoroutineDispatchers;
import app.regate.inject.ApplicationScope;
import kotlinx.coroutines.Dispatchers;
import me.tatarka.inject.annotations.IntoSet;
import me.tatarka.inject.annotations.Provides;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017J\b\u0010\u0006\u001a\u00020\u0007H\u0017\u00a8\u0006\b"}, d2 = {"Lapp/regate/map/inject/AppComponent;", "", "provideApplicationId", "Lapp/regate/app/ApplicationInfo;", "application", "Landroid/app/Application;", "provideCoroutineDispatchers", "Lapp/regate/util/AppCoroutineDispatchers;", "map_release"})
public abstract interface AppComponent {
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    @app.regate.inject.ApplicationScope
    public abstract app.regate.app.ApplicationInfo provideApplicationId(@org.jetbrains.annotations.NotNull
    android.app.Application application);
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    @app.regate.inject.ApplicationScope
    public abstract app.regate.util.AppCoroutineDispatchers provideCoroutineDispatchers();
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 3)
    public final class DefaultImpls {
        
        @org.jetbrains.annotations.NotNull
        @me.tatarka.inject.annotations.Provides
        @app.regate.inject.ApplicationScope
        public static app.regate.app.ApplicationInfo provideApplicationId(@org.jetbrains.annotations.NotNull
        app.regate.map.inject.AppComponent $this, @org.jetbrains.annotations.NotNull
        android.app.Application application) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @me.tatarka.inject.annotations.Provides
        @app.regate.inject.ApplicationScope
        public static app.regate.util.AppCoroutineDispatchers provideCoroutineDispatchers(@org.jetbrains.annotations.NotNull
        app.regate.map.inject.AppComponent $this) {
            return null;
        }
    }
}