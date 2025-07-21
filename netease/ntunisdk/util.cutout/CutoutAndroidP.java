package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import com.netease.ntunisdk.util.cutout.SingleScreenFoldingUtil;
import java.util.List;

/* loaded from: classes.dex */
public class CutoutAndroidP implements CutoutInterface {
    private static final String TAG = "CutoutAndroidP";
    private Rect rect;
    private WindowInsets windowInsets;

    private boolean checkAndInit(Activity activity) {
        if (this.windowInsets == null) {
            View decorView = activity.getWindow().getDecorView();
            Log.i(TAG, "decorView:" + decorView);
            this.windowInsets = decorView.getRootWindowInsets();
            Log.i(TAG, "windowInsets:" + this.windowInsets);
            WindowInsets windowInsets = this.windowInsets;
            if (windowInsets == null) {
                return false;
            }
            DisplayCutout displayCutout = windowInsets.getDisplayCutout();
            Log.i(TAG, "displayCutout:" + displayCutout);
            if (displayCutout == null) {
                boolean z = (activity.getWindow().getAttributes().flags & 1024) != 0;
                Log.i(TAG, "FLAG_FULLSCREEN:" + z);
                if (!z) {
                    return false;
                }
                this.rect = new Rect();
                activity.getWindow().getDecorView().getRootView().getWindowVisibleDisplayFrame(this.rect);
                if (this.rect.left == 0 && this.rect.top == 0) {
                    this.rect = null;
                } else {
                    if (this.rect.left == 0) {
                        Rect rect = this.rect;
                        rect.bottom = rect.top;
                    } else {
                        Rect rect2 = this.rect;
                        rect2.right = rect2.left;
                    }
                    Rect rect3 = this.rect;
                    rect3.top = 0;
                    rect3.left = 0;
                }
            } else {
                List<Rect> boundingRects = displayCutout.getBoundingRects();
                Log.i(TAG, "displayCutout:" + boundingRects);
                if (boundingRects != null && !boundingRects.isEmpty()) {
                    this.rect = boundingRects.get(0);
                }
            }
        }
        return true;
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        return Build.VERSION.SDK_INT >= 28 && checkAndInit(activity) && this.rect != null;
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutWidthHeight(Activity activity) {
        return hasCutout(activity) ? new int[]{this.rect.right - this.rect.left, this.rect.bottom - this.rect.top} : new int[]{0, 0};
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutPosition(Activity activity) {
        if (!hasCutout(activity)) {
            return new int[]{0, 0, 0, 0};
        }
        int[] iArr = {this.rect.left, this.rect.top, this.rect.right, this.rect.bottom};
        handleFoldingScreen(activity, iArr);
        return iArr;
    }

    private void handleFoldingScreen(Activity activity, int[] iArr) {
        Log.d(TAG, "handleFoldingScreen -> model: " + Build.MODEL);
        if ("PAL-AL00".equalsIgnoreCase(Build.MODEL)) {
            int[] cutoutLocation = SingleScreenFoldingUtil.getCutoutLocation(activity, iArr, SingleScreenFoldingUtil.LocationType.BASE_RIGHT);
            System.arraycopy(cutoutLocation, 0, iArr, 0, cutoutLocation.length);
        }
    }
}