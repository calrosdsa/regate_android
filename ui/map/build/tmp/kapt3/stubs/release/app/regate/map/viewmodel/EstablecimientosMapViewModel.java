package app.regate.map.viewmodel;

import android.util.Log;
import android.widget.ImageView;
import androidx.lifecycle.ViewModel;
import app.regate.data.dto.empresa.establecimiento.EstablecimientoDto;
import app.regate.data.establecimiento.EstablecimientoRepository;
import app.regate.util.ObservableLoadingCounter;
import coil.request.CachePolicy;
import coil.request.Disposable;
import coil.transform.CircleCropTransformation;
import coil.transform.RoundedCornersTransformation;
import io.ktor.http.ContentType;
import kotlinx.coroutines.flow.SharingStarted;
import kotlinx.coroutines.flow.StateFlow;
import me.tatarka.inject.annotations.Inject;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0014\u001a\u00020\u0015J\u0018\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00112\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\r0\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\u0011X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u001b"}, d2 = {"Lapp/regate/map/viewmodel/EstablecimientosMapViewModel;", "Landroidx/lifecycle/ViewModel;", "client", "Lio/ktor/client/HttpClient;", "(Lio/ktor/client/HttpClient;)V", "establecimientos", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lapp/regate/data/dto/empresa/establecimiento/EstablecimientoDto;", "loadingState", "Lapp/regate/util/ObservableLoadingCounter;", "state", "Lkotlinx/coroutines/flow/StateFlow;", "Lapp/regate/map/viewmodel/EstablecimientosMapState;", "getState", "()Lkotlinx/coroutines/flow/StateFlow;", "textValue", "", "getTextValue", "()Ljava/lang/String;", "getEstablecimientos", "", "loadImageFromUrl", "url", "img", "Landroid/widget/ImageView;", "observeState", "map_release"})
@me.tatarka.inject.annotations.Inject
public final class EstablecimientosMapViewModel extends androidx.lifecycle.ViewModel {
    private final io.ktor.client.HttpClient client = null;
    private final app.regate.util.ObservableLoadingCounter loadingState = null;
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<app.regate.data.dto.empresa.establecimiento.EstablecimientoDto>> establecimientos = null;
    @org.jetbrains.annotations.NotNull
    private final java.lang.String textValue = "EXAMPLE FOR VIEWModel";
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<app.regate.map.viewmodel.EstablecimientosMapState> state = null;
    
    public EstablecimientosMapViewModel(@org.jetbrains.annotations.NotNull
    io.ktor.client.HttpClient client) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String getTextValue() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<app.regate.map.viewmodel.EstablecimientosMapState> getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<app.regate.map.viewmodel.EstablecimientosMapState> observeState() {
        return null;
    }
    
    public final void loadImageFromUrl(@org.jetbrains.annotations.NotNull
    java.lang.String url, @org.jetbrains.annotations.Nullable
    android.widget.ImageView img) {
    }
    
    public final void getEstablecimientos() {
    }
}