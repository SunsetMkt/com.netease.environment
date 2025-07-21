package com.netease.ntunisdk.util.cutout;

import android.app.Activity;

/* loaded from: classes.dex */
public interface WaterfallInterface {
    int[] getSafeInset(Activity activity);

    boolean hasWaterfall(Activity activity);
}