package com.netease.ntunisdk.base;

/* loaded from: classes2.dex */
public interface OnFinishInitListener {
    public static final int FAILED = 1;
    public static final int MISSED = 2;
    public static final int NEED_UPDATE_PACKAGE = 3;
    public static final int OK = 0;

    void finishInit(int i);
}