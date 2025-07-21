package com.netease.cloud.nos.android.core;

import android.util.Base64;

/* loaded from: classes5.dex */
public class CallRet {
    private String callbackRetMsg;
    private Exception exception;
    private Object fileParam;
    private int httpCode;
    private String requestId;
    private String response;
    private String uploadContext;

    public CallRet(Object obj, String str, int i, String str2, String str3, String str4, Exception exc) {
        this.fileParam = obj;
        this.uploadContext = str;
        this.httpCode = i;
        this.requestId = str2;
        this.callbackRetMsg = new String(Base64.decode(str3, 0));
        this.response = str4;
        this.exception = exc;
    }

    public String getCallbackRetMsg() {
        return this.callbackRetMsg;
    }

    public Exception getException() {
        return this.exception;
    }

    public Object getFileParam() {
        return this.fileParam;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getResponse() {
        return this.response;
    }

    public String getUploadContext() {
        return this.uploadContext;
    }

    public boolean isOK() {
        return this.httpCode == 200;
    }

    public void setCallbackRetMsg(String str) {
        this.callbackRetMsg = str;
    }

    public void setException(Exception exc) {
        this.exception = exc;
    }

    public void setFileParam(Object obj) {
        this.fileParam = obj;
    }

    public void setHttpCode(int i) {
        this.httpCode = i;
    }

    public void setRequestId(String str) {
        this.requestId = str;
    }

    public void setResponse(String str) {
        this.response = str;
    }

    public void setUploadContext(String str) {
        this.uploadContext = str;
    }
}