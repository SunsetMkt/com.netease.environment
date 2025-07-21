package com.netease.ntunisdk.base.callback;

import com.netease.ntunisdk.base.model.SdkDlBytes;

/* loaded from: classes6.dex */
public interface U3dDlCallback {
    void onFinish(String str, SdkDlBytes sdkDlBytes);

    void onProgress(String str);
}