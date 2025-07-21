package com.netease.ntunisdk.external.protocol.plugins;

import org.json.JSONObject;

/* loaded from: classes.dex */
public class Result {
    private final int code;
    private JSONObject data;
    private boolean isSuccess;
    private final String msg;

    public Result(JSONObject jSONObject) {
        this.isSuccess = false;
        this.data = jSONObject;
        this.isSuccess = true;
        this.code = 0;
        this.msg = "";
    }

    public Result(int i, String str) {
        this.isSuccess = false;
        this.code = i;
        this.msg = str;
        this.isSuccess = false;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }
}