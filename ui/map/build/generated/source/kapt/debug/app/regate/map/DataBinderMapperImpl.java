package app.regate.map;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import app.regate.map.databinding.EstablecimientoMapFragmentBindingImpl;
import app.regate.map.databinding.ItemCalloutViewBindingImpl;
import app.regate.map.databinding.MainFragmentBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ESTABLECIMIENTOMAPFRAGMENT = 1;

  private static final int LAYOUT_ITEMCALLOUTVIEW = 2;

  private static final int LAYOUT_MAINFRAGMENT = 3;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(3);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(app.regate.map.R.layout.establecimiento_map_fragment, LAYOUT_ESTABLECIMIENTOMAPFRAGMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(app.regate.map.R.layout.item_callout_view, LAYOUT_ITEMCALLOUTVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(app.regate.map.R.layout.main_fragment, LAYOUT_MAINFRAGMENT);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ESTABLECIMIENTOMAPFRAGMENT: {
          if ("layout/establecimiento_map_fragment_0".equals(tag)) {
            return new EstablecimientoMapFragmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for establecimiento_map_fragment is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMCALLOUTVIEW: {
          if ("layout/item_callout_view_0".equals(tag)) {
            return new ItemCalloutViewBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_callout_view is invalid. Received: " + tag);
        }
        case  LAYOUT_MAINFRAGMENT: {
          if ("layout/main_fragment_0".equals(tag)) {
            return new MainFragmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for main_fragment is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(4);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "establecimiento");
      sKeys.put(2, "lastName");
      sKeys.put(3, "name");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(3);

    static {
      sKeys.put("layout/establecimiento_map_fragment_0", app.regate.map.R.layout.establecimiento_map_fragment);
      sKeys.put("layout/item_callout_view_0", app.regate.map.R.layout.item_callout_view);
      sKeys.put("layout/main_fragment_0", app.regate.map.R.layout.main_fragment);
    }
  }
}
