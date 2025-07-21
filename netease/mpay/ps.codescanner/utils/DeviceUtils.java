package com.netease.mpay.ps.codescanner.utils;

import java.util.Locale;

/* loaded from: classes6.dex */
public class DeviceUtils {
    public static String getLocale() {
        Locale locale = Locale.getDefault();
        if (locale == null) {
            return "zh-cn";
        }
        String language = locale.getLanguage();
        String country = locale.getCountry();
        if (language == null || country == null) {
            return "zh-cn";
        }
        String lowerCase = language.trim().toLowerCase();
        String lowerCase2 = country.trim().toLowerCase();
        if (lowerCase.equals("") || lowerCase2.equals("")) {
            return "zh-cn";
        }
        return lowerCase + "-" + lowerCase2;
    }
}