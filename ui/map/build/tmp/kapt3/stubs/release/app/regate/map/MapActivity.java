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

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u00062\u0006\u0010\b\u001a\u00020\tJ\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\rH\u0015R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lapp/regate/map/MapActivity;", "Landroidx/fragment/app/FragmentActivity;", "()V", "component", "Lapp/regate/map/MapActivityComponent;", "getAllRunningActivities", "", "Landroid/content/pm/ActivityInfo;", "context", "Landroid/content/Context;", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "map_release"})
public final class MapActivity extends androidx.fragment.app.FragmentActivity {
    private app.regate.map.MapActivityComponent component;
    
    public MapActivity() {
        super();
    }
    
    @android.annotation.SuppressLint(value = {"SetTextI18n"})
    @java.lang.Override
    protected void onCreate(@org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.util.List<android.content.pm.ActivityInfo> getAllRunningActivities(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        return null;
    }
}