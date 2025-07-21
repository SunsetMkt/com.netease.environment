package com.netease.ntunisdk.base.function;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.ResUtils;
import com.netease.ntunisdk.base.view.Alerter;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import org.json.JSONException;

/* compiled from: UniSauth.java */
/* loaded from: classes4.dex */
public final class h {
    /* JADX WARN: Removed duplicated region for block: B:53:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x015d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void a(final android.content.Context r19, final com.netease.ntunisdk.base.SdkBase r20, final int r21) {
        /*
            Method dump skipped, instructions count: 514
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.function.h.a(android.content.Context, com.netease.ntunisdk.base.SdkBase, int):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void a(SdkBase sdkBase, int i) throws JSONException {
        sdkBase.setPropInt(ConstProp.LOGIN_STAT, 0);
        sdkBase.setPropStr(ConstProp.SAUTH_STR, null);
        sdkBase.setPropStr(ConstProp.SAUTH_JSON, null);
        sdkBase.jfCheckRealNameDone(i);
    }

    static /* synthetic */ void a(final String str, int i, int i2, String str2, final Context context, SdkBase sdkBase, String str3, boolean z) throws JSONException {
        if (!TextUtils.isEmpty(str) && SdkMgr.getInst().getPropInt(str3, 0) != 1) {
            ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.function.h.2
                @Override // java.lang.Runnable
                public final void run() throws Resources.NotFoundException {
                    boolean zShowAASDialog;
                    try {
                        zShowAASDialog = UniSdkUtils.showAASDialog(str, null);
                    } catch (Exception unused) {
                        UniSdkUtils.e("UniSauth", "showAASDialog failed");
                        zShowAASDialog = false;
                    }
                    if (zShowAASDialog) {
                        return;
                    }
                    new Alerter(context).showDialog(context.getResources().getString(ResUtils.getResId(context, "unisdk_alert_dialog_tips", ResIdReader.RES_TYPE_STRING)), str, context.getResources().getString(ResUtils.getResId(context, "unisdk_alert_dialog_positive", ResIdReader.RES_TYPE_STRING)), null, null);
                }
            });
        }
        if (z) {
            SdkMgr.getInst().setPropStr(ConstProp.NT_ERROR_CODE, String.valueOf(i));
            SdkMgr.getInst().setPropStr(ConstProp.NT_ERROR_SUB_CODE, String.valueOf(i2));
            SdkMgr.getInst().setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, str2);
            a(sdkBase, 10);
        }
    }
}