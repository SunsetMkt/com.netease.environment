package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.os.Build;

/* loaded from: classes.dex */
public class CutoutLg implements CutoutInterface {
    private int left = 432;
    private int top = 0;
    private int right = 1008;
    private int bottom = 94;

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        return Build.MODEL.contains("LM-G710");
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