package com.netease.vrlib.strategy;

import android.app.Activity;
import android.content.Context;

/* loaded from: classes5.dex */
public interface IModeStrategy {
    boolean isSupport(Activity activity);

    void off(Activity activity);

    void on(Activity activity);

    void onPause(Context context);

    void onResume(Context context);
}