package app.regate.map.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.regate.map.R;

@kotlin.Metadata(mv = {1, 8, 0}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\b\u0010\u001a\u001a\u0004\u0018\u00010\tJ\u0006\u0010\u001b\u001a\u00020\u000bJ\u0006\u0010\u001c\u001a\u00020\u000bJ\b\u0010\u001d\u001a\u0004\u0018\u00010\tJ\b\u0010\u001e\u001a\u0004\u0018\u00010\tJ\b\u0010\u001f\u001a\u00020\u0019H\u0002J\u000e\u0010 \u001a\u00020\u00192\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010!\u001a\u00020\u00192\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\"\u001a\u00020\u00192\u0006\u0010\u000e\u001a\u00020\u000bJ\u000e\u0010#\u001a\u00020\u00192\u0006\u0010\u0012\u001a\u00020\tJ\u000e\u0010$\u001a\u00020\u00192\u0006\u0010\u0016\u001a\u00020\tR\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006%"}, d2 = {"Lapp/regate/map/view/CollapsingProfileHeaderView;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "misc", "", "miscIcon", "", "miscTextView", "Landroid/widget/TextView;", "profileDrawable", "profileImage", "Landroid/widget/ImageView;", "profileMiscTextSize", "profileName", "profileNameTextSize", "profileNameTextView", "profileSubtitleTextSize", "subtitle", "subtitleTextView", "applyAttributes", "", "getMisc", "getMiscIcon", "getProfileDrawable", "getProfileName", "getSubtitle", "loadViews", "setMisc", "setMiscIcon", "setProfileDrawable", "setProfileName", "setSubtitle", "map_debug"})
public final class CollapsingProfileHeaderView extends android.widget.LinearLayout {
    private int profileDrawable = 0;
    private java.lang.String profileName;
    private java.lang.String subtitle;
    private java.lang.String misc;
    private int miscIcon = 0;
    private int profileNameTextSize = 0;
    private int profileSubtitleTextSize = 0;
    private int profileMiscTextSize = 0;
    private android.widget.ImageView profileImage;
    private android.widget.TextView profileNameTextView;
    private android.widget.TextView subtitleTextView;
    private android.widget.TextView miscTextView;
    
    public CollapsingProfileHeaderView(@org.jetbrains.annotations.NotNull
    android.content.Context context) {
        super(null);
    }
    
    public CollapsingProfileHeaderView(@org.jetbrains.annotations.NotNull
    android.content.Context context, @org.jetbrains.annotations.NotNull
    android.util.AttributeSet attrs) {
        super(null);
    }
    
    private final void loadViews() {
    }
    
    private final void applyAttributes() {
    }
    
    public final int getProfileDrawable() {
        return 0;
    }
    
    public final void setProfileDrawable(int profileDrawable) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getProfileName() {
        return null;
    }
    
    public final void setProfileName(@org.jetbrains.annotations.NotNull
    java.lang.String profileName) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getSubtitle() {
        return null;
    }
    
    public final void setSubtitle(@org.jetbrains.annotations.NotNull
    java.lang.String subtitle) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getMisc() {
        return null;
    }
    
    public final void setMisc(@org.jetbrains.annotations.NotNull
    java.lang.String misc) {
    }
    
    public final int getMiscIcon() {
        return 0;
    }
    
    public final void setMiscIcon(int miscIcon) {
    }
}