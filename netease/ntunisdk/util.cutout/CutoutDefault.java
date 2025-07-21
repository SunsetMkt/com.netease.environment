package com.netease.ntunisdk.util.cutout;

import android.app.Activity;

/* loaded from: classes.dex */
public class CutoutDefault implements CutoutInterface {
    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        return false;
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutWidthHeight(Activity activity) {
        return new int[]{0, 0};
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutPosition(Activity activity) {
        return new int[]{0, 0, 0, 0};
    }
}