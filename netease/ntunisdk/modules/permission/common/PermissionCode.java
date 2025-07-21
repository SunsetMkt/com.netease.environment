package com.netease.ntunisdk.modules.permission.common;

/* loaded from: classes.dex */
public enum PermissionCode {
    UNSUPPORTED_OPERATION(-3, "Unsupported operation"),
    JSON_ERROR(-2, "Incoming json format error"),
    FAILURE(-1, "Call failed"),
    SUCCESS(0, "Call success"),
    NO_EXIST_METHOD(1, "Method does not exist (methodId does not exist under channel)"),
    PARAM_ERROR(2, "Parameter error (required, wrong type)"),
    UNKNOWN_ERROR(10000, "Unknown error");

    private final int code;
    private final String msg;

    PermissionCode(int i, String str) {
        this.code = i;
        this.msg = str;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}