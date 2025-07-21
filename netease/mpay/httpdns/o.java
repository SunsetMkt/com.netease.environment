package com.netease.mpay.httpdns;

/* loaded from: classes5.dex */
public class o {

    /* renamed from: a, reason: collision with root package name */
    public final int f1597a;

    public o(int i, int i2, String str) {
        this.f1597a = i;
    }

    public String a(int i, String str) {
        return String.format("[%s]:%s", i != -1 ? i != 0 ? i != 2 ? "INFO" : "DEBUG" : "WARN" : "ERROR", str);
    }
}