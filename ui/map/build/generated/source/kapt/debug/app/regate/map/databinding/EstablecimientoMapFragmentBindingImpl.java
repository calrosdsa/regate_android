package app.regate.map.databinding;
import app.regate.map.R;
import app.regate.map.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class EstablecimientoMapFragmentBindingImpl extends EstablecimientoMapFragmentBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.mapView, 2);
        sViewsWithIds.put(R.id.bottomSheet, 3);
        sViewsWithIds.put(R.id.closeButton, 4);
        sViewsWithIds.put(R.id.img, 5);
        sViewsWithIds.put(R.id.navToDetail, 6);
    }
    // views
    @NonNull
    private final androidx.coordinatorlayout.widget.CoordinatorLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public EstablecimientoMapFragmentBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 7, sIncludes, sViewsWithIds));
    }
    private EstablecimientoMapFragmentBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[3]
            , (android.widget.ImageButton) bindings[4]
            , (com.google.android.material.imageview.ShapeableImageView) bindings[5]
            , (com.mapbox.maps.MapView) bindings[2]
            , (android.widget.TextView) bindings[1]
            , (android.widget.Button) bindings[6]
            );
        this.mboundView0 = (androidx.coordinatorlayout.widget.CoordinatorLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.name.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.establecimiento == variableId) {
            setEstablecimiento((app.regate.data.dto.empresa.establecimiento.EstablecimientoDto) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setEstablecimiento(@Nullable app.regate.data.dto.empresa.establecimiento.EstablecimientoDto Establecimiento) {
        this.mEstablecimiento = Establecimiento;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.establecimiento);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        app.regate.data.dto.empresa.establecimiento.EstablecimientoDto establecimiento = mEstablecimiento;
        java.lang.String establecimientoName = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (establecimiento != null) {
                    // read establecimiento.name
                    establecimientoName = establecimiento.getName();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.name, establecimientoName);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): establecimiento
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}