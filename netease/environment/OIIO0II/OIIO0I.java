package com.netease.environment.OIIO0II;

import android.text.TextUtils;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.jose4j.mac.MacUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: HMACUtils.java */
/* loaded from: classes5.dex */
public class OIIO0I {
    private static byte[] OIIO00I(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(str.getBytes(), MacUtil.HMAC_SHA256);
        Mac mac = Mac.getInstance(secretKeySpec.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(bArr);
    }

    public static String OIIO00I(String str, String str2, String str3, String str4) {
        String strOIIO0O0 = "";
        try {
            OIIO.OIIO0O0("EnvSDK_", "gameId: " + str3);
            OIIO.OIIO0O0("EnvSDK_", "encryptKey: " + str4);
            OIIO.OIIO0O0("EnvSDK_", "content: " + str);
            OIIO.OIIO0O0("EnvSDK_", "timestamp: " + str2);
            String str5 = str + str2 + str3;
            OIIO.OIIO0O0("EnvSDK_", "passincontent: " + str5);
            strOIIO0O0 = OIIO0O0.OIIO0O0(OIIO00I(str5.getBytes(), str4));
            OIIO.OIIO0O0("EnvSDK_", " base64: : " + strOIIO0O0);
            return strOIIO0O0;
        } catch (Exception e) {
            e.printStackTrace();
            return strOIIO0O0;
        }
    }

    public static String OIIO00I(String str, String str2) throws JSONException {
        try {
            if (com.netease.environment.OIIO0OO.OIIO0OI.OIIOOOO() && !TextUtils.isEmpty(str)) {
                JSONObject jSONObject = new JSONObject(str2);
                String strValueOf = String.valueOf(System.currentTimeMillis());
                String strOIIO00I = OIIO00I(str, strValueOf, com.netease.environment.OIIO0OO.OIIO0OI.OIIO0I(), com.netease.environment.OIIO0OO.OIIO0OI.OIIOO0());
                jSONObject.put("timestamp", strValueOf);
                if (jSONObject.optInt("code") == 206) {
                    jSONObject.put("sign", OIIO00I(jSONObject.optString("message"), strValueOf, com.netease.environment.OIIO0OO.OIIO0OI.OIIO0I(), com.netease.environment.OIIO0OO.OIIO0OI.OIIOO0()));
                    jSONObject.put("osign", strOIIO00I);
                } else {
                    jSONObject.put("sign", strOIIO00I);
                }
                return jSONObject.toString();
            }
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
            OIIO.OIIO00I("EnvSDK_", e.toString());
            return str2;
        }
    }
}