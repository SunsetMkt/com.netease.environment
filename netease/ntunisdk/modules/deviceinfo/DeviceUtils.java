package com.netease.ntunisdk.modules.deviceinfo;

import android.app.ActivityManager;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.LocaleList;
import android.os.Process;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import com.netease.download.Const;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.deviceinfo.GaidUtils;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.tencent.connect.common.Constants;
import com.wali.gamecenter.report.ReportOrigin;
import com.xiaomi.onetrack.b.a;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;
import yangchaoyue.yangchaoyue.yangchaoyue.yangchaoyue.yangchaoyue.p006do.b;

/* loaded from: classes.dex */
public class DeviceUtils {
    private static final String DEFAULT = "default";
    private static final String TAG = "UNISDK DeviceUtils";
    private static String androidId = "default";
    public static String appChannel = "unknown";
    private static int appVersionCode = -1;
    private static String appVersionName = null;
    private static String cellID = "default";
    public static NetworkCapabilities commonNetworkCapabilities = null;
    public static NetworkInfo commonNetworkInfo = null;
    public static WifiInfo commonWifiInfo = null;
    public static boolean hasRefreshWifiList = false;
    private static String imei = "default";
    private static String imsi = "default";
    public static boolean isFallbackNetReceiver = false;
    private static boolean isGetCommonWifiInfoFirst = true;
    private static boolean isGetWifiList = true;
    public static boolean isOversea = false;
    public static boolean isReqLocation = false;
    private static String macAddress = "default";
    private static String macIp = "default";
    private static String netIp = "default";
    private static String sTransid = null;
    private static String serial = null;
    private static String simCountry = "default";
    private static String systemLanguage = "";
    private static String timeArea = "default";
    private static String timeZone = "default";
    private static String wifiListJson = "default";

    public interface GaidCallback extends GaidUtils.Callback {
    }

    public static synchronized void setImei(String str) {
        if (TextUtils.isEmpty(imei) || "default".equalsIgnoreCase(imei)) {
            imei = str;
        }
    }

    public static synchronized void setImsi(String str) {
        if (TextUtils.isEmpty(imsi) || "default".equalsIgnoreCase(imsi)) {
            imsi = str;
        }
    }

    public static synchronized void setAndroidId(String str) {
        if (TextUtils.isEmpty(androidId) || "default".equalsIgnoreCase(androidId)) {
            androidId = str;
        }
    }

    public static synchronized void setMacAddress(String str) {
        if (TextUtils.isEmpty(macAddress) || "default".equalsIgnoreCase(macAddress)) {
            macAddress = str;
        }
    }

    public static synchronized void setTimeZone(String str) {
        if (TextUtils.isEmpty(timeZone) || "default".equalsIgnoreCase(timeZone)) {
            timeZone = str;
        }
    }

    public static synchronized void setTimeArea(String str) {
        if (TextUtils.isEmpty(timeArea) || "default".equalsIgnoreCase(timeArea)) {
            timeArea = str;
        }
    }

    public static synchronized String getTransid(Context context, boolean z, boolean z2) {
        LogModule.d(TAG, "DeviceUtils [getTransid]");
        if (context == null) {
            LogModule.w(TAG, "DeviceUtils [getTransid] ctx is null");
            return sTransid != null ? sTransid : "unknow";
        }
        if (TextUtils.isEmpty(sTransid)) {
            sTransid = getDeviceUDID(context, z, z2) + "_" + System.currentTimeMillis() + "_" + String.format(Locale.US, "%09d", Integer.valueOf(new Random().nextInt(1000000000)));
        }
        LogModule.d(TAG, "DeviceUtils [getTransid] sTransid=" + sTransid);
        return sTransid;
    }

