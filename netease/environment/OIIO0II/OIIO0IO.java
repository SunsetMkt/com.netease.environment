package com.netease.environment.OIIO0II;

import java.net.MalformedURLException;
import java.net.URL;

/* compiled from: HttpUtils.java */
/* loaded from: classes5.dex */
public class OIIO0IO {
    public static boolean OIIO00I(String str) {
        if (str == null) {
            return false;
        }
        try {
            new URL(str);
            return true;
        } catch (MalformedURLException unused) {
            return false;
        }
    }
}