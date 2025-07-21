package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class CutoutVivo implements CutoutInterface, WaterfallInterface {
    private int cutoutHeight;
    private int cutoutWidth;
    private Method get;
    private boolean hasCutout;
    private int screenHeight;
    private int screenWidth;

    private int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private void getSizes(Activity activity) {
        if (this.cutoutWidth == 0 || this.cutoutHeight == 0) {
            this.cutoutWidth = dip2px(activity, 100.0f);
            this.cutoutHeight = dip2px(activity, 27.0f);
        }
        if (this.screenHeight == 0 || this.screenWidth == 0) {
            Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            this.screenWidth = point.x;
            this.screenHeight = point.y;
        }
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) throws ClassNotFoundException {
        if (this.get == null) {
            try {
                Class<?> clsLoadClass = activity.getClassLoader().loadClass("android.util.FtFeature");
                this.get = clsLoadClass.getMethod("isFeatureSupport", Integer.TYPE);
                this.hasCutout = ((Boolean) this.get.invoke(clsLoadClass, 32)).booleanValue();
            } catch (ClassNotFoundException unused) {
                Log.w("cutout", "hasCutout ClassNotFoundException");
            } catch (NoSuchMethodException unused2) {
                Log.w("cutout", "hasCutout NoSuchMethodException");
            } catch (Exception unused3) {
                Log.w("cutout", "hasCutout Exception");
            }
        }
        return this.hasCutout;
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutWidthHeight(Activity activity) {
        if (!hasCutout(activity)) {
            return new int[]{0, 0};
        }
        getSizes(activity);
        return new int[]{this.cutoutWidth, this.cutoutHeight};
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutPosition(Activity activity) {
        if (!hasCutout(activity)) {
            return new int[]{0, 0, 0, 0};
        }
        int i = this.screenWidth;
        int i2 = this.cutoutWidth;
        return new int[]{(i - i2) >> 1, 0, (i + i2) >> 1, this.cutoutHeight};
    }

    @Override // com.netease.ntunisdk.util.cutout.WaterfallInterface
    public boolean hasWaterfall(Activity activity) {
        String str = Build.MODEL;
        return str.startsWith("V1923") || str.startsWith("V1924") || str.startsWith("V1950");
    }

    @Override // com.netease.ntunisdk.util.cutout.WaterfallInterface
    public int[] getSafeInset(Activity activity) {
        return CutoutUtil.FALSE_WATERFALL_SAFETY;
    }
}