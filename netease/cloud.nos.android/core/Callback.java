package com.netease.cloud.nos.android.core;

/* loaded from: classes5.dex */
public interface Callback {
    void onCanceled(CallRet callRet);

    void onFailure(CallRet callRet);

    void onProcess(Object obj, long j, long j2);

    void onSuccess(CallRet callRet);

    void onUploadContextCreate(Object obj, String str, String str2);
}