package com.netease.ntsharesdk;

/* loaded from: classes.dex */
public interface OnShareEndListener {
    public static final int CANCEL = 1;
    public static final int FAILED = 2;
    public static final int OK = 0;
    public static final int UNINSTALLED = 3;

    void onShareEnd(String str, int i, ShareArgs shareArgs);
}