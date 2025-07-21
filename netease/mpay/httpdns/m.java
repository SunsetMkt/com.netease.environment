package com.netease.mpay.httpdns;

import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: classes5.dex */
public class m {
    public static String a(String str) {
        try {
            return new URL(str).getHost();
        } catch (MalformedURLException | Exception e) {
            p.a(e);
            return "";
        }
    }
}