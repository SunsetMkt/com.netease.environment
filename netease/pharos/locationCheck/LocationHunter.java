package com.netease.pharos.locationCheck;

import android.text.TextUtils;
import com.facebook.hermes.intl.Constants;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.pharos.config.CheckResult;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.network.NetworkStatus;
import com.netease.pharos.protocolCheck.ProtocolCheckListener;
import com.netease.pharos.protocolCheck.ProtocolCheckProxy;
import com.netease.pharos.util.LogUtil;
import java.util.Map;

/* loaded from: classes5.dex */
public class LocationHunter {
    private static final String TAG = "LocationHunter";
    private final ProtocolCheckListener mListener = new ProtocolCheckListener() { // from class: com.netease.pharos.locationCheck.LocationHunter.1
        @Override // com.netease.pharos.protocolCheck.ProtocolCheckListener
        public void callBack(CheckResult checkResult) {
            LogUtil.i(LocationHunter.TAG, "LocationHunter \u56de\u8c03\u7ed3\u679c=" + checkResult.toString());
            RecheckResult.getInstance().getList().add(checkResult);
        }
    };

    public DeviceInfo start() {
        LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5f00\u59cb");
        DeviceInfo deviceInfo = DeviceInfo.getInstance();
        LogUtil.i(TAG, "deviceInfo=" + deviceInfo.toString());
        String networkIsp = deviceInfo.getNetworkIsp();
        String network = NetworkStatus.getInstance().getNetwork();
        LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5224\u65ad\u7f51\u7edc\uff0cnetwork_isp=" + networkIsp + ", network=" + network);
        if (!TextUtils.isEmpty(networkIsp) && !TextUtils.isEmpty(network) && networkIsp.startsWith("460") && ConstProp.NT_AUTH_NAME_MOBILE.equals(network)) {
            deviceInfo.setmRegion("cn");
            deviceInfo.setmMethod("isp");
            LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5224\u65ad\u7f51\u7edc\uff0c\u7ed3\u679c=" + deviceInfo.getRegion() + ", \u65b9\u6cd5=" + deviceInfo.getmMethod());
            return deviceInfo;
        }
        String ipContinent = deviceInfo.getIpContinent();
        String ipCountry = deviceInfo.getIpCountry();
        String strIpHashMapGetValue = NetAreaInfo.getInstances().ipHashMapGetValue("continent", ipContinent);
        LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5224\u65adip\u5730\u5740 continent=" + ipContinent + ", country=" + ipCountry + ", continent=" + strIpHashMapGetValue);
        if (!TextUtils.isEmpty(strIpHashMapGetValue)) {
            deviceInfo.setmRegion(strIpHashMapGetValue);
            deviceInfo.setmMethod("ip-continent");
        }
        String strIpHashMapGetValue2 = NetAreaInfo.getInstances().ipHashMapGetValue(Const.COUNTRY, ipCountry);
        LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5224\u65adip\u5730\u5740 continent=" + ipContinent + ", country=" + ipCountry + ", country=" + strIpHashMapGetValue2);
        if (!TextUtils.isEmpty(strIpHashMapGetValue2)) {
            deviceInfo.setmRegion(strIpHashMapGetValue2);
            deviceInfo.setmMethod("ip-country");
        }
        if (!TextUtils.isEmpty(deviceInfo.getRegion())) {
            LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5224\u65adip\u5730\u5740\uff0c\u7ed3\u679c=" + deviceInfo.getRegion() + ", \u65b9\u6cd5=" + deviceInfo.getmMethod());
            return deviceInfo;
        }
        String areazoneContinent = deviceInfo.getAreazoneContinent();
        String areazoneCountry = deviceInfo.getAreazoneCountry();
        String strTimezonehashMapGetValue = NetAreaInfo.getInstances().timezonehashMapGetValue("continent", areazoneContinent);
        LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5224\u65ad\u65f6\u533a\uff08\u5730\u533a\uff09 zoneContinent=" + areazoneContinent + ", zoneCountry=" + areazoneCountry + ", continent=" + strTimezonehashMapGetValue);
        if (!TextUtils.isEmpty(strTimezonehashMapGetValue)) {
            deviceInfo.setmRegion(strTimezonehashMapGetValue);
            deviceInfo.setmMethod("areazone");
        }
        String strTimezonehashMapGetValue2 = NetAreaInfo.getInstances().timezonehashMapGetValue(Const.COUNTRY, areazoneCountry);
        LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5224\u65ad\u65f6\u533a\uff08\u5730\u533a\uff09 zoneContinent=" + areazoneContinent + ", zoneCountry=" + areazoneCountry + ", country=" + areazoneCountry);
        if (!TextUtils.isEmpty(strTimezonehashMapGetValue2)) {
            deviceInfo.setmRegion(strTimezonehashMapGetValue2);
            deviceInfo.setmMethod("areazone");
        }
        if (!TextUtils.isEmpty(deviceInfo.getRegion())) {
            LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5224\u65ad\u65f6\u533a\uff08\u5730\u533a\uff09\uff0c\u7ed3\u679c=" + deviceInfo.getRegion() + ", \u65b9\u6cd5=" + deviceInfo.getmMethod());
            return deviceInfo;
        }
        String timezone = deviceInfo.getTimezone();
        String strTimezonehashMapGetValue3 = NetAreaInfo.getInstances().timezonehashMapGetValue("timezone", timezone);
        LogUtil.i(TAG, "LocationHunter---\u521d\u6b65\u5224\u65ad---\u5224\u65ad\u65f6\u533a\uff08\u5730\u533a\uff09 timezone=" + timezone + ", netTimezone=" + strTimezonehashMapGetValue3);
        if (!TextUtils.isEmpty(strTimezonehashMapGetValue3)) {
            deviceInfo.setmRegion(strTimezonehashMapGetValue3);
            deviceInfo.setmMethod("timezone");
        } else {
            deviceInfo.setmRegion(NetAreaInfo.getInstances().timezonehashMapGetValue("timezone", Constants.COLLATION_DEFAULT));
            deviceInfo.setmMethod("timezone");
        }
        if (!TextUtils.isEmpty(deviceInfo.getRegion())) {
            LogUtil.i(TAG, "\u521d\u6b65\u5224\u65ad---\u5224\u65ad\u5730\u533a\u65f6\u533a\uff0c\u7ed3\u679c=" + deviceInfo.getRegion() + ", \u65b9\u6cd5=" + deviceInfo.getmMethod());
            StringBuilder sb = new StringBuilder("\u521d\u6b65\u5224\u65ad---\u5224\u65ad\u5730\u533a\u65f6\u533a\uff0c\u7ed3\u679c=");
            sb.append(deviceInfo);
            LogUtil.i(TAG, sb.toString());
            return deviceInfo;
        }
        deviceInfo.setmRegion("cn");
        deviceInfo.setmMethod(Constants.COLLATION_DEFAULT);
        LogUtil.i(TAG, "\u521d\u6b65\u5224\u65ad---\u51fa\u73b0\u5f02\u5e38\u8d70\u9ed8\u8ba4\uff0c\u7ed3\u679c=" + deviceInfo.getRegion() + ", \u65b9\u6cd5=" + deviceInfo.getmMethod());
        return deviceInfo;
    }

