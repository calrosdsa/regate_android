package app.regate.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import app.regate.inject.ActivityScope;
import app.regate.map.fragment.InjectFragmentFactory;
import app.regate.map.fragment.MainFragment;
import app.regate.map.inject.ApplicationComponent;
import app.regate.util.AppDateFormatter;
import me.tatarka.inject.annotations.Component;
import java.util.Arrays;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\'\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0012\u0010\u0005\u001a\u00020\u0006X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000b"}, d2 = {"Lapp/regate/map/MapActivityComponent;", "", "parent", "Lapp/regate/map/inject/ApplicationComponent;", "(Lapp/regate/map/inject/ApplicationComponent;)V", "fragmentFactory", "Lapp/regate/map/fragment/InjectFragmentFactory;", "getFragmentFactory", "()Lapp/regate/map/fragment/InjectFragmentFactory;", "getParent", "()Lapp/regate/map/inject/ApplicationComponent;", "map_debug"})
@me.tatarka.inject.annotations.Component
@app.regate.inject.ActivityScope
public abstract class MapActivityComponent {
    @org.jetbrains.annotations.NotNull
    private final app.regate.map.inject.ApplicationComponent parent = null;
    
    public MapActivityComponent(@org.jetbrains.annotations.NotNull
    @me.tatarka.inject.annotations.Component
    app.regate.map.inject.ApplicationComponent parent) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final app.regate.map.inject.ApplicationComponent getParent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public abstract app.regate.map.fragment.InjectFragmentFactory getFragmentFactory();
}