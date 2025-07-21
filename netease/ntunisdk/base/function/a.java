package com.netease.ntunisdk.base.function;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.GamerInterface;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.ApiRequestUtil;
import com.netease.ntunisdk.base.utils.HTTPCallback;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.rnccplayer.VideoViewManager;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: AiDetect.java */
/* loaded from: classes4.dex */
public final class a {
    public static void a() {
        boolean z = SdkMgr.getInst().getPropInt(ConstProp.REQUIRE_AI_DETECT, 0) != 0;
        UniSdkUtils.i("AiDetect", "shouldAiDetect:".concat(String.valueOf(z)));
        if (!z) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.putOpt("methodId", "fromAiDetect");
                jSONObject.putOpt(VideoViewManager.PROP_SRC, "aiDetect4GameLoginSuc");
                SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            GamerInterface inst = SdkMgr.getInst();
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis());
            String propStr = inst.getPropStr(ConstProp.AI_GLDT_TIMESTAMP, sb.toString());
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.putOpt("methodId", "aiDetect4GameLoginSuc");
            jSONObject2.putOpt("token", SdkMgr.getInst().getPropStr(ConstProp.AI_GLDT_TOKEN));
            jSONObject2.putOpt("timestamp", Long.valueOf(Long.parseLong(propStr)));
            jSONObject2.putOpt(Const.CONFIG_KEY.ALL, new JSONObject(SdkMgr.getInst().getPropStr(ConstProp.AI_GLDT_ALL)));
            SdkMgr.getInst().ntExtendFunc(jSONObject2.toString());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void b(OrderInfo orderInfo, boolean z) throws Throwable {
        try {
            GamerInterface inst = SdkMgr.getInst();
            StringBuilder sb = new StringBuilder();
            sb.append(System.currentTimeMillis());
            String propStr = inst.getPropStr(ConstProp.AI_GLDT_TIMESTAMP, sb.toString());
            JSONObject jSONObject = new JSONObject();
            jSONObject.putOpt("methodId", "aiDetect4CheckOrder");
            jSONObject.putOpt("autoRegProduct", Boolean.valueOf(z));
            jSONObject.putOpt("token", SdkMgr.getInst().getPropStr(ConstProp.AI_GLDT_TOKEN));
            jSONObject.putOpt("timestamp", Long.valueOf(Long.parseLong(propStr)));
            jSONObject.putOpt(Const.CONFIG_KEY.ALL, new JSONObject(SdkMgr.getInst().getPropStr(ConstProp.AI_GLDT_ALL)));
            SdkMgr.getInst().ntExtendFunc(jSONObject.toString(), orderInfo);
        } catch (Exception e) {
            e.printStackTrace();
            ((SdkBase) SdkMgr.getInst()).continueOrderSetting(orderInfo, z);
        }
    }

    /* compiled from: AiDetect.java */
    /* renamed from: com.netease.ntunisdk.base.function.a$1 */
    static class AnonymousClass1 implements HTTPCallback {
        final /* synthetic */ OrderInfo b;
        final /* synthetic */ boolean c;

        AnonymousClass1(OrderInfo orderInfo, boolean z) {
            orderInfo = orderInfo;
            z = z;
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) throws Throwable {
            UniSdkUtils.d("AiDetect", "queryDashenRecognition Result : ".concat(String.valueOf(str)));
            try {
                JSONObject jSONObject = new JSONObject(str);
                int iOptInt = jSONObject.optInt("code");
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("dashen");
                if (iOptInt != 200) {
                    sdkBase.continueOrderSetting(orderInfo, z);
                } else if (jSONObjectOptJSONObject != null) {
                    a.c(sdkBase, jSONObjectOptJSONObject);
                    a.b(orderInfo, z);
                } else {
                    sdkBase.setFeature(ConstProp.REQUIRE_AI_DETECT, false);
                    sdkBase.continueOrderSetting(orderInfo, z);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sdkBase.continueOrderSetting(orderInfo, z);
            }
            return false;
        }
    }

    public static void a(SdkBase sdkBase, JSONObject jSONObject) {
        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("dashen");
        if (jSONObjectOptJSONObject != null) {
            c(sdkBase, jSONObjectOptJSONObject);
        } else {
            sdkBase.setFeature(ConstProp.REQUIRE_AI_DETECT, false);
            UniSdkUtils.i("AiDetect", "sauth response not contain dashen info");
        }
    }

