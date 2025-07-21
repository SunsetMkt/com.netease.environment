package com.netease.ntunisdk.base.function;

import android.content.Context;
import com.netease.mpay.ps.codescanner.CodeScannerConst;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.xiaomi.onetrack.OneTrack;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: JfLogInfo.java */
/* loaded from: classes4.dex */
public final class f {
    public static JSONObject a(Context context, OrderInfo orderInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("device_model", UniSdkUtils.getMobileModel());
            jSONObject.putOpt("os_name", "android");
            jSONObject.putOpt("os_ver", UniSdkUtils.getMobileVersion());
            jSONObject.putOpt("udid", SdkMgr.getInst().getUdid());
            StringBuilder sb = new StringBuilder();
            sb.append(UniSdkUtils.getAppVersionCode(context));
            jSONObject.putOpt("app_ver", sb.toString());
            jSONObject.putOpt("imei", UniSdkUtils.getMobileIMEI(context));
            jSONObject.putOpt("area_code", c.a());
            int i = 1;
            jSONObject.putOpt("is_emulator", Integer.valueOf(UniSdkUtils.isEmulator(context) ? 1 : 0));
            if (!UniSdkUtils.isDeviceRooted()) {
                i = 0;
            }
            jSONObject.putOpt("is_root", Integer.valueOf(i));
            jSONObject.putOpt(OneTrack.Param.OAID, SdkMgr.getInst().getPropStr("OAID"));
            jSONObject.putOpt("msa_oaid", SdkMgr.getInst().getPropStr(ConstProp.MSA_OAID));
            jSONObject.putOpt("role_name", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_NAME));
            int propInt = SdkMgr.getInst().getPropInt(ConstProp.USERINFO_GRADE, -1);
            if (propInt >= 0) {
                jSONObject.putOpt("role_level", Integer.valueOf(propInt));
            }
            int propInt2 = SdkMgr.getInst().getPropInt(ConstProp.USERINFO_VIP, -1);
            if (propInt2 >= 0) {
                jSONObject.putOpt(OneTrack.Param.VIP_LEVEL, Integer.valueOf(propInt2));
            }
            if (orderInfo != null) {
                jSONObject.putOpt(CodeScannerConst.PAY_CHANNEL, orderInfo.getOrderChannel());
            }
        } catch (Exception unused) {
        }
        return jSONObject;
    }
}