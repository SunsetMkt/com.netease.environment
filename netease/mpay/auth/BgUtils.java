package com.netease.mpay.auth;

import android.app.Activity;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;

/* loaded from: classes.dex */
public class BgUtils {
    public static String UNISDK_QUICKQR_BG_RES_NAME = "ntunisdk_quickqr__assist_background";
    private static int quickQrBgResId;

    public static int getQuickQrBgResId(Activity activity) {
        int i = quickQrBgResId;
        if (i > 0) {
            return i;
        }
        try {
            quickQrBgResId = activity.getResources().getIdentifier(UNISDK_QUICKQR_BG_RES_NAME, ResIdReader.RES_TYPE_DRAWABLE, activity.getPackageName());
        } catch (Exception unused) {
        }
        return quickQrBgResId;
    }
}