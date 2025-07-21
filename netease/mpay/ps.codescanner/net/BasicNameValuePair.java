package com.netease.mpay.ps.codescanner.net;

import java.io.Serializable;

/* loaded from: classes5.dex */
public class BasicNameValuePair implements NameValuePair, Serializable {
    private static final long serialVersionUID = -6437800752854696262L;
    private final String name;
    private final String value;

    public BasicNameValuePair(String str, String str2) {
        if (str == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.name = str;
        this.value = str2;
    }

    @Override // com.netease.mpay.ps.codescanner.net.NameValuePair
    public String getName() {
        return this.name;
    }

    @Override // com.netease.mpay.ps.codescanner.net.NameValuePair
    public String getValue() {
        return this.value;
    }
}