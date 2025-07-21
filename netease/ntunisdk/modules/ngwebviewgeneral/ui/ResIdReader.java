package com.netease.ntunisdk.modules.ngwebviewgeneral.ui;

import android.content.Context;
import android.text.TextUtils;

/* loaded from: classes.dex */
public class ResIdReader {
    private static final int ERROR_ID = 0;
    public static final String RES_TYPE_ARRAY = "array";
    public static final String RES_TYPE_COLOR = "color";
    public static final String RES_TYPE_DIMEN = "dimen";
    public static final String RES_TYPE_DRAWABLE = "drawable";
    public static final String RES_TYPE_ID = "id";
    public static final String RES_TYPE_LAYOUT = "layout";
    public static final String RES_TYPE_STRING = "string";
    public static final String RES_TYPE_STYLE = "style";

    public static int getResId(Context context, String str, String str2) {
        if (context == null) {
            return 0;
        }
        return getResId(context, str, str2, context.getPackageName());
    }

    public static int getResId(Context context, String str, String str2, String str3) {
        if (context == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return 0;
        }
        return context.getResources().getIdentifier(str, str2, str3);
    }

    public static int getId(Context context, String str) {
        return getResId(context, str, RES_TYPE_ID);
    }

    public static int getLayoutId(Context context, String str) {
        return getResId(context, str, RES_TYPE_LAYOUT);
    }

    public static int getStyleId(Context context, String str) {
        return getResId(context, str, RES_TYPE_STYLE);
    }

    public static int getDrawableId(Context context, String str) {
        return getResId(context, str, RES_TYPE_DRAWABLE);
    }

    public static int getStringId(Context context, String str) {
        return getResId(context, str, RES_TYPE_STRING);
    }

    public static int getDimenId(Context context, String str) {
        return getResId(context, str, RES_TYPE_DIMEN);
    }

    public static int getArrayId(Context context, String str) {
        return getResId(context, str, RES_TYPE_ARRAY);
    }

    public static int getColorId(Context context, String str) {
        return getResId(context, str, "color");
    }
}