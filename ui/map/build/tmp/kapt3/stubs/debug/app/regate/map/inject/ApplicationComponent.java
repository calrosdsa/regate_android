package app.regate.map.inject;

import android.app.Application;
import android.content.Context;
import app.regate.api.NetworkComponent;
import app.regate.data.RoomDatabaseComponent;
import app.regate.data.account.AccountBinds;
import app.regate.data.establecimiento.EstablecimeintoBinds;
import app.regate.data.instalacion.InstalacionBinds;
import app.regate.data.reserva.ReservaBinds;
import app.regate.data.sala.SalaBinds;
import app.regate.data.users.UsersBinds;
import app.regate.inject.ApplicationScope;
import app.regate.settings.PreferencesComponent;
import me.tatarka.inject.annotations.Component;
import me.tatarka.inject.annotations.Provides;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\b\'\u0018\u0000 \u000f2\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u00052\u00020\u00062\u00020\u00072\u00020\b2\u00020\t:\u0001\u000fB\r\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fR\u0013\u0010\n\u001a\u00020\u000b8\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0010"}, d2 = {"Lapp/regate/map/inject/ApplicationComponent;", "Lapp/regate/data/RoomDatabaseComponent;", "Lapp/regate/api/NetworkComponent;", "Lapp/regate/settings/PreferencesComponent;", "Lapp/regate/data/account/AccountBinds;", "Lapp/regate/data/establecimiento/EstablecimeintoBinds;", "Lapp/regate/data/instalacion/InstalacionBinds;", "Lapp/regate/data/reserva/ReservaBinds;", "Lapp/regate/data/sala/SalaBinds;", "Lapp/regate/data/users/UsersBinds;", "application", "Landroid/app/Application;", "(Landroid/app/Application;)V", "getApplication", "()Landroid/app/Application;", "Companion", "map_debug"})
@app.regate.inject.ApplicationScope
@me.tatarka.inject.annotations.Component
public abstract class ApplicationComponent implements app.regate.data.RoomDatabaseComponent, app.regate.api.NetworkComponent, app.regate.settings.PreferencesComponent, app.regate.data.account.AccountBinds, app.regate.data.establecimiento.EstablecimeintoBinds, app.regate.data.instalacion.InstalacionBinds, app.regate.data.reserva.ReservaBinds, app.regate.data.sala.SalaBinds, app.regate.data.users.UsersBinds {
    @org.jetbrains.annotations.NotNull
    private final android.app.Application application = null;
    @org.jetbrains.annotations.NotNull
    public static final app.regate.map.inject.ApplicationComponent.Companion Companion = null;
    private static app.regate.map.inject.ApplicationComponent instance;
    
    public ApplicationComponent(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public final android.app.Application getApplication() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.account.AccountDataSource provideAccountDataSource(@org.jetbrains.annotations.NotNull
    app.regate.data.account.AccountDataSourceImpl bind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.users.UsersDataSource provideAccountDataSource(@org.jetbrains.annotations.NotNull
    app.regate.data.users.UsersDataSourceImpl bind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.daos.CupoDao provideAppCupoDao(@org.jetbrains.annotations.NotNull
    app.regate.data.db.AppDatabase db) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.db.AppDatabase provideAppDatabase(@org.jetbrains.annotations.NotNull
    app.regate.data.AppRoomDatabase bind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.daos.InstalacionDao provideAppInstalacionDao(@org.jetbrains.annotations.NotNull
    app.regate.data.db.AppDatabase db) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.daos.LabelDao provideAppLabelsDao(@org.jetbrains.annotations.NotNull
    app.regate.data.db.AppDatabase db) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    @app.regate.inject.ApplicationScope
    public android.content.SharedPreferences provideAppPreferences(@org.jetbrains.annotations.NotNull
    android.app.Application context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    @app.regate.inject.ApplicationScope
    public app.regate.data.AppRoomDatabase provideAppRoomDatabase(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.daos.EstablecimientoDao provideAppShowDao(@org.jetbrains.annotations.NotNull
    app.regate.data.db.AppDatabase db) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.daos.UserDao provideAppUserDao(@org.jetbrains.annotations.NotNull
    app.regate.data.db.AppDatabase db) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    @app.regate.inject.ApplicationScope
    public android.content.SharedPreferences provideAuthSharedPrefs(@org.jetbrains.annotations.NotNull
    android.app.Application application) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    @app.regate.inject.ApplicationScope
    public app.regate.data.auth.store.AuthStore provideAuthStore(@org.jetbrains.annotations.NotNull
    app.regate.settings.store.AppAuthStore manager) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.db.DatabaseTransactionRunner provideDatabaseTransactionRunner(@org.jetbrains.annotations.NotNull
    app.regate.data.db.RoomTransactionRunner runner) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.establecimiento.EstablecimientoDataSource provideEstablecimientoDataSource(@org.jetbrains.annotations.NotNull
    app.regate.data.establecimiento.EstablecimientoDataSourceImpl bind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.daos.GrupoDao provideGrupoDao(@org.jetbrains.annotations.NotNull
    app.regate.data.db.AppDatabase db) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.instalacion.InstalacionDataSource provideInstalacionDataSource(@org.jetbrains.annotations.NotNull
    app.regate.data.instalacion.InstalacionDataSourceImpl bind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.sala.SalaDataSource provideInstalacionDataSource(@org.jetbrains.annotations.NotNull
    app.regate.data.sala.SalaDataSourceImpl bind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    @app.regate.inject.ApplicationScope
    public io.ktor.client.HttpClient provideKtorClient(@org.jetbrains.annotations.NotNull
    okhttp3.OkHttpClient client, @org.jetbrains.annotations.NotNull
    app.regate.settings.AppPreferences preferences) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.daos.MessageProfileDao provideMessageProfileDao(@org.jetbrains.annotations.NotNull
    app.regate.data.db.AppDatabase db) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    @app.regate.inject.ApplicationScope
    public okhttp3.OkHttpClient provideOkHttpClient(@org.jetbrains.annotations.NotNull
    android.app.Application context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    @app.regate.inject.ApplicationScope
    public app.regate.settings.AppPreferences providePreferences(@org.jetbrains.annotations.NotNull
    app.regate.settings.AppPreferencesImpl bind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.daos.ProfileDao provideProfileDao(@org.jetbrains.annotations.NotNull
    app.regate.data.db.AppDatabase db) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.reserva.ReservaDataSource provideReservaDataSource(@org.jetbrains.annotations.NotNull
    app.regate.data.reserva.ReservaDataSourceImpl bind) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Provides
    public app.regate.data.daos.UserGrupoDao provideUserGrupoDao(@org.jetbrains.annotations.NotNull
    app.regate.data.db.AppDatabase db) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lapp/regate/map/inject/ApplicationComponent$Companion;", "", "()V", "instance", "Lapp/regate/map/inject/ApplicationComponent;", "getInstance", "context", "Landroid/content/Context;", "map_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Get a singleton instance of [ApplicationComponent].
         */
        @org.jetbrains.annotations.NotNull
        public final app.regate.map.inject.ApplicationComponent getInstance(@org.jetbrains.annotations.NotNull
        android.content.Context context) {
            return null;
        }
    }
}