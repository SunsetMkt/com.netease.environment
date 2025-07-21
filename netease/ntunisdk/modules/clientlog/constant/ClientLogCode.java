package com.netease.ntunisdk.modules.clientlog.constant;

/* loaded from: classes6.dex */
public enum ClientLogCode {
    SUCCESS(0, "Success"),
    NO_EXIST_METHOD(1, "Method does not exist (methodId does not exist under channel)"),
    PARAM_ERROR(2, "Parameter error (required, wrong type)"),
    DATABASE_NOT_OPEN_ERROR(7, "Database error"),
    UNKNOWN_ERROR(1000, "Unknown error");

    private final int code;
    private final String msg;

    ClientLogCode(int i, String str) {
        this.code = i;
        this.msg = str;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public boolean isSuccess() {
        return getCode() == 0;
    }
}