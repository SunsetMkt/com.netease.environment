package com.netease.ntunisdk.base.function;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.ApiRequestUtil;
import com.netease.ntunisdk.base.utils.HTTPCallbackExt;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.external.protocol.Const;
import com.tencent.open.SocialConstants;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: UploadRealName.java */
/* loaded from: classes4.dex */
public final class i {
    public static void a(final SdkBase sdkBase, Map<String, String> map, final int i) {
        final JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("gameid", sdkBase.getPropStr("JF_GAMEID"));
            jSONObject.putOpt("hostid", Integer.valueOf(sdkBase.getPropInt(ConstProp.USERINFO_HOSTID, 0)));
            jSONObject.putOpt("login_channel", sdkBase.getChannel());
            jSONObject.putOpt(Const.APP_CHANNEL, sdkBase.getAppChannel());
            jSONObject.putOpt("platform", sdkBase.getPlatform());
            jSONObject.putOpt("sdk_version", sdkBase.getSDKVersion());
            jSONObject.putOpt("sdkuid", sdkBase.getPropStr(ConstProp.UID));
            jSONObject.putOpt("udid", sdkBase.getUdid());
            String propStr = sdkBase.getPropStr(ConstProp.PRI_SP);
            if (TextUtils.isEmpty(propStr)) {
                propStr = "";
            }
            jSONObject.putOpt("source_platform", propStr);
            jSONObject.putOpt("client_login_sn", sdkBase.getPropStr(ConstProp.CLIENT_LOGIN_SN));
            jSONObject.putOpt("realname", map.get("realname"));
            String propStr2 = sdkBase.getPropStr(ConstProp.JF_AIM_INFO_2);
            if (TextUtils.isEmpty(propStr2)) {
                JSONObject jSONObject2 = new JSONObject(propStr2);
                String strOptString = jSONObject2.optString(Const.COUNTRY);
                if (!TextUtils.isEmpty(strOptString)) {
                    if ("HK".equalsIgnoreCase(strOptString)) {
                        strOptString = "UNKNOWN_1";
                    } else if ("MO".equalsIgnoreCase(strOptString)) {
                        strOptString = "UNKNOWN_2";
                    } else if ("TW".equalsIgnoreCase(strOptString)) {
                        strOptString = "UNKNOWN_3";
                    }
                }
                jSONObject2.put(Const.COUNTRY, strOptString);
                jSONObject.putOpt("aim_info", jSONObject2.toString());
            }
            String propStr3 = sdkBase.getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
            StringBuilder sb = new StringBuilder();
            sb.append(propStr3);
            sb.append(propStr3.endsWith("/") ? "upload_realname" : "/upload_realname");
            String string = sb.toString();
            HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
            queueItemNewQueueItem.method = "POST";
            queueItemNewQueueItem.url = string;
            queueItemNewQueueItem.bSync = Boolean.TRUE;
            queueItemNewQueueItem.leftRetry = 0;
            queueItemNewQueueItem.setBody(jSONObject.toString());
            queueItemNewQueueItem.transParam = "upload_realname";
            queueItemNewQueueItem.callback = new HTTPCallbackExt() { // from class: com.netease.ntunisdk.base.function.i.1
                @Override // com.netease.ntunisdk.base.utils.HTTPCallback
                public final boolean processResult(String str, String str2) throws JSONException {
                    UniSdkUtils.i("UploadRealName", "upload_realname processResult: " + this.responseCode);
                    UniSdkUtils.i("UploadRealName", "upload_realname processResult: ".concat(String.valueOf(str)));
                    JSONObject jSONObject3 = new JSONObject();
                    try {
                        jSONObject3.putOpt(OneTrackParams.XMSdkParams.STEP, "upload_realname");
                        jSONObject3.putOpt(SocialConstants.TYPE_REQUEST, jSONObject);
                        jSONObject3.putOpt(com.xiaomi.onetrack.api.b.I, "no response");
                        jSONObject3.putOpt("responseCode", Integer.valueOf(this.responseCode));
                        jSONObject3.putOpt("exception", this.throwable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(str)) {
                        sdkBase.setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, str);
                        try {
                            jSONObject3.putOpt(com.xiaomi.onetrack.api.b.I, str);
                            JSONObject jSONObject4 = new JSONObject(str);
                            jSONObject3.putOpt(com.xiaomi.onetrack.api.b.I, jSONObject4);
                            if (200 != jSONObject4.optInt("code")) {
                                UniSdkUtils.e("UploadRealName", "upload_realname result code != 200, result:".concat(String.valueOf(str)));
                                sdkBase.setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, str);
                            } else {
                                j.a(sdkBase, i);
                                sdkBase.saveClientLog(null, jSONObject3.toString());
                                return false;
                            }
                        } catch (JSONException e2) {
                            String str3 = "upload_realname json result exception:" + e2.getMessage();
                            UniSdkUtils.d("UploadRealName", str3);
                            sdkBase.setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, str3);
                        }
                    } else {
                        UniSdkUtils.d("UploadRealName", "upload_realname result is invalid");
                        sdkBase.setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, "upload_realname result is invalid");
                    }
                    sdkBase.setPropInt(ConstProp.LOGIN_STAT, 0);
                    sdkBase.saveClientLog(null, jSONObject3.toString());
                    sdkBase.jfCheckRealNameDone(10);
                    return false;
                }
            };
            String propStr4 = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
            try {
                HashMap map2 = new HashMap();
                ApiRequestUtil.addSecureHeader(map2, propStr4, queueItemNewQueueItem.method, string, jSONObject.toString(), false);
                queueItemNewQueueItem.setHeaders(map2);
                HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).addItem(queueItemNewQueueItem);
            } catch (Exception e) {
                String str = "header signature exception:" + e.getMessage();
                SdkMgr.getInst().setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, str);
                UniSdkUtils.d("UploadRealName", str);
                a(sdkBase);
            }
        } catch (Exception e2) {
            String str2 = "json exception:" + e2.getMessage();
            UniSdkUtils.e("UploadRealName", str2);
            SdkMgr.getInst().setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, str2);
            a(sdkBase);
        }
    }

    private static void a(SdkBase sdkBase) throws JSONException {
        sdkBase.setPropInt(ConstProp.LOGIN_STAT, 0);
        sdkBase.jfCheckRealNameDone(10);
    }
}