package com.netease.cloud.nos.android.http;

import org.json.JSONObject;

/* loaded from: classes6.dex */
public class HttpResult {
    private Exception exception;
    private JSONObject msg;
    private int statusCode;

    public HttpResult(int i, JSONObject jSONObject, Exception exc) {
        this.statusCode = i;
        this.msg = jSONObject;
        this.exception = exc;
    }

    public Exception getException() {
        return this.exception;
    }

    public JSONObject getMsg() {
        return this.msg;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    protected void setException(Exception exc) {
        this.exception = exc;
    }

    public void setMsg(JSONObject jSONObject) {
        this.msg = jSONObject;
    }

    public void setStatusCode(int i) {
        this.statusCode = i;
    }
}