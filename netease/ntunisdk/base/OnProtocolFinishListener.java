package com.netease.ntunisdk.base;

/* loaded from: classes2.dex */
public interface OnProtocolFinishListener {
    public static final int ACCEPT = 1;
    public static final int CONFIRM = 0;
    public static final int NOT_NEED_SHOW = 3;
    public static final int REJECT = 2;

    void onProtocolFinish(int i);
}