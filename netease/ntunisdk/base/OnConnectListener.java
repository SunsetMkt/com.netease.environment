package com.netease.ntunisdk.base;

/* loaded from: classes2.dex */
public interface OnConnectListener {
    public static final int CANCELED = 5;
    public static final int Cancel = 0;
    public static final int Create = 3;
    public static final int FAILED = 1;
    public static final int Load = 2;
    public static final int MODIFIED = 4;
    public static final int NEWID = 2;
    public static final int OK = 0;
    public static final int SIGN_CANCELED = 6;
    public static final int USEDID = 3;
    public static final int Update = 1;

    void onConnectToChannelFinished(int i);

    void onDisConnectToChannelFinished(int i);

    void onSelectChannelOptionFinished(boolean z);
}