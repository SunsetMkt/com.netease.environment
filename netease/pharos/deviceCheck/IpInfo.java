package com.netease.pharos.deviceCheck;

import android.text.TextUtils;
import android.util.Base64;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.pharos.util.LogUtil;
import org.jose4j.jwk.Use;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class IpInfo {
    private static final String TAG = "IpInfo";
    private static IpInfo sIpInfo;
    private String mIp_addr = "";
    private String mIp_continent = "";
    private String mIp_country = "";
    private String mIp_province = "";
    private String mIp_payload = "";
    private String mIp_sig = "";

    private IpInfo() {
    }

    public static IpInfo getInstances() {
        if (sIpInfo == null) {
            synchronized (IpInfo.class) {
                if (sIpInfo == null) {
                    sIpInfo = new IpInfo();
                }
            }
        }
        return sIpInfo;
    }

    public void init(String str) throws JSONException {
        LogUtil.i(TAG, "\u89e3\u6790\u5185\u5bb9---" + str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String string = jSONObject.has("payload") ? jSONObject.getString("payload") : "";
            JSONObject jSONObject2 = new JSONObject(new String(Base64.decode(string.getBytes(), 0)));
            this.mIp_addr = jSONObject2.has("ip") ? jSONObject2.getString("ip") : "";
            if (jSONObject2.has("subdivisions")) {
                JSONObject jSONObject3 = jSONObject2.getJSONObject("subdivisions");
                if (jSONObject3.has("names")) {
                    JSONObject jSONObject4 = jSONObject3.getJSONObject("names");
                    if (jSONObject4.has("en")) {
                        this.mIp_province = jSONObject4.getString("en");
                    }
                }
            }
            if (jSONObject2.has(Const.COUNTRY)) {
                JSONObject jSONObject5 = jSONObject2.getJSONObject(Const.COUNTRY);
                if (jSONObject5.has("names")) {
                    JSONObject jSONObject6 = jSONObject5.getJSONObject("names");
                    if (jSONObject6.has("en")) {
                        this.mIp_country = jSONObject6.getString("en");
                    }
                }
            }
            if (jSONObject2.has("continent")) {
                JSONObject jSONObject7 = jSONObject2.getJSONObject("continent");
                if (jSONObject7.has("names")) {
                    JSONObject jSONObject8 = jSONObject7.getJSONObject("names");
                    if (jSONObject8.has("en")) {
                        this.mIp_continent = jSONObject8.getString("en");
                    }
                }
            }
            this.mIp_payload = string;
            this.mIp_sig = jSONObject.has(Use.SIGNATURE) ? jSONObject.getString(Use.SIGNATURE) : "";
        } catch (JSONException e) {
            LogUtil.w(TAG, "\u89e3\u6790\u5185\u5bb9=" + e);
            e.printStackTrace();
        }
    }

    public String getmIp_addr() {
        return this.mIp_addr;
    }

    public void setmIp_addr(String str) {
        this.mIp_addr = str;
    }

    public String getmIp_continent() {
        return this.mIp_continent;
    }

    public void setmIp_continent(String str) {
        this.mIp_continent = str;
    }

    public String getmIp_country() {
        return this.mIp_country;
    }

    public void setmIp_country(String str) {
        this.mIp_country = str;
    }

    public String getmIp_province() {
        return this.mIp_province;
    }

    public void setmIp_province(String str) {
        this.mIp_province = str;
    }

    public String getmIp_payload() {
        return this.mIp_payload;
    }

    public void setmIp_payload(String str) {
        this.mIp_payload = str;
    }

    public String getmIp_sig() {
        return this.mIp_sig;
    }

    public void setmIp_sig(String str) {
        this.mIp_sig = str;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("mIp_addr=");
        stringBuffer.append(this.mIp_addr).append("\nmIp_continent=");
        stringBuffer.append(this.mIp_continent).append("\nmIp_country=");
        stringBuffer.append(this.mIp_country).append("\nmIp_province=");
        stringBuffer.append(this.mIp_province).append("\nmIp_payload=");
        stringBuffer.append(this.mIp_payload).append("\nmIp_sig=");
        stringBuffer.append(this.mIp_sig).append("\n");
        return stringBuffer.toString();
    }
}