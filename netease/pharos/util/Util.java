package com.netease.pharos.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import com.alipay.sdk.m.s.a;
import com.facebook.hermes.intl.Constants;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.pharos.Const;
import com.xiaomi.onetrack.util.z;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class Util {
    private static final int RAW_OFFSET_EAST_8 = 28800000;
    private static final String TAG = "StrUtil";
    public static AtomicBoolean isNeedReadIp = new AtomicBoolean(true);
    private static String[] ips = null;

    public static String getUpperVersion() {
        return "2";
    }

    public static File create2kFile(Context context) throws IOException {
        File file = new File(context.getFilesDir().getPath(), Const.UPLOAD_FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(new byte[2048]);
                fileOutputStream.close();
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        return file;
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

    public static String getDomainFromUrl(String str) {
        int iIndexOf;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String strSubstring = str.substring(str.indexOf("//") + 2);
        if (strSubstring.contains("/") || !strSubstring.contains(a.l)) {
            iIndexOf = strSubstring.indexOf(47);
        } else {
            iIndexOf = strSubstring.indexOf(63);
        }
        if (iIndexOf < 0) {
            iIndexOf = strSubstring.length();
        }
        return strSubstring.substring(0, iIndexOf);
    }

    public static String unicode2String(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (true) {
            int iIndexOf = str.indexOf("\\u", i);
            if (iIndexOf != -1) {
                sb.append(str.substring(i, iIndexOf));
                if (iIndexOf + 5 < str.length()) {
                    i = iIndexOf + 6;
                    sb.append((char) Integer.parseInt(str.substring(iIndexOf + 2, i), 16));
                }
            } else {
                return sb.toString();
            }
        }
    }

    public static String getDeviceId() throws JSONException {
        String str = System.currentTimeMillis() + "";
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getUDID");
            return ModulesManager.getInst().extendFunc("pharos", "deviceInfo", jSONObject.toString());
        } catch (Exception e) {
            LogUtil.i(TAG, "Exception=" + e);
            return str;
        }
    }

    public static String getUnisdkDeviceId() throws JSONException {
        Log.i("pharos", "Util [getUnisdkDeviceId] start");
        String deviceId = getDeviceId();
        Log.i("pharos", "Util [getUnisdkDeviceId] final udid =" + deviceId);
        return deviceId;
    }

    public static String getTransId() throws JSONException {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("methodId", "getTransId");
            jSONObject.put("isNoAndroidId", true);
            return ModulesManager.getInst().extendFunc("pharos", "deviceInfo", jSONObject.toString());
        } catch (Exception unused) {
            return "";
        }
    }

    public static String replaceDomainWithIpAddr(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
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
        return sb.toString();
    }

    public static boolean isApkDebugable(Context context) {
        try {
            boolean zEquals = Environment.getExternalStorageState().equals("mounted");
            Log.i(TAG, "isApkDebugable debugLogFile sdCardExist=" + zEquals);
            if (zEquals) {
                String str = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ".data" + File.separator + "ntUniSDK" + File.separator + Constants.SENSITIVITY_BASE + File.separator + "debug_log";
                Log.i(TAG, "isApkDebugable debugLogFile patch=" + str);
                if (new File(str).exists()) {
                    return true;
                }
            }
        } catch (Throwable th) {
            LogUtil.i(TAG, "isApkDebugable Exception2=" + th);
        }
        try {
            return (context.getApplicationInfo().flags & 2) != 0;
        } catch (Throwable th2) {
            LogUtil.i(TAG, "isApkDebugable Exception1=" + th2);
            return false;
        }
    }

    public static int getCellId() {
        try {
            return Integer.parseInt(getSystemParams("getCellId"));
        } catch (Throwable unused) {
            return -1;
        }
    }

    public static String getCountryCode() {
        LogUtil.i(TAG, "getCountryCode getCountryCode start ");
        String country = com.tencent.connect.common.Constants.APP_VERSION_UNKNOWN;
        try {
            country = Locale.getDefault().getCountry();
            LogUtil.w(TAG, "getCountryCode = " + country);
            return country;
        } catch (Exception e) {
            LogUtil.w(TAG, "NetDevices [getCountryCode] Exception = " + e);
            e.printStackTrace();
            return country;
        }
    }

    public static synchronized String getSystemParams(String str) {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject();
            jSONObject.put("methodId", str);
        } catch (Exception unused) {
            return "";
        }
        return ModulesManager.getInst().extendFunc("pharos", "deviceInfo", jSONObject.toString());
    }

    public static synchronized String getLocalIp() {
        return getSystemParams("getLocalIpAddress");
    }

    public static synchronized String getLocalIpv6() {
        return "";
    }

    @Deprecated
    private static synchronized String[] getLocalIpAddress() {
        if (ips != null && !isNeedReadIp.get()) {
            return ips;
        }
        ips = new String[4];
        isNeedReadIp.set(true);
        try {
            Iterator it = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
            while (it.hasNext()) {
                NetworkInterface networkInterface = (NetworkInterface) it.next();
                Iterator it2 = Collections.list(networkInterface.getInetAddresses()).iterator();
                while (it2.hasNext()) {
                    InetAddress inetAddress = (InetAddress) it2.next();
                    String hostAddress = inetAddress.getHostAddress();
                    String displayName = networkInterface.getDisplayName();
                    if (displayName != null && !inetAddress.isAnyLocalAddress() && !inetAddress.isLoopbackAddress()) {
                        if (displayName.contains("wlan")) {
                            LogUtil.i(TAG, "getLocalIpAddress#wlan-->:" + hostAddress + ", displayName:" + networkInterface.getDisplayName() + ", name:" + networkInterface.getName());
                            if (isipv4(hostAddress)) {
                                String[] strArr = ips;
                                if (hostAddress == null) {
                                    hostAddress = "";
                                }
                                strArr[0] = hostAddress;
                            } else if (inetAddress instanceof Inet6Address) {
                                String str = hostAddress.split(":")[0];
                                if (str.startsWith("2") || str.startsWith("3")) {
                                    String[] strArr2 = ips;
                                    if (hostAddress == null) {
                                        hostAddress = "";
                                    }
                                    strArr2[1] = hostAddress;
                                }
                            } else {
                                continue;
                            }
                        } else if (displayName.contains("rmnet") || displayName.contains("ccmni") || displayName.contains("wimax") || displayName.contains("ccinet") || displayName.contains("vsnet") || displayName.contains("qmi")) {
                            LogUtil.i(TAG, "getLocalIpAddress#mobile-->:" + hostAddress + ", displayName:" + networkInterface.getDisplayName() + ", name:" + networkInterface.getName());
                            if (isipv4(hostAddress)) {
                                String[] strArr3 = ips;
                                if (hostAddress == null) {
                                    hostAddress = "";
                                }
                                strArr3[3] = hostAddress;
                            } else if (inetAddress instanceof Inet6Address) {
                                String str2 = hostAddress.split(":")[0];
                                if (str2.startsWith("2") || str2.startsWith("3")) {
                                    String[] strArr4 = ips;
                                    if (hostAddress == null) {
                                        hostAddress = "";
                                    }
                                    strArr4[2] = hostAddress;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
            isNeedReadIp.set(false);
        } catch (Exception e) {
            LogUtil.e("localip", e.toString());
        }
        return ips;
    }

    public static String intToIp(int i) {
        return (i & 255) + "." + ((i >> 8) & 255) + "." + ((i >> 16) & 255) + "." + ((i >> 24) & 255);
    }

    private static int getTimeZoneRawOffset() {
        return TimeZone.getDefault().getRawOffset();
    }

    public static boolean isZoneEast8() {
        return RAW_OFFSET_EAST_8 == getTimeZoneRawOffset();
    }

    public static String getHttpDnsDomain2IpUrl(String str, String str2) {
        StringBuffer stringBuffer = new StringBuffer("https://");
        stringBuffer.append(str).append("/v2/?domain=").append(str2);
        return stringBuffer.toString();
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

    public static int string2Int(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            LogUtil.e(TAG, "String2Int Exception =" + e);
            return -1;
        }
    }

    public static String getDecisionTag(String str) {
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "Util [getDecisionTag] url is error");
            return null;
        }
        return str.substring(str.lastIndexOf("/") + 1);
    }

    public static boolean isipv4(String str) throws NumberFormatException {
        String[] strArrSplit;
        if (TextUtils.isEmpty(str) || str.length() == 0 || (strArrSplit = str.split(z.f2789a)) == null || strArrSplit.length != 4) {
            return false;
        }
        for (String str2 : strArrSplit) {
            try {
                int i = Integer.parseInt(str2);
                if (i < 0 || i > 255) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "Util [Isipv4] Exception =" + e);
                return false;
            }
        }
        return true;
    }

    public static boolean info2File(String str, String str2, boolean z) {
        LogUtil.i(TAG, "Util [info2File]");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogUtil.i(TAG, "Util [info2File] param error");
            return false;
        }
        return info2File(str, new File(str2), z);
    }

    private static String encodeHex(byte[] bArr) {
        char[] charArray = "0123456789abcdef".toCharArray();
        char[] cArr = new char[bArr.length * 2];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            int i3 = i * 2;
            cArr[i3] = charArray[i2 >>> 4];
            cArr[i3 + 1] = charArray[i2 & 15];
        }
        return new String(cArr);
    }

    public static String getFileMd5(Context context, String str) {
        return (context == null || TextUtils.isEmpty(str)) ? "" : getFileMd5(context.getFileStreamPath(str));
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0053 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getFileMd5(java.io.File r5) throws java.lang.Throwable {
        /*
            if (r5 == 0) goto L5c
            boolean r0 = r5.exists()
            if (r0 != 0) goto L9
            goto L5c
        L9:
            r0 = 0
            java.lang.String r1 = "MD5"
            java.security.MessageDigest r1 = java.security.MessageDigest.getInstance(r1)     // Catch: java.lang.Throwable -> L3d java.lang.Exception -> L3f
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L3d java.lang.Exception -> L3f
            r2.<init>(r5)     // Catch: java.lang.Throwable -> L3d java.lang.Exception -> L3f
            r5 = 8192(0x2000, float:1.148E-41)
            byte[] r5 = new byte[r5]     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L4f
        L19:
            int r3 = r2.read(r5)     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L4f
            r4 = -1
            if (r3 == r4) goto L25
            r4 = 0
            r1.update(r5, r4, r3)     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L4f
            goto L19
        L25:
            java.lang.String r5 = new java.lang.String     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L4f
            byte[] r1 = r1.digest()     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L4f
            java.lang.String r1 = encodeHex(r1)     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L4f
            r5.<init>(r1)     // Catch: java.lang.Exception -> L3b java.lang.Throwable -> L4f
            r2.close()     // Catch: java.io.IOException -> L36
            goto L3a
        L36:
            r0 = move-exception
            r0.printStackTrace()
        L3a:
            return r5
        L3b:
            r5 = move-exception
            goto L41
        L3d:
            r5 = move-exception
            goto L51
        L3f:
            r5 = move-exception
            r2 = r0
        L41:
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L4f
            if (r2 == 0) goto L4e
            r2.close()     // Catch: java.io.IOException -> L4a
            goto L4e
        L4a:
            r5 = move-exception
            r5.printStackTrace()
        L4e:
            return r0
        L4f:
            r5 = move-exception
            r0 = r2
        L51:
            if (r0 == 0) goto L5b
            r0.close()     // Catch: java.io.IOException -> L57
            goto L5b
        L57:
            r0 = move-exception
            r0.printStackTrace()
        L5b:
            throw r5
        L5c:
            java.lang.String r5 = ""
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.util.Util.getFileMd5(java.io.File):java.lang.String");
    }

    public static boolean info2File(String str, File file, boolean z) throws Throwable {
        StringBuilder sb;
        if (file != null && file.exists() && z) {
            LogUtil.i(TAG, "Util [info2File] \u6587\u4ef6\u5b58\u5728\uff0c\u5220\u9664\u6587\u4ef6");
            file.delete();
        }
        if (file != null && !file.getParentFile().exists()) {
            LogUtil.i(TAG, "Util [info2File] \u7236\u76ee\u5f55\u4e0d\u5b58\u5728\uff0c\u521b\u5efa\u7236\u76ee\u5f55");
            file.getParentFile().mkdirs();
        }
        if (file != null && !file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LogUtil.w(TAG, "Util [info2File] createNewFile =" + e);
            }
        }
        if (file != null && file.exists()) {
            BufferedWriter bufferedWriter = null;
            try {
                try {
                    BufferedWriter bufferedWriter2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                    try {
                        LogUtil.i(TAG, "Util [info2File] config=" + str);
                        bufferedWriter2.write(str);
                        bufferedWriter2.flush();
                        try {
                            bufferedWriter2.close();
                            return true;
                        } catch (IOException e2) {
                            LogUtil.w(TAG, "Util [info2File] IOException2=" + e2);
                            return true;
                        }
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        bufferedWriter = bufferedWriter2;
                        LogUtil.w(TAG, "Util [info2File] FileNotFoundException=" + e);
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e4) {
                                e = e4;
                                sb = new StringBuilder("Util [info2File] IOException2=");
                                sb.append(e);
                                LogUtil.w(TAG, sb.toString());
                                return false;
                            }
                        }
                        return false;
                    } catch (IOException e5) {
                        e = e5;
                        bufferedWriter = bufferedWriter2;
                        LogUtil.w(TAG, "Util [info2File] IOException1=" + e);
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e6) {
                                e = e6;
                                sb = new StringBuilder("Util [info2File] IOException2=");
                                sb.append(e);
                                LogUtil.w(TAG, sb.toString());
                                return false;
                            }
                        }
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        bufferedWriter = bufferedWriter2;
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (IOException e7) {
                                LogUtil.w(TAG, "Util [info2File] IOException2=" + e7);
                            }
                        }
                        throw th;
                    }
                } catch (FileNotFoundException e8) {
                    e = e8;
                } catch (IOException e9) {
                    e = e9;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
        return false;
    }

    public static String file2Info(String str) {
        LogUtil.i(TAG, "Util [file2Info]");
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "Util [file2Info] param error");
            return null;
        }
        return file2Info(new File(str));
    }

    public static ArrayList<String> file2Infos(File file) throws IOException {
        ArrayList<String> arrayList = new ArrayList<>();
        if (file != null && !file.exists()) {
            LogUtil.i(TAG, "Util [file2Infos] \u6587\u4ef6\u4e0d\u5b58\u5728");
            return arrayList;
        }
        BufferedReader bufferedReader = null;
        try {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            while (true) {
                try {
                    String line = bufferedReader2.readLine();
                    if (line == null) {
                        break;
                    }
                    arrayList.add(line);
                } catch (Throwable unused) {
                    bufferedReader = bufferedReader2;
                    arrayList.clear();
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return arrayList;
                }
            }
        } catch (Throwable unused2) {
        }
        return arrayList;
    }

    public static String file2Info(File file) throws IOException {
        if (file != null && !file.exists()) {
            LogUtil.i(TAG, "Util [file2Info] \u6587\u4ef6\u4e0d\u5b58\u5728");
            return null;
        }
        StringBuilder sb = new StringBuilder();
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
            LogUtil.w(TAG, "Util [info2File] Exception=" + e);
        }
        String string = sb.toString();
        LogUtil.i(TAG, "Util [file2Info] result =" + string);
        return string;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static String getAssetFileContent(Context context, String str) throws Throwable {
        InputStream inputStreamOpen;
        InputStream inputStream = null;
        str = null;
        str = null;
        String str2 = null;
        if (context != null) {
            try {
                if (!TextUtils.isEmpty(str)) {
                    try {
                        inputStreamOpen = context.getAssets().open(str);
                    } catch (Exception e) {
                        e = e;
                        inputStreamOpen = null;
                    } catch (Throwable th) {
                        th = th;
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e2) {
                                LogUtil.i(TAG, "Utils [getAssetFileContent] IOException2=" + e2);
                                e2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                    try {
                        byte[] bArr = new byte[inputStreamOpen.available()];
                        inputStreamOpen.read(bArr);
                        String str3 = new String(bArr);
                        Context context2 = inputStreamOpen;
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                                context2 = inputStreamOpen;
                            } catch (IOException e3) {
                                LogUtil.i(TAG, "Utils [getAssetFileContent] IOException2=" + e3);
                                e3.printStackTrace();
                                context2 = e3;
                            }
                        }
                        str2 = str3;
                        context = context2;
                    } catch (Exception e4) {
                        e = e4;
                        LogUtil.i(TAG, "Utils [getAssetFileContent] IOException1=" + e);
                        e.printStackTrace();
                        context = inputStreamOpen;
                        if (inputStreamOpen != null) {
                            try {
                                inputStreamOpen.close();
                                context = inputStreamOpen;
                            } catch (IOException e5) {
                                LogUtil.i(TAG, "Utils [getAssetFileContent] IOException2=" + e5);
                                e5.printStackTrace();
                                context = e5;
                            }
                        }
                        return str2;
                    }
                    return str2;
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = context;
            }
        }
        LogUtil.i(TAG, "Utils [getAssetFileContent] param is error");
        return null;
    }

    public static String getNetworkIspName(String str) {
        String str2 = "unknown";
        LogUtil.e(TAG, "getNetworkIsp network_isp = " + str);
        try {
            if (!TextUtils.isEmpty(str)) {
                LogUtil.i(TAG, "network_isp=" + str);
                if (str.startsWith("46000") || str.startsWith("46002") || str.startsWith("46007") || str.startsWith("46020") || str.startsWith("46008") || str.startsWith("10086")) {
                    str2 = "cmcc";
                } else if (str.startsWith("46001") || str.startsWith("46006") || str.startsWith("46009") || str.startsWith("10010")) {
                    str2 = "cucc";
                } else if (str.startsWith("46003") || str.startsWith("46005") || str.startsWith("46011") || str.startsWith("10000")) {
                    str2 = "ctcc";
                }
            } else {
                LogUtil.i(TAG, "\u5339\u914d\u5931\u8d25");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "getNetworkIsp Exception = " + e);
        }
        return str2;
    }
}