package com.netease.ntunisdk.external.protocol.plugins;

import android.content.Context;

/* loaded from: classes.dex */
public interface Plugin {
    void exit(Context context, Callback callback);

    String getChannel();

    boolean hasInit();

    void init(Context context, Callback callback);

    boolean isSupport();

    boolean isSupportRunning();
}