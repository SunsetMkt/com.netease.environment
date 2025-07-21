package com.netease.ntunisdk.external.protocol.utils;

import android.content.Context;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;

/* loaded from: classes.dex */
public class ResUtils {
    public static int getResId(Context context, String str, String str2) {
        return context.getResources().getIdentifier(str, str2, context.getPackageName());
    }

    public static String getString(Context context, String str) {
        return getString(context, str, null);
    }

    public static String getString(Context context, String str, String str2) {
        try {
            return context.getResources().getString(getResId(context, str, ResIdReader.RES_TYPE_STRING)).trim();
        } catch (Exception unused) {
            return str2;
        }
    }
}