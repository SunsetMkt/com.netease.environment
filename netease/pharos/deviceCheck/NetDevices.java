package com.netease.pharos.deviceCheck;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView;
import com.netease.pharos.network.NetworkStatus;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import com.tencent.connect.common.Constants;
import java.util.Locale;
import java.util.TimeZone;
import org.slf4j.Marker;

/* loaded from: classes5.dex */
public class NetDevices {
    private static final String TAG = "NetDevices";
    private static NetDevices sNetDevices;
    private Context mContext = null;

    public String getOsName() {
        return "android";
    }

    private NetDevices() {
    }

    public static NetDevices getInstances() {
        if (sNetDevices == null) {
            sNetDevices = new NetDevices();
        }
        return sNetDevices;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public int start() throws NumberFormatException {
        String networkIsp;
        String networkIspName;
        String str;
        String str2;
        String[] strArrSplit;
        int i;
        String networkType = getNetworkType();
        NetworkStatus.getInstance().setNetwork(networkType);
        if (TextUtils.isEmpty(networkType) || !ConstProp.NT_AUTH_NAME_MOBILE.equals(networkType)) {
            networkIsp = "00000";
            networkIspName = "";
        } else {
            networkIsp = getNetworkIsp();
            if (!TextUtils.isEmpty(networkIsp) && networkIsp.length() > 4) {
                networkIsp = networkIsp.substring(0, 5);
            }
            networkIspName = Util.getNetworkIspName(networkIsp);
        }
        String networkSignal = getNetworkSignal();
        String osName = getOsName();
        String osVer = getOsVer();
        String gateWay = getGateWay();
        String countryCode = getCountryCode();
        String timeZone = getTimeZone();
        if (timeZone != null && timeZone.contains(Marker.ANY_NON_NULL_MARKER) && timeZone.contains(":") && (strArrSplit = timeZone.split("\\+|\\:")) != null && strArrSplit.length > 2) {
            try {
                i = Integer.parseInt(strArrSplit[1]);
            } catch (Exception unused) {
                i = 100;
            }
            timeZone = Marker.ANY_NON_NULL_MARKER + i;
        }
        String[] strArrSplit2 = getAreaZone().split("/");
        if (strArrSplit2 == null || strArrSplit2.length <= 1) {
            str = null;
            str2 = null;
        } else {
            str = strArrSplit2[0];
            str2 = strArrSplit2[1];
        }
        DeviceInfo.getInstance().setGateway(gateWay);
        DeviceInfo.getInstance().setTimezone(timeZone);
        DeviceInfo.getInstance().setAreazoneContinent(str);
        DeviceInfo.getInstance().setAreazoneCountry(str2);
        DeviceInfo.getInstance().setNetworkIsp(networkIsp);
        DeviceInfo.getInstance().setNetworkIspName(networkIspName);
        DeviceInfo.getInstance().setNetworkSignal(networkSignal);
        DeviceInfo.getInstance().setmCountryCode(countryCode);
        DeviceInfo.getInstance().setIpLocalAddrV6(Util.getLocalIpv6());
        DeviceInfo.getInstance().setOsName(osName);
        DeviceInfo.getInstance().setOsVer(osVer);
        StringBuffer stringBuffer = new StringBuffer("network=");
        stringBuffer.append(networkType).append("\nnetwork_isp=");
        stringBuffer.append(networkIsp).append("\nnetwork_isp_name=");
        stringBuffer.append(networkIspName).append("\nnetwork_signal=");
        stringBuffer.append(networkSignal).append("\nos_name=");
        stringBuffer.append(osName).append("\nos_ver=");
        stringBuffer.append(osVer).append("\ngateway=");
        stringBuffer.append(gateWay).append("\ntimezone=");
        stringBuffer.append(timeZone).append("\nareazone_continent=");
        stringBuffer.append(str).append("\nareazone_country=");
        stringBuffer.append(str2).append("\n");
        LogUtil.i(TAG, "\u7ed3\u679c=" + ((Object) stringBuffer));
        return 0;
    }

    public String getTimeZone() {
        String displayName = TimeZone.getDefault().getDisplayName(false, 0);
        LogUtil.i(TAG, "\u7f51\u7edc\u76d1\u63a7\u6a21\u5757---\u65f6\u5dee=" + displayName);
        return displayName;
    }

    public String getAreaZone() {
        String id = TimeZone.getDefault().getID();
        LogUtil.i(TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u5730\u533a=" + id);
        return id;
    }

    public String getOsVer() {
        return Build.VERSION.RELEASE;
    }

    public String getCountryCode() {
        try {
            return Locale.getDefault().getCountry();
        } catch (Exception e) {
            LogUtil.w(TAG, "NetDevices [getCountryCode] Exception = " + e);
            e.printStackTrace();
            return Constants.APP_VERSION_UNKNOWN;
        }
    }

    public String getNetworkSignal() {
        return Util.getSystemParams("getWifiSignal");
    }

    public String getNetworkIsp() {
        LogUtil.i(TAG, "getNetworkSignal sdk level = " + Build.VERSION.SDK_INT);
        if (this.mContext == null) {
            return "00000";
        }
        String systemParams = Util.getSystemParams("getImsi");
        LogUtil.i(TAG, "getNetworkSignal IMSI = " + systemParams);
        return systemParams;
    }

    public static String getGateWay() {
        LogUtil.i(TAG, "NetDevices [getGateWay] start");
        String systemParams = Util.getSystemParams("getGateWay");
        LogUtil.i(TAG, "NetDevices [getGateWay] gateWayIp=" + systemParams);
        return systemParams;
    }

    public String getNetworkType() {
        String systemParams = Util.getSystemParams(UniWebView.ACTION_GETNETWORKTYPE);
        LogUtil.i(TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---Network Type[netDevices]: " + systemParams);
        return systemParams;
    }
}