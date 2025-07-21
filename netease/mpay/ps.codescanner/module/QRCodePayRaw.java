package com.netease.mpay.ps.codescanner.module;

/* loaded from: classes6.dex */
public class QRCodePayRaw extends QRCodeRaw {
    public String dataId;
    public String uid;

    public QRCodePayRaw(String str, String str2) {
        this.uid = str;
        this.dataId = str2;
    }
}