    /* JADX WARN: Removed duplicated region for block: B:62:0x004a A[Catch: all -> 0x0066, TRY_LEAVE, TryCatch #1 {, blocks: (B:40:0x0003, B:46:0x000d, B:48:0x0017, B:61:0x0047, B:62:0x004a, B:49:0x001e, B:51:0x0028, B:53:0x002c, B:55:0x0030, B:56:0x0039, B:58:0x003d), top: B:70:0x0003, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized java.lang.String getMacAddress(android.content.Context r3) {
        /*
            java.lang.Class<com.netease.ntunisdk.modules.deviceinfo.DeviceUtils> r0 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.class
            monitor-enter(r0)
            boolean r1 = com.netease.ntunisdk.modules.deviceinfo.DeviceInfoModule.disableAccessData     // Catch: java.lang.Throwable -> L66
            if (r1 == 0) goto Lb
            java.lang.String r3 = "unknown"
            monitor-exit(r0)
            return r3
        Lb:
            if (r3 == 0) goto L4a
            java.lang.String r1 = "default"
            java.lang.String r2 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.macAddress     // Catch: java.lang.Throwable -> L66
            boolean r1 = r1.equalsIgnoreCase(r2)     // Catch: java.lang.Throwable -> L66
            if (r1 == 0) goto L4a
            java.lang.String r1 = "UNISDK DeviceUtils"
            java.lang.String r2 = "getMacAddress..."
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r1, r2)     // Catch: java.lang.Throwable -> L66
            java.lang.String r1 = "wifi"
            java.lang.Object r3 = r3.getSystemService(r1)     // Catch: java.lang.Throwable -> L46
            android.net.wifi.WifiManager r3 = (android.net.wifi.WifiManager) r3     // Catch: java.lang.Throwable -> L46
            if (r3 == 0) goto L4a
            android.net.wifi.WifiInfo r1 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.commonWifiInfo     // Catch: java.lang.Throwable -> L46
            if (r1 != 0) goto L39
            boolean r1 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.isGetCommonWifiInfoFirst     // Catch: java.lang.Throwable -> L46
            if (r1 == 0) goto L39
            android.net.wifi.WifiInfo r3 = com.netease.ntunisdk.modules.personalinfolist.HookManager.getConnectionInfo(r3)     // Catch: java.lang.Throwable -> L46
            com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.commonWifiInfo = r3     // Catch: java.lang.Throwable -> L46
            r3 = 0
            com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.isGetCommonWifiInfoFirst = r3     // Catch: java.lang.Throwable -> L46
        L39:
            android.net.wifi.WifiInfo r3 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.commonWifiInfo     // Catch: java.lang.Throwable -> L46
            if (r3 == 0) goto L4a
            android.net.wifi.WifiInfo r3 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.commonWifiInfo     // Catch: java.lang.Throwable -> L46
            java.lang.String r3 = r3.getMacAddress()     // Catch: java.lang.Throwable -> L46
            com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.macAddress = r3     // Catch: java.lang.Throwable -> L46
            goto L4a
        L46:
            r3 = move-exception
            r3.printStackTrace()     // Catch: java.lang.Throwable -> L66
        L4a:
            java.lang.String r3 = "UNISDK DeviceUtils"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L66
            r1.<init>()     // Catch: java.lang.Throwable -> L66
            java.lang.String r2 = "macAddr: "
            r1.append(r2)     // Catch: java.lang.Throwable -> L66
            java.lang.String r2 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.macAddress     // Catch: java.lang.Throwable -> L66
            r1.append(r2)     // Catch: java.lang.Throwable -> L66
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Throwable -> L66
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r3, r1)     // Catch: java.lang.Throwable -> L66
            java.lang.String r3 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.macAddress     // Catch: java.lang.Throwable -> L66
            monitor-exit(r0)
            return r3
        L66:
            r3 = move-exception
            monitor-exit(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.getMacAddress(android.content.Context):java.lang.String");
    }

    @Deprecated
    static synchronized String getLocalIpAddress(Context context) {
        if (context != null) {
            try {
                if (!"default".equalsIgnoreCase(macIp)) {
                    return macIp;
                }
                if (!"default".equalsIgnoreCase(netIp)) {
                    return netIp;
                }
                WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                if (wifiManager != null && wifiManager.isWifiEnabled()) {
                    if (commonWifiInfo == null && isGetCommonWifiInfoFirst) {
                        commonWifiInfo = HookManager.getConnectionInfo(wifiManager);
                        isGetCommonWifiInfoFirst = false;
                    }
                    if (commonWifiInfo != null) {
                        macIp = formatIpAddress(commonWifiInfo.getIpAddress());
                        if (TextUtils.isEmpty(macIp)) {
                            macIp = "127.0.0.1";
                        }
                    } else {
                        macIp = "127.0.0.1";
                    }
                    return macIp;
                }
                String hostAddress = "127.0.0.1";
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                    while (true) {
                        if (inetAddresses.hasMoreElements()) {
                            InetAddress inetAddressNextElement = inetAddresses.nextElement();
                            if (!inetAddressNextElement.isLoopbackAddress()) {
                                hostAddress = inetAddressNextElement.getHostAddress();
                                break;
                            }
                        }
                    }
                }
                if (TextUtils.isEmpty(hostAddress)) {
                    hostAddress = "127.0.0.1";
                }
                netIp = hostAddress;
                return netIp;
            } catch (Exception unused) {
            }
        }
        return "127.0.0.1";
    }

    private static String formatIpAddress(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    public static synchronized String getMobileIMSI(Context context) {
        if (DeviceInfoModule.disableAccessData) {
            return "unknown";
        }
        if (!"default".equalsIgnoreCase(imsi)) {
            return imsi;
        }
        if (context == null) {
            return "";
        }
        boolean z = true;
        if (Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
            z = false;
        }
        if (!z) {
            return "";
        }
        String subscriberId = "";
        try {
            subscriberId = ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        } catch (Exception e) {
            LogModule.d(TAG, "getMobileIMSI: " + e.getMessage());
        }
        if (subscriberId == null) {
            subscriberId = "";
        }
        LogModule.d(TAG, "getMobileIMSI, IMSI=" + subscriberId);
        imsi = subscriberId;
        return subscriberId;
    }

    public static synchronized String getMobileIMEI(Context context) {
        if (DeviceInfoModule.disableAccessData) {
            return "unknown";
        }
        if (!"default".equalsIgnoreCase(imei)) {
            return imei;
        }
        if (context == null) {
            return "";
        }
        boolean z = true;
        if (Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission("android.permission.READ_PHONE_STATE") != 0) {
            z = false;
        }
        if (!z) {
            return "";
        }
        String deviceId = "";
        if (context != null) {
            try {
                deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
            } catch (Exception e) {
                LogModule.d(TAG, "getMobileIMEI: " + e.getMessage());
            }
        }
        if (deviceId == null) {
            deviceId = "";
        }
        LogModule.d(TAG, "getMobileIMEI, IMEI=" + deviceId);
        imei = deviceId;
        return deviceId;
    }

    public static synchronized String getDeviceUDID(Context context, boolean z, boolean z2) {
        LogModule.i(TAG, "isEnableFake: " + z2);
        if (z2) {
            String cachedFakeGaid = GaidUtils.getCachedFakeGaid(context);
            if (!TextUtils.isEmpty(cachedFakeGaid)) {
                return cachedFakeGaid;
            }
            return GaidUtils.getRandomUDID(context);
        }
        return getDeviceUDID(context, z);
    }

    static synchronized String getDeviceUDID(Context context, boolean z) {
        LogModule.i(TAG, "toGetGaid: " + z);
        return z ? getUnisdkDeviceId(context, z, (GaidCallback) null) : getAndroidId(context);
    }

    public static synchronized String getAndroidId(Context context) {
        if (context == null) {
            return "unknown";
        }
        if ("default".equalsIgnoreCase(androidId)) {
            try {
                String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
                if (TextUtils.isEmpty(string)) {
                    string = "unknown";
                }
                androidId = string;
            } catch (Exception e) {
                LogModule.e(TAG, "getAndroidId: " + e.getMessage());
                androidId = "unknown";
            }
            return androidId;
        }
        return androidId;
    }

    public static synchronized String getUnisdkDeviceId(Context context, boolean z, boolean z2) {
        LogModule.i(TAG, "isEnableFake: " + z2);
        if (z2) {
            return getFakeUnisdkDeviceId(context, z, null);
        }
        return getUnisdkDeviceId(context, z);
    }

    public static synchronized String getUnisdkDeviceId(Context context, boolean z) {
        return getUnisdkDeviceId(context, z, (GaidCallback) null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x0065, code lost:
    
        r5 = r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static synchronized java.lang.String getUnisdkDeviceId(android.content.Context r5, boolean r6, com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.GaidCallback r7) {
        /*
            java.lang.Class<com.netease.ntunisdk.modules.deviceinfo.DeviceUtils> r0 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.class
            monitor-enter(r0)
            java.lang.String r1 = ""
            if (r5 != 0) goto L12
            java.lang.String r5 = "UNISDK DeviceUtils"
            java.lang.String r6 = "context is null"
            com.netease.ntunisdk.modules.base.utils.LogModule.d(r5, r6)     // Catch: java.lang.Throwable -> L67
            java.lang.String r5 = "unknown"
            monitor-exit(r0)
            return r5
        L12:
            java.lang.String r2 = "UNISDK DeviceUtils"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L67
            r3.<init>()     // Catch: java.lang.Throwable -> L67
            java.lang.String r4 = "toGetGaid: "
            r3.append(r4)     // Catch: java.lang.Throwable -> L67
            r3.append(r6)     // Catch: java.lang.Throwable -> L67
            java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L67
            com.netease.ntunisdk.modules.base.utils.LogModule.i(r2, r3)     // Catch: java.lang.Throwable -> L67
            boolean r2 = com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.isOversea     // Catch: java.lang.Throwable -> L60
            if (r2 != 0) goto L34
            if (r6 == 0) goto L2f
            goto L34
        L2f:
            java.lang.String r5 = getAndroidId(r5)     // Catch: java.lang.Throwable -> L60
            goto L65
        L34:
            java.lang.String r1 = com.netease.ntunisdk.modules.deviceinfo.GaidUtils.getCachedGaid(r7)     // Catch: java.lang.Throwable -> L60
            java.lang.String r6 = "UNISDK DeviceUtils"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L60
            r7.<init>()     // Catch: java.lang.Throwable -> L60
            java.lang.String r2 = "getUnisdkDeviceId getCachedGaid: "
            r7.append(r2)     // Catch: java.lang.Throwable -> L60
            r7.append(r1)     // Catch: java.lang.Throwable -> L60
            java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> L60
            com.netease.ntunisdk.modules.base.utils.LogModule.i(r6, r7)     // Catch: java.lang.Throwable -> L60
            if (r1 != 0) goto L53
            java.lang.String r5 = "unknown"
            goto L65
        L53:
            java.lang.String r6 = ""
            boolean r6 = r6.equals(r1)     // Catch: java.lang.Throwable -> L60
            if (r6 == 0) goto L64
            java.lang.String r5 = getAndroidId(r5)     // Catch: java.lang.Throwable -> L60
            goto L65
        L60:
            r5 = move-exception
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L67
        L64:
            r5 = r1
        L65:
            monitor-exit(r0)
            return r5
        L67:
            r5 = move-exception
            monitor-exit(r0)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.getUnisdkDeviceId(android.content.Context, boolean, com.netease.ntunisdk.modules.deviceinfo.DeviceUtils$GaidCallback):java.lang.String");
    }

    static synchronized String getFakeUnisdkDeviceId(Context context, boolean z, GaidCallback gaidCallback) {
        String cachedFakeGaid = "";
        if (context == null) {
            LogModule.d(TAG, "context is null");
            return "unknown";
        }
        LogModule.i(TAG, "toGetGaid: " + z);
        try {
            cachedFakeGaid = GaidUtils.getCachedFakeGaid(context, gaidCallback);
            if (!TextUtils.isEmpty(cachedFakeGaid)) {
                return cachedFakeGaid;
            }
            return GaidUtils.getRandomUDID(context);
        } catch (Throwable th) {
            th.printStackTrace();
            return cachedFakeGaid;
        }
    }

    private static synchronized boolean isInvalidImei(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        char cCharAt = str.charAt(0);
        for (int i = 0; i != str.length(); i++) {
            if (str.charAt(i) != cCharAt) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDomestic(Context context) {
        return (GaidUtils.hasInstalledGooglePlayServices(context) || GaidUtils.hasInstalledAmazonAdvertisingId(context)) ? false : true;
    }

    public static synchronized String getTimeZone() {
        String[] strArrSplit;
        if (!"default".equalsIgnoreCase(timeZone)) {
            return timeZone;
        }
        try {
            String displayName = TimeZone.getDefault().getDisplayName(false, 0);
            if (displayName != null && displayName.contains(Marker.ANY_NON_NULL_MARKER) && displayName.contains(":") && (strArrSplit = displayName.split("\\+|\\:")) != null && strArrSplit.length > 2) {
                int iIntValue = 100;
                try {
                    iIntValue = Integer.valueOf(strArrSplit[1]).intValue();
                } catch (Exception unused) {
                }
                displayName = Marker.ANY_NON_NULL_MARKER + iIntValue;
            }
            timeZone = displayName;
        } catch (Exception e) {
            LogModule.d(TAG, "getTimeZone: " + e.getMessage());
        }
        if (timeZone == null) {
            timeZone = "";
        }
        LogModule.d(TAG, "getTimeZone, TimeZone=" + timeZone);
        return timeZone;
    }

    public static synchronized String getAreaZone() {
        if (!"default".equalsIgnoreCase(timeArea)) {
            return timeArea;
        }
        String id = "";
        try {
            id = TimeZone.getDefault().getID();
            timeArea = id;
        } catch (Exception e) {
            LogModule.d(TAG, "getAreaZone: " + e.getMessage());
        }
        if (id == null) {
            id = "";
            timeArea = "";
        }
        LogModule.d(TAG, "getAreaZone, TimeArea=" + id);
        return id;
    }

    static synchronized String getWifiSignal(Context context) {
        int iCalculateSignalLevel;
        iCalculateSignalLevel = -1;
        if (context != null) {
            try {
                WifiInfo connectionInfo = HookManager.getConnectionInfo((WifiManager) context.getSystemService("wifi"));
                if (connectionInfo != null) {
                    iCalculateSignalLevel = WifiManager.calculateSignalLevel(connectionInfo.getRssi(), 5);
                }
                LogModule.d(TAG, "getWifiSignal, signalLevel=" + iCalculateSignalLevel);
            } catch (Exception e) {
                LogModule.d(TAG, "getWifiSignal, exeption=" + e.getMessage());
            }
        } else {
            LogModule.d(TAG, "getWifiSignal, signalLevel=" + iCalculateSignalLevel);
        }
        return String.valueOf(iCalculateSignalLevel);
    }

    static synchronized String getCellId(Context context) {
        int systemId;
        if (!"default".equalsIgnoreCase(cellID)) {
            return cellID;
        }
        int i = -1;
        if (context == null) {
            LogModule.d(TAG, "Util [getCellId] context is null");
            cellID = String.valueOf(-1);
            return cellID;
        }
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(0);
            if (networkInfo != null && networkInfo.isConnected()) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
                CellLocation cellLocation = telephonyManager.getCellLocation();
                LogModule.d(TAG, "getCellId nPhoneType=" + telephonyManager.getPhoneType());
                if (cellLocation instanceof GsmCellLocation) {
                    systemId = ((GsmCellLocation) cellLocation).getCid();
                    if (systemId <= 0 || systemId == 65535) {
                        systemId = -1;
                    }
                } else if (cellLocation instanceof CdmaCellLocation) {
                    systemId = ((CdmaCellLocation) cellLocation).getSystemId();
                }
                i = systemId;
            }
        } catch (Exception e) {
            LogModule.d(TAG, "getCellId Exception = " + e);
        }
        LogModule.d(TAG, "cellId=" + i);
        cellID = String.valueOf(i);
        return cellID;
    }

    static synchronized String getGateWay(Context context) {
        LogModule.d(TAG, "getGateWay start");
        try {
        } catch (Exception e) {
            LogModule.d(TAG, "getGateWay e=" + e.getMessage());
        }
        if (context == null) {
            LogModule.d(TAG, "getGateWay param error");
            return null;
        }
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        strIntToIp = wifiManager != null ? Utils.intToIp(wifiManager.getDhcpInfo().gateway) : null;
        LogModule.d(TAG, "getGateWay gateWayIp=" + strIntToIp);
        return strIntToIp;
    }

    static synchronized String getNetworkType(Context context) {
        ConnectivityManager connectivityManager;
        String str = "unknown";
        try {
        } catch (Exception e) {
            LogModule.d(TAG, "getNetworkType Exception=" + e.getMessage());
        }
        if (context == null) {
            LogModule.d(TAG, "getNetworkType mContext is null");
            return "unknown";
        }
        if (useNetworkCapabilities()) {
            if (commonNetworkCapabilities == null) {
                ConnectivityManager connectivityManager2 = (ConnectivityManager) context.getSystemService("connectivity");
                commonNetworkCapabilities = connectivityManager2.getNetworkCapabilities(connectivityManager2.getActiveNetwork());
            }
            if (commonNetworkCapabilities != null && commonNetworkCapabilities.hasCapability(16)) {
                if (commonNetworkCapabilities.hasTransport(1)) {
                    str = "wifi";
                } else if (commonNetworkCapabilities.hasTransport(0)) {
                    str = ConstProp.NT_AUTH_NAME_MOBILE;
                }
            }
        } else {
            if (commonNetworkInfo == null && (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) != null) {
                commonNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }
            if (commonNetworkInfo != null && commonNetworkInfo.isConnected()) {
                int type = commonNetworkInfo.getType();
                if (1 == type) {
                    str = "wifi";
                } else if (type == 0 || 4 == type || 2 == type || 5 == type || 3 == type) {
                    str = ConstProp.NT_AUTH_NAME_MOBILE;
                }
            }
        }
        LogModule.d(TAG, "getNetworkType Network Type : " + str);
        return str;
    }

    static synchronized String getNetworkType2(Context context, boolean z) {
        ConnectivityManager connectivityManager;
        String str;
        String str2 = "unknown";
        try {
        } catch (Exception e) {
            LogModule.d(TAG, "getNetworkType2 Exception=" + e.getMessage());
        }
        if (context == null) {
            LogModule.d(TAG, "getNetworkType2 mContext is null");
            return "unknown";
        }
        if (useNetworkCapabilities() && (commonNetworkCapabilities == null || z)) {
            ConnectivityManager connectivityManager2 = (ConnectivityManager) context.getSystemService("connectivity");
            commonNetworkCapabilities = connectivityManager2.getNetworkCapabilities(connectivityManager2.getActiveNetwork());
        }
        if ((commonNetworkInfo == null || z) && (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) != null) {
            commonNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (commonNetworkInfo != null) {
            if (useNetworkCapabilities() && commonNetworkCapabilities != null) {
                if (commonNetworkCapabilities.hasCapability(16)) {
                    int type = commonNetworkInfo.getType();
                    int subtype = commonNetworkInfo.getSubtype();
                    LogModule.d(TAG, "getNetworkType2 type=" + type + ", subType=" + subtype);
                    if (commonNetworkCapabilities.hasTransport(1)) {
                        str = "wifi";
                    } else if (type == 0) {
                        if (subtype != 20) {
                            switch (subtype) {
                                case 1:
                                case 2:
                                case 4:
                                case 7:
                                case 11:
                                    str = b.d;
                                    break;
                                case 3:
                                case 5:
                                case 6:
                                case 8:
                                case 9:
                                case 10:
                                case 12:
                                case 14:
                                case 15:
                                    str = b.c;
                                    break;
                                case 13:
                                    str = b.b;
                                    break;
                                default:
                                    str = "unknown";
                                    break;
                            }
                        } else {
                            str = "5G";
                        }
                    }
                    str2 = str;
                } else {
                    str2 = "unconnected";
                }
            } else if (commonNetworkInfo.isConnected()) {
                int type2 = commonNetworkInfo.getType();
                int subtype2 = commonNetworkInfo.getSubtype();
                LogModule.d(TAG, "getNetworkType2 type=" + type2 + ", subType=" + subtype2);
                if (1 == type2) {
                    str2 = "wifi";
                } else if (type2 == 0) {
                    if (subtype2 != 20) {
                        switch (subtype2) {
                            case 1:
                            case 2:
                            case 4:
                            case 7:
                            case 11:
                                str2 = b.d;
                                break;
                            case 3:
                            case 5:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 14:
                            case 15:
                                str2 = b.c;
                                break;
                            case 13:
                                str2 = b.b;
                                break;
                            default:
                                str2 = "unknown";
                                break;
                        }
                    } else {
                        str2 = "5G";
                    }
                }
            } else {
                str2 = "unconnected";
            }
        }
        LogModule.d(TAG, "getNetworkType2 Network Type : " + str2);
        return str2;
    }

    static synchronized String getNetworkInfoJson(Context context) {
        JSONObject jSONObject;
        ConnectivityManager connectivityManager;
        String string = "{}";
        try {
            jSONObject = new JSONObject();
            jSONObject.put("methodId", "getNetworkInfoJson");
        } catch (Exception e) {
            String str = "getNetworkInfoJson Exception=" + e.getMessage();
            LogModule.d(TAG, str);
            try {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("methodId", "getNetworkInfoJson");
                jSONObject2.put(RespUtil.UniSdkField.RESP_CODE, 10000);
                jSONObject2.put(RespUtil.UniSdkField.RESP_MSG, str);
                string = jSONObject2.toString();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (context == null) {
            LogModule.d(TAG, "getNetworkInfoJson mContext is null");
            jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 2);
            jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "getNetworkInfoJson mContext is null");
            return jSONObject.toString();
        }
        if (useNetworkCapabilities() && commonNetworkCapabilities == null) {
            ConnectivityManager connectivityManager2 = (ConnectivityManager) context.getSystemService("connectivity");
            commonNetworkCapabilities = connectivityManager2.getNetworkCapabilities(connectivityManager2.getActiveNetwork());
        }
        if (commonNetworkInfo == null && (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) != null) {
            commonNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (commonNetworkInfo != null) {
            jSONObject.put("getSubtypeName", commonNetworkInfo.getSubtypeName());
            jSONObject.put("getType", commonNetworkInfo.getType());
            jSONObject.put("getDetailedState", commonNetworkInfo.getDetailedState());
            if (useNetworkCapabilities() && commonNetworkCapabilities != null) {
                jSONObject.put("isConnected", commonNetworkCapabilities.hasCapability(16));
            } else {
                jSONObject.put("isConnected", commonNetworkInfo.isConnected());
            }
            jSONObject.put("getSubtype", commonNetworkInfo.getSubtype());
            jSONObject.put("isAvailable", commonNetworkInfo.isAvailable());
            jSONObject.put("getTypeName", commonNetworkInfo.getTypeName());
            jSONObject.put("getState", commonNetworkInfo.getState());
            jSONObject.put("isConnectedOrConnecting", commonNetworkInfo.isConnectedOrConnecting());
            jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 0);
            jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "success");
        } else {
            jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 5);
            jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "commonNetworkInfo is null, may be the network is disconnected");
        }
        string = jSONObject.toString();
        LogModule.d(TAG, "getNetworkInfoJson Network Info Json : " + string);
        return string;
    }

    /* JADX WARN: Failed to find 'out' block for switch in B:119:0x0084. Please report as an issue. */
    static synchronized String ntGetNetworktype(Context context) {
        String subtypeName;
        String str;
        ConnectivityManager connectivityManager;
        String str2 = "unknown";
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (context == null) {
            LogModule.d("DCTOOL", "\u68c0\u67e5\u4e0b\u521d\u59cb\u5316\u65f6\u4f20\u5165\u7684AppContext\u662f\u5426\u4e3anull\uff0c\u4e00\u822c\u662f\u5728\u8c03\u7528\u65f6\u95f4\u8fc7\u65e9\u5bfc\u81f4AppContext\u4e3anull\u5bfc\u81f4\u7684\u3002\u8fd9\u79cd\u60c5\u51b5\u53ef\u4ee5\u5ffd\u7565\uff1b\u6216\u8005\u8c03\u6574\u4e0b\u521d\u59cb\u5316\u7684\u8c03\u7528\u65f6\u673a\uff1b\u6216\u8005\u589e\u52a0\u5224\u65ad\uff0c\u5728AppContext\u4e3anull\u7684\u65f6\u5019\uff0c\u4e0d\u8981\u8c03\u7528\u8bca\u65ad\u3002");
            return "unknown";
        }
        if (useNetworkCapabilities() && commonNetworkCapabilities == null) {
            ConnectivityManager connectivityManager2 = (ConnectivityManager) context.getSystemService("connectivity");
            commonNetworkCapabilities = connectivityManager2.getNetworkCapabilities(connectivityManager2.getActiveNetwork());
        }
        if (commonNetworkInfo == null && (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) != null) {
            commonNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (useNetworkCapabilities() && commonNetworkCapabilities != null) {
            if (commonNetworkInfo != null && commonNetworkCapabilities.hasCapability(16)) {
                String typeName = commonNetworkInfo.getTypeName();
                if (commonNetworkCapabilities.hasTransport(1)) {
                    str = "wifi";
                } else if (typeName.equalsIgnoreCase("MOBILE")) {
                    int subtype = commonNetworkInfo.getSubtype();
                    subtypeName = commonNetworkInfo.getSubtypeName();
                    if (subtype != 20) {
                        switch (subtype) {
                            case 1:
                            case 2:
                            case 4:
                            case 7:
                            case 11:
                                str = b.d;
                                break;
                            case 3:
                            case 5:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 14:
                            case 15:
                                str = b.c;
                                break;
                            case 13:
                                str = b.b;
                                break;
                            default:
                                if (!subtypeName.equalsIgnoreCase("TD-SCDMA")) {
                                    if (!subtypeName.equalsIgnoreCase("WCDMA")) {
                                        if (subtypeName.equalsIgnoreCase("CDMA2000")) {
                                        }
                                        str2 = subtypeName;
                                        break;
                                    }
                                }
                                str = b.c;
                                break;
                        }
                    } else {
                        str = "5G";
                    }
                }
                str2 = str;
            }
        } else if (commonNetworkInfo != null && commonNetworkInfo.isConnected()) {
            String typeName2 = commonNetworkInfo.getTypeName();
            if (typeName2.equalsIgnoreCase("WIFI")) {
                str2 = "wifi";
            } else if (typeName2.equalsIgnoreCase("MOBILE")) {
                int subtype2 = commonNetworkInfo.getSubtype();
                subtypeName = commonNetworkInfo.getSubtypeName();
                if (subtype2 != 20) {
                    switch (subtype2) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            str2 = b.d;
                            break;
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 14:
                        case 15:
                            str2 = b.c;
                            break;
                        case 13:
                            str2 = b.b;
                            break;
                        default:
                            if (!subtypeName.equalsIgnoreCase("TD-SCDMA") && !subtypeName.equalsIgnoreCase("WCDMA") && !subtypeName.equalsIgnoreCase("CDMA2000")) {
                                str2 = subtypeName;
                                break;
                            } else {
                                str2 = b.c;
                                break;
                            }
                            break;
                    }
                } else {
                    str2 = "5G";
                }
            }
        }
        LogModule.d("DCTOOL", "Getting NETWORK_TYPE in java : " + str2);
        return str2;
    }

