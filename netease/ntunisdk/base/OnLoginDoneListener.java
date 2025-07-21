package com.netease.ntunisdk.base;

/* loaded from: classes2.dex */
public interface OnLoginDoneListener {
    public static final int AAS_LIMIT = 22;
    public static final int ACCOUNT_REGION_ERR = 15;
    public static final int ACCOUNT_REMOVED = 25;
    public static final int ACCOUNT_REMOVING = 24;
    public static final int BIND_OK = 13;
    public static final int CANCEL = 1;
    public static final int CHILD_PROTECTION_ERR = 16;
    public static final int CHILD_PROTECTION_NEED_LOGOUT = 17;
    public static final int LOGIN_GREEN = 14;
    public static final int NEED_GS_CONFIRM = 11;
    public static final int NEED_RELOGIN = 12;
    public static final int NET_TIME_OUT = 5;
    public static final int NET_UNAVAILABLE = 3;
    public static final int NOT_REAL_NAME = 21;
    public static final int OK = 0;
    public static final int QUICK_REPEAT_LOGIN = 23;
    public static final int SDK_NOT_AGREE_PROTOCOL = 20;
    public static final int SDK_NOT_INIT = 6;
    public static final int SDK_SERV_ERR = 4;
    public static final int UNISDK_BIND_FAIL = -130;
    public static final int UNISDK_BIND_OK = 130;
    public static final int UNKNOWN_ERR = 10;
    public static final int WRONG_PASSWD = 2;

    void loginDone(int i);
}