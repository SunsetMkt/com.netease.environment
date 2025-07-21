package com.netease.ntunisdk.lbs;

import android.text.TextUtils;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.cutout.RespUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PermissionHandler {
    public static void requestPermission(String str, int i, String[] strArr, String str2, String str3, String str4, String str5, String str6) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("methodId", "requestPermission");
        jSONObject.put("channel", "netease_permission_kit");
        jSONObject.put("requestChannel", str);
        jSONObject.put("requestCode", i);
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (i2 == 0) {
                sb.append(strArr[i2]);
            } else {
                sb.append(",");
                sb.append(strArr[i2]);
            }
        }
        jSONObject.put("permissionName", sb.toString());
        jSONObject.put("positiveText", str2);
        jSONObject.put("negativeText", str3);
        jSONObject.put("firstText", str4);
        jSONObject.put("isCallGame", true);
        jSONObject.put("shouldRetry", false);
        jSONObject.put("gotoSetting", true);
        jSONObject.put("gotoSettingReason", str5);
        jSONObject.put("refuseTip", str6);
        SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
    }

    public static boolean handleRequestResult(JSONObject jSONObject) {
        String strOptString = jSONObject.optString("result");
        if (jSONObject.optInt(RespUtil.UniSdkField.RESP_CODE, -1) == 0 && !TextUtils.isEmpty(strOptString)) {
            String[] strArrSplit = strOptString.split(",");
            if (strArrSplit.length > 0) {
                for (String str : strArrSplit) {
                    if (Integer.parseInt(str) != 0) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}