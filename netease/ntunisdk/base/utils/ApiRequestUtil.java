package com.netease.ntunisdk.base.utils;

import android.text.TextUtils;
import android.util.Base64;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.base.utils.clientlog.ClientLog;
import com.tencent.connect.common.Constants;
import com.xiaomi.onetrack.util.z;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.jose4j.jwt.ReservedClaimNames;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class ApiRequestUtil {
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final long INTERVAL = 3600;
    private static final int LENGTH = 32;
    private static final String TAG = "ApiRequestUtil";
    private static String accessToken;
    private static long accessTokenExp;
    private static boolean isFromSauth;
    private static long refreshTokenExp;
    private static long timestampDiff;

    public static String generateNonce() {
        try {
            return UUID.randomUUID().toString().replace("-", "");
        } catch (Exception unused) {
            Random random = new Random();
            StringBuilder sb = new StringBuilder(32);
            for (int i = 0; i < 32; i++) {
                sb.append(CHARACTERS.charAt(random.nextInt(62)));
            }
            return sb.toString();
        }
    }

    public static void generateTimestampDiff() {
        String string;
        if (SdkMgr.getInst() == null) {
            UniSdkUtils.e(TAG, "SdkMgr.getInst() is null");
            timestampDiff = 0L;
            return;
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
        if (TextUtils.isEmpty(propStr)) {
            string = "";
        } else {
            StringBuilder sb = new StringBuilder(propStr);
            if (propStr.endsWith("/")) {
                sb.append("server_time");
            } else {
                sb.append("/server_time");
            }
            string = sb.toString();
        }
        if (TextUtils.isEmpty(string)) {
            UniSdkUtils.e(TAG, "UNISDK_JF_GAS3_URL is empty");
            timestampDiff = 0L;
        } else {
            NetUtil.wget(string, new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.utils.ApiRequestUtil.1
                @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
                public final void ProcessResult(String str) {
                    UniSdkUtils.i(ApiRequestUtil.TAG, "generateTimestampDiff wget: ".concat(String.valueOf(str)));
                    if (ApiRequestUtil.isFromSauth) {
                        UniSdkUtils.i(ApiRequestUtil.TAG, "TimestampDiff is set by Sauth, pass");
                        return;
                    }
                    if (TextUtils.isEmpty(str)) {
                        UniSdkUtils.e(ApiRequestUtil.TAG, "result is empty");
                        long unused = ApiRequestUtil.timestampDiff = 0L;
                        return;
                    }
                    try {
                        JSONObject jSONObject = new JSONObject(str);
                        int iOptInt = jSONObject.optInt("code");
                        int iOptInt2 = jSONObject.optInt("subcode");
                        if (iOptInt != 200 || iOptInt2 != 1) {
                            long unused2 = ApiRequestUtil.timestampDiff = 0L;
                        } else {
                            long jOptLong = jSONObject.optLong("server_time");
                            long jCurrentTimeMillis = System.currentTimeMillis() / 1000;
                            UniSdkUtils.i(ApiRequestUtil.TAG, "generateTimestampDiff server_time: ".concat(String.valueOf(jOptLong)));
                            UniSdkUtils.i(ApiRequestUtil.TAG, "generateTimestampDiff cur_time: ".concat(String.valueOf(jCurrentTimeMillis)));
                            if (jCurrentTimeMillis == 0) {
                                long unused3 = ApiRequestUtil.timestampDiff = 0L;
                            } else {
                                long unused4 = ApiRequestUtil.timestampDiff = jOptLong - jCurrentTimeMillis;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        long unused5 = ApiRequestUtil.timestampDiff = 0L;
                    }
                    UniSdkUtils.i(ApiRequestUtil.TAG, "generateTimestampDiff timestampDiff: " + ApiRequestUtil.timestampDiff);
                }
            });
        }
    }

    public static void setTimestampDiff(long j) {
        timestampDiff = j;
        UniSdkUtils.i(TAG, "generateTimestampDiff timestampDiff: ".concat(String.valueOf(j)));
        isFromSauth = true;
    }

    public static long generateTimestamp() {
        return (System.currentTimeMillis() / 1000) + timestampDiff;
    }

    public static void setAccessToken(String str) {
        accessToken = str;
    }

    public static void getExpiration() {
        if (SdkMgr.getInst() == null || TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_REFRESH_TOKEN))) {
            return;
        }
        accessTokenExp = getExpFromToken(accessToken);
        refreshTokenExp = getExpFromToken(SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_REFRESH_TOKEN));
        UniSdkUtils.d(TAG, "accessTokenExp = " + accessTokenExp);
        UniSdkUtils.d(TAG, "refreshTokenExp = " + refreshTokenExp);
    }

    private static long getExpFromToken(String str) {
        String[] strArrSplit;
        try {
            strArrSplit = str.split(z.f2789a);
        } catch (Exception e) {
            UniSdkUtils.e(TAG, e.getMessage());
        }
        if (strArrSplit != null && strArrSplit.length >= 2) {
            JSONObject jSONObject = new JSONObject(new String(Base64.decode(strArrSplit[1], 0), "UTF-8"));
            if (jSONObject.has(ReservedClaimNames.EXPIRATION_TIME)) {
                return jSONObject.optLong(ReservedClaimNames.EXPIRATION_TIME);
            }
            return 0L;
        }
        return 0L;
    }

    public static Map<String, String> getSecureHeaderMap(String str, String str2, String str3, boolean z) throws Exception {
        HashMap map = new HashMap();
        addSecureHeader(map, null, str, str2, str3, z);
        return map;
    }

    public static void addSecureHeader(Map<String, String> map, String str, String str2, String str3, boolean z) throws Exception {
        addSecureHeader(map, null, str, str2, str3, z);
    }

    public static void addSecureHeader(Map<String, String> map, String str, String str2, String str3, String str4, boolean z) throws Exception {
        if (map == null || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return;
        }
        if (TextUtils.isEmpty(str) && SdkMgr.getInst() != null) {
            str = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
        }
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.d(TAG, "JF_CLIENT_KEY empty");
            return;
        }
        if (TextUtils.isEmpty(str4)) {
            str4 = "";
        }
        if (!map.containsKey("X-TASK-ID") && SdkMgr.getInst() != null) {
            map.put("X-TASK-ID", "transid=" + SdkMgr.getInst().getPropStr(ConstProp.TRANS_ID) + ",uni_transaction_id=" + ClientLog.getInst().getUniTransactionId());
        }
        if (SdkMgr.getInst() != null && SdkMgr.getInst().getPropInt(ConstProp.DISABLE_INTERFACE_PROTECTION, 0) == 1) {
            map.put("X-Client-Sign", Crypto.hmacSHA256Signature(str, Crypto.getSignSrc(str2, str3, str4)));
            map.put("X-Common-SDK", "ad=" + SdkMgr.getBaseVersion());
            return;
        }
        String strValueOf = String.valueOf(generateTimestamp());
        String strGenerateNonce = generateNonce();
        map.put("X-Client-Sign", Crypto.hmacSHA256Signature(str, Crypto.getSignSrc(str2, str3, str4) + "\n" + strValueOf + "\n" + strGenerateNonce));
        map.put("X-Gas-Timestamp", strValueOf);
        map.put("X-Gas-Nonce", strGenerateNonce);
        StringBuilder sb = new StringBuilder("ad=");
        sb.append(SdkMgr.getBaseVersion());
        map.put("X-Common-SDK", sb.toString());
        if (SdkMgr.getInst() == null || !z) {
            return;
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_ACCESS_TOKEN);
        if (TextUtils.isEmpty(propStr)) {
            return;
        }
        map.put("Authorization", "Bearer ".concat(String.valueOf(propStr)));
    }

    public static void modifySecureHeader(HTTPQueue.QueueItem queueItem) throws Exception {
        Map<String, String> headers;
        if (queueItem != null && (headers = queueItem.getHeaders()) != null && headers.containsKey("X-Client-Sign") && headers.containsKey("X-Gas-Timestamp") && headers.containsKey("X-Gas-Nonce")) {
            addSecureHeader(headers, null, queueItem.method, queueItem.url, queueItem.getBodyStr(), headers.containsKey("Authorization"));
        }
    }

    public static void refreshToken() {
        if (SdkMgr.getInst() == null || TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_REFRESH_TOKEN)) || TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_GAS3_URL))) {
            return;
        }
        if (accessTokenExp != 0 && generateTimestamp() > accessTokenExp - INTERVAL) {
            UniSdkUtils.d(TAG, "do refresh the accessToken");
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
            StringBuilder sb = new StringBuilder(propStr);
            if (propStr.endsWith("/")) {
                sb.append("iam/oauth2/refresh");
            } else {
                sb.append("/iam/oauth2/refresh");
            }
            String string = sb.toString();
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("refresh_token", SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_REFRESH_TOKEN));
                HashMap map = new HashMap();
                addSecureHeader(map, SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY), "POST", string, jSONObject.toString(), false);
                NetUtil.wpost_http_https(string, jSONObject.toString(), map, new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.utils.ApiRequestUtil.2
                    @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
                    public final void ProcessResult(String str) {
                        UniSdkUtils.d(ApiRequestUtil.TAG, "refreshToken result = ".concat(String.valueOf(str)));
                        try {
                            JSONObject jSONObject2 = new JSONObject(str);
                            int iOptInt = jSONObject2.optInt("code");
                            int iOptInt2 = jSONObject2.optInt("subcode");
                            if (iOptInt == 200 && iOptInt2 == 1) {
                                String strOptString = jSONObject2.optString(Constants.PARAM_ACCESS_TOKEN);
                                if (TextUtils.isEmpty(strOptString)) {
                                    return;
                                }
                                SdkMgr.getInst().setPropStr(ConstProp.UNISDK_JF_ACCESS_TOKEN, strOptString);
                                ApiRequestUtil.getExpiration();
                                return;
                            }
                            if (iOptInt == 401) {
                                if (iOptInt2 == 100 || iOptInt2 == 101 || iOptInt2 == 102) {
                                    ((SdkBase) SdkMgr.getInst()).resetCommonProp();
                                    ((SdkBase) SdkMgr.getInst()).finishLoginDone(12);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}