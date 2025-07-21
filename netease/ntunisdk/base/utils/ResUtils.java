package com.netease.ntunisdk.base.utils;

import android.content.Context;

/* loaded from: classes3.dex */
public class ResUtils {
    public static int getResId(Context context, String str, String str2) {
        return context.getResources().getIdentifier(str, str2, context.getPackageName());
    }
}