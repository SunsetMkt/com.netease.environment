package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.graphics.Insets;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;

/* loaded from: classes.dex */
public class WaterfallAndroidR implements WaterfallInterface {
    private static final String TAG = "WaterfallAndroidR";
    private boolean hasWaterfall = false;
    private int[] safeInset = {0, 0, 0, 0};
    private WindowInsets windowInsets;

    private boolean checkAndInit(Activity activity) {
        if (this.windowInsets == null) {
            View decorView = activity.getWindow().getDecorView();
            Log.i(TAG, "decorView:" + decorView);
            this.windowInsets = decorView.getRootWindowInsets();
            Log.i(TAG, "windowInsets:" + this.windowInsets);
            WindowInsets windowInsets = this.windowInsets;
            if (windowInsets == null) {
                this.hasWaterfall = false;
                return false;
            }
            DisplayCutout displayCutout = windowInsets.getDisplayCutout();
            Log.i(TAG, "displayCutout:" + displayCutout);
            if (displayCutout != null) {
                if (Insets.NONE == displayCutout.getWaterfallInsets()) {
                    this.hasWaterfall = false;
                    return false;
                }
                this.safeInset = new int[]{displayCutout.getSafeInsetLeft(), displayCutout.getSafeInsetTop(), displayCutout.getSafeInsetRight(), displayCutout.getSafeInsetBottom()};
                int[] iArr = this.safeInset;
                this.hasWaterfall = (iArr[0] == 0 && iArr[2] == 0) ? false : true;
            }
        }
        return this.hasWaterfall;
    }

    @Override // com.netease.ntunisdk.util.cutout.WaterfallInterface
    public boolean hasWaterfall(Activity activity) {
        return checkAndInit(activity);
    }

    @Override // com.netease.ntunisdk.util.cutout.WaterfallInterface
    public int[] getSafeInset(Activity activity) {
        checkAndInit(activity);
        return this.safeInset;
    }
}