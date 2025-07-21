package com.netease.ntunisdk.base.utils.clientlog;

import android.content.Context;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.unilogger.global.Const;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialOperation;
import com.xiaomi.onetrack.OneTrack;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ClientLogUtils {
    private static final String INNER_CLIENT_LOG_URL = "https://applog.matrix.netease.com/client/sdk/clientlog";
    private static final String OVERSEA_CLIENT_LOG_URL = "https://applog.matrix.easebar.com/client/sdk/clientlog";

    public static boolean isEBOversea() {
        return SdkMgr.getInst().getPropInt("EB", -1) == 1;
    }

    public static String getClientLogUrl() {
        String propStr = isEBOversea() ? OVERSEA_CLIENT_LOG_URL : INNER_CLIENT_LOG_URL;
        if (!TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.JF_CLIENT_LOG_URL))) {
            propStr = SdkMgr.getInst().getPropStr(ConstProp.JF_CLIENT_LOG_URL);
        }
        return (!isEBOversea() || TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.JF_OVERSEA_CLIENT_LOG_URL))) ? propStr : SdkMgr.getInst().getPropStr(ConstProp.JF_OVERSEA_CLIENT_LOG_URL);
    }

    public static void addBasicInfo2Json(Context context, JSONObject jSONObject) {
        try {
            if (!jSONObject.has("res_code")) {
                jSONObject.put("res_code", 200);
            }
            jSONObject.put("gameid", SdkMgr.getInst().getPropStr("JF_GAMEID"));
            jSONObject.put("uid", SdkMgr.getInst().getPropStr(ConstProp.UID));
            jSONObject.put("aid", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_AID));
            jSONObject.put(Const.CONFIG_KEY.ROLEID, SdkMgr.getInst().getPropStr(ConstProp.USERINFO_UID));
            jSONObject.put("hostid", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_HOSTID));
            jSONObject.put(ClientLogConstant.TRANSID, UniSdkUtils.getTransid(context));
            jSONObject.put("packagename", UniSdkUtils.getAppPackageName(context));
            jSONObject.put("channel", getOriginChannel());
            jSONObject.put(com.netease.ntunisdk.external.protocol.Const.APP_CHANNEL, getOriginAppChannel());
            jSONObject.put("platform", "ad");
            jSONObject.put("account_channel", SdkMgr.getInst().getChannel());
            jSONObject.put("account_app_channel", SdkMgr.getInst().getAppChannel());
            jSONObject.put("account_platform", SdkMgr.getInst().getPlatform());
            jSONObject.put("version", SdkMgr.getInst().getSDKVersion(SdkMgr.getInst().getChannel()));
            jSONObject.put("base_version", SdkMgr.getBaseVersion());
            jSONObject.put(ConstProp.UDID, SdkMgr.getInst().getUdid());
            jSONObject.put("unisdk_deviceid", UniSdkUtils.getUnisdkDeviceId(context));
            jSONObject.put(OneTrack.Param.OAID, UniSdkUtils.getOAID(context));
            String str = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.US).format(new Date());
            jSONObject.put("logtime", str);
            jSONObject.put("timestamp", str);
            jSONObject.put(Constants.TS, System.currentTimeMillis());
            jSONObject.put("uni_transaction_id", ClientLog.getInst().getUniTransactionId());
            jSONObject.put("is_root", SdkMgr.getInst().getPropStr("IS_DEVICE_ROOTED"));
        } catch (JSONException unused) {
        }
    }

    private static String getOriginChannel() {
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.ORIGIN_CHANNEL);
        return !TextUtils.isEmpty(propStr) ? propStr : SdkMgr.getInst().getChannel();
    }

    private static String getOriginAppChannel() {
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.ORIGIN_APP_CHANNEL);
        return !TextUtils.isEmpty(propStr) ? propStr : SdkMgr.getInst().getAppChannel();
    }

    public static void addOrderInfo2Json(OrderInfo orderInfo, JSONObject jSONObject) {
        if (orderInfo == null) {
            return;
        }
        try {
            jSONObject.put("goodsid", orderInfo.getProductId());
            jSONObject.put("goodsname", orderInfo.getProductName());
            jSONObject.put("sn", orderInfo.getOrderId());
            jSONObject.put("orderid", orderInfo.getOrderId());
            jSONObject.put("consumesn", orderInfo.getSdkOrderId());
            StringBuilder sb = new StringBuilder();
            sb.append(orderInfo.getProductCurrentPrice() * orderInfo.getCount());
            jSONObject.put("ordermoney", sb.toString());
            jSONObject.put(ConstProp.CURRENCY, orderInfo.getOrderCurrency());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(orderInfo.getProductCurrentPrice() * orderInfo.getCount());
            jSONObject.put("paymoney", sb2.toString());
            jSONObject.put("paycurrency", orderInfo.getOrderCurrency());
            jSONObject.put("orderchannel", orderInfo.getOrderChannel());
            jSONObject.put("orderEtc", orderInfo.getOrderEtc());
            jSONObject.put(SocialOperation.GAME_SIGNATURE, orderInfo.getSignature());
            jSONObject.put("webpayment", orderInfo.isWebPayment());
            jSONObject.put("orderstatus", orderInfo.getOrderStatus());
            jSONObject.putOpt("reason", orderInfo.getOrderErrReason());
            if (!TextUtils.isEmpty(orderInfo.getUid())) {
                jSONObject.put("uid", orderInfo.getUid());
            }
            if (!TextUtils.isEmpty(orderInfo.getAid())) {
                jSONObject.put("aid", orderInfo.getAid());
            }
            if (!TextUtils.isEmpty(orderInfo.getUserName())) {
                jSONObject.put(Const.CONFIG_KEY.ROLEID, orderInfo.getUserName());
            }
            if (!TextUtils.isEmpty(orderInfo.getServerId())) {
                jSONObject.put("hostid", orderInfo.getServerId());
            }
        } catch (JSONException unused) {
        }
        try {
            if (orderInfo.isCartOrder()) {
                jSONObject.putOpt("goodscart", new JSONArray(orderInfo.getExtendJson()));
                return;
            }
            JSONObject jSONObject2 = new JSONObject(orderInfo.getExtendJson());
            Iterator<String> itKeys = jSONObject2.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                if (!jSONObject.has(next)) {
                    jSONObject.putOpt(next, jSONObject2.get(next));
                }
            }
        } catch (Exception unused2) {
        }
    }

    public static boolean isClientLogEnabled() {
        return SdkMgr.getInst().getPropInt(ConstProp.UNISDK_LOG_STATUS, 1) != 0;
    }

    public static JSONObject serverApiRawInfo(String str, Object[] objArr) {
        HashMap map = new HashMap();
        map.put("url", str);
        map.putAll(objArgs2Map(objArr));
        return buildRawInfo("api", map);
    }

    public static JSONObject buildRawInfo(String str, Map<String, Object> map) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("key", str);
            jSONObject.put("segmentation", new JSONObject(map));
        } catch (JSONException e) {
            UniSdkUtils.d(ClientLog.TAG, "buildRawInfo exception: " + e.getMessage());
        }
        return jSONObject;
    }

    public static boolean isClassInstalled(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static JSONObject objArgs2Json(Object[] objArr) {
        return new JSONObject(objArgs2Map(objArr));
    }

    public static Map<String, Object> objArgs2Map(Object[] objArr) {
        HashMap map = new HashMap();
        if (objArr != null && objArr.length % 2 == 0) {
            for (int i = 0; i < objArr.length; i += 2) {
                map.put(String.valueOf(objArr[i]), objArr[i + 1]);
            }
        }
        return map;
    }
}