package com.netease.ntunisdk.external.protocol;

/* loaded from: classes.dex */
public interface ProtocolCallback {
    public static final int ACCEPT = 1;
    public static final int CONFIRM = 0;
    public static final int NEED_QUERY = 4;
    public static final int NOT_NEED_SHOW = 3;
    public static final int REJECT = 2;

    void onFinish(int i);

    void onOpen();
}