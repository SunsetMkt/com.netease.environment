package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class CutoutHuawei implements CutoutInterface, WaterfallInterface {
    private int cutoutHeight;
    private int cutoutWidth;
    private Method get;
    private boolean hasCutout;
    private int screenHeight;
    private int screenWidth;

    private boolean hasNotchInScreen(Context context) throws ClassNotFoundException {
        if (this.get == null) {
            try {
                Class<?> clsLoadClass = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
                this.get = clsLoadClass.getMethod("hasNotchInScreen", new Class[0]);
                this.hasCutout = ((Boolean) this.get.invoke(clsLoadClass, new Object[0])).booleanValue();
            } catch (ClassNotFoundException unused) {
                Log.w("cutout", "hasNotchInScreen ClassNotFoundException");
            } catch (NoSuchMethodException unused2) {
                Log.w("cutout", "hasNotchInScreen NoSuchMethodException");
            } catch (Exception unused3) {
                Log.w("cutout", "hasNotchInScreen Exception");
            }
        }
        return this.hasCutout;
    }

    private void getNotchSize(Context context) throws ClassNotFoundException {
        if (this.cutoutWidth == 0 || this.cutoutHeight == 0) {
            try {
                Class<?> clsLoadClass = context.getClassLoader().loadClass("com.huawei.android.util.HwNotchSizeUtil");
                int[] iArr = (int[]) clsLoadClass.getMethod("getNotchSize", new Class[0]).invoke(clsLoadClass, new Object[0]);
                this.cutoutWidth = iArr[0];
                this.cutoutHeight = iArr[1];
            } catch (ClassNotFoundException unused) {
                Log.w("cutout", "getNotchSize ClassNotFoundException");
            } catch (NoSuchMethodException unused2) {
                Log.w("cutout", "getNotchSize NoSuchMethodException");
            } catch (Exception unused3) {
                Log.w("cutout", "getNotchSize Exception");
            }
        }
        if (this.screenHeight == 0 || this.screenWidth == 0) {
            Display defaultDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            this.screenWidth = point.x;
            this.screenHeight = point.y;
        }
        int i = this.screenWidth;
        int i2 = this.screenHeight;
        if (i > i2) {
            this.screenWidth = i2;
            this.screenHeight = i;
        }
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        return hasNotchInScreen(activity);
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutWidthHeight(Activity activity) throws ClassNotFoundException {
        getNotchSize(activity);
        return new int[]{this.cutoutWidth, this.cutoutHeight};
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutPosition(Activity activity) throws ClassNotFoundException {
        getNotchSize(activity);
        if (!hasCutout(activity)) {
            return new int[]{0, 0, 0, 0};
        }
        int i = this.screenWidth;
        int i2 = this.cutoutWidth;
        return new int[]{(i - i2) >> 1, 0, (i + i2) >> 1, this.cutoutHeight};
    }

    @Override // com.netease.ntunisdk.util.cutout.WaterfallInterface
    public boolean hasWaterfall(Activity activity) {
        return Build.MODEL.startsWith("LIO");
    }

    @Override // com.netease.ntunisdk.util.cutout.WaterfallInterface
    public int[] getSafeInset(Activity activity) {
        return CutoutUtil.FALSE_WATERFALL_SAFETY;
    }
}