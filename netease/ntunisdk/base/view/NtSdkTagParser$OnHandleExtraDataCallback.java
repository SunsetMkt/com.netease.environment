package com.netease.ntunisdk.base.view;

/* loaded from: classes5.dex */
public interface NtSdkTagParser$OnHandleExtraDataCallback {
    public static final int LOGIN_ERROR = 1;
    public static final int NETWORK_ERROR = 2;
    public static final int UNKNOWN_ERROR = 3;

    void onFailure(int i, String str);

    void onSuccess(String str);
}