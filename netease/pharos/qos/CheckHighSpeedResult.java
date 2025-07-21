package com.netease.pharos.qos;

import android.text.TextUtils;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.config.CheckResult;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.network.NetworkStatus;
import com.netease.pharos.util.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class CheckHighSpeedResult {
    private static final String TAG = "CheckHighSpeedResult";
    private static CheckHighSpeedResult sCheckHighSpeedResult;
    private final String mProject = null;
    private final String mUdid = null;
    private final String mNetid = null;
    private final String mRegion = null;
    private final String mMethod = null;
    private final String mIpAddr = null;
    private final String mIpPayLoad = null;
    private final String mIpSig = null;
    private final String mServer = null;
    private String mIp = null;
    private String mPort = null;
    private JSONArray mPorts = null;
    private ArrayList<CheckResult> mHighSpeedUdpResult = null;

    private CheckHighSpeedResult() {
    }

    public static CheckHighSpeedResult getInstance() {
        if (sCheckHighSpeedResult == null) {
            synchronized (CheckHighSpeedResult.class) {
                if (sCheckHighSpeedResult == null) {
                    sCheckHighSpeedResult = new CheckHighSpeedResult();
                }
            }
        }
        return sCheckHighSpeedResult;
    }

    public void setHighSpeedUdpResult(ArrayList<CheckResult> arrayList) {
        this.mHighSpeedUdpResult = arrayList;
    }

    public void init(String str, JSONArray jSONArray) {
        this.mIp = str;
        this.mPorts = jSONArray;
    }

    public void init(String str, String str2) {
        this.mIp = str;
        this.mPort = str2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r15v0, types: [java.lang.Object, org.json.JSONArray] */
    /* JADX WARN: Type inference failed for: r4v10, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r4v13, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r4v15, types: [java.lang.StringBuilder] */
    /* JADX WARN: Type inference failed for: r4v3 */
    /* JADX WARN: Type inference failed for: r4v4 */
    /* JADX WARN: Type inference failed for: r4v6, types: [org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r4v7, types: [java.lang.Object, org.json.JSONArray] */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.Object, org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r9v13, types: [java.lang.Object, org.json.JSONObject] */
    public JSONObject getResult(int i) throws JSONException {
        JSONObject jSONObject;
        boolean z;
        ?? r4;
        JSONObject jSONObject2;
        boolean z2;
        String string;
        boolean z3;
        boolean z4;
        String str;
        String[] strArrSplit;
        String str2;
        String str3;
        String[] strArrSplit2;
        JSONObject jSONObject3 = new JSONObject();
        ?? jSONObject4 = new JSONObject();
        PharosProxy.getInstance().getmIp();
        boolean z5 = i == 0;
        try {
            jSONObject4.put("project", DeviceInfo.getInstance().getProject());
            jSONObject4.put("udid", DeviceInfo.getInstance().getUdid());
            jSONObject4.put("netid", DeviceInfo.getInstance().getNetid());
            jSONObject4.put("region", DeviceInfo.getInstance().getRegion());
            jSONObject4.put("method", DeviceInfo.getInstance().getmMethod());
            jSONObject4.put("ipaddr", DeviceInfo.getInstance().getIpaddr());
            jSONObject4.put("ip_payload", DeviceInfo.getInstance().getIpPayload());
            jSONObject4.put("ip_sig", DeviceInfo.getInstance().getIpSig());
            jSONObject4.put("network_ipv6", DeviceInfo.getInstance().isSupportIpV6());
            jSONObject4.put("ip_local_v6", DeviceInfo.getInstance().getIpLocalAddrV6());
            jSONObject4.put("ipaddr_v6", DeviceInfo.getInstance().getIpaddrV6());
            jSONObject4.put("network", NetworkStatus.getInstance().getNetwork());
            jSONObject4.put("network_switch", NetworkStatus.getInstance().isNetworkChanged() ? "1" : "0");
            ?? jSONObject5 = new JSONObject();
            JSONArray jSONArray = this.mPorts;
            try {
                if (jSONArray != null && jSONArray.length() > 0) {
                    int i2 = 0;
                    boolean z6 = true;
                    while (i2 < this.mPorts.length()) {
                        String string2 = this.mPorts.getString(i2);
                        ?? jSONArray2 = new JSONArray();
                        ArrayList<CheckResult> arrayList = this.mHighSpeedUdpResult;
                        if (arrayList != null && arrayList.size() > 0) {
                            Iterator<CheckResult> it = this.mHighSpeedUdpResult.iterator();
                            while (it.hasNext()) {
                                CheckResult next = it.next();
                                String ip = next.getIp();
                                next.getmPort();
                                String str4 = next.getmExtra();
                                Iterator<CheckResult> it2 = it;
                                StringBuilder sb = new StringBuilder();
                                JSONObject jSONObject6 = jSONObject3;
                                sb.append("CheckHighSpeedResult [getResult] port=");
                                sb.append(string2);
                                sb.append(", pExtra=");
                                sb.append(str4);
                                LogUtil.i(TAG, sb.toString());
                                if (TextUtils.isEmpty(str4) || !str4.contains(",") || (strArrSplit2 = str4.split(",")) == null || strArrSplit2.length <= 1) {
                                    str2 = null;
                                    str3 = null;
                                } else {
                                    String str5 = strArrSplit2[0];
                                    str2 = strArrSplit2[1];
                                    str3 = str5;
                                }
                                if (!TextUtils.isEmpty(string2) && !TextUtils.isEmpty(str3) && string2.equals(str3)) {
                                    JSONArray jSONArray3 = new JSONArray();
                                    jSONArray3.put(ip);
                                    jSONArray3.put(str2);
                                    jSONArray2.put(jSONArray3);
                                }
                                it = it2;
                                jSONObject3 = jSONObject6;
                            }
                        }
                        JSONObject jSONObject7 = jSONObject3;
                        LogUtil.i(TAG, "CheckHighSpeedResult [getResult] portArray111=" + jSONArray2);
                        JSONArray jSONArray4 = new JSONArray();
                        if (TextUtils.isEmpty(this.mIp)) {
                            this.mIp = "";
                        }
                        if (TextUtils.isEmpty(string2)) {
                            string2 = "";
                        }
                        jSONArray4.put(this.mIp);
                        jSONArray4.put(string2);
                        jSONArray2.put(jSONArray4);
                        z6 = z6 && jSONArray2.length() > 1;
                        LogUtil.i(TAG, "CheckHighSpeedResult [getResult] portArray222=" + jSONArray2);
                        jSONObject5.put(string2, jSONArray2);
                        LogUtil.i(TAG, "CheckHighSpeedResult [getResult] serverData=" + jSONObject5);
                        i2++;
                        jSONObject3 = jSONObject7;
                    }
                    jSONObject = jSONObject3;
                    z = z5 && z6;
                    try {
                        jSONObject4.put("server", jSONObject5);
                    } catch (Exception e) {
                        e = e;
                        z5 = z;
                        LogUtil.w(TAG, "CheckHighSpeedResult [getResult] Exception1=" + e);
                        z = z5;
                        jSONObject4.put("harbor_status", z);
                        r4 = jSONObject;
                        try {
                            r4.put("server", jSONObject4);
                            jSONObject2 = r4;
                        } catch (Exception e2) {
                            e = e2;
                            LogUtil.w(TAG, "CheckHighSpeedResult [getResult] Exception2=" + e);
                            jSONObject2 = r4;
                            return jSONObject2;
                        }
                        return jSONObject2;
                    }
                } else {
                    jSONObject = jSONObject3;
                    ?? jSONArray5 = new JSONArray();
                    ArrayList<CheckResult> arrayList2 = this.mHighSpeedUdpResult;
                    if (arrayList2 != null && arrayList2.size() > 0) {
                        LogUtil.i(TAG, "CheckHighSpeedResult [getResult] mHighSpeedUdpResult=" + this.mHighSpeedUdpResult);
                        Iterator<CheckResult> it3 = this.mHighSpeedUdpResult.iterator();
                        while (it3.hasNext()) {
                            CheckResult next2 = it3.next();
                            String str6 = next2.getmExtra();
                            if (TextUtils.isEmpty(str6) || !str6.contains(",") || (strArrSplit = str6.split(",")) == null || strArrSplit.length <= 1) {
                                str = null;
                            } else {
                                String str7 = strArrSplit[0];
                                str = strArrSplit[1];
                            }
                            JSONArray jSONArray6 = new JSONArray();
                            jSONArray6.put(next2.getIp());
                            jSONArray6.put(str);
                            jSONArray5.put(jSONArray6);
                        }
                    }
                    JSONArray jSONArray7 = new JSONArray();
                    if (TextUtils.isEmpty(this.mIp)) {
                        this.mIp = "";
                    }
                    JSONArray jSONArray8 = this.mPorts;
                    if (jSONArray8 != null && jSONArray8.length() >= 1) {
                        z2 = false;
                        string = this.mPorts.getString(0);
                    } else {
                        z2 = false;
                        string = this.mPort;
                    }
                    LogUtil.i(TAG, "CheckHighSpeedResult [getResult] mIp=" + this.mIp + ", mPort=" + string);
                    if (TextUtils.isEmpty(string)) {
                        z3 = true;
                        z4 = true;
                    } else {
                        jSONArray7.put(this.mIp);
                        jSONArray7.put(string);
                        jSONArray5.put(jSONArray7);
                        z3 = true;
                        z4 = jSONArray5.length() > 1 ? true : z2;
                        jSONObject4.put("server", jSONArray5);
                    }
                    z = (z5 && z4) ? z3 : z2;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } catch (Exception e4) {
            e = e4;
            jSONObject = jSONObject3;
        }
        try {
            jSONObject4.put("harbor_status", z);
            r4 = jSONObject;
            r4.put("server", jSONObject4);
            jSONObject2 = r4;
        } catch (Exception e5) {
            e = e5;
            r4 = jSONObject;
        }
        return jSONObject2;
    }

    public void reset() {
        sCheckHighSpeedResult = null;
    }

    public void cleanData() {
        this.mHighSpeedUdpResult = null;
    }
}