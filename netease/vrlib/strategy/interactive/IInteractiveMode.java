package com.netease.vrlib.strategy.interactive;

import android.app.Activity;
import android.content.Context;

/* loaded from: classes5.dex */
public interface IInteractiveMode {
    boolean handleDrag(int i, int i2);

    void onOrientationChanged(Activity activity);

    void onPause(Context context);

    void onResume(Context context);
}