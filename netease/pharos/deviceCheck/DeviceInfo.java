package com.netease.pharos.deviceCheck;

import android.text.TextUtils;
import com.netease.pharos.Const;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.locationCheck.NetAreaInfo;
import com.netease.pharos.network.NetworkStatus;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import com.tencent.connect.common.Constants;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class DeviceInfo {
    private static final String TAG = "DeviceInfo";
    private static DeviceInfo sDeviceInfo;
    private String mNetid;
    private String mProject;
    private String mUdid;
    private String mIpaddr = "";
    private String mIpContinent = "";
    private String mIpCountry = "";
    private String mipProvince = "";
    private String mIpLocalV6 = "";
    private boolean isSupportIpV6 = false;
    private String mIpaddrV6 = "";
    private String mIpPayload = "";
    private String mIpSig = "";
    private String mNameserver = "";
    private String mNetworkIsp = "";
    private String mNetworkSignal = "";
    private String mNetworkIspName = "";
    private String mGateway = "";
    private String mTimezone = "";
    private String mAreazoneContinent = "";
    private String mAreazoneCountry = "";
    private String mOsName = "";
    private String mOsVer = "";
    private String mCountryCode = Constants.APP_VERSION_UNKNOWN;
    private String mLocation = "";
    private String mRegion = "";
    private String mProbeRegion = "";
    private String mMethod = "";
    private String mDecisionTag = "";
    private String mProbeTag = "";
    private Map<String, ArrayList<String>> mUdpMap = new ConcurrentHashMap();
    private String mIpIspId = "";
    private String mIpIspName = "unknown";

    private DeviceInfo() {
        this.mProject = "";
        this.mUdid = "";
        this.mNetid = "";
        this.mProject = PharosProxy.getInstance().getProjectId();
        this.mUdid = PharosProxy.getInstance().getUdid();
        this.mNetid = PharosProxy.getInstance().getmNetId();
    }

    public static DeviceInfo getInstance() {
        if (sDeviceInfo == null) {
            synchronized (DeviceInfo.class) {
                if (sDeviceInfo == null) {
                    sDeviceInfo = new DeviceInfo();
                }
            }
        }
        return sDeviceInfo;
    }

    public String getProject() {
        return this.mProject;
    }

    public void setProject(String str) {
        this.mProject = str;
    }

    public String getUdid() {
        return this.mUdid;
    }

    public void setUdid(String str) {
        this.mUdid = str;
    }

    public String getNetid() {
        return this.mNetid;
    }

    public void setNetid(String str) {
        this.mNetid = str;
    }

    public String getIpaddr() {
        return this.mIpaddr;
    }

    public void setIpaddr(String str) {
        this.mIpaddr = str;
    }

    public String getIpaddrV6() {
        return this.mIpaddrV6;
    }

    public void setIpaddrV6(String str) {
        this.mIpaddrV6 = str;
        this.isSupportIpV6 = !TextUtils.isEmpty(str);
    }

    public String getIpLocalAddrV6() {
        String str = this.mIpLocalV6;
        return str == null ? "" : str;
    }

    public void setIpLocalAddrV6(String str) {
        this.mIpLocalV6 = str;
    }

    public boolean isSupportIpV6() {
        boolean z = (TextUtils.isEmpty(this.mIpaddrV6) && (PharosProxy.getInstance().isIpv6Verify() || TextUtils.isEmpty(this.mIpLocalV6))) ? false : true;
        this.isSupportIpV6 = z;
        return z;
    }

    public String getIpContinent() {
        return this.mIpContinent;
    }

    public void setIpContinent(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mIpContinent = str.toLowerCase().replaceAll("_", "").replaceAll(" ", "");
    }

    public String getIpCountry() {
        return this.mIpCountry;
    }

    public void setIpCountry(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mIpCountry = str.toLowerCase().replaceAll("_", "").replaceAll(" ", "");
    }

    public String getipProvince() {
        return this.mipProvince;
    }

    public void setipProvince(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mipProvince = str.toLowerCase().replaceAll("_", "").replaceAll(" ", "");
    }

    public String getIpPayload() {
        return this.mIpPayload;
    }

    public void setIpPayload(String str) {
        this.mIpPayload = str;
    }

    public String getIpSig() {
        return this.mIpSig;
    }

    public void setIpSig(String str) {
        this.mIpSig = str;
    }

    public String getGateway() {
        return this.mGateway;
    }

    public void setGateway(String str) {
        this.mGateway = str;
    }

    public String getTimezone() {
        return this.mTimezone;
    }

    public void setTimezone(String str) {
        this.mTimezone = str;
    }

    public String getAreazoneContinent() {
        return this.mAreazoneContinent;
    }

    public void setAreazoneContinent(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mAreazoneContinent = str.toLowerCase().replaceAll("_", "").replaceAll(" ", "");
    }

    public String getAreazoneCountry() {
        return this.mAreazoneCountry;
    }

    public void setAreazoneCountry(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mAreazoneCountry = str.toLowerCase().replaceAll("_", "").replaceAll(" ", "");
    }

    public String getNetworkIsp() {
        return this.mNetworkIsp;
    }

    public void setNetworkIsp(String str) {
        this.mNetworkIsp = str;
    }

    public String getNetworkIspName() {
        return this.mNetworkIspName;
    }

    public void setNetworkIspName(String str) {
        this.mNetworkIspName = str;
    }

    public String getNetworkSignal() {
        return this.mNetworkSignal;
    }

    public void setNetworkSignal(String str) {
        this.mNetworkSignal = str;
    }

    public String getNameserver() {
        return this.mNameserver;
    }

    public void setNameserver(String str) {
        this.mNameserver = str;
    }

    public String getOsName() {
        return this.mOsName;
    }

    public void setOsName(String str) {
        this.mOsName = str;
    }

    public String getOsVer() {
        return this.mOsVer;
    }

    public void setOsVer(String str) {
        this.mOsVer = str;
    }

    public String getRegion() {
        LogUtil.i("DeviceInfo", "getRegion:" + this.mRegion);
        return this.mRegion;
    }

    public void setmRegion(String str) {
        LogUtil.i("DeviceInfo", "setRegion:" + str);
        this.mRegion = str;
    }

    public String getmProbeRegion() {
        return this.mProbeRegion;
    }

    public void setmProbeRegion(String str) {
        this.mProbeRegion = str;
    }

    public String getmMethod() {
        return this.mMethod;
    }

    public void setmMethod(String str) {
        this.mMethod = str;
    }

    public String getmDecisionTag() {
        return this.mDecisionTag;
    }

    public void setmDecisionTag(String str) {
        this.mDecisionTag = str;
    }

    public String getmProbeTag() {
        return this.mProbeTag;
    }

    public void setmProbeTag(String str) {
        this.mProbeTag = str;
    }

    public String getmLocation() {
        return this.mLocation;
    }

    public void setmLocation(String str) {
        this.mLocation = str;
    }

    public Map<String, ArrayList<String>> getmUdpMap() {
        return this.mUdpMap;
    }

    public void setmUdpMap(Map<String, ArrayList<String>> map) {
        if (map == null) {
            return;
        }
        this.mUdpMap = this.mUdpMap;
    }

    public String getmCountryCode() {
        return this.mCountryCode;
    }

    public void setmCountryCode(String str) {
        this.mCountryCode = str;
    }

    public String getmIpIspId() {
        return this.mIpIspId;
    }

    public void setmIpIspId(String str) {
        this.mIpIspId = str;
    }

    public String getmIpIspName() {
        return this.mIpIspName;
    }

    public void setmIpIspName(String str) {
        this.mIpIspName = str;
    }

    public String getDeviceInfo(boolean z) {
        JSONObject jSONObject = new JSONObject();
        try {
            Object obj = this.mProject;
            if (obj == null) {
                obj = "00000";
            }
            jSONObject.put("project", obj);
            jSONObject.put("version", Const.VERSION);
            Object obj2 = this.mUdid;
            if (obj2 == null) {
                obj2 = "00000";
            }
            jSONObject.put("udid", obj2);
            Object obj3 = this.mNetid;
            jSONObject.put("netid", obj3 != null ? obj3 : "00000");
            jSONObject.put("ipaddr", this.mIpaddr);
            jSONObject.put("ip_continent", this.mIpContinent);
            jSONObject.put("ip_country", this.mIpCountry);
            jSONObject.put("ip_province", this.mipProvince);
            jSONObject.put("network_ipv6", isSupportIpV6());
            Object obj4 = this.mIpLocalV6;
            if (obj4 == null) {
                obj4 = "";
            }
            jSONObject.put("ip_local_v6", obj4);
            jSONObject.put("ipaddr_v6", isSupportIpV6() ? this.mIpaddrV6 : "");
            if (z) {
                jSONObject.put("ip_payload", this.mIpPayload);
                jSONObject.put("ip_sig", this.mIpSig);
            }
            jSONObject.put("nameserver", this.mNameserver);
            jSONObject.put("network", NetworkStatus.getInstance().getNetwork());
            jSONObject.put("network_switch", NetworkStatus.getInstance().isNetworkChanged() ? "1" : "0");
            jSONObject.put("network_isp", this.mNetworkIsp);
            jSONObject.put("network_signal", this.mNetworkSignal);
            jSONObject.put("network_isp_name", this.mNetworkIspName);
            jSONObject.put("gateway", this.mGateway);
            jSONObject.put("timezone", this.mTimezone);
            jSONObject.put("areazone_continent", this.mAreazoneContinent);
            jSONObject.put("areazone_country", this.mAreazoneCountry);
            jSONObject.put("os_name", this.mOsName);
            jSONObject.put("os_ver", this.mOsVer);
            jSONObject.put("location", this.mLocation);
            Map<String, ArrayList<String>> map = this.mUdpMap;
            if (map != null && map.size() > 0) {
                JSONObject jSONObject2 = new JSONObject();
                String str = this.mRegion;
                LogUtil.i("DeviceInfo", "mUdpMap=" + this.mUdpMap.toString());
                double d = 5000.0d;
                for (String str2 : this.mUdpMap.keySet()) {
                    JSONArray jSONArray = new JSONArray();
                    ArrayList<String> arrayList = this.mUdpMap.get(str2);
                    double d2 = d;
                    String str3 = str;
                    for (int i = 0; i < arrayList.size(); i++) {
                        jSONArray.put(arrayList.get(i));
                        if (1 == i && arrayList.size() > 1) {
                            try {
                                double d3 = Double.parseDouble(arrayList.get(i));
                                LogUtil.i("DeviceInfo", "pRtt=" + d3 + ", rtt=" + d2);
                                if (-1.0d != d3 && d3 < d2) {
                                    try {
                                        LogUtil.i("DeviceInfo", "tBestRegion=" + str2 + ", pRegion=" + str2);
                                        d2 = d3;
                                        str3 = str2;
                                    } catch (Exception e) {
                                        e = e;
                                        d2 = d3;
                                        str3 = str2;
                                        LogUtil.i("DeviceInfo", "Exception=" + e);
                                    }
                                }
                            } catch (Exception e2) {
                                e = e2;
                            }
                        }
                    }
                    if (5000.0d != d2) {
                        setmRegion(str3);
                        this.mMethod = "udpping";
                    }
                    jSONObject2.put(str2, jSONArray);
                    str = str3;
                    d = d2;
                }
                jSONObject.put("udp", jSONObject2);
            }
            jSONObject.put("region", this.mRegion);
            jSONObject.put("probe_region", this.mProbeRegion);
            jSONObject.put("method", this.mMethod);
            jSONObject.put("type", "decision");
            jSONObject.put("testlog", PharosProxy.getInstance().getTestLog());
            jSONObject.put("country_code", this.mCountryCode);
            jSONObject.put("cell_id", Util.getCellId());
            jSONObject.put("ip_local", Util.getLocalIp());
            jSONObject.put("decision_tag", this.mDecisionTag);
            jSONObject.put("probe_tag", this.mProbeTag);
            jSONObject.put("ip_isp_id", this.mIpIspId);
            jSONObject.put("ip_isp_name", Util.getNetworkIspName(this.mIpIspId));
        } catch (JSONException e3) {
            e3.printStackTrace();
            LogUtil.w("DeviceInfo", "DeviceInfo JSONException = " + e3);
        }
        return jSONObject.toString();
    }

    public String toString() {
        return getDeviceInfo(true);
    }

    public void handleProbeAndHarborRegion() {
        LogUtil.i("DeviceInfo", "DeviceInfo [handleProbeAndHarborRegion] start");
        LogUtil.i("DeviceInfo", "DeviceInfo [handleProbeAndHarborRegion] [region \u5206\u88c2]");
        setmProbeRegion(this.mRegion);
        LogUtil.i("DeviceInfo", "DeviceInfo [handleProbeAndHarborRegion] [region \u5206\u88c2] mProbeRegion=" + this.mProbeRegion + ", mRegion=" + this.mRegion);
        String str = NetAreaInfo.getInstances().getmConfigRegion();
        StringBuilder sb = new StringBuilder("DeviceInfo [handleProbeAndHarborRegion] [region \u5206\u88c2] configRegion= ");
        sb.append(str);
        LogUtil.i("DeviceInfo", sb.toString());
        double d = NetAreaInfo.getInstances().getmIspNum();
        double d2 = NetAreaInfo.getInstances().getmIpv6Num();
        double dRandom = Math.random();
        double dRandom2 = Math.random();
        LogUtil.i("DeviceInfo", "DeviceInfo [handleProbeAndHarborRegion] [region \u5206\u88c2] ispRandomNum=" + dRandom + ", ispNum=" + d + ", ipv6RandomNum=" + dRandom2 + ", ipv6Num=" + d2);
        if (!TextUtils.isEmpty(str)) {
            LogUtil.i("DeviceInfo", "DeviceInfo [handleProbeAndHarborRegion] [region \u5206\u88c2] ismHarbor= " + NetAreaInfo.getInstances().ismHarbor());
            LogUtil.i("DeviceInfo", "DeviceInfo [handleProbeAndHarborRegion] [region \u5206\u88c2] ismProbe= " + NetAreaInfo.getInstances().ismProbe());
            if (NetAreaInfo.getInstances().ismHarbor() && dRandom < d) {
                setmRegion(str);
            }
            if (NetAreaInfo.getInstances().ismProbe() && dRandom < d) {
                setmProbeRegion(str);
            }
            LogUtil.i("DeviceInfo", "DeviceInfo [handleProbeAndHarborRegion] [region \u5206\u88c2] mProbeRegion1= " + this.mProbeRegion + ", mRegion=" + this.mRegion);
        }
        if (dRandom2 < d2 && isSupportIpV6()) {
            String str2 = NetAreaInfo.getInstances().getmConfigConfigIpv6RegionTag();
            LogUtil.i("DeviceInfo", "DeviceInfo [handleProbeAndHarborRegion] [region \u5206\u88c2] mConfigConfigIpv6RegionTag= " + str2);
            setmRegion(this.mRegion + str2);
        }
        LogUtil.i("DeviceInfo", "DeviceInfo [handleProbeAndHarborRegion] [region \u5206\u88c2] mProbeRegion3= " + this.mProbeRegion + ", mRegion=" + this.mRegion);
    }

    public synchronized void clean() {
        sDeviceInfo = new DeviceInfo();
    }
}