package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class CutoutXiaomi implements CutoutInterface {
    private Method get;
    private boolean hasCutout;
    private int cutoutWidth = 0;
    private int cutoutHeight = 0;
    private int screenWidth = 0;
    private int screenHeight = 0;

    private void getSizes(Activity activity) {
        if (this.cutoutHeight == 0) {
            int identifier = activity.getResources().getIdentifier("notch_height", ResIdReader.RES_TYPE_DIMEN, "android");
            if (identifier <= 0) {
                identifier = activity.getResources().getIdentifier("status_bar_height", ResIdReader.RES_TYPE_DIMEN, "android");
            }
            if (identifier > 0) {
                this.cutoutHeight = activity.getResources().getDimensionPixelSize(identifier);
            } else {
                this.cutoutHeight = 89;
            }
        }
        if (this.cutoutWidth == 0) {
            int identifier2 = activity.getResources().getIdentifier("notch_width", ResIdReader.RES_TYPE_DIMEN, "android");
            if (identifier2 <= 0) {
                identifier2 = activity.getResources().getIdentifier("status_bar_width", ResIdReader.RES_TYPE_DIMEN, "android");
            }
            if (identifier2 > 0) {
                this.cutoutWidth = activity.getResources().getDimensionPixelSize(identifier2);
            } else {
                resetCutoutSize();
            }
        }
        if (this.screenHeight == 0 || this.screenWidth == 0) {
            Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
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
    public boolean hasCutout(Activity activity) throws ClassNotFoundException {
        if (this.get == null) {
            try {
                Class<?> clsLoadClass = activity.getClassLoader().loadClass("android.os.SystemProperties");
                this.get = clsLoadClass.getMethod("getInt", String.class, Integer.TYPE);
                this.get.setAccessible(true);
                this.hasCutout = 1 == ((Integer) this.get.invoke(clsLoadClass, "ro.miui.notch", 0)).intValue();
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
        getSizes(activity);
        int i = this.screenWidth;
        int i2 = this.cutoutWidth;
        return new int[]{(i - i2) >> 1, 0, (i + i2) >> 1, this.cutoutHeight};
    }

    private void resetCutoutSize() {
        String str = Build.DEVICE;
        if ("dipper".equals(str)) {
            this.cutoutWidth = 560;
            this.cutoutHeight = 89;
            return;
        }
        if ("sirius".equals(str)) {
            this.cutoutWidth = 540;
            this.cutoutHeight = 85;
        } else if ("ursa".equals(str)) {
            this.cutoutWidth = 560;
            this.cutoutHeight = 89;
        } else if ("sakura".equals(str)) {
            this.cutoutWidth = 352;
            this.cutoutHeight = 89;
        }
    }
}