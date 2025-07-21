package com.netease.mpay.ps.codescanner.server.api;

/* loaded from: classes5.dex */
public class ConfirmLoginResp extends Response {
    public final String mRedirectUrl;
    public final String scene;

    public ConfirmLoginResp(String str, String str2) {
        this.mRedirectUrl = str;
        this.scene = str2;
    }
}