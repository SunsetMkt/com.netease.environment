package com.netease.ntunisdk.base;

/* loaded from: classes2.dex */
public interface OnLogoutDoneListener {
    public static final int FAILED = 1;
    public static final int NEED_RELOGIN = 3;
    public static final int NOLOGIN = 2;
    public static final int OK = 0;

    void logoutDone(int i);
}