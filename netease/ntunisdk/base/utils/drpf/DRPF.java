package com.netease.ntunisdk.base.utils.drpf;

import android.content.Context;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.CachedThreadPoolUtil;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.base.utils.NetConnectivity;
import com.netease.ntunisdk.base.utils.StrUtil;
import com.netease.ntunisdk.base.utils.clientlog.MCountProxy;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import com.xiaomi.onetrack.OneTrack;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class DRPF {
    private static final String MESSENGER_CHANNEL = "messengerKit";
    private static final String TAG = "UniSDK DRPF";

    public static void dispatchDrpf(final Context context, final JSONObject jSONObject, final String str, final String str2, final String str3) {
        if (context == null || jSONObject == null) {
            return;
        }
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.utils.drpf.DRPF.1
            @Override // java.lang.Runnable
            public final void run() throws JSONException {
                JSONObject jsonToSend = DRPF.getJsonToSend(context, jSONObject, str, str2, str3);
                UniSdkUtils.d(DRPF.TAG, "jsonToSend=".concat(String.valueOf(jsonToSend)));
                if (jsonToSend == null || jsonToSend.length() == 0 || MCountProxy.getInst().sendDrpf(jSONObject, str, str2, str3)) {
                    return;
                }
                if (DRPF.hasDrpfMessenger()) {
                    DRPF.sendDrpfByMessenger(jsonToSend, str);
                } else {
                    DRPF.sendDrpfByBase(jsonToSend, str);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasDrpfMessenger() {
        SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
        if (sdkBase == null) {
            return false;
        }
        return sdkBase.getSdkInstMap().containsKey(MESSENGER_CHANNEL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendDrpfByBase(JSONObject jSONObject, String str) {
        String strOptString = jSONObject.optString("drpf_headers");
        UniSdkUtils.d(TAG, "drpf_headers=".concat(String.valueOf(strOptString)));
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_DRPF_URL);
        if (TextUtils.isEmpty(propStr)) {
            if (SdkMgr.getInst().hasFeature("EB")) {
                propStr = "https://drpf-" + str + ".proxima.nie.easebar.com";
            } else {
                propStr = "https://drpf-" + str + ".proxima.nie.netease.com";
            }
        }
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.i(TAG, "null or empty url, drpf will not go on");
            return;
        }
        UniSdkUtils.d(TAG, "url=".concat(String.valueOf(propStr)));
        HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = propStr;
        queueItemNewQueueItem.bSync = Boolean.TRUE;
        queueItemNewQueueItem.setBody(jSONObject.toString());
        if (!TextUtils.isEmpty(strOptString)) {
            try {
                queueItemNewQueueItem.setHeaders(StrUtil.jsonToStrMap(new JSONObject(strOptString)));
            } catch (JSONException e) {
                UniSdkUtils.w(TAG, "drpf_headers json parse error");
                e.printStackTrace();
            }
        }
        queueItemNewQueueItem.transParam = "DRPF";
        HTTPQueue.getInstance("LOG").checkResend();
        HTTPQueue.getInstance("LOG").addItem(queueItemNewQueueItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendDrpfByMessenger(JSONObject jSONObject, String str) throws JSONException {
        String strOptString = jSONObject.optString("drpf_headers");
        UniSdkUtils.d(TAG, "drpf_headers=".concat(String.valueOf(strOptString)));
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_DRPF_URL);
        if (TextUtils.isEmpty(propStr)) {
            if (SdkMgr.getInst().hasFeature("EB")) {
                propStr = "https://drpf-" + str + ".proxima.nie.easebar.com";
            } else {
                propStr = "https://drpf-" + str + ".proxima.nie.netease.com";
            }
        }
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.i(TAG, "null or empty url, drpf will not go on");
            return;
        }
        UniSdkUtils.d(TAG, "url=".concat(String.valueOf(propStr)));
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("methodId", "MessengerPostData");
            jSONObject2.put("channel", MESSENGER_CHANNEL);
            jSONObject2.put(SocialConstants.PARAM_SOURCE, "drpf");
            jSONObject2.put("header", strOptString);
            jSONObject2.put("logurl", propStr);
            jSONObject2.put(ClientLogConstant.LOG, jSONObject.toString());
            jSONObject2.put("cached", 1);
            SdkMgr.getInst().ntExtendFunc(jSONObject2.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static JSONObject getJsonToSend(Context context, JSONObject jSONObject, String str, String str2, String str3) throws JSONException {
        JSONArray jSONArray;
        Object obj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US).format(new Date());
        Object mobileModel = UniSdkUtils.getMobileModel();
        Object mobileVersion = UniSdkUtils.getMobileVersion();
        Object macAddress = UniSdkUtils.getMacAddress(context);
        Object mobileIMSI = UniSdkUtils.getMobileIMSI(context);
        Object mobileIMEI = UniSdkUtils.getMobileIMEI(context);
        Object appChannel = SdkMgr.getInst().getAppChannel();
        Object sDKVersion = SdkMgr.getInst().getSDKVersion(SdkMgr.getInst().getChannel());
        Object networkType = NetConnectivity.getNetworkType(context);
        Object unisdkDeviceId = UniSdkUtils.getUnisdkDeviceId(context);
        String oaid = UniSdkUtils.getOAID(context);
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.MSA_OAID);
        String propStr2 = SdkMgr.getInst().getPropStr("VAID");
        String propStr3 = SdkMgr.getInst().getPropStr("AAID");
        Object transid = UniSdkUtils.getTransid(context);
        boolean zIsDeviceRooted = UniSdkUtils.isDeviceRooted();
        boolean zIsEmulator = UniSdkUtils.isEmulator(context);
        Object propStr4 = SdkMgr.getInst().getPropStr(ConstProp.ENGINE_VERSION);
        Object propStr5 = SdkMgr.getInst().getPropStr("JF_GAMEID");
        Object baseVersion = SdkMgr.getBaseVersion();
        String propStr6 = SdkMgr.getInst().getPropStr("ntes_id");
        Object systemTimeZone = UniSdkUtils.getSystemTimeZone();
        Object systemLanguage = UniSdkUtils.getSystemLanguage();
        String propStr7 = SdkMgr.getInst().getPropStr(ConstProp.WIFI_INFO_LIST);
        try {
            jSONArray = new JSONArray(propStr7);
        } catch (Exception unused) {
            jSONArray = null;
        }
        JSONArray jSONArray2 = jSONArray;
        Object propStr8 = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_FIRST_DEVICE_ID);
        UniSdkUtils.d(TAG, "project=".concat(String.valueOf(str)));
        UniSdkUtils.d(TAG, "source=".concat(String.valueOf(str2)));
        UniSdkUtils.d(TAG, "type=".concat(String.valueOf(str3)));
        UniSdkUtils.d(TAG, "timeString=".concat(String.valueOf(obj)));
        UniSdkUtils.d(TAG, "device_model=".concat(String.valueOf(mobileModel)));
        UniSdkUtils.d(TAG, "os_ver=".concat(String.valueOf(mobileVersion)));
        UniSdkUtils.d(TAG, "mac_addr=".concat(String.valueOf(macAddress)));
        UniSdkUtils.d(TAG, "idfa=".concat(String.valueOf(mobileIMSI)));
        UniSdkUtils.d(TAG, "imei=".concat(String.valueOf(mobileIMEI)));
        UniSdkUtils.d(TAG, "app_channel=".concat(String.valueOf(appChannel)));
        UniSdkUtils.d(TAG, "sdk_ver=".concat(String.valueOf(sDKVersion)));
        UniSdkUtils.d(TAG, "network=".concat(String.valueOf(networkType)));
        UniSdkUtils.d(TAG, "transid=".concat(String.valueOf(transid)));
        UniSdkUtils.d(TAG, "is_root=".concat(String.valueOf(zIsDeviceRooted ? 1 : 0)));
        UniSdkUtils.d(TAG, "is_emulator=".concat(String.valueOf(zIsEmulator ? 1 : 0)));
        UniSdkUtils.d(TAG, "jf_gameid=".concat(String.valueOf(propStr5)));
        UniSdkUtils.d(TAG, "engine_ver=".concat(String.valueOf(propStr4)));
        UniSdkUtils.d(TAG, "common_sdk_ver=".concat(String.valueOf(baseVersion)));
        UniSdkUtils.d(TAG, "ntes_id=".concat(String.valueOf(propStr6)));
        UniSdkUtils.d(TAG, "timezone=".concat(String.valueOf(systemTimeZone)));
        UniSdkUtils.d(TAG, "sys_language=".concat(String.valueOf(systemLanguage)));
        UniSdkUtils.d(TAG, "wifiArray=".concat(String.valueOf(propStr7)));
        UniSdkUtils.d(TAG, "first_deviceid=".concat(String.valueOf(propStr8)));
        try {
            jSONObject.put("os_name", "android");
            if (!jSONObject.has("time")) {
                jSONObject.put("time", obj);
            }
            if (!jSONObject.has("device_model")) {
                jSONObject.put("device_model", mobileModel);
            }
            if (!jSONObject.has("os_ver")) {
                jSONObject.put("os_ver", mobileVersion);
            }
            if (!jSONObject.has("mac_addr")) {
                jSONObject.put("mac_addr", macAddress);
            }
            if (!jSONObject.has("idfa")) {
                jSONObject.put("idfa", mobileIMSI);
            }
            jSONObject.put("imei", mobileIMEI);
            if (!jSONObject.has(Const.APP_CHANNEL)) {
                jSONObject.put(Const.APP_CHANNEL, appChannel);
            }
            if (!jSONObject.has(Constants.PARAM_SDK_VER)) {
                jSONObject.put(Constants.PARAM_SDK_VER, sDKVersion);
            }
            if (!jSONObject.has("common_sdk_ver")) {
                jSONObject.putOpt("common_sdk_ver", baseVersion);
            }
            if (!jSONObject.has("network")) {
                jSONObject.put("network", networkType);
            }
            if (!jSONObject.has("unisdk_deviceid")) {
                jSONObject.putOpt("unisdk_deviceid", unisdkDeviceId);
            }
            if (!jSONObject.has(OneTrack.Param.OAID) && !TextUtils.isEmpty(oaid)) {
                jSONObject.putOpt(OneTrack.Param.OAID, oaid);
            }
            if (!jSONObject.has("msa_oaid") && !TextUtils.isEmpty(propStr)) {
                jSONObject.putOpt("msa_oaid", propStr);
            }
            if (!jSONObject.has("vaid") && !TextUtils.isEmpty(propStr2)) {
                jSONObject.putOpt("vaid", propStr2);
            }
            if (!jSONObject.has("aaid") && !TextUtils.isEmpty(propStr3)) {
                jSONObject.putOpt("aaid", propStr3);
            }
            jSONObject.put(ClientLogConstant.TRANSID, transid);
            jSONObject.put("is_root", zIsDeviceRooted ? 1 : 0);
            jSONObject.put("is_emulator", zIsEmulator ? 1 : 0);
            if (!jSONObject.has("jf_gameid")) {
                jSONObject.put("jf_gameid", propStr5);
            }
            if (!jSONObject.has("engine_ver")) {
                jSONObject.put("engine_ver", propStr4);
            }
            if (!jSONObject.has("ntesid") && !TextUtils.isEmpty(propStr6)) {
                jSONObject.putOpt("ntesid", propStr6);
            }
            if (!jSONObject.has("timezone")) {
                jSONObject.putOpt("timezone", systemTimeZone);
            }
            if (!jSONObject.has("sys_language")) {
                jSONObject.putOpt("sys_language", systemLanguage);
            }
            if (!jSONObject.has("wifiArray") && jSONArray2 != null && jSONArray2.length() > 0) {
                jSONObject.putOpt("wifiArray", jSONArray2);
            }
            if (!jSONObject.has("first_deviceid")) {
                jSONObject.put("first_deviceid", propStr8);
            }
            return jSONObject;
        } catch (Exception e) {
            JSONObject jSONObject2 = new JSONObject();
            e.printStackTrace();
            return jSONObject2;
        }
    }
}