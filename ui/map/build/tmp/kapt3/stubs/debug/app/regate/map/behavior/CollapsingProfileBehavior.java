package app.regate.map.behavior;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import app.regate.map.R;
import com.google.android.material.appbar.AppBarLayout;

@kotlin.Suppress(names = {"unused"})
@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020\u000bH\u0002J\b\u0010#\u001a\u00020\u000bH\u0002J \u0010$\u001a\u00020\u00122\u0006\u0010%\u001a\u00020\u00122\u0006\u0010&\u001a\u00020\u00122\u0006\u0010\'\u001a\u00020\u0012H\u0002J\u0018\u0010(\u001a\u00020\u00122\u0006\u0010)\u001a\u00020\u00122\u0006\u0010*\u001a\u00020\u0012H\u0002J\u0018\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020\u00022\u0006\u0010.\u001a\u00020\tH\u0002J \u0010/\u001a\u0002002\u0006\u00101\u001a\u0002022\u0006\u0010-\u001a\u00020\u00022\u0006\u0010.\u001a\u00020\tH\u0016J \u00103\u001a\u00020\u00122\u0006\u00104\u001a\u00020\u00122\u0006\u00105\u001a\u00020\u00122\u0006\u00106\u001a\u00020\u0012H\u0002J \u00107\u001a\u0002002\u0006\u00101\u001a\u0002022\u0006\u0010-\u001a\u00020\u00022\u0006\u0010.\u001a\u00020\tH\u0016J\b\u00108\u001a\u00020,H\u0002J\b\u00109\u001a\u00020,H\u0002J\b\u0010:\u001a\u00020,H\u0002J\b\u0010;\u001a\u00020,H\u0002J\b\u0010<\u001a\u00020,H\u0002J\b\u0010=\u001a\u00020,H\u0002J\b\u0010>\u001a\u00020,H\u0002J\b\u0010?\u001a\u00020,H\u0002R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\u00020\r8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0010\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006@"}, d2 = {"Lapp/regate/map/behavior/CollapsingProfileBehavior;", "Landroidx/coordinatorlayout/widget/CoordinatorLayout$Behavior;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "appBar", "Landroid/view/View;", "appBarHeight", "", "displaySize", "Landroid/graphics/Point;", "getDisplaySize", "()Landroid/graphics/Point;", "headerProfile", "normalizedRange", "", "profileImage", "profileImageMaxMargin", "profileImageSizeBig", "profileImageSizeSmall", "profileMisc", "profileName", "profileNameHeight", "profileSubtitle", "profileTextContainer", "profileTextContainerMaxHeight", "toolBarHeight", "windowSize", "calculateMaxHeightFromTextView", "textView", "Landroid/widget/TextView;", "calculateProfileImageSmallMargin", "calculateProfileTextMargin", "getIntercept", "m", "x", "b", "getUpdatedInterpolatedValue", "openSizeTarget", "closedSizeTarget", "initialize", "", "child", "dependency", "layoutDependsOn", "", "parent", "Landroidx/coordinatorlayout/widget/CoordinatorLayout;", "normalize", "currentValue", "minValue", "maxValue", "onDependentViewChanged", "updateHeaderProfileOffset", "updateNormalizedRange", "updateOffset", "updateProfileImageMargins", "updateProfileImageSize", "updateProfileTextContainerHeight", "updateProfileTextMargin", "updateSubtitleAndMiscAlpha", "map_debug"})
public final class CollapsingProfileBehavior extends androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior<android.widget.LinearLayout> {
    private final android.content.Context context = null;
    private android.view.View appBar;
    private android.view.View headerProfile;
    private android.view.View profileImage;
    private android.view.View profileTextContainer;
    private android.view.View profileName;
    private android.view.View profileSubtitle;
    private android.view.View profileMisc;
    private android.graphics.Point windowSize;
    private int appBarHeight = 0;
    private final int profileImageSizeSmall = 0;
    private final int profileImageSizeBig = 0;
    private final int profileImageMaxMargin = 0;
    private int toolBarHeight = 0;
    private int profileTextContainerMaxHeight = 0;
    private int profileNameHeight = 0;
    private float normalizedRange = 0.0F;
    
    public CollapsingProfileBehavior(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.util.AttributeSet attrs) {
        super();
    }
    
    private final android.graphics.Point getDisplaySize() {
        return null;
    }
    
    @java.lang.Override
    public boolean layoutDependsOn(@org.jetbrains.annotations.NotNull
    androidx.coordinatorlayout.widget.CoordinatorLayout parent, @org.jetbrains.annotations.NotNull
    android.widget.LinearLayout child, @org.jetbrains.annotations.NotNull
    android.view.View dependency) {
        return false;
    }
    
    private final void initialize(android.widget.LinearLayout child, android.view.View dependency) {
    }
    
    private final int calculateMaxHeightFromTextView(android.widget.TextView textView) {
        return 0;
    }
    
    @java.lang.Override
    public boolean onDependentViewChanged(@org.jetbrains.annotations.NotNull
    androidx.coordinatorlayout.widget.CoordinatorLayout parent, @org.jetbrains.annotations.NotNull
    android.widget.LinearLayout child, @org.jetbrains.annotations.NotNull
    android.view.View dependency) {
        return false;
    }
    
    private final void updateNormalizedRange() {
    }
    
    private final float normalize(float currentValue, float minValue, float maxValue) {
        return 0.0F;
    }
    
    private final void updateOffset() {
    }
    
    private final void updateHeaderProfileOffset() {
    }
    
    private final void updateProfileImageSize() {
    }
    
    private final void updateProfileImageMargins() {
    }
    
    private final int calculateProfileImageSmallMargin() {
        return 0;
    }
    
    private final void updateProfileTextContainerHeight() {
    }
    
    private final void updateProfileTextMargin() {
    }
    
    private final int calculateProfileTextMargin() {
        return 0;
    }
    
    private final void updateSubtitleAndMiscAlpha() {
    }
    
    private final float getIntercept(float m, float x, float b) {
        return 0.0F;
    }
    
    private final float getUpdatedInterpolatedValue(float openSizeTarget, float closedSizeTarget) {
        return 0.0F;
    }
}