    static synchronized String getNetworkType4Downloader(Context context, boolean z) {
        ConnectivityManager connectivityManager;
        String str;
        String str2 = "unknown";
        try {
        } catch (Exception e) {
            LogModule.d(TAG, "getNetworkType4Downloader Exception=" + e.getMessage());
        }
        if (context == null) {
            LogModule.d(TAG, "getNetworkType4Downloader mContext is null");
            return "unknown";
        }
        if (useNetworkCapabilities()) {
            if (commonNetworkCapabilities == null || z) {
                ConnectivityManager connectivityManager2 = (ConnectivityManager) context.getSystemService("connectivity");
                commonNetworkCapabilities = connectivityManager2.getNetworkCapabilities(connectivityManager2.getActiveNetwork());
            }
            if (commonNetworkCapabilities != null && commonNetworkCapabilities.hasCapability(16)) {
                if (commonNetworkCapabilities.hasTransport(1)) {
                    str = "wifi";
                } else if (commonNetworkCapabilities.hasTransport(0)) {
                    str = ConstProp.NT_AUTH_NAME_MOBILE;
                } else if (commonNetworkCapabilities.hasTransport(3)) {
                    str = "lan";
                }
                str2 = str;
            }
        } else {
            if ((commonNetworkInfo == null || z) && (connectivityManager = (ConnectivityManager) context.getSystemService("connectivity")) != null) {
                commonNetworkInfo = connectivityManager.getActiveNetworkInfo();
            }
            if (commonNetworkInfo != null && commonNetworkInfo.isConnected()) {
                int type = commonNetworkInfo.getType();
                if (1 == type) {
                    str = "wifi";
                } else if (type == 0 || 4 == type || 2 == type || 5 == type || 3 == type) {
                    str = ConstProp.NT_AUTH_NAME_MOBILE;
                } else if (9 == type) {
                    str = "lan";
                }
                str2 = str;
            }
        }
        LogModule.d(TAG, "getNetworkType4Downloader Network Type : " + str2);
        return str2;
    }

