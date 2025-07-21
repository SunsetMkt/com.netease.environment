package com.netease.mpay.ps.codescanner.server;

/* loaded from: classes5.dex */
public class ApiCallException extends Exception {
    private static final long serialVersionUID = -6598728910413838206L;
    private String mApiErrorMessage;

    public String getErrorMsg() {
        return this.mApiErrorMessage;
    }

    public ApiCallException(String str) {
        this.mApiErrorMessage = str;
    }
}