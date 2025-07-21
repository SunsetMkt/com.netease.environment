package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.lang.reflect.Field;

/* loaded from: classes.dex */
public class CutoutMeizu implements CutoutInterface {
    private int cutoutHeight;
    private int cutoutWidth;
    private Field field;
    private boolean hasCutout;
    private int screenHeight;
    private int screenWidth;

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        if (!this.hasCutout) {
            try {
                if (this.field == null) {
                    this.field = Class.forName("flyme.config.FlymeFeature").getDeclaredField("IS_FRINGE_DEVICE");
                    this.hasCutout = ((Boolean) this.field.get(null)).booleanValue();
                }
            } catch (ClassNotFoundException unused) {
                Log.w("cutout", "hasCutout ClassNotFoundException");
            } catch (IllegalAccessException unused2) {
                Log.w("cutout", "hasCutout IllegalAccessException");
            } catch (NoSuchFieldException unused3) {
                Log.w("cutout", "hasCutout NoSuchFieldException");
            }
            if (!this.hasCutout) {
                Rect rect = new Rect();
                activity.getWindow().getDecorView().getRootView().getWindowVisibleDisplayFrame(rect);
                this.hasCutout = (rect.left == 0 && rect.top == 0) ? false : true;
            }
            if (this.screenHeight == 0 || this.screenWidth == 0) {
                Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
                Point point = new Point();
                defaultDisplay.getSize(point);
                this.screenWidth = point.x;
                this.screenHeight = point.y;
            }
        }
        return this.hasCutout;
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutWidthHeight(Activity activity) {
        if (hasCutout(activity) && (this.cutoutWidth == 0 || this.cutoutHeight == 0)) {
            int identifier = activity.getResources().getIdentifier("fringe_height", ResIdReader.RES_TYPE_DIMEN, "android");
            if (identifier > 0) {
                this.cutoutHeight = activity.getResources().getDimensionPixelSize(identifier);
            }
            int identifier2 = activity.getResources().getIdentifier("fringe_width", ResIdReader.RES_TYPE_DIMEN, "android");
            if (identifier2 > 0) {
                this.cutoutWidth = activity.getResources().getDimensionPixelSize(identifier2);
            }
        }
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
}