    public static synchronized String getSerial(Context context) {
        if (DeviceInfoModule.disableAccessData) {
            return "unknown";
        }
        if (serial == null) {
            LogModule.d(TAG, "getSerial");
            if (Build.VERSION.SDK_INT >= 28) {
                serial = getSerialOnAndroidP(context);
            } else {
                LogModule.d(TAG, "getSerial from System API");
                serial = Build.SERIAL;
                if (serial == null) {
                    serial = "";
                }
            }
        }
        return TextUtils.isEmpty(serial) ? "default" : serial;
    }

    private static synchronized String getSerialOnAndroidP(Context context) {
        try {
            if (!Utils.checkSelfPermission(context, "android.permission.READ_PHONE_STATE")) {
                return null;
            }
            LogModule.d(TAG, "getSerialOnAndroidP from System API");
            String serial2 = Build.getSerial();
            if (serial2 == null) {
                serial2 = "";
            }
            return serial2;
        } catch (SecurityException e) {
            LogModule.d(TAG, "getSerialOnAndroidP: " + e.getMessage());
            return "";
        } catch (Exception e2) {
            LogModule.d(TAG, "getSerialOnAndroidP: " + e2.getMessage());
            return null;
        }
    }

    static synchronized String getSystemLanguage(boolean z) {
        Locale locale;
        if (!TextUtils.isEmpty(systemLanguage) && !z) {
            return systemLanguage;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        if (locale != null) {
            systemLanguage = locale.getLanguage() + "_" + locale.getCountry();
        }
        LogModule.d(TAG, "StrUtil [getSystemLanguage] final System Language=" + systemLanguage);
        return systemLanguage;
    }

    static synchronized String getSimCountryIso(Context context, boolean z) {
        if (!"default".equalsIgnoreCase(simCountry) && !z) {
            return simCountry;
        }
        if (context == null) {
            return "";
        }
        String simCountryIso = "";
        try {
            simCountryIso = ((TelephonyManager) context.getSystemService("phone")).getSimCountryIso();
        } catch (Exception e) {
            LogModule.d(TAG, "getSimCountry: " + e.getMessage());
        }
        String upperCase = simCountryIso.toUpperCase(Locale.ROOT);
        LogModule.d(TAG, "getSimCountry, simCountry=" + upperCase);
        simCountry = upperCase;
        return simCountry;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v3, types: [org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.Object, org.json.JSONArray] */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v3 */
    /* JADX WARN: Type inference failed for: r5v6, types: [org.json.JSONObject] */
    static synchronized String getWifiListJson(Context context, boolean z) {
        ?? jSONObject;
        JSONObject jSONObject2;
        if (commonWifiInfo == null && isGetCommonWifiInfoFirst) {
            try {
                WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                if (wifiManager != null) {
                    commonWifiInfo = HookManager.getConnectionInfo(wifiManager);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isGetCommonWifiInfoFirst = false;
        }
        ?? jSONArray = new JSONArray();
        try {
            jSONObject = new JSONObject();
            try {
                jSONObject.put("methodId", "getWifiListJson");
            } catch (Exception e2) {
                e = e2;
                String str = "getWifiListJson Exception=" + e.getMessage();
                LogModule.d(TAG, str);
                try {
                    ?? jSONObject3 = new JSONObject();
                    try {
                        jSONObject3.put("methodId", "getWifiListJson");
                        jSONObject3.put(RespUtil.UniSdkField.RESP_CODE, 10000);
                        jSONObject3.put(RespUtil.UniSdkField.RESP_MSG, str);
                        jSONObject3.putOpt("wifi", jSONArray);
                        jSONObject2 = jSONObject3;
                    } catch (Exception e3) {
                        e = e3;
                        jSONObject = jSONObject3;
                        e.printStackTrace();
                        jSONObject2 = jSONObject;
                        wifiListJson = jSONObject2.toString();
                        LogModule.d(TAG, "getWifiListJson wifiList Json : " + wifiListJson);
                        return wifiListJson;
                    }
                } catch (Exception e4) {
                    e = e4;
                }
                wifiListJson = jSONObject2.toString();
                LogModule.d(TAG, "getWifiListJson wifiList Json : " + wifiListJson);
                return wifiListJson;
            }
        } catch (Exception e5) {
            e = e5;
            jSONObject = 0;
        }
        if (context == null) {
            LogModule.d(TAG, "getWifiListJson mContext is null");
            jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 2);
            jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "getWifiListJson mContext is null");
            jSONObject.putOpt("wifi", jSONArray);
            wifiListJson = jSONObject.toString();
            isGetWifiList = true;
            LogModule.d(TAG, "getWifiListJson wifiList Json : " + wifiListJson);
            return wifiListJson;
        }
        if (Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") != 0 && context.checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
            LogModule.d(TAG, "getWifiListJson android.permission.ACCESS_COARSE_LOCATION or android.permission.ACCESS_FINE_LOCATION has not been granted");
            jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 4);
            jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "getWifiListJson android.permission.ACCESS_COARSE_LOCATION or android.permission.ACCESS_FINE_LOCATION has not been granted");
            jSONObject.putOpt("wifi", jSONArray);
            wifiListJson = jSONObject.toString();
            isGetWifiList = true;
            LogModule.d(TAG, "getWifiListJson wifiList Json : " + wifiListJson);
            return wifiListJson;
        }
        if (!isReqLocation) {
            WifiManager wifiManager2 = (WifiManager) context.getApplicationContext().getSystemService("wifi");
            if (wifiManager2 != null) {
                commonWifiInfo = HookManager.getConnectionInfo(wifiManager2);
            }
            isReqLocation = true;
        }
        if (!z && !isValidWifiList() && isGetWifiList) {
            isGetWifiList = false;
            z = true;
        }
        if (!z && hasRefreshWifiList) {
            hasRefreshWifiList = false;
            z = true;
        }
        if (!z) {
            LogModule.d(TAG, "getWifiListJson wifiList Json : " + wifiListJson);
            return wifiListJson;
        }
        try {
            WifiManager wifiManager3 = (WifiManager) context.getApplicationContext().getSystemService("wifi");
            if (wifiManager3 != null) {
                List<ScanResult> scanResults = wifiManager3.getScanResults();
                if (scanResults != null && !scanResults.isEmpty()) {
                    String bssid = (commonWifiInfo == null || DeviceInfoModule.disableAccessData) ? "" : commonWifiInfo.getBSSID();
                    for (ScanResult scanResult : scanResults) {
                        JSONObject jSONObject4 = new JSONObject();
                        try {
                            if (DeviceInfoModule.disableAccessData) {
                                jSONObject4.put("ssid", "unknown");
                                jSONObject4.put("bssid", "unknown");
                            } else {
                                jSONObject4.put("ssid", scanResult.SSID);
                                jSONObject4.put("bssid", scanResult.BSSID);
                            }
                            jSONObject4.put("frequency", scanResult.frequency);
                            if (isValidBssid(bssid) && bssid.equals(scanResult.BSSID)) {
                                jSONObject4.put("connected", 1);
                            } else {
                                jSONObject4.put("connected", 0);
                            }
                            jSONObject4.put("rss", scanResult.level);
                            jSONArray.put(jSONObject4);
                        } catch (JSONException e6) {
                            e6.printStackTrace();
                        }
                    }
                } else if (!DeviceInfoModule.disableAccessData && commonWifiInfo != null) {
                    String bssid2 = commonWifiInfo.getBSSID();
                    if (isValidBssid(bssid2)) {
                        JSONObject jSONObject5 = new JSONObject();
                        try {
                            String ssid = commonWifiInfo.getSSID();
                            if (!TextUtils.isEmpty(ssid) && ssid.contains("\"")) {
                                ssid = ssid.replace("\"", "");
                            }
                            jSONObject5.put("ssid", ssid);
                            jSONObject5.put("bssid", bssid2);
                            if (Build.VERSION.SDK_INT >= 21) {
                                jSONObject5.put("frequency", commonWifiInfo.getFrequency());
                            } else {
                                jSONObject5.put("frequency", 0);
                            }
                            jSONObject5.put("connected", 1);
                            jSONObject5.put("rss", commonWifiInfo.getRssi());
                            jSONArray.put(jSONObject5);
                        } catch (JSONException e7) {
                            e7.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e8) {
            e8.printStackTrace();
        }
        if (jSONArray.length() != 0) {
            jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 0);
            jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "success");
            isGetWifiList = true;
        } else {
            jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 10000);
            jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "getWifiListJson wifiList is empty");
        }
        jSONObject.putOpt("wifi", jSONArray);
        jSONObject2 = jSONObject;
        wifiListJson = jSONObject2.toString();
        LogModule.d(TAG, "getWifiListJson wifiList Json : " + wifiListJson);
        return wifiListJson;
    }

