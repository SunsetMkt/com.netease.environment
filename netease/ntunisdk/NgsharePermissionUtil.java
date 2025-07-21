package com.netease.ntunisdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.view.Alerter;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;

/* loaded from: classes.dex */
public class NgsharePermissionUtil {
    private static boolean sHasAlter;
    private static boolean sHasCheckAlter;

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

    public static void showDialog(Activity activity, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) throws ClassNotFoundException {
        if (!SdkMgr.getInst().hasFeature(SdkQRCode.ENABLE_UNISDK_PERMISSION_TIPS)) {
            onClickListener.onClick(null, 0);
        } else {
            showDialog(activity, SdkMgr.getInst().getPropStr("UNISDK_NGSHARE_PERMISSION_TIPS", "\u8981\u901a\u8fc7\u56fe\u7247\u5206\u4eab\uff0c\u8bf7\u5141\u8bb8\u8bbf\u95ee\u76f8\u518c"), getString(activity, android.R.string.ok), getString(activity, android.R.string.cancel), onClickListener, onClickListener2);
        }
    }

    private static void showDialog(Activity activity, String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) throws ClassNotFoundException {
        if (!sHasCheckAlter) {
            try {
                Class.forName("com.netease.ntunisdk.base.view.Alerter");
                sHasAlter = true;
            } catch (Exception unused) {
            }
            sHasCheckAlter = true;
        }
        if (sHasAlter) {
            new Alerter(activity).showDialog(null, str, str2, onClickListener, str3, onClickListener2, null);
        } else {
            new AlertDialog.Builder(activity).setMessage(str).setPositiveButton(str2, onClickListener).setNeutralButton(str3, onClickListener2).create().show();
        }
    }
}