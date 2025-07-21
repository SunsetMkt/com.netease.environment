package com.netease.cloud.nos.android.exception;

/* loaded from: classes3.dex */
public class InvalidParameterException extends Exception {
    private static final long serialVersionUID = 5066837976183983722L;

    public InvalidParameterException() {
    }

    public InvalidParameterException(String str) {
        super(str);
    }

    public InvalidParameterException(String str, Throwable th) {
        super(str, th);
    }

    public InvalidParameterException(Throwable th) {
        super(th);
    }
}