    private static boolean isValidBssid(String str) {
        return (TextUtils.isEmpty(str) || "02:00:00:00:00:00".equals(str)) ? false : true;
    }

    private static boolean isValidWifiList() {
        try {
            JSONObject jSONObject = new JSONObject(wifiListJson);
            if (jSONObject.has("wifi")) {
                return jSONObject.optJSONArray("wifi").length() != 0;
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    static synchronized String getAppVersionCode(Context context) {
        if (-1 != appVersionCode) {
        } else {
            if (context != null) {
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getAppPackageName(context), 0);
                    appVersionCode = packageInfo.versionCode;
                    if (appVersionName == null) {
                        appVersionName = packageInfo.versionName;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    appVersionCode = 0;
                }
            } else {
                appVersionCode = 0;
            }
        }
        return appVersionCode + "";
    }

    static synchronized String getAppVersionName(Context context) {
        if (appVersionName != null) {
        } else {
            if (context != null) {
                try {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getAppPackageName(context), 0);
                    appVersionName = packageInfo.versionName;
                    if (-1 == appVersionCode) {
                        appVersionCode = packageInfo.versionCode;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    appVersionName = "";
                }
            } else {
                appVersionName = "";
            }
        }
        return appVersionName;
    }

    public static String getAppPackageName(Context context) {
        return context != null ? context.getPackageName() : "";
    }

    static synchronized String getOsVer(Context context) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    static synchronized String getMobileType(Context context) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return Build.MODEL;
    }

    static synchronized String getAppFilesCacheDir(Context context) {
        try {
            try {
            } catch (Exception unused) {
                return context.getFilesDir().getAbsolutePath();
            }
        } catch (Throwable unused2) {
            return "";
        }
        return context.getFilesDir().getCanonicalPath();
    }

    static synchronized String getFirstDeviceId(Context context) {
        String strSpGet;
        try {
            strSpGet = Utils.spGet(context, Utils.KEY_FIRST_DEVICE_ID_CACHED);
            if (TextUtils.isEmpty(strSpGet)) {
                String string = UUID.randomUUID().toString();
                if (!TextUtils.isEmpty(string)) {
                    char[] charArray = string.toCharArray();
                    strSpGet = string + "-" + (String.valueOf(charArray[10]) + String.valueOf(charArray[1]) + String.valueOf(charArray[6]) + String.valueOf(charArray[3]));
                    Utils.spCache(context, Utils.KEY_FIRST_DEVICE_ID_CACHED, strSpGet);
                }
            }
        } catch (Exception unused) {
            strSpGet = "";
        }
        LogModule.d(TAG, "getFirstDeviceId : " + strSpGet);
        return strSpGet;
    }

    /* renamed from: com.netease.ntunisdk.modules.deviceinfo.DeviceUtils$1 */
    static class AnonymousClass1 implements InvocationHandler {
        final /* synthetic */ DeviceInfoModule val$deviceInfoModule;
        final /* synthetic */ JSONObject val$jsonObj;

        AnonymousClass1(JSONObject jSONObject, DeviceInfoModule deviceInfoModule) {
            jSONObject = jSONObject;
            deviceInfoModule = deviceInfoModule;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            Object obj2;
            LogModule.d(DeviceUtils.TAG, method + "");
            try {
                if (!method.getName().equals("onSuccess") || objArr == null || objArr.length <= 0 || (obj2 = objArr[0]) == null) {
                    return null;
                }
                Method declaredMethod = obj2.getClass().getDeclaredMethod("getScope", new Class[0]);
                Method declaredMethod2 = obj2.getClass().getDeclaredMethod("getId", new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod2.setAccessible(true);
                JSONObject jSONObject = jSONObject;
                if (jSONObject == null) {
                    jSONObject = new JSONObject();
                    jSONObject.put("methodId", "getAppSetID");
                }
                jSONObject.put(Constants.PARAM_SCOPE, declaredMethod.invoke(obj2, new Object[0]));
                jSONObject.put(ResIdReader.RES_TYPE_ID, declaredMethod2.invoke(obj2, new Object[0]));
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 0);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "success");
                LogModule.d(DeviceUtils.TAG, "getAppSetID : " + jSONObject);
                deviceInfoModule.onCallback("native", ConstProp.UNISDKBASE, jSONObject.toString());
                return null;
            } catch (Throwable th) {
                th.printStackTrace();
                try {
                    JSONObject jSONObject2 = jSONObject;
                    if (jSONObject2 == null) {
                        jSONObject2 = new JSONObject();
                        jSONObject2.put("methodId", "getAppSetID");
                    }
                    jSONObject2.put(RespUtil.UniSdkField.RESP_CODE, 10000);
                    jSONObject2.put(RespUtil.UniSdkField.RESP_MSG, "getAppSetID failed");
                    deviceInfoModule.onCallback("native", ConstProp.UNISDKBASE, jSONObject2.toString());
                    return null;
                } catch (Exception unused) {
                    return null;
                }
            }
        }
    }

