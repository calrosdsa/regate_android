package app.regate.map.fragment;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.TaskStackBuilder;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto;
import app.regate.map.databinding.EstablecimientoMapFragmentBinding;
import app.regate.map.databinding.ItemCalloutViewBinding;
import app.regate.map.viewmodel.EstablecimientosMapState;
import app.regate.map.viewmodel.EstablecimientosMapViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.ViewAnnotationAnchor;
import com.mapbox.maps.viewannotation.ViewAnnotationManager;
import me.tatarka.inject.annotations.Inject;
import app.regate.map.R;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 )2\u00020\u0001:\u0001)B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0019\u001a\u00020\u001aH\u0002J$\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\u001a\u0010\"\u001a\u00020\u001a2\u0006\u0010#\u001a\u00020\u00122\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\u0018\u0010$\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020(H\u0003R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0002\u001a\u00020\u00048BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0017\u0010\u0018\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006*"}, d2 = {"Lapp/regate/map/fragment/EstablecimientosMapFragment;", "Landroidx/fragment/app/Fragment;", "viewModel", "Lkotlin/Function0;", "Lapp/regate/map/viewmodel/EstablecimientosMapViewModel;", "(Lkotlin/jvm/functions/Function0;)V", "bottomSheet", "Landroidx/constraintlayout/widget/ConstraintLayout;", "bottomSheetBehavior", "Lcom/google/android/material/bottomsheet/BottomSheetBehavior;", "establecimientoMapBinding", "Lapp/regate/map/databinding/EstablecimientoMapFragmentBinding;", "mapView", "Lcom/mapbox/maps/MapView;", "state", "Lapp/regate/map/viewmodel/EstablecimientosMapState;", "viewAnnotationList", "", "Landroid/view/View;", "viewAnnotationManager", "Lcom/mapbox/maps/viewannotation/ViewAnnotationManager;", "getViewModel", "()Lapp/regate/map/viewmodel/EstablecimientosMapViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "bottomSheetToggle", "", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "prepareViewAnnotation", "point", "Lcom/mapbox/geojson/Point;", "item", "Lapp/regate/data/dto/empresa/establecimiento/EstablecimientoDto;", "Companion", "map_release"})
@me.tatarka.inject.annotations.Inject
public final class EstablecimientosMapFragment extends androidx.fragment.app.Fragment {
    private final kotlin.Lazy viewModel$delegate = null;
    private com.google.android.material.bottomsheet.BottomSheetBehavior<androidx.constraintlayout.widget.ConstraintLayout> bottomSheetBehavior;
    private androidx.constraintlayout.widget.ConstraintLayout bottomSheet;
    private com.mapbox.maps.viewannotation.ViewAnnotationManager viewAnnotationManager;
    private com.mapbox.maps.MapView mapView;
    private app.regate.map.viewmodel.EstablecimientosMapState state;
    private app.regate.map.databinding.EstablecimientoMapFragmentBinding establecimientoMapBinding;
    private java.util.List<android.view.View> viewAnnotationList;
    @org.jetbrains.annotations.NotNull
    private static final app.regate.map.fragment.EstablecimientosMapFragment.Companion Companion = null;
    @java.lang.Deprecated
    public static final int SELECTED_ADD_COEF_PX = 25;
    @org.jetbrains.annotations.NotNull
    @java.lang.Deprecated
    private static final com.mapbox.geojson.Point POINT = null;
    @org.jetbrains.annotations.NotNull
    @java.lang.Deprecated
    private static final com.mapbox.geojson.Point POINT2 = null;
    @org.jetbrains.annotations.NotNull
    @java.lang.Deprecated
    private static final com.mapbox.geojson.Point POINT3 = null;
    @org.jetbrains.annotations.NotNull
    @java.lang.Deprecated
    private static final java.util.List<com.mapbox.geojson.Point> listPoints = null;
    
    public EstablecimientosMapFragment(@org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function0<app.regate.map.viewmodel.EstablecimientosMapViewModel> viewModel) {
        super();
    }
    
    private final app.regate.map.viewmodel.EstablecimientosMapViewModel getViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override
    public void onViewCreated(@org.jetbrains.annotations.NotNull
    android.view.View view, @org.jetbrains.annotations.Nullable
    android.os.Bundle savedInstanceState) {
    }
    
    private final void bottomSheetToggle() {
    }
    
    @android.annotation.SuppressLint(value = {"SuspiciousIndentation"})
    private final void prepareViewAnnotation(com.mapbox.geojson.Point point, app.regate.data.dto.empresa.establecimiento.EstablecimientoDto item) {
    }
    
    @kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\b\u0003\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u000e\u0010\u000b\u001a\u00020\fX\u0086T\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00040\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0011"}, d2 = {"Lapp/regate/map/fragment/EstablecimientosMapFragment$Companion;", "", "()V", "POINT", "Lcom/mapbox/geojson/Point;", "getPOINT", "()Lcom/mapbox/geojson/Point;", "POINT2", "getPOINT2", "POINT3", "getPOINT3", "SELECTED_ADD_COEF_PX", "", "listPoints", "", "getListPoints", "()Ljava/util/List;", "map_release"})
    static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.mapbox.geojson.Point getPOINT() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.mapbox.geojson.Point getPOINT2() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final com.mapbox.geojson.Point getPOINT3() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final java.util.List<com.mapbox.geojson.Point> getListPoints() {
            return null;
        }
    }
}