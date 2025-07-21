package com.netease.ntunisdk.modules.personalinfolist;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.personalinfolist.net.ClientLogHttpCallbackExt;
import com.netease.ntunisdk.modules.personalinfolist.net.ClientLogHttpQueue;
import com.netease.ntunisdk.modules.personalinfolist.net.Crypto;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import org.jose4j.jwx.HeaderParameterNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ClientLogReporter {
    private static final String defaultFunc = "personal_info_list";
    private static final String defaultStep = "personal_info_list";
    private static final String platform = "Android";
    private static final int res_code = 200;
    private static final String tag = "MpayPICC";
    private static final String TAG = ClientLogReporter.class.getSimpleName();
    private static String gameid = "";
    private static String username = "";
    private static String aid = "";
    private static String transid = "";
    private static String UDID = "";
    private static String timestamp = "";
    private static String channel = "";
    private static String app_channel = "";
    private static String version = "";
    private static SimpleDateFormat sf = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.US);
    public static boolean isInit = false;

    public static void init(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            PILConstant.INNER_CLIENT_LOG_URL = jSONObject.optString(ConstProp.JF_CLIENT_LOG_URL, PILConstant.INNER_CLIENT_LOG_URL);
            PILConstant.JF_LOG_KEY_CONTENT = jSONObject.optString(ConstProp.JF_LOG_KEY, "");
            gameid = jSONObject.optString("gameid", "");
            username = jSONObject.optString("username", "");
            aid = jSONObject.optString("aid", "");
            transid = jSONObject.optString(ClientLogConstant.TRANSID, "");
            UDID = jSONObject.optString(ConstProp.UDID, "");
            channel = jSONObject.optString("channel", "");
            app_channel = jSONObject.optString(Const.APP_CHANNEL, "");
            version = jSONObject.optString("version", "");
            isInit = true;
        } catch (Exception e) {
            e.printStackTrace();
            isInit = false;
        }
    }

    public static void report(String str) {
        if (PILConstant.isReport && isInit) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String strOptString = jSONObject.optString(ConstProp.JF_CLIENT_LOG_URL, PILConstant.INNER_CLIENT_LOG_URL);
                String strOptString2 = jSONObject.optString(ConstProp.JF_LOG_KEY, PILConstant.JF_LOG_KEY_CONTENT);
                if (!TextUtils.isEmpty(strOptString) && !TextUtils.isEmpty(strOptString2)) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put(OneTrackParams.XMSdkParams.STEP, jSONObject.optString(OneTrackParams.XMSdkParams.STEP, "personal_info_list"));
                    jSONObject2.put("func", jSONObject.optString("func", "personal_info_list"));
                    jSONObject2.put("gameid", jSONObject.optString("gameid", gameid));
                    jSONObject2.put(HeaderParameterNames.AUTHENTICATION_TAG, jSONObject.optString(HeaderParameterNames.AUTHENTICATION_TAG, tag));
                    jSONObject2.put("username", jSONObject.optString("username", username));
                    jSONObject2.put("aid", jSONObject.optString("aid", aid));
                    jSONObject2.put(ClientLogConstant.TRANSID, jSONObject.optString(ClientLogConstant.TRANSID, transid));
                    jSONObject2.put(ConstProp.UDID, jSONObject.optString(ConstProp.UDID, UDID));
                    jSONObject2.put("timestamp", sf.format(new Date()));
                    jSONObject2.put("channel", TextUtils.isEmpty(channel) ? jSONObject.optString("channel", channel) : channel);
                    jSONObject2.put(Const.APP_CHANNEL, jSONObject.optString(Const.APP_CHANNEL, app_channel));
                    jSONObject2.put("version", jSONObject.optString("version", version));
                    jSONObject2.put("platform", jSONObject.optString("platform", platform));
                    jSONObject2.put("res_code", jSONObject.optInt("res_code", 200));
                    if (jSONObject.has("upload_type")) {
                        jSONObject2.put("upload_type", jSONObject.optString("upload_type"));
                    }
                    JSONArray jSONArray = new JSONArray();
                    jSONArray.put(jSONObject.optJSONObject("event"));
                    jSONObject2.putOpt("picc_events", jSONArray);
                    report2Server(strOptString, strOptString2, jSONObject2.toString());
                    return;
                }
                LogModule.e(TAG, "null or empty clientLogUrl/jfLogKey");
            } catch (Exception e) {
                e.printStackTrace();
                LogModule.e(TAG, "\u3010ClientLogReporter#report\u3011 error: " + e.getMessage());
            }
        }
    }

    public static void reportDirectly(String str) throws JSONException {
        if (PILConstant.isReport) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                String strOptString = jSONObject.optString(ConstProp.JF_CLIENT_LOG_URL, PILConstant.INNER_CLIENT_LOG_URL);
                String strOptString2 = jSONObject.optString(ConstProp.JF_LOG_KEY, PILConstant.JF_LOG_KEY_CONTENT);
                if (!TextUtils.isEmpty(strOptString) && !TextUtils.isEmpty(strOptString2)) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put(OneTrackParams.XMSdkParams.STEP, jSONObject.optString(OneTrackParams.XMSdkParams.STEP, "personal_info_list"));
                    jSONObject2.put("func", jSONObject.optString("func", "personal_info_list"));
                    jSONObject2.put("gameid", jSONObject.optString("gameid", gameid));
                    jSONObject2.put(HeaderParameterNames.AUTHENTICATION_TAG, jSONObject.optString(HeaderParameterNames.AUTHENTICATION_TAG, tag));
                    jSONObject2.put("username", jSONObject.optString("username", username));
                    jSONObject2.put("aid", jSONObject.optString("aid", aid));
                    jSONObject2.put(ClientLogConstant.TRANSID, jSONObject.optString(ClientLogConstant.TRANSID, transid));
                    jSONObject2.put(ConstProp.UDID, jSONObject.optString(ConstProp.UDID, UDID));
                    jSONObject2.put("timestamp", sf.format(new Date()));
                    jSONObject2.put("channel", jSONObject.optString("channel", channel));
                    jSONObject2.put(Const.APP_CHANNEL, jSONObject.optString(Const.APP_CHANNEL, app_channel));
                    jSONObject2.put("version", jSONObject.optString("version", version));
                    jSONObject2.put("platform", jSONObject.optString("platform", platform));
                    jSONObject2.put("res_code", jSONObject.optInt("res_code", 200));
                    if (jSONObject.has("upload_type")) {
                        jSONObject2.put("upload_type", jSONObject.optString("upload_type"));
                    }
                    jSONObject2.putOpt("picc_events", jSONObject.optJSONArray("picc_events"));
                    report2Server(strOptString, strOptString2, jSONObject2.toString());
                    return;
                }
                LogModule.e(TAG, "null or empty clientLogUrl/jfLogKey");
            } catch (Exception e) {
                e.printStackTrace();
                LogModule.e(TAG, "\u3010ClientLogReporter#report\u3011 error: " + e.getMessage());
            }
        }
    }

    private static void report2Server(String str, String str2, String str3) {
        LogModule.d(TAG, String.format("/report2Server url=%s, bodyPairs=%s", str, str3));
        ClientLogHttpQueue.QueueItem queueItemNewQueueItem = ClientLogHttpQueue.NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = str;
        queueItemNewQueueItem.bSync = true;
        queueItemNewQueueItem.leftRetry = 0;
        queueItemNewQueueItem.setBody(str3);
        queueItemNewQueueItem.transParam = ConstProp.JF_CLIENT_LOG_URL;
        queueItemNewQueueItem.callback = new ClientLogHttpCallbackExt() { // from class: com.netease.ntunisdk.modules.personalinfolist.ClientLogReporter.1
            @Override // com.netease.ntunisdk.modules.personalinfolist.net.ClientLogHttpCallback
            public boolean processResult(String str4, String str5) {
                LogModule.d(ClientLogReporter.TAG, "report2Server processResult responseCode: " + this.responseCode);
                LogModule.d(ClientLogReporter.TAG, "report2Server processResult result: " + str4);
                if (this.throwable == null) {
                    return false;
                }
                LogModule.d(ClientLogReporter.TAG, "report2Server processResult throwable: " + this.throwable.getMessage());
                return false;
            }
        };
        if (!TextUtils.isEmpty(str2)) {
            HashMap map = new HashMap();
            try {
                map.put("Gas3-Clientlog-Signature", Crypto.hmacSHA256Signature(str2, str3.toString()));
            } catch (Exception e) {
                LogModule.d(TAG, "hmacSHA256Signature exception:" + e.getMessage());
            }
            queueItemNewQueueItem.setHeaders(map);
        } else {
            LogModule.d(TAG, "JF_LOG_KEY empty");
        }
        ClientLogHttpQueue.getInstance(ClientLogHttpQueue.HTTPQUEUE_CLIENTLOGREPORTER).addItem(queueItemNewQueueItem);
    }
}