    public DeviceInfo checkRegion(DeviceInfo deviceInfo) throws NumberFormatException {
        String str;
        int i;
        LogUtil.i(TAG, "\u68c0\u9a8c\u5730\u533a---\u5f00\u59cb");
        String str2 = null;
        if (deviceInfo == null) {
            LogUtil.i(TAG, "\u68c0\u9a8c\u5730\u533a---\u53c2\u6570\u4e3anull");
            return null;
        }
        String region = deviceInfo.getRegion();
        String str3 = deviceInfo.getmMethod();
        LogUtil.i(TAG, "checkRegion method=" + str3);
        if ("isp".equals(str3)) {
            return deviceInfo;
        }
        String str4 = NetAreaInfo.getInstances().getmLocation();
        LogUtil.i(TAG, "checkRegion loaction=" + str4 + ", region=" + region);
        boolean z = "cn".equals(str4) && region.equals(str4);
        boolean z2 = "oversea".equals(str4) && !"cn".equals(region);
        LogUtil.i(TAG, "checkRegion cnMatch=" + z + ", overseaMatch=" + z2);
        if (!z && !z2) {
            Map<String, String> mudphashMap = NetAreaInfo.getInstances().getMudphashMap();
            ProtocolCheckProxy protocolCheckProxy = ProtocolCheckProxy.getInstance();
            for (String str5 : mudphashMap.keySet()) {
                String str6 = mudphashMap.get(str5);
                LogUtil.i(TAG, "ip=" + str6);
                String[] strArrSplit = str6.split(":");
                if (strArrSplit == null || strArrSplit.length <= 1) {
                    str = str2;
                    i = -1;
                } else {
                    String str7 = strArrSplit[0];
                    try {
                        i = Integer.parseInt(strArrSplit[1]);
                    } catch (Exception e) {
                        LogUtil.w(TAG, "\u89e3\u6790\u9519\u8bef Exception=" + e);
                        i = -1;
                    }
                    str = str7;
                }
                LogUtil.i(TAG, "pIp=" + str + ", pPort=" + i);
                if (str != null && -1 != i) {
                    LogUtil.i(TAG, "LocationHunter [checkRegion] \u53d1\u8d77udp\u63a2\u6d4b");
                    protocolCheckProxy.addProtocolCheckCore(2, str, i, NetAreaInfo.getInstances().getUdpCount(), 800, NetAreaInfo.getInstances().getPackageNum() * 32, str5, this.mListener, 0, null, null, null);
                }
                str2 = null;
            }
            LogUtil.i(TAG, "deviceInfo \u7ed3\u679c=" + deviceInfo);
        }
        return deviceInfo;
    }
}