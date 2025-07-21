package com.netease.mpay.ps.codescanner;

import android.text.TextUtils;

/* loaded from: classes3.dex */
public class Configs {
    public static final String API_PREFIX = "https://service.mkey.163.com/mpay";
    public static final String API_PREFIX_TEST = "https://qatest.g.mkey.163.com/mpay_test";
    public static boolean DEBUG_MODE = false;
    public static boolean IS_TEST_ENV = false;
    private static String sHost = "https://service.mkey.163.com/mpay";

    public static String getHost() {
        return sHost;
    }

    public static void setHost(String str) {
        if (TextUtils.isEmpty(str)) {
            sHost = API_PREFIX;
        } else {
            sHost = str;
        }
    }
}