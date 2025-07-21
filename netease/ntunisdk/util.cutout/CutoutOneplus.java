package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.os.Build;

/* loaded from: classes.dex */
public class CutoutOneplus implements CutoutInterface {
    private int left = 378;
    private int top = 0;
    private int right = 702;
    private int bottom = 80;

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        String str = Build.MODEL;
        return str.endsWith("A6000") || str.endsWith("A6003") || activity.getPackageManager().hasSystemFeature("com.oneplus.screen.cameranotch");
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