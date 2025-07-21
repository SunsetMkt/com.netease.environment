package com.netease.cloud.nos.android.utils;

import java.util.regex.Pattern;

/* loaded from: classes5.dex */
public class ValidIP {
    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static boolean validate(String str) {
        return Pattern.compile(IPADDRESS_PATTERN).matcher(str).matches();
    }
}