    public static void c(SdkBase sdkBase, JSONObject jSONObject) throws JSONException {
        UniSdkUtils.i("AiDetect", "dashen = ".concat(String.valueOf(jSONObject)));
        String strOptString = jSONObject.optString("black_adult_sign");
        jSONObject.optInt("as_age_range_v2");
        String strOptString2 = jSONObject.optString("timestamp");
        SdkBase sdkBase2 = sdkBase.getSdkInstMap().get("ngaidetect");
        if (sdkBase2 == null) {
            UniSdkUtils.w("AiDetect", "not pack ngaidetect");
        }
        sdkBase.setFeature(ConstProp.REQUIRE_AI_DETECT, (TextUtils.isEmpty(strOptString) || sdkBase2 == null || !sdkBase.hasFeature(ConstProp.INNER_MODE_AI_DETECT) || TextUtils.isEmpty(strOptString2)) ? false : true);
        sdkBase.setPropStr(ConstProp.AI_GLDT_TOKEN, strOptString);
        sdkBase.setPropStr(ConstProp.AI_GLDT_TIMESTAMP, strOptString2);
        sdkBase.setPropStr(ConstProp.AI_GLDT_ALL, jSONObject.toString());
    }

    public static void a(OrderInfo orderInfo, boolean z) {
        SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
        if (sdkBase.hasFeature(ConstProp.REQUIRE_AI_DETECT)) {
            b(orderInfo, z);
            return;
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
        String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
        if (TextUtils.isEmpty(propStr) || TextUtils.isEmpty(propStr2)) {
            orderInfo.setOrderStatus(3);
            orderInfo.setOrderErrReason("UNISDK_JF_GAS3_URL / JF_LOG_KEY is empty");
            sdkBase.checkOrderDone(orderInfo);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(propStr);
        sb.append(propStr.endsWith("/") ? "query_dashen_recognition" : "/query_dashen_recognition");
        sb.append("?sdkuid=");
        sb.append(sdkBase.getPropStr(ConstProp.UID));
        sb.append("&login_channel=");
        sb.append(sdkBase.getChannel());
        String string = sb.toString();
        HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
        queueItemNewQueueItem.method = "GET";
        queueItemNewQueueItem.url = string;
        queueItemNewQueueItem.connectionTimeout = 3000;
        queueItemNewQueueItem.soTimeout = 3000;
        queueItemNewQueueItem.leftRetry = 0;
        queueItemNewQueueItem.callback = new HTTPCallback() { // from class: com.netease.ntunisdk.base.function.a.1
            final /* synthetic */ OrderInfo b;
            final /* synthetic */ boolean c;

            AnonymousClass1(OrderInfo orderInfo2, boolean z2) {
                orderInfo = orderInfo2;
                z = z2;
            }

            @Override // com.netease.ntunisdk.base.utils.HTTPCallback
            public final boolean processResult(String str, String str2) throws Throwable {
                UniSdkUtils.d("AiDetect", "queryDashenRecognition Result : ".concat(String.valueOf(str)));
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    int iOptInt = jSONObject.optInt("code");
                    JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("dashen");
                    if (iOptInt != 200) {
                        sdkBase.continueOrderSetting(orderInfo, z);
                    } else if (jSONObjectOptJSONObject != null) {
                        a.c(sdkBase, jSONObjectOptJSONObject);
                        a.b(orderInfo, z);
                    } else {
                        sdkBase.setFeature(ConstProp.REQUIRE_AI_DETECT, false);
                        sdkBase.continueOrderSetting(orderInfo, z);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sdkBase.continueOrderSetting(orderInfo, z);
                }
                return false;
            }
        };
        HashMap map = new HashMap();
        try {
            ApiRequestUtil.addSecureHeader(map, propStr2, queueItemNewQueueItem.method, string, "", true);
        } catch (Exception e) {
            UniSdkUtils.d("AiDetect", "hmacSHA256Signature exception:" + e.getMessage());
        }
        queueItemNewQueueItem.setHeaders(map);
        HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).addItem(queueItemNewQueueItem);
    }
}