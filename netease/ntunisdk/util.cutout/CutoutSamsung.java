package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.util.List;

/* loaded from: classes.dex */
public class CutoutSamsung implements CutoutInterface {
    private static final String TAG = "CutoutSamsung";
    private boolean mHasDisplayCutout;
    private Rect rect;

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        if (!this.mHasDisplayCutout) {
            try {
                if (Build.VERSION.SDK_INT < 23) {
                    return false;
                }
                Resources resources = activity.getResources();
                int identifier = resources.getIdentifier("config_mainBuiltInDisplayCutout", ResIdReader.RES_TYPE_STRING, "android");
                String string = identifier > 0 ? resources.getString(identifier) : null;
                this.mHasDisplayCutout = (string == null || TextUtils.isEmpty(string)) ? false : true;
                Resources resources2 = activity.getApplicationContext().getResources();
                int identifier2 = resources2.getIdentifier("", ResIdReader.RES_TYPE_DIMEN, "android");
                Log.i(TAG, "statusBarHeight: " + (identifier2 > 0 ? resources2.getDimensionPixelSize(identifier2) : 0));
                View decorView = activity.getWindow().getDecorView();
                Log.i(TAG, "decorView:" + decorView);
                WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
                Log.i(TAG, "windowInsets:" + rootWindowInsets);
                if (rootWindowInsets == null) {
                    return this.mHasDisplayCutout;
                }
                Object objInvoke = WindowInsets.class.getDeclaredMethod("getDisplayCutout", new Class[0]).invoke(rootWindowInsets, new Object[0]);
                if (objInvoke == null) {
                    if (Build.MODEL.equalsIgnoreCase("SM-G8870")) {
                        this.rect = getA8sRect(activity);
                    } else {
                        Object objInvoke2 = WindowInsets.class.getDeclaredMethod("getStableInsetTop", new Class[0]).invoke(rootWindowInsets, new Object[0]);
                        if (objInvoke2 != null) {
                            this.rect = new Rect(0, 0, 0, ((Integer) objInvoke2).intValue());
                        }
                    }
                } else {
                    List list = (List) objInvoke.getClass().getDeclaredMethod("getBoundingRects", new Class[0]).invoke(objInvoke, new Object[0]);
                    if (list != null && !list.isEmpty()) {
                        this.rect = (Rect) list.get(0);
                    }
                }
            } catch (Throwable th) {
                Log.i(TAG, "Can not update hasDisplayCutout. " + th.toString());
            }
        }
        return this.mHasDisplayCutout;
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutWidthHeight(Activity activity) {
        Rect rect;
        return (!hasCutout(activity) || (rect = this.rect) == null) ? new int[]{0, 0} : new int[]{rect.right - this.rect.left, this.rect.bottom - this.rect.top};
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutPosition(Activity activity) {
        Rect rect;
        return (!hasCutout(activity) || (rect = this.rect) == null) ? new int[]{0, 0, 0, 0} : new int[]{rect.left, this.rect.top, this.rect.right, this.rect.bottom};
    }

    private Rect getA8sRect(Activity activity) {
        Rect rect = new Rect(0, 0, 135, 135);
        WindowManager windowManager = (WindowManager) activity.getSystemService("window");
        if (windowManager == null) {
            return rect;
        }
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i = point.x;
        int i2 = point.y;
        int iMax = Math.max(i, i);
        int i3 = (i + i2) - iMax;
        int orientation = windowManager.getDefaultDisplay().getOrientation();
        if (orientation == 1) {
            return new Rect(0, i3 - 135, 135, i3);
        }
        if (orientation != 2) {
            return orientation != 3 ? rect : new Rect(iMax - 135, 0, iMax, 135);
        }
        return new Rect(i3 - 135, iMax - 135, i3, iMax);
    }
}