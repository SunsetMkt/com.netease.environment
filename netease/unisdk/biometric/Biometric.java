package com.netease.unisdk.biometric;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/* loaded from: classes.dex */
public interface Biometric {
    public static final int ERROR_AUTH_FAILED = 1;
    public static final int ERROR_NONE_ENROLLED = 3;
    public static final int ERROR_UNSUPPORTED = 2;
    public static final int IS_NOT_SUPPORT = 0;
    public static final int IS_SUPPORT_BIOMETRIC = 1;
    public static final int IS_SUPPORT_ONLY_KEYGUARD = 2;
    public static final int SUCCESS = 0;

    int isDeviceSecured(Context context);

    void onActivityResult(int i, int i2, Intent intent);

    void onWindowFocusChanged(boolean z);

    void verify(Activity activity, String str, String str2, Callback callback);
}