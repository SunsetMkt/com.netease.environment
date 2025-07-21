package com.netease.cloud.nos.android.exception;

/* loaded from: classes3.dex */
public class InvalidChunkSizeException extends Exception {
    private static final long serialVersionUID = -9081338843636519886L;

    public InvalidChunkSizeException() {
    }

    public InvalidChunkSizeException(String str) {
        super(str);
    }

    public InvalidChunkSizeException(String str, Throwable th) {
        super(str, th);
    }

    public InvalidChunkSizeException(Throwable th) {
        super(th);
    }
}