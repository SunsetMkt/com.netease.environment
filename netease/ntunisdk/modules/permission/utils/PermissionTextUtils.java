package com.netease.ntunisdk.modules.permission.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import java.util.HashMap;
import java.util.Locale;

/* loaded from: classes.dex */
public class PermissionTextUtils {
    private static final HashMap<String, Resources> sCachedRes = new HashMap<>();
    private static String language = null;
    private static String region = null;

    public static void setLanguageAndRegion(String str, String str2) {
        language = str;
        region = str2;
    }

    private static Locale getLocale() {
        String str = language;
        if (str == null) {
            return Locale.getDefault();
        }
        return new Locale(str, region);
    }

    private static Locale changeLocale() {
        Locale locale = getLocale();
        return locale == null ? Locale.getDefault() : locale;
    }

    private static String getKey() {
        return language + "_" + region;
    }

    public static String getStringByLocale(Context context, int i) {
        Resources resources;
        String key = getKey();
        if (language == null) {
            resources = context.getResources();
        } else if (sCachedRes.containsKey(key)) {
            resources = sCachedRes.get(key);
        } else {
            Resources resources2 = context.getResources();
            Configuration configuration = new Configuration(resources2.getConfiguration());
            Locale localeChangeLocale = changeLocale();
            if (Build.VERSION.SDK_INT <= 17) {
                configuration.locale = localeChangeLocale;
                resources = new Resources(resources2.getAssets(), resources2.getDisplayMetrics(), configuration);
            } else {
                configuration.setLocale(localeChangeLocale);
                Resources resources3 = context.createConfigurationContext(configuration).getResources();
                resources = resources3 == null ? context.getResources() : resources3;
            }
            sCachedRes.put(key, resources);
        }
        return resources.getString(i);
    }

    public static String getString(Context context, int i) {
        if (language == null) {
            return context.getResources().getString(i);
        }
        return getStringByLocale(context, i);
    }
}