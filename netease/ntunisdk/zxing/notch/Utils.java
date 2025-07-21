package com.netease.ntunisdk.zxing.notch;

import android.content.Context;
import android.content.res.Resources;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
class Utils {
    private static Method SystemPropGet;

    Utils() {
    }

    public static int getStatusBarHeight(Context context, int i) {
        try {
            Resources resources = context.getResources();
            int identifier = resources.getIdentifier("status_bar_height", ResIdReader.RES_TYPE_DIMEN, "android");
            return identifier > 0 ? resources.getDimensionPixelSize(identifier) : i;
        } catch (Throwable unused) {
            return i;
        }
    }
}