package com.netease.pushclient;

/* loaded from: classes3.dex */
public interface OnSubscriberListener {
    public static final int ERROR = 404;
    public static final int OK = 0;

    void SubscriberDone(int i, String str, String str2);
}