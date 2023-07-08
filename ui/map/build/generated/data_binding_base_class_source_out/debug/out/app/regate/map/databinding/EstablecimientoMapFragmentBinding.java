// Generated by data binding compiler. Do not edit!
package app.regate.map.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.Bindable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto;
import app.regate.map.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.mapbox.maps.MapView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class EstablecimientoMapFragmentBinding extends ViewDataBinding {
  @NonNull
  public final ConstraintLayout bottomSheet;

  @NonNull
  public final ImageButton closeButton;

  @NonNull
  public final ShapeableImageView img;

  @NonNull
  public final MapView mapView;

  @NonNull
  public final TextView name;

  @NonNull
  public final Button navToDetail;

  @Bindable
  protected EstablecimientoDto mEstablecimiento;

  protected EstablecimientoMapFragmentBinding(Object _bindingComponent, View _root,
      int _localFieldCount, ConstraintLayout bottomSheet, ImageButton closeButton,
      ShapeableImageView img, MapView mapView, TextView name, Button navToDetail) {
    super(_bindingComponent, _root, _localFieldCount);
    this.bottomSheet = bottomSheet;
    this.closeButton = closeButton;
    this.img = img;
    this.mapView = mapView;
    this.name = name;
    this.navToDetail = navToDetail;
  }

  public abstract void setEstablecimiento(@Nullable EstablecimientoDto establecimiento);

  @Nullable
  public EstablecimientoDto getEstablecimiento() {
    return mEstablecimiento;
  }

  @NonNull
  public static EstablecimientoMapFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.establecimiento_map_fragment, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static EstablecimientoMapFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<EstablecimientoMapFragmentBinding>inflateInternal(inflater, R.layout.establecimiento_map_fragment, root, attachToRoot, component);
  }

  @NonNull
  public static EstablecimientoMapFragmentBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.establecimiento_map_fragment, null, false, component)
   */
  @NonNull
  @Deprecated
  public static EstablecimientoMapFragmentBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<EstablecimientoMapFragmentBinding>inflateInternal(inflater, R.layout.establecimiento_map_fragment, null, false, component);
  }

  public static EstablecimientoMapFragmentBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static EstablecimientoMapFragmentBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (EstablecimientoMapFragmentBinding)bind(component, view, R.layout.establecimiento_map_fragment);
  }
}
