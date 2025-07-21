package com.netease.ntunisdk;

import android.app.Activity;
import android.content.Context;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.lbs.PermissionHandler;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import org.json.JSONException;

/* loaded from: classes.dex */
class LbsPermissionUtil {
    LbsPermissionUtil() {
    }

    private static int getStringId(Context context, String str) {
        return context.getResources().getIdentifier(str, ResIdReader.RES_TYPE_STRING, context.getPackageName());
    }

    private static String getString(Context context, String str) {
        int stringId = getStringId(context, str);
        return stringId <= 0 ? "" : context.getString(stringId);
    }

    private static String getString(Context context, int i) {
        return context.getString(i);
    }

    static void toReqPermissionWithPermissionKit(String str, Activity activity, int i, String[] strArr) throws JSONException {
        PermissionHandler.requestPermission(str, i, strArr, getString(activity, android.R.string.ok), getString(activity, android.R.string.cancel), SdkMgr.getInst().getPropStr("UNISDK_LBS_PERMISSION_TIPS", getString(activity, "unisdk_lbs_permission_tips")), getString(activity, "unisdk_lbs_permission_to_setting_ression"), getString(activity, "unisdk_lbs_permission_deny"));
    }
}