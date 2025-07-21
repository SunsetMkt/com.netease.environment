package com.netease.download.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import com.netease.download.Const;
import com.netease.download.downloader.DownloadInitInfo;
import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.xiaomi.gamecenter.sdk.report.SDefine;
import com.xiaomi.onetrack.api.b;
import com.xiaomi.onetrack.util.z;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class Util {
    private static final String TAG = "StrUtil";

    public static String getDomainFromUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            str.trim();
            if (str.contains("://")) {
                String[] strArrSplit = str.split("//|:|/");
                if (strArrSplit != null && strArrSplit.length > 2) {
                    return strArrSplit[2];
                }
            } else {
                String[] strArrSplit2 = str.split(":|/");
                if (strArrSplit2 != null && strArrSplit2.length > 0) {
                    return strArrSplit2[0];
                }
            }
        }
        return "";
    }

    public static String string2HexString(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            stringBuffer.append(Integer.toHexString(str.charAt(i)));
        }
        return stringBuffer.toString();
    }

    public static String bytesToHexString(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public static String getPortFromUrl(String str) {
        String[] strArrSplit;
        String[] strArrSplit2;
        String str2;
        String[] strArrSplit3;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        str.trim();
        if (str.contains("://")) {
            String[] strArrSplit4 = str.split("//|/");
            String str3 = (strArrSplit4 == null || strArrSplit4.length <= 1) ? "" : strArrSplit4[1];
            if (TextUtils.isEmpty(str3) || !str3.contains(":") || (strArrSplit3 = str3.split(":")) == null || strArrSplit3.length <= 1) {
                return "";
            }
            str2 = strArrSplit3[1];
        } else {
            if (!str.contains("/")) {
                return (!str.contains(":") || (strArrSplit = str.split(":")) == null || strArrSplit.length <= 1) ? "" : strArrSplit[1];
            }
            String[] strArrSplit5 = str.split("/");
            String str4 = (strArrSplit5 == null || strArrSplit5.length <= 0) ? "" : strArrSplit5[0];
            if (TextUtils.isEmpty(str4) || !str4.contains(":") || (strArrSplit2 = str4.split(":")) == null || strArrSplit2.length <= 1) {
                return "";
            }
            str2 = strArrSplit2[1];
        }
        return str2;
    }

    public static String parseChannel(String str) {
        return TextUtils.isEmpty(str) ? "other" : str.contains("update.") ? SDefine.e : str.contains("gph.") ? "gph" : str.contains("gdl.") ? "gdl" : "other";
    }

    public static String getCdnChannel(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.contains("https://")) {
            str = str.replaceAll("https://", "");
        } else if (str.contains("http://")) {
            str = str.replaceAll("http://", "");
        }
        String[] strArrSplit = str.split(z.f2789a);
        if (strArrSplit.length >= 2) {
            return strArrSplit[1];
        }
        return null;
    }

    public static String getSuffixFromUrl(String str) {
        int i;
        if (str == null) {
            return "";
        }
        if (str.contains("https://")) {
            i = 9;
        } else {
            i = str.contains("http://") ? 8 : 0;
        }
        int iIndexOf = str.indexOf("/", i);
        return iIndexOf > 0 ? str.substring(iIndexOf + 1) : "";
    }

    public static String getPrefixFromUrl(String str) {
        int i;
        if (str == null) {
            return "";
        }
        if (str.contains("https://")) {
            i = 9;
        } else {
            i = str.contains("http://") ? 8 : 0;
        }
        int iIndexOf = str.indexOf("/", i);
        if (iIndexOf < 0) {
            iIndexOf = str.length();
        }
        return str.substring(0, iIndexOf);
    }

    public static String replaceDomainWithIpAddr(String str, String str2, String str3) {
        String string;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            string = "";
        } else {
            int iIndexOf = str.indexOf("//");
            int iIndexOf2 = str.indexOf(str3, iIndexOf + 2);
            if (iIndexOf < 0) {
                iIndexOf = -2;
            }
            if (iIndexOf2 < 0) {
                iIndexOf2 = str.length();
            }
            StringBuilder sb = new StringBuilder(str);
            sb.replace(iIndexOf + 2, iIndexOf2, str2);
            string = sb.toString();
        }
        LogUtil.i(TAG, "DownloadPartCore [getDownloadUrl] resultresult=" + string);
        return string;
    }

    public static String replaceDomainWithIpAddr(String str, String str2, String str3, String str4) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str4)) {
            return "";
        }
        int iIndexOf = str.indexOf("//");
        int iIndexOf2 = str.indexOf(str4, iIndexOf + 2);
        if (iIndexOf < 0) {
            iIndexOf = -2;
        }
        if (iIndexOf2 < 0) {
            iIndexOf2 = str.length();
        }
        StringBuilder sb = new StringBuilder(str);
        sb.replace(iIndexOf + 2, iIndexOf2, str2);
        return sb.toString();
    }

    public static boolean isIpAddrDomain(String str) throws NumberFormatException {
        int i;
        String[] strArrSplit = getDomainFromUrl(str).split(z.f2789a);
        if (strArrSplit == null || strArrSplit.length != 4) {
            return false;
        }
        for (String str2 : strArrSplit) {
            try {
                i = Integer.parseInt(str2);
            } catch (Exception unused) {
            }
            if (i < 0 || 255 < i) {
                return false;
            }
        }
        LogUtil.i(TAG, "is IpAddr\uff0cAddr=" + str);
        return true;
    }

    public static String getDomainFirstPart(String str) {
        if (str == null) {
            return null;
        }
        if (str.contains("https://")) {
            str = str.replaceAll("https://", "");
        } else if (str.contains("http://")) {
            str = str.replaceAll("http://", "");
        }
        String[] strArrSplit = str.split(z.f2789a);
        StringBuilder sb = new StringBuilder("");
        if (strArrSplit.length >= 1) {
            sb.append(strArrSplit[0]);
        }
        return sb.toString();
    }

    public static boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static boolean isHttpdnsServerIP(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.contains(b.P);
    }

    public static String[] getHttpdnsIpArray(String str) {
        String[] strArrSplit = !TextUtils.isEmpty(str) ? str.split(":") : null;
        return (strArrSplit == null || strArrSplit.length <= 1) ? strArrSplit : strArrSplit[1].split(",");
    }

    public static String getCdnIndexUrl(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer();
        String strSubstring = str2.substring(0, str2.indexOf(46));
        stringBuffer.append(strSubstring).append("-").append(str);
        return str2.replace(strSubstring, stringBuffer.toString());
    }

    public static String getHttpdnsHost(String str, String str2) {
        String strSubstring = str2.substring(str2.indexOf(45) + 1, str2.indexOf(46));
        LogUtil.i(TAG, "oldString=" + strSubstring);
        String domainFromUrl = getDomainFromUrl(str2.replace(strSubstring, str + ""));
        StringBuilder sb = new StringBuilder("rusult=");
        sb.append(domainFromUrl);
        LogUtil.i(TAG, sb.toString());
        return domainFromUrl;
    }

    public static String getCdnIndex(String str) {
        String domainFromUrl = getDomainFromUrl(str);
        int iIndexOf = domainFromUrl.indexOf(45);
        int iIndexOf2 = domainFromUrl.indexOf(46);
        return (-1 == iIndexOf || -1 == iIndexOf2) ? "-1" : domainFromUrl.substring(iIndexOf + 1, iIndexOf2);
    }

    public static String getFixLenthString(int i) {
        return String.valueOf((new Random().nextDouble() + 1.0d) * Math.pow(10.0d, i)).substring(2, i + 2);
    }

    public static String getRandomId() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + getFixLenthString(9);
    }

    public static String getConnectedWifiMacAddress(Context context) {
        LogUtil.i(TAG, "StrUtil [getConnectedWifiMacAddress]");
        String str = null;
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null) {
                List<ScanResult> scanResults = wifiManager.getScanResults();
                WifiInfo connectionInfo = HookManager.getConnectionInfo(wifiManager);
                if (scanResults != null && connectionInfo != null) {
                    LogUtil.i(TAG, "StrUtil [getConnectedWifiMacAddress] wifiList.size()=" + scanResults.size());
                    for (int i = 0; i < scanResults.size(); i++) {
                        ScanResult scanResult = scanResults.get(i);
                        LogUtil.i(TAG, "StrUtil [getConnectedWifiMacAddress] info.getBSSID()=" + connectionInfo.getBSSID());
                        if (connectionInfo.getBSSID().equals(scanResult.BSSID)) {
                            str = scanResult.BSSID;
                        }
                    }
                } else {
                    LogUtil.i(TAG, "StrUtil [getConnectedWifiMacAddress] wifiList or info is null");
                }
            } else {
                LogUtil.i(TAG, "StrUtil [getConnectedWifiMacAddress] wifiManager is null");
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "StrUtil [getConnectedWifiMacAddress] Exception =" + e);
        }
        LogUtil.i(TAG, "StrUtil [getConnectedWifiMacAddress] result=" + str);
        return str;
    }

    public static String getWifiRouteIPAddress(Context context) {
        String ipAddress = "";
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
            if (wifiManager != null) {
                ipAddress = Formatter.formatIpAddress(wifiManager.getDhcpInfo().gateway);
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "StrUtil getWifiRouteIPAddress Exception=" + e);
        }
        LogUtil.i(TAG, "StrUtil [getConnectedWifiMacAddress] result=" + ipAddress);
        return ipAddress;
    }

    public static String parseType(String str) {
        return TextUtils.isEmpty(str) ? "error" : str.contains("update.") ? Const.TYPE_TARGET_NORMAL : (str.contains("gph.") || str.contains("gdl.")) ? Const.TYPE_TARGET_PATCH : "other";
    }

    public static String file2Info(String str) throws IOException {
        LogUtil.i(TAG, "StrUtil [file2Info] start");
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "StrUtil [file2Str] \u53c2\u6570\u9519\u8bef");
            return null;
        }
        LogUtil.i(TAG, "StrUtil [file2Info] file name=" + str);
        File file = new File(str);
        if (!file.exists()) {
            LogUtil.i(TAG, "StrUtil [file2Str] \u914d\u7f6e\u6587\u4ef6\u4e0d\u5b58\u5728");
            return null;
        }
        StringBuilder sb = new StringBuilder("");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bArr = new byte[1024];
            while (true) {
                int i = fileInputStream.read(bArr);
                if (i <= 0) {
                    break;
                }
                sb.append(new String(bArr, 0, i));
            }
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.i(TAG, "StrUtil [file2Str] file config info =" + sb.toString());
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:175:0x014f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r11v17 */
    /* JADX WARN: Type inference failed for: r11v18 */
    /* JADX WARN: Type inference failed for: r11v19 */
    /* JADX WARN: Type inference failed for: r11v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void info2File(java.lang.String r9, java.lang.String r10, boolean r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 372
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.util.Util.info2File(java.lang.String, java.lang.String, boolean):void");
    }

    public static String replaceDomain(String str, String[] strArr, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || strArr == null || strArr.length <= 0) {
            LogUtil.i(TAG, "StrUtil [replaceDomain] param error");
        }
        String strReplaceAll = null;
        for (String str3 : strArr) {
            if (str.contains(str3)) {
                strReplaceAll = str.replaceAll(str3, str2);
            }
        }
        LogUtil.i(TAG, "StrUtil [replaceDomain] result=" + strReplaceAll);
        return strReplaceAll;
    }

    public static int getValidLength(byte[] bArr) {
        int i = 0;
        if (bArr != null && bArr.length != 0) {
            while (i < bArr.length && bArr[i] != 0) {
                i++;
            }
        }
        return i;
    }

    public static boolean isIpv6(String str) {
        return !TextUtils.isEmpty(str) && str.split(":").length > 2;
    }

    public static long getDownloadedSize(List<DownloadParams> list) {
        long jCheckAndCount = 0;
        if (!TaskHandleOp.getInstance().getTaskHandle().isRenew()) {
            int i = 0;
            for (DownloadParams downloadParams : list) {
                if (!downloadParams.ismWriteToExistFile()) {
                    ArrayList<DownloadParams.Element> arrayList = downloadParams.getmElementList();
                    LogUtil.i(TAG, "getDownloadedSize list size=" + arrayList.size());
                    if (arrayList != null && arrayList.size() > 0) {
                        Iterator<DownloadParams.Element> it = arrayList.iterator();
                        while (it.hasNext()) {
                            String str = it.next().getmElementFilePath();
                            i++;
                            LogUtil.i(TAG, "\u626b\u63cf \u7b2c" + i + " \u4e2a\u6587\u4ef6");
                            jCheckAndCount = checkAndCount(downloadParams, jCheckAndCount, str);
                            LogUtil.i(TAG, "getDownloadedSize size=" + jCheckAndCount);
                        }
                    } else {
                        i++;
                        LogUtil.i(TAG, "\u626b\u63cf \u7b2c" + i + " \u4e2a\u6587\u4ef6");
                        jCheckAndCount = checkAndCount(downloadParams, jCheckAndCount, downloadParams.getFilePath());
                    }
                }
            }
        }
        LogUtil.i(TAG, "StrUtil [getDownloadedSize] \u5df2\u4e0b\u8f7d\u597d\u7684\u6587\u4ef6\u603b\u5927\u5c0f =" + jCheckAndCount);
        DownloadListenerProxy.getInstances();
        DownloadListenerProxy.getDownloadListenerHandler().sendProgressMsg(TaskHandleOp.getInstance().getTaskHandle().getTaskMergeAllSizes(), jCheckAndCount, "xxxx", "xxxx", TaskHandleOp.getInstance().getTaskHandle().getDownloadId(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
        return jCheckAndCount;
    }

    private static long checkAndCount(DownloadParams downloadParams, long j, String str) {
        if (downloadParams == null || TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "StrUtil [checkAndCount]");
            return j;
        }
        File file = new File(str);
        if (file.exists()) {
            LogUtil.i(TAG, "StrUtil [checkAndCount] file.length()=" + file.length() + ", \u6587\u4ef6\u8def\u5f84=" + downloadParams.getFilePath());
            return j + file.length();
        }
        LogUtil.i(TAG, "\u53c2\u6570MD5=" + downloadParams.getMd5() + ", \u771f\u5b9e\u6587\u4ef6md5=null, \u6587\u4ef6\u8def\u5f84=" + downloadParams.getFilePath());
        return j;
    }

    public static String createSessionId() {
        String str = "AD_" + getRandomId();
        LogUtil.i(TAG, "StrUtil [createSessionId] mSessionId=" + str);
        return str;
    }

    public static String createTransId(Context context) throws ClassNotFoundException {
        Exception e;
        String str;
        InvocationTargetException e2;
        NoSuchMethodException e3;
        IllegalArgumentException e4;
        IllegalAccessException e5;
        ClassNotFoundException e6;
        if (context == null) {
            return "";
        }
        try {
            Class<?> cls = Class.forName("com.netease.ntunisdk.base.UniSdkUtils");
            str = (String) cls.getDeclaredMethod("getTransid", Context.class).invoke(cls, context);
        } catch (ClassNotFoundException e7) {
            e6 = e7;
            str = "";
        } catch (IllegalAccessException e8) {
            e5 = e8;
            str = "";
        } catch (IllegalArgumentException e9) {
            e4 = e9;
            str = "";
        } catch (NoSuchMethodException e10) {
            e3 = e10;
            str = "";
        } catch (InvocationTargetException e11) {
            e2 = e11;
            str = "";
        } catch (Exception e12) {
            e = e12;
            str = "";
        }
        try {
            LogUtil.i(TAG, "StrUtil [createTransId] use reflex");
        } catch (ClassNotFoundException e13) {
            e6 = e13;
            LogUtil.w(TAG, "StrUtil [createTransId] ClassNotFoundException=" + e6);
            LogUtil.i(TAG, "StrUtil [createTransId] transId=" + str);
            return str;
        } catch (IllegalAccessException e14) {
            e5 = e14;
            LogUtil.w(TAG, "StrUtil [createTransId] IllegalAccessException=" + e5);
            LogUtil.i(TAG, "StrUtil [createTransId] transId=" + str);
            return str;
        } catch (IllegalArgumentException e15) {
            e4 = e15;
            LogUtil.w(TAG, "StrUtil [createTransId] IllegalArgumentException=" + e4);
            LogUtil.i(TAG, "StrUtil [createTransId] transId=" + str);
            return str;
        } catch (NoSuchMethodException e16) {
            e3 = e16;
            LogUtil.w(TAG, "StrUtil [createTransId] NoSuchMethodException=" + e3);
            LogUtil.i(TAG, "StrUtil [createTransId] transId=" + str);
            return str;
        } catch (InvocationTargetException e17) {
            e2 = e17;
            LogUtil.w(TAG, "StrUtil [createTransId] InvocationTargetException=" + e2);
            LogUtil.i(TAG, "StrUtil [createTransId] transId=" + str);
            return str;
        } catch (Exception e18) {
            e = e18;
            LogUtil.w(TAG, "StrUtil [createTransId] Exception=" + e);
            LogUtil.i(TAG, "StrUtil [createTransId] transId=" + str);
            return str;
        }
        LogUtil.i(TAG, "StrUtil [createTransId] transId=" + str);
        return str;
    }

    public static void getUniqueData(Context context) {
        Log.i(TAG, "StrUtil [getUniqueData] start");
        if (context == null) {
            Log.w(TAG, "StrUtil [getUniqueData] context error");
            return;
        }
        ModulesManager.getInst().init(context);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "getDeviceBaseInfo");
            jSONObject.put("isNoAndroidId", false);
            String strExtendFunc = ModulesManager.getInst().extendFunc(CUniLogger.source, "deviceInfo", jSONObject.toString());
            Log.i("Downloader", "DownloadProxy [getUniqueData] resultString=" + strExtendFunc);
            JSONObject jSONObject2 = new JSONObject(strExtendFunc);
            if (jSONObject2.has("imei")) {
                DownloadInitInfo.getInstances().setmImei(jSONObject2.optString("imei"));
            }
            if (jSONObject2.has("imsi")) {
                DownloadInitInfo.getInstances().setmImsi(jSONObject2.optString("imsi"));
            }
            if (jSONObject2.has("udid")) {
                DownloadInitInfo.getInstances().setmUdid(jSONObject2.optString("udid"));
            }
            if (jSONObject2.has("transId")) {
                DownloadInitInfo.getInstances().setmTransid(jSONObject2.optString("transId"));
            }
            if (jSONObject2.has("unisdkDeviceId")) {
                DownloadInitInfo.getInstances().setmUnisdkDeviceId(jSONObject2.optString("unisdkDeviceId"));
            }
            if (jSONObject2.has("macAddress")) {
                DownloadInitInfo.getInstances().setmMacAddr(jSONObject2.optString("macAddress"));
            }
            if (jSONObject2.has("localIpAddress")) {
                DownloadInitInfo.getInstances().setmLocalIp(jSONObject2.optString("localIpAddress"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Downloader", "DownloadProxy [getUniqueData] Exception=" + e.toString());
        }
    }
}