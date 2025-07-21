package com.netease.environment.OIIO0OO;

import android.content.Context;
import android.content.SharedPreferences;

/* compiled from: SdkConfig.java */
/* loaded from: classes5.dex */
public class OIIO0O0 {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static SharedPreferences f1576OIIO00I;

    private static SharedPreferences OIIO00I(Context context) {
        if (f1576OIIO00I == null) {
            f1576OIIO00I = context.getApplicationContext().getSharedPreferences("environment_preferences_config", 32768);
        }
        return f1576OIIO00I;
    }

    public static void OIIO0O0(Context context, String str, boolean z) {
        SharedPreferences.Editor editorEdit;
        if (context == null || (editorEdit = OIIO00I(context).edit()) == null) {
            return;
        }
        editorEdit.putBoolean(str, z);
        editorEdit.apply();
    }

    public static void OIIO0OI(Context context, long j) {
        OIIO0O0(context, "update_interval", j);
    }

    public static void OIIO0OO(Context context, String str, String str2) {
        SharedPreferences.Editor editorEdit;
        if (context == null || (editorEdit = OIIO00I(context).edit()) == null) {
            return;
        }
        editorEdit.putString(str, str2);
        editorEdit.apply();
    }

    public static void OIIO0OI(Context context, String str, String str2) {
        OIIO0OO(context, "regex_file_url_" + str, str2);
    }

    public static boolean OIIO00I(Context context, String str, boolean z) {
        return context == null ? z : OIIO00I(context).getBoolean(str, z);
    }

    public static void OIIO0O0(Context context, String str, long j) {
        SharedPreferences.Editor editorEdit;
        if (context == null || (editorEdit = OIIO00I(context).edit()) == null) {
            return;
        }
        editorEdit.putLong(str, j);
        editorEdit.apply();
    }

    public static void OIIO0OO(Context context, long j) {
        OIIO0O0(context, "update_data_time", j);
    }

    public static long OIIO00I(Context context, String str, long j) {
        return context == null ? j : OIIO00I(context).getLong(str, j);
    }

    public static void OIIO0OO(Context context, boolean z) {
        OIIO0O0(context, "enable", z);
    }

    public static boolean OIIO00I(Context context, boolean z) {
        return OIIO00I(context, "enable", z);
    }

    public static long OIIO00I(Context context, long j) {
        return OIIO00I(context, "task_timeout", j);
    }

    public static String OIIO00I(Context context, String str, String str2) {
        return OIIO0O0(context, "regex_file_url_" + str, str2);
    }

    public static String OIIO0O0(Context context, String str, String str2) {
        return context == null ? str2 : OIIO00I(context).getString(str, str2);
    }

    public static void OIIO0O0(Context context, boolean z) {
        OIIO0O0(context, "downlaoding", z);
    }

    public static void OIIO0O0(Context context, long j) {
        OIIO0O0(context, "task_timeout", j);
    }
}