package com.netease.download.util;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes5.dex */
public class SpUtil {
    private static final String COMMON_SP_NAME = "download_info";
    private static final String TAG = "SpUtil";
    private static Context sAppContext;
    private static SpUtil sInstance;
    private Map<String, PreferenceUnit> sMap;

    public static void initialize(Context context) {
        if (sInstance == null) {
            synchronized (SpUtil.class) {
                if (sInstance == null) {
                    sInstance = new SpUtil(context);
                }
            }
        }
    }

    private SpUtil(Context context) {
        sAppContext = context.getApplicationContext();
        this.sMap = new HashMap();
    }

    public static SpUtil getInstance() {
        Context context;
        if (sInstance == null && (context = sAppContext) != null) {
            initialize(context);
        }
        return sInstance;
    }

    private PreferenceUnit getPreference(Object obj) {
        String strValueOf = String.valueOf(obj);
        PreferenceUnit preferenceUnit = this.sMap.get(strValueOf);
        if (preferenceUnit != null) {
            return preferenceUnit;
        }
        PreferenceUnit preferenceUnit2 = new PreferenceUnit(sAppContext, strValueOf);
        this.sMap.put(strValueOf, preferenceUnit2);
        return preferenceUnit2;
    }

    private void set(Object obj, String str, String str2, boolean z) {
        SharedPreferences.Editor editor = getPreference(obj).editor;
        editor.putString(str, str2);
        if (z) {
            editor.commit();
        }
    }

    private void remove(Object obj, String str, boolean z) {
        SharedPreferences.Editor editor = getPreference(obj).editor;
        editor.remove(str);
        if (z) {
            editor.commit();
        }
    }

    private String get(Object obj, String str, String str2) {
        try {
            return getPreference(obj).preferences.getString(str, str2);
        } catch (Exception unused) {
            return str2;
        }
    }

    public synchronized void setString(Object obj, String str, String str2, boolean z) {
        set(obj, str, str2, z);
    }

    public synchronized String getString(Object obj, String str, String str2) {
        return get(obj, str, str2);
    }

    public synchronized void setLong(Object obj, String str, long j, boolean z) {
        set(obj, str, String.valueOf(j), z);
    }

    public synchronized long getLong(Object obj, String str, long j) {
        try {
        } catch (Exception e) {
            LogUtil.w(TAG, "" + e);
            return j;
        }
        return Long.valueOf(get(obj, str, "")).longValue();
    }

    public synchronized void clear(Object obj) {
        getPreference(obj).editor.clear().commit();
    }

    static class PreferenceUnit {
        public SharedPreferences.Editor editor;
        public SharedPreferences preferences;

        public PreferenceUnit(Context context, String str) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(str, 0);
            this.preferences = sharedPreferences;
            this.editor = sharedPreferences.edit();
        }
    }
}