    static synchronized void getAppSetID(Context context, JSONObject jSONObject, DeviceInfoModule deviceInfoModule) {
        try {
            Object objNewProxyInstance = Proxy.newProxyInstance(DeviceUtils.class.getClassLoader(), new Class[]{Class.forName("com.google.android.gms.tasks.OnSuccessListener")}, new InvocationHandler() { // from class: com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.1
                final /* synthetic */ DeviceInfoModule val$deviceInfoModule;
                final /* synthetic */ JSONObject val$jsonObj;

                AnonymousClass1(JSONObject jSONObject2, DeviceInfoModule deviceInfoModule2) {
                    jSONObject = jSONObject2;
                    deviceInfoModule = deviceInfoModule2;
                }

                @Override // java.lang.reflect.InvocationHandler
                public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                    Object obj2;
                    LogModule.d(DeviceUtils.TAG, method + "");
                    try {
                        if (!method.getName().equals("onSuccess") || objArr == null || objArr.length <= 0 || (obj2 = objArr[0]) == null) {
                            return null;
                        }
                        Method declaredMethod = obj2.getClass().getDeclaredMethod("getScope", new Class[0]);
                        Method declaredMethod2 = obj2.getClass().getDeclaredMethod("getId", new Class[0]);
                        declaredMethod.setAccessible(true);
                        declaredMethod2.setAccessible(true);
                        JSONObject jSONObject2 = jSONObject;
                        if (jSONObject2 == null) {
                            jSONObject2 = new JSONObject();
                            jSONObject2.put("methodId", "getAppSetID");
                        }
                        jSONObject2.put(Constants.PARAM_SCOPE, declaredMethod.invoke(obj2, new Object[0]));
                        jSONObject2.put(ResIdReader.RES_TYPE_ID, declaredMethod2.invoke(obj2, new Object[0]));
                        jSONObject2.put(RespUtil.UniSdkField.RESP_CODE, 0);
                        jSONObject2.put(RespUtil.UniSdkField.RESP_MSG, "success");
                        LogModule.d(DeviceUtils.TAG, "getAppSetID : " + jSONObject2);
                        deviceInfoModule.onCallback("native", ConstProp.UNISDKBASE, jSONObject2.toString());
                        return null;
                    } catch (Throwable th) {
                        th.printStackTrace();
                        try {
                            JSONObject jSONObject22 = jSONObject;
                            if (jSONObject22 == null) {
                                jSONObject22 = new JSONObject();
                                jSONObject22.put("methodId", "getAppSetID");
                            }
                            jSONObject22.put(RespUtil.UniSdkField.RESP_CODE, 10000);
                            jSONObject22.put(RespUtil.UniSdkField.RESP_MSG, "getAppSetID failed");
                            deviceInfoModule.onCallback("native", ConstProp.UNISDKBASE, jSONObject22.toString());
                            return null;
                        } catch (Exception unused) {
                            return null;
                        }
                    }
                }
            });
            Object objInvoke = Class.forName("com.google.android.gms.appset.AppSet").getMethod("getClient", Context.class).invoke(null, context.getApplicationContext());
            Method declaredMethod = objInvoke.getClass().getDeclaredMethod("getAppSetIdInfo", new Class[0]);
            declaredMethod.setAccessible(true);
            Object objInvoke2 = declaredMethod.invoke(objInvoke, new Object[0]);
            Method declaredMethod2 = objInvoke2.getClass().getDeclaredMethod("addOnSuccessListener", Class.forName("com.google.android.gms.tasks.OnSuccessListener"));
            declaredMethod2.setAccessible(true);
            declaredMethod2.invoke(objInvoke2, objNewProxyInstance);
        } catch (Throwable th) {
            th.printStackTrace();
            if (jSONObject2 == null) {
                try {
                    jSONObject2 = new JSONObject();
                    jSONObject2.put("methodId", "getAppSetID");
                } catch (Exception unused) {
                }
            }
            jSONObject2.put(RespUtil.UniSdkField.RESP_CODE, 10000);
            jSONObject2.put(RespUtil.UniSdkField.RESP_MSG, "getAppSetID failed");
            deviceInfoModule2.onCallback("native", ConstProp.UNISDKBASE, jSONObject2.toString());
        }
    }

    /* renamed from: com.netease.ntunisdk.modules.deviceinfo.DeviceUtils$2 */
    static class AnonymousClass2 implements AsyncCallback<Map<String, Long>> {
        final /* synthetic */ DeviceInfoModule val$deviceInfoModule;
        final /* synthetic */ JSONObject val$jsonObj;

        AnonymousClass2(JSONObject jSONObject, DeviceInfoModule deviceInfoModule) {
            jSONObject = jSONObject;
            deviceInfoModule = deviceInfoModule;
        }

        @Override // com.netease.ntunisdk.modules.deviceinfo.AsyncCallback
        public void onFinished(Map<String, Long> map) throws JSONException {
            try {
                JSONObject jSONObject = jSONObject;
                if (jSONObject == null) {
                    jSONObject = new JSONObject();
                    jSONObject.put("methodId", "getAppOccupiedStorage");
                }
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 0);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "success");
                if (map != null) {
                    for (Map.Entry<String, Long> entry : map.entrySet()) {
                        jSONObject.put(entry.getKey(), entry.getValue());
                    }
                }
                LogModule.d(DeviceUtils.TAG, "getAppOccupiedStorage : " + jSONObject);
                deviceInfoModule.onCallback("native", ConstProp.UNISDKBASE, jSONObject.toString());
                deviceInfoModule.onCallback("native", CUniLogger.source, jSONObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static synchronized void getAppOccupiedStorage(Context context, JSONObject jSONObject, DeviceInfoModule deviceInfoModule) {
        try {
            AnonymousClass2 anonymousClass2 = new AsyncCallback<Map<String, Long>>() { // from class: com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.2
                final /* synthetic */ DeviceInfoModule val$deviceInfoModule;
                final /* synthetic */ JSONObject val$jsonObj;

                AnonymousClass2(JSONObject jSONObject2, DeviceInfoModule deviceInfoModule2) {
                    jSONObject = jSONObject2;
                    deviceInfoModule = deviceInfoModule2;
                }

                @Override // com.netease.ntunisdk.modules.deviceinfo.AsyncCallback
                public void onFinished(Map<String, Long> map) throws JSONException {
                    try {
                        JSONObject jSONObject2 = jSONObject;
                        if (jSONObject2 == null) {
                            jSONObject2 = new JSONObject();
                            jSONObject2.put("methodId", "getAppOccupiedStorage");
                        }
                        jSONObject2.put(RespUtil.UniSdkField.RESP_CODE, 0);
                        jSONObject2.put(RespUtil.UniSdkField.RESP_MSG, "success");
                        if (map != null) {
                            for (Map.Entry<String, Long> entry : map.entrySet()) {
                                jSONObject2.put(entry.getKey(), entry.getValue());
                            }
                        }
                        LogModule.d(DeviceUtils.TAG, "getAppOccupiedStorage : " + jSONObject2);
                        deviceInfoModule.onCallback("native", ConstProp.UNISDKBASE, jSONObject2.toString());
                        deviceInfoModule.onCallback("native", CUniLogger.source, jSONObject2.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            if (Build.VERSION.SDK_INT >= 26) {
                LogModule.d(TAG, "getAppSizeTop26:");
                getAppSizeTop26(context, context.getPackageName(), anonymousClass2);
            } else {
                LogModule.d(TAG, "getAppSize:");
                getAppSize(context, context.getPackageName(), anonymousClass2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAppSize(Context context, String str, AsyncCallback<Map<String, Long>> asyncCallback) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            PackageManager.class.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class).invoke(context.getPackageManager(), str, new IPackageStatsObserver.Stub() { // from class: com.netease.ntunisdk.modules.deviceinfo.DeviceUtils.3
                AnonymousClass3() {
                }

                @Override // android.content.pm.IPackageStatsObserver
                public void onGetStatsCompleted(PackageStats packageStats, boolean z) throws RemoteException {
                    LogModule.i(DeviceUtils.TAG, "\u7f13\u5b58\u5927\u5c0f=" + packageStats.cacheSize);
                    LogModule.i(DeviceUtils.TAG, "\u6570\u636e\u5927\u5c0f=" + packageStats.dataSize);
                    LogModule.i(DeviceUtils.TAG, "\u7a0b\u5e8f\u5927\u5c0f=" + packageStats.codeSize);
                    HashMap map = new HashMap();
                    map.put("cacheSize", Long.valueOf(packageStats.cacheSize));
                    map.put("dataSize", Long.valueOf(packageStats.dataSize));
                    map.put("appSize", Long.valueOf(packageStats.codeSize));
                    map.put("totalSize", Long.valueOf(packageStats.codeSize + packageStats.dataSize));
                    asyncCallback.onFinished(map);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            HashMap map = new HashMap();
            map.put("cacheSize", 0L);
            map.put("dataSize", 0L);
            map.put("appSize", 0L);
            map.put("totalSize", 0L);
            asyncCallback.onFinished(map);
        }
    }

    /* renamed from: com.netease.ntunisdk.modules.deviceinfo.DeviceUtils$3 */
    static class AnonymousClass3 extends IPackageStatsObserver.Stub {
        AnonymousClass3() {
        }

        @Override // android.content.pm.IPackageStatsObserver
        public void onGetStatsCompleted(PackageStats packageStats, boolean z) throws RemoteException {
            LogModule.i(DeviceUtils.TAG, "\u7f13\u5b58\u5927\u5c0f=" + packageStats.cacheSize);
            LogModule.i(DeviceUtils.TAG, "\u6570\u636e\u5927\u5c0f=" + packageStats.dataSize);
            LogModule.i(DeviceUtils.TAG, "\u7a0b\u5e8f\u5927\u5c0f=" + packageStats.codeSize);
            HashMap map = new HashMap();
            map.put("cacheSize", Long.valueOf(packageStats.cacheSize));
            map.put("dataSize", Long.valueOf(packageStats.dataSize));
            map.put("appSize", Long.valueOf(packageStats.codeSize));
            map.put("totalSize", Long.valueOf(packageStats.codeSize + packageStats.dataSize));
            asyncCallback.onFinished(map);
        }
    }

    public static void getAppSizeTop26(Context context, String str, AsyncCallback<Map<String, Long>> asyncCallback) {
        try {
            StorageStatsManager storageStatsManager = (StorageStatsManager) context.getSystemService("storagestats");
            StorageManager storageManager = (StorageManager) context.getSystemService("storage");
            File file = new File(context.getDataDir().getParent(), str);
            if (file.exists()) {
                try {
                    StorageStats storageStatsQueryStatsForUid = storageStatsManager.queryStatsForUid(storageManager.getUuidForPath(file), context.getPackageManager().getApplicationInfo(str, 128).uid);
                    if (storageStatsQueryStatsForUid != null) {
                        LogModule.i(TAG, "\u7f13\u5b58\u5927\u5c0f=" + storageStatsQueryStatsForUid.getCacheBytes());
                        LogModule.i(TAG, "\u6570\u636e\u5927\u5c0f=" + storageStatsQueryStatsForUid.getDataBytes());
                        LogModule.i(TAG, "\u7a0b\u5e8f\u5927\u5c0f=" + storageStatsQueryStatsForUid.getAppBytes());
                        HashMap map = new HashMap();
                        map.put("cacheSize", Long.valueOf(storageStatsQueryStatsForUid.getCacheBytes()));
                        map.put("dataSize", Long.valueOf(storageStatsQueryStatsForUid.getDataBytes()));
                        map.put("appSize", Long.valueOf(storageStatsQueryStatsForUid.getAppBytes()));
                        map.put("totalSize", Long.valueOf(storageStatsQueryStatsForUid.getAppBytes() + storageStatsQueryStatsForUid.getDataBytes()));
                        asyncCallback.onFinished(map);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        HashMap map2 = new HashMap();
        map2.put("cacheSize", 0L);
        map2.put("dataSize", 0L);
        map2.put("appSize", 0L);
        map2.put("totalSize", 0L);
        asyncCallback.onFinished(map2);
    }

    public static boolean useNetworkCapabilities() {
        return Build.VERSION.SDK_INT >= 24 && !isFallbackNetReceiver;
    }

    static synchronized String baseSupportDeviceInfo(Context context, JSONObject jSONObject) {
        try {
            if (!jSONObject.has(ReportOrigin.ORIGIN_CATEGORY)) {
                return "";
            }
            String strOptString = jSONObject.optString(ReportOrigin.ORIGIN_CATEGORY);
            if (TextUtils.isEmpty(strOptString)) {
                return "";
            }
            if (strOptString.contains("screen")) {
                JSONObject jSONObject2 = new JSONObject();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                if (context != null) {
                    try {
                        WindowManager windowManager = (WindowManager) context.getSystemService("window");
                        if (Build.VERSION.SDK_INT >= 17) {
                            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
                        } else {
                            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                        }
                        float f = displayMetrics.density;
                        int i = displayMetrics.heightPixels;
                        int i2 = displayMetrics.widthPixels;
                        jSONObject2.put("screen_density", f);
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("height", i);
                        jSONObject3.put("width", i2);
                        jSONObject2.put("screen_resoultion", jSONObject3);
                        JSONObject jSONObject4 = new JSONObject();
                        jSONObject4.put("height", (int) (i / f));
                        jSONObject4.put("width", (int) (i2 / f));
                        jSONObject2.put("screen_size", jSONObject4);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                jSONObject.putOpt("screen", jSONObject2);
            }
            if (strOptString.contains("battery")) {
                JSONObject jSONObject5 = new JSONObject();
                if (context != null) {
                    try {
                        if (Build.VERSION.SDK_INT >= 21) {
                            BatteryManager batteryManager = (BatteryManager) context.getSystemService("batterymanager");
                            int intProperty = batteryManager.getIntProperty(4);
                            int intProperty2 = batteryManager.getIntProperty(6);
                            if (intProperty2 == 1) {
                                jSONObject5.put(a.d, intProperty);
                                jSONObject5.put("state", 0);
                                jSONObject5.put("state_desc", "unknown");
                            } else if (intProperty2 == 2 || intProperty2 == 4) {
                                jSONObject5.put(a.d, intProperty);
                                jSONObject5.put("state", 2);
                                jSONObject5.put("state_desc", "charging");
                            } else if (intProperty2 == 3) {
                                jSONObject5.put(a.d, intProperty);
                                jSONObject5.put("state", 1);
                                jSONObject5.put("state_desc", "unplugged");
                            } else if (intProperty2 == 5) {
                                jSONObject5.put(a.d, intProperty);
                                jSONObject5.put("state", 3);
                                jSONObject5.put("state_desc", "full");
                            }
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                jSONObject.putOpt("battery", jSONObject5);
            }
            if (strOptString.contains("memory")) {
                JSONObject jSONObject6 = new JSONObject();
                if (context != null) {
                    try {
                        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
                        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
                        activityManager.getMemoryInfo(memoryInfo);
                        if (Build.VERSION.SDK_INT >= 16) {
                            long systemTotalMemory = memoryInfo.totalMem;
                            double d = systemTotalMemory;
                            Double.isNaN(d);
                            double d2 = ((d / 1024.0d) / 1024.0d) / 1024.0d;
                            if (memoryInfo.totalMem <= 0 || "0.00".equals(String.format(Locale.ROOT, "%.2f", Double.valueOf(d2)))) {
                                systemTotalMemory = getSystemTotalMemory();
                                double d3 = systemTotalMemory;
                                Double.isNaN(d3);
                                d2 = ((d3 / 1024.0d) / 1024.0d) / 1024.0d;
                            }
                            jSONObject6.put("memory_total", systemTotalMemory);
                            jSONObject6.put("memory_total_format", String.format(Locale.ROOT, "%.2f", Double.valueOf(d2)) + " GB");
                            if (Build.VERSION.SDK_INT > 30) {
                                Debug.getMemoryInfo(new Debug.MemoryInfo());
                                long totalPss = r0.getTotalPss() * 1024;
                                double d4 = totalPss;
                                Double.isNaN(d4);
                                double d5 = (d4 / 1024.0d) / 1024.0d;
                                jSONObject6.put("memory_usage", totalPss);
                                jSONObject6.put("memory_usage_format", String.format(Locale.ROOT, "%.1f", Double.valueOf(d5)) + " MB");
                            } else {
                                if (activityManager.getProcessMemoryInfo(new int[]{Process.myPid()}).length > 0) {
                                    long totalPss2 = r0[0].getTotalPss() * 1024;
                                    double d6 = totalPss2;
                                    Double.isNaN(d6);
                                    double d7 = (d6 / 1024.0d) / 1024.0d;
                                    jSONObject6.put("memory_usage", totalPss2);
                                    jSONObject6.put("memory_usage_format", String.format(Locale.ROOT, "%.1f", Double.valueOf(d7)) + " MB");
                                }
                            }
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                }
                jSONObject.putOpt("memory", jSONObject6);
            }
            if (strOptString.contains("disk")) {
                JSONObject jSONObject7 = new JSONObject();
                try {
                    long blockSize = new StatFs(Environment.getDataDirectory().getPath()).getBlockSize();
                    long blockCount = r4.getBlockCount() * blockSize;
                    double d8 = blockCount;
                    Double.isNaN(d8);
                    double d9 = d8 / 1.0E9d;
                    long availableBlocks = blockSize * r4.getAvailableBlocks();
                    double d10 = availableBlocks;
                    Double.isNaN(d10);
                    double d11 = d10 / 1.0E9d;
                    jSONObject7.put("disk_total", blockCount);
                    jSONObject7.put("disk_total_format", String.format(Locale.ROOT, "%.2f", Double.valueOf(d9)) + " GB");
                    jSONObject7.put("disk_usage", blockCount - availableBlocks);
                    jSONObject7.put("disk_usage_format", String.format(Locale.ROOT, "%.2f", Double.valueOf(d9 - d11)) + " GB");
                    jSONObject7.put("disk_available", availableBlocks);
                    jSONObject7.put("disk_available_format", String.format(Locale.ROOT, "%.2f", Double.valueOf(d11)) + " GB");
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
                jSONObject.putOpt("disk", jSONObject7);
            }
            return jSONObject.toString();
        } catch (Exception e5) {
            e5.printStackTrace();
            return "";
        }
    }

    private static long getSystemTotalMemory() throws IOException {
        LogModule.i(TAG, "getSystemTotalMemory start");
        long jLongValue = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            jLongValue = Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]).longValue() * 1024;
            bufferedReader.close();
            return jLongValue;
        } catch (IOException e) {
            LogModule.w(TAG, "getSystemTotalMemory IOException=" + e.toString());
            e.toString();
            return jLongValue;
        } catch (Exception e2) {
            LogModule.w(TAG, "getSystemTotalMemory Exception=" + e2.toString());
            e2.toString();
            return jLongValue;
        }
    }

    static synchronized String getIssuerUrl(JSONObject jSONObject, String str) {
        String strOptString = "";
        try {
            if (TextUtils.isEmpty(jSONObject.optString("url"))) {
                return "";
            }
            strOptString = jSONObject.optString("url");
            String strOptString2 = jSONObject.optString(Const.NT_PARAM_DOMAIN);
            if (TextUtils.isEmpty(strOptString2)) {
                return strOptString;
            }
            if (TextUtils.isEmpty(str)) {
                return strOptString;
            }
            return strOptString.replace(strOptString2, str);
        } catch (Exception e) {
            e.printStackTrace();
            return strOptString;
        }
    }

    static synchronized String getAppChannel(Context context) {
        if (!"unknown".equals(appChannel)) {
            return appChannel;
        }
        try {
            String appChannel2 = AppChannel.getAppChannel(context);
            if (!TextUtils.isEmpty(appChannel2)) {
                appChannel = appChannel2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appChannel;
    }
}