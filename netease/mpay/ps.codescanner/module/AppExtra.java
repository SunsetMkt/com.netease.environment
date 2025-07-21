package com.netease.mpay.ps.codescanner.module;

import java.io.Serializable;

/* loaded from: classes6.dex */
public class AppExtra implements Serializable {
    private static final long serialVersionUID = 6679031982038319029L;
    public String channel;
    public String extra;
    public String extraUniData;
    public String sdkJsonData;
    public String udid;
    public String version;

    public AppExtra(String str, String str2, String str3, String str4, String str5, String str6) {
        this.udid = str;
        this.channel = str2;
        this.version = str3;
        this.sdkJsonData = str4;
        this.extra = str5;
        this.extraUniData = str6;
    }
}