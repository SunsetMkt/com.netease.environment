package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.os.Build;

/* loaded from: classes.dex */
public class CutoutNubia implements CutoutInterface {
    private int left = 462;
    private int top = 0;
    private int right = 618;
    private int bottom = 78;

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        return Build.MODEL.equalsIgnoreCase("NX606J");
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutWidthHeight(Activity activity) {
        return hasCutout(activity) ? new int[]{this.right - this.left, this.bottom - this.top} : new int[]{0, 0};
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutPosition(Activity activity) {
        return hasCutout(activity) ? new int[]{this.left, this.top, this.right, this.bottom} : new int[]{0, 0, 0, 0};
    }
}