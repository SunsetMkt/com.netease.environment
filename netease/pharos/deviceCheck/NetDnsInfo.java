package com.netease.pharos.deviceCheck;

import android.text.TextUtils;
import com.facebook.common.util.UriUtil;
import com.netease.pharos.util.LogUtil;
import com.xiaomi.onetrack.api.b;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NetDnsInfo {
    private static final String TAG = "NetDnsInfo";
    private static NetDnsInfo sNetDnsInfo;
    private String mDns_province = null;
    private String mIp_city = null;
    private String mIp = null;
    private String mIp_province = null;
    private String mIp_isp = null;
    private String mRes = null;
    private String mDns_city = null;
    private String mDns_isp = null;
    private String mDns = null;
    private String mMsg = null;

    private NetDnsInfo() {
    }

    public static NetDnsInfo getInstances() {
        if (sNetDnsInfo == null) {
            sNetDnsInfo = new NetDnsInfo();
        }
        return sNetDnsInfo;
    }

    public void init(String str) {
        LogUtil.i(TAG, "\u89e3\u6790\u5185\u5bb9---" + str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            this.mDns_province = jSONObject.has("dns_province") ? jSONObject.getString("dns_province") : "";
            this.mIp_city = jSONObject.has("ip_city") ? jSONObject.getString("ip_city") : "";
            this.mIp = jSONObject.has("ip") ? jSONObject.getString("ip") : "";
            this.mIp_province = jSONObject.has("ip_province") ? jSONObject.getString("ip_province") : "";
            this.mIp_isp = jSONObject.has("ip_isp") ? jSONObject.getString("ip_isp") : "";
            this.mRes = jSONObject.has(UriUtil.LOCAL_RESOURCE_SCHEME) ? jSONObject.getString(UriUtil.LOCAL_RESOURCE_SCHEME) : "";
            this.mDns_city = jSONObject.has("dns_city") ? jSONObject.getString("dns_city") : "";
            this.mDns_isp = jSONObject.has("dns_isp") ? jSONObject.getString("dns_isp") : "";
            this.mDns = jSONObject.has(b.P) ? jSONObject.getString(b.P) : "";
            this.mMsg = jSONObject.has("msg") ? jSONObject.getString("msg") : "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static NetDnsInfo getsNetDnsInfo() {
        return sNetDnsInfo;
    }

    public static void setsNetDnsInfo(NetDnsInfo netDnsInfo) {
        sNetDnsInfo = netDnsInfo;
    }

    public String getmDns_province() {
        return this.mDns_province;
    }

    public void setmDns_province(String str) {
        this.mDns_province = str;
    }

    public String getmIp_city() {
        return this.mIp_city;
    }

    public void setmIp_city(String str) {
        this.mIp_city = str;
    }

    public String getmIp() {
        return this.mIp;
    }

    public void setmIp(String str) {
        this.mIp = str;
    }

    public String getmIp_province() {
        return this.mIp_province;
    }

    public void setmIp_province(String str) {
        this.mIp_province = str;
    }

    public String getmIp_isp() {
        return this.mIp_isp;
    }

    public void setmIp_isp(String str) {
        this.mIp_isp = str;
    }

    public String getmRes() {
        return this.mRes;
    }

    public void setmRes(String str) {
        this.mRes = str;
    }

    public String getmDns_city() {
        return this.mDns_city;
    }

    public void setmDns_city(String str) {
        this.mDns_city = str;
    }

    public String getmDns_isp() {
        return this.mDns_isp;
    }

    public void setmDns_isp(String str) {
        this.mDns_isp = str;
    }

    public String getmDns() {
        return this.mDns;
    }

    public void setmDns(String str) {
        this.mDns = str;
    }

    public String getMmsg() {
        return this.mMsg;
    }

    public void setMmsg(String str) {
        this.mMsg = str;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("\nmDns_province = ");
        stringBuffer.append(this.mDns_province).append("\nmIp_city = ");
        stringBuffer.append(this.mIp_city).append("\nmIp = ");
        stringBuffer.append(this.mIp).append("\nmIp_province = ");
        stringBuffer.append(this.mIp_province).append("\nmIp_isp = ");
        stringBuffer.append(this.mIp_isp).append("\nmRes = ");
        stringBuffer.append(this.mRes).append("\nmDns_city = ");
        stringBuffer.append(this.mDns_city).append("\nmDns_isp = ");
        stringBuffer.append(this.mDns_isp).append("\nmDns = ");
        stringBuffer.append(this.mDns).append("\nmmsg = ");
        stringBuffer.append(this.mMsg).append("\n");
        return stringBuffer.toString();
    }
}