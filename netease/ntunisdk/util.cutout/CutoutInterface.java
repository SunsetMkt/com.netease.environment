package com.netease.ntunisdk.util.cutout;

import android.app.Activity;

/* loaded from: classes.dex */
public interface CutoutInterface {
    int[] getCutoutPosition(Activity activity);

    int[] getCutoutWidthHeight(Activity activity);

    boolean hasCutout(Activity activity);
}