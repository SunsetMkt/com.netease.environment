package com.netease.ntunisdk.util.cutout;

import android.app.Activity;

/* loaded from: classes.dex */
public class CutoutLenovo implements CutoutInterface {
    private int h;
    private boolean hasCutout = false;
    private int w;
    private int x;
    private int y;

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        if (!this.hasCutout) {
            int identifier = activity.getResources().getIdentifier("config_screen_has_notch", "bool", "android");
            this.hasCutout = identifier != 0 && activity.getResources().getBoolean(identifier);
            int identifier2 = activity.getResources().getIdentifier("notch_x", "integer", "android");
            this.x = identifier2 != 0 ? activity.getResources().getInteger(identifier2) : 0;
            int identifier3 = activity.getResources().getIdentifier("notch_y", "integer", "android");
            this.y = identifier3 != 0 ? activity.getResources().getInteger(identifier3) : 0;
            int identifier4 = activity.getResources().getIdentifier("notch_w", "integer", "android");
            this.w = identifier4 != 0 ? activity.getResources().getInteger(identifier4) : 0;
            int identifier5 = activity.getResources().getIdentifier("notch_h", "integer", "android");
            this.h = identifier5 != 0 ? activity.getResources().getInteger(identifier5) : 0;
        }
        return this.hasCutout;
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutWidthHeight(Activity activity) {
        return hasCutout(activity) ? new int[]{this.w, this.h} : new int[]{0, 0};
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutPosition(Activity activity) {
        if (!hasCutout(activity)) {
            return new int[]{0, 0, 0, 0};
        }
        int i = this.x;
        int i2 = this.y;
        return new int[]{i, i2, i + this.w, i2 + this.h};
    }
}