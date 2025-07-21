package com.netease.download.reporter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import com.alipay.sdk.m.s.a;
import com.netease.download.Const;
import com.netease.download.config.ConfigParams;
import com.netease.download.config.ConfigProxy;
import com.netease.download.util.DeviceInfo;
import com.netease.download.util.LogUtil;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.Response;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.TimeZone;
import org.apache.james.mime4j.util.CharsetUtil;
import org.slf4j.Marker;

/* loaded from: classes4.dex */
public class ReportUtil {
    private static final String TAG = "ReportUtil";
    private static ReportUtil sReportUtil;
    private Context mContext = null;

    public String getOsName() {
        return "android";
    }

    public String getUdtVer() {
        return Const.VERSION;
    }

    private ReportUtil() {
    }

    public static ReportUtil getInstances() {
        if (sReportUtil == null) {
            sReportUtil = new ReportUtil();
            LogUtil.i(TAG, "ReportUtil [getInstances] new Instances");
        }
        return sReportUtil;
    }

    public void init(Context context) {
        if (this.mContext == null) {
            LogUtil.i(TAG, "ReportUtil [init] context is null");
            this.mContext = context;
        } else {
            LogUtil.i(TAG, "ReportUtil [init] context is not null");
        }
    }

    public String getNetworkType() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
            NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            return (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) ? "" : activeNetworkInfo.getType() == 1 ? "WIFI" : activeNetworkInfo.getType() == 0 ? ConstProp.NT_AUTH_NAME_MOBILE : "";
        } catch (Exception e) {
            LogUtil.i(TAG, "ReportUtil [getNetworkType] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---Network getSubtype Exception: " + e);
            return "";
        }
    }

    public int getNetworkSignal() {
        WifiManager wifiManager;
        try {
            Context context = this.mContext;
            if (context == null || (wifiManager = (WifiManager) context.getSystemService("wifi")) == null) {
                return -1;
            }
            WifiInfo connectionInfo = HookManager.getConnectionInfo(wifiManager);
            if (connectionInfo.getBSSID() != null) {
                return WifiManager.calculateSignalLevel(connectionInfo.getRssi(), 5);
            }
            return -1;
        } catch (Exception e) {
            LogUtil.e(TAG, "ReportUtil [getNetworkSignal] Exception=" + e);
            return -1;
        }
    }

    public String getTimeZone() {
        String[] strArrSplit;
        String displayName = TimeZone.getDefault().getDisplayName(false, 0);
        if (!TextUtils.isEmpty(displayName) && (strArrSplit = displayName.split("\\+|:")) != null && strArrSplit.length > 1) {
            displayName = Marker.ANY_NON_NULL_MARKER + strArrSplit[1];
        }
        LogUtil.i(TAG, "ReportUtil [getTimeZone] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u65f6\u5dee=" + displayName);
        return displayName;
    }

    public String getAreaZone() {
        String id = TimeZone.getDefault().getID();
        LogUtil.i(TAG, "ReportUtil [getTimeZone] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u5730\u533a=" + id);
        return id;
    }

    public void getQuery() {
        ConfigParams configParams = ConfigProxy.getInstances().getmConfigParams();
        if (configParams == null || !configParams.mIpDnsPicker) {
            return;
        }
        LogUtil.i(TAG, "ReportUtil [getQuery] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u8bf7\u6c42nstool\uff0c\u83b7\u53d6\u7f51\u5173\uff0cdns, ipDnsPicker=" + configParams.mIpDnsPicker);
        new Thread(new Runnable() { // from class: com.netease.download.reporter.ReportUtil.1
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                LogUtil.i(ReportUtil.TAG, "ReportUtil [getQuery] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u8bf7\u6c42nstool\uff0c\u83b7\u53d6\u7f51\u5173\uff0cdns");
                new Callback() { // from class: com.netease.download.reporter.ReportUtil.1.1
                    C00611() {
                    }

                    @Override // com.netease.ntunisdk.okhttp3.Callback
                    public void onFailure(Call call, IOException iOException) {
                        LogUtil.stepLog("ReportUtil [okhttpCallback] [onFailure] start");
                        if (call == null) {
                            return;
                        }
                        LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onFailure] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
                    }

                    @Override // com.netease.ntunisdk.okhttp3.Callback
                    public void onResponse(Call call, Response response) throws IOException {
                        LogUtil.stepLog("ReportUtil [okhttpCallback] [onResponse] start");
                        if (call == null || response == null) {
                            return;
                        }
                        LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
                        LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream(), RSASignature.c));
                        HashMap map = new HashMap();
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            String[] strArrSplit = line.split("=");
                            map.put(strArrSplit[0], strArrSplit[1]);
                        }
                        LogUtil.d(ReportUtil.TAG, "ReportUtil [getQuery] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u8bf7\u6c42nstool,\u7ed3\u679c= " + map);
                        if (map.containsKey("netdns")) {
                        }
                        if (map.containsKey("gw")) {
                        }
                        if (map.containsKey("gwdns")) {
                        }
                    }
                };
            }

            /* renamed from: com.netease.download.reporter.ReportUtil$1$1 */
            class C00611 implements Callback {
                C00611() {
                }

                @Override // com.netease.ntunisdk.okhttp3.Callback
                public void onFailure(Call call, IOException iOException) {
                    LogUtil.stepLog("ReportUtil [okhttpCallback] [onFailure] start");
                    if (call == null) {
                        return;
                    }
                    LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onFailure] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
                }

                @Override // com.netease.ntunisdk.okhttp3.Callback
                public void onResponse(Call call, Response response) throws IOException {
                    LogUtil.stepLog("ReportUtil [okhttpCallback] [onResponse] start");
                    if (call == null || response == null) {
                        return;
                    }
                    LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
                    LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream(), RSASignature.c));
                    HashMap map = new HashMap();
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        String[] strArrSplit = line.split("=");
                        map.put(strArrSplit[0], strArrSplit[1]);
                    }
                    LogUtil.d(ReportUtil.TAG, "ReportUtil [getQuery] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u8bf7\u6c42nstool,\u7ed3\u679c= " + map);
                    if (map.containsKey("netdns")) {
                    }
                    if (map.containsKey("gw")) {
                    }
                    if (map.containsKey("gwdns")) {
                    }
                }
            }
        }).start();
    }

    /* renamed from: com.netease.download.reporter.ReportUtil$1 */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            LogUtil.i(ReportUtil.TAG, "ReportUtil [getQuery] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u8bf7\u6c42nstool\uff0c\u83b7\u53d6\u7f51\u5173\uff0cdns");
            new Callback() { // from class: com.netease.download.reporter.ReportUtil.1.1
                C00611() {
                }

                @Override // com.netease.ntunisdk.okhttp3.Callback
                public void onFailure(Call call, IOException iOException) {
                    LogUtil.stepLog("ReportUtil [okhttpCallback] [onFailure] start");
                    if (call == null) {
                        return;
                    }
                    LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onFailure] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
                }

                @Override // com.netease.ntunisdk.okhttp3.Callback
                public void onResponse(Call call, Response response) throws IOException {
                    LogUtil.stepLog("ReportUtil [okhttpCallback] [onResponse] start");
                    if (call == null || response == null) {
                        return;
                    }
                    LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
                    LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream(), RSASignature.c));
                    HashMap map = new HashMap();
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        String[] strArrSplit = line.split("=");
                        map.put(strArrSplit[0], strArrSplit[1]);
                    }
                    LogUtil.d(ReportUtil.TAG, "ReportUtil [getQuery] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u8bf7\u6c42nstool,\u7ed3\u679c= " + map);
                    if (map.containsKey("netdns")) {
                    }
                    if (map.containsKey("gw")) {
                    }
                    if (map.containsKey("gwdns")) {
                    }
                }
            };
        }

        /* renamed from: com.netease.download.reporter.ReportUtil$1$1 */
        class C00611 implements Callback {
            C00611() {
            }

            @Override // com.netease.ntunisdk.okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                LogUtil.stepLog("ReportUtil [okhttpCallback] [onFailure] start");
                if (call == null) {
                    return;
                }
                LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onFailure] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
            }

            @Override // com.netease.ntunisdk.okhttp3.Callback
            public void onResponse(Call call, Response response) throws IOException {
                LogUtil.stepLog("ReportUtil [okhttpCallback] [onResponse] start");
                if (call == null || response == null) {
                    return;
                }
                LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onResponse] Call Header=" + call.request().headers().toMultimap().toString() + ", request = " + call.request().toString());
                LogUtil.i(ReportUtil.TAG, "ReportUtil [okhttpCallback] [onResponse] Response Header=" + response.headers().toMultimap().toString() + ", hashCode=" + response.code() + ", resUrl=" + response.request().url() + ", protocol=" + response.protocol() + ", " + response.request().toString());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.body().byteStream(), RSASignature.c));
                HashMap map = new HashMap();
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    String[] strArrSplit = line.split("=");
                    map.put(strArrSplit[0], strArrSplit[1]);
                }
                LogUtil.d(ReportUtil.TAG, "ReportUtil [getQuery] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u8bf7\u6c42nstool,\u7ed3\u679c= " + map);
                if (map.containsKey("netdns")) {
                }
                if (map.containsKey("gw")) {
                }
                if (map.containsKey("gwdns")) {
                }
            }
        }
    }

    public String getOsVer() {
        return Build.VERSION.RELEASE;
    }

    /* renamed from: com.netease.download.reporter.ReportUtil$2 */
    class AnonymousClass2 implements Runnable {
        final /* synthetic */ String val$gateway;

        AnonymousClass2(String str) {
            str = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            Process processExec = null;
            try {
                try {
                    LogUtil.i(ReportUtil.TAG, "ReportUtil [ping] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---ping \u7f51\u5173=" + str);
                    processExec = Runtime.getRuntime().exec("/system/bin/ping -c 1 www.baidu.com");
                    new String();
                    new String();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    new String();
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line == null) {
                            break;
                        }
                        stringBuffer.append(line + CharsetUtil.CRLF);
                    }
                    LogUtil.i(ReportUtil.TAG, "ReportUtil [ping] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---ping\u4fe1\u606f=" + stringBuffer.toString());
                    if (processExec == null) {
                        return;
                    }
                } catch (IOException e) {
                    LogUtil.e(ReportUtil.TAG, "ReportUtil [ping] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---ping IOException \u5f02\u5e38 =" + e.toString());
                    e.printStackTrace();
                    if (processExec == null) {
                        return;
                    }
                }
                processExec.destroy();
            } catch (Throwable th) {
                if (processExec != null) {
                    processExec.destroy();
                }
                throw th;
            }
        }
    }

    public void ping(String str) {
        new Thread(new Runnable() { // from class: com.netease.download.reporter.ReportUtil.2
            final /* synthetic */ String val$gateway;

            AnonymousClass2(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                Process processExec = null;
                try {
                    try {
                        LogUtil.i(ReportUtil.TAG, "ReportUtil [ping] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---ping \u7f51\u5173=" + str);
                        processExec = Runtime.getRuntime().exec("/system/bin/ping -c 1 www.baidu.com");
                        new String();
                        new String();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processExec.getInputStream()));
                        StringBuffer stringBuffer = new StringBuffer();
                        new String();
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            stringBuffer.append(line + CharsetUtil.CRLF);
                        }
                        LogUtil.i(ReportUtil.TAG, "ReportUtil [ping] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---ping\u4fe1\u606f=" + stringBuffer.toString());
                        if (processExec == null) {
                            return;
                        }
                    } catch (IOException e) {
                        LogUtil.e(ReportUtil.TAG, "ReportUtil [ping] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---ping IOException \u5f02\u5e38 =" + e.toString());
                        e.printStackTrace();
                        if (processExec == null) {
                            return;
                        }
                    }
                    processExec.destroy();
                } catch (Throwable th) {
                    if (processExec != null) {
                        processExec.destroy();
                    }
                    throw th;
                }
            }
        }).start();
    }

    private int pingExec(String str, int i, int i2) {
        int iIndexOf;
        int i3;
        Process processExec = null;
        try {
            try {
                processExec = Runtime.getRuntime().exec("/system/bin/ping -c " + i + " -s " + i2 + " " + str);
                LineNumberReader lineNumberReader = new LineNumberReader(new InputStreamReader(processExec.getInputStream()));
                String str2 = "";
                while (true) {
                    String line = lineNumberReader.readLine();
                    if (line == null) {
                        try {
                            break;
                        } catch (Exception unused) {
                        }
                    } else if (line.contains("loss") && line.contains("%")) {
                        str2 = str2 + line;
                    }
                }
                LogUtil.i(TAG, "ReportUtil [pingExec] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---ping \u4fe1\u606f=" + str2.toString());
                String[] strArrSplit = str2.split(",");
                int i4 = 0;
                while (true) {
                    if (i4 >= strArrSplit.length) {
                        iIndexOf = -1;
                        break;
                    }
                    if (strArrSplit[i4].contains("loss") && strArrSplit[i4].contains("%")) {
                        str2 = strArrSplit[i4];
                        iIndexOf = str2.indexOf(37);
                        break;
                    }
                    i4++;
                }
                int i5 = iIndexOf;
                while (true) {
                    i3 = i5 - 1;
                    if (i5 <= 0 || str2.charAt(i3) < '0' || str2.charAt(i3) > '9') {
                        break;
                    }
                    i5 = i3;
                }
                int i6 = i3 + 1;
                if (i6 == iIndexOf || i6 < 0 || iIndexOf > str2.length() - 1) {
                    return -1;
                }
                return Integer.valueOf(str2.substring(i6, iIndexOf)).intValue();
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    processExec.destroy();
                } catch (Exception unused2) {
                }
                return -1;
            }
        } finally {
            try {
                processExec.destroy();
            } catch (Exception unused3) {
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:186:0x01da A[PHI: r13
  0x01da: PHI (r13v10 java.lang.Process) = (r13v7 java.lang.Process), (r13v8 java.lang.Process), (r13v9 java.lang.Process), (r13v12 java.lang.Process) binds: [B:185:0x01d8, B:191:0x01f9, B:195:0x0215, B:198:0x022f] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String ping(java.lang.String r17, int r18, int r19) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 594
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.reporter.ReportUtil.ping(java.lang.String, int, int):java.lang.String");
    }

    public String getDomainFromUrl(String str) {
        int iIndexOf;
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String strSubstring = str.substring(str.indexOf("//") + 2, str.length());
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

    public String replaceDomainWithIpAddr(String str, String str2, String str3) {
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

    public String getSystemModel() {
        return Build.MODEL;
    }

    public String[] getCpuInfo() throws IOException {
        String[] strArrSplit;
        String[] strArrSplit2;
        String[] strArr = {"", ""};
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"), 8192);
            String line = bufferedReader.readLine();
            if (!TextUtils.isEmpty(line) && (strArrSplit2 = line.split("\\s+")) != null) {
                for (int i = 2; i < strArrSplit2.length; i++) {
                    strArr[0] = strArr[0] + strArrSplit2[i] + " ";
                }
            }
            String line2 = bufferedReader.readLine();
            if (!TextUtils.isEmpty(line2) && (strArrSplit = line2.split("\\s+")) != null) {
                strArr[1] = strArr[1] + strArrSplit[2];
            }
            bufferedReader.close();
        } catch (IOException e) {
            LogUtil.w(TAG, "ReportInfo [getCpuInfo] IOException=" + e);
        } catch (Exception e2) {
            LogUtil.w(TAG, "ReportInfo [getCpuInfo] Exception=" + e2);
        }
        return strArr;
    }

    public String getCpuClockspeed() throws IOException {
        String[] cpuInfo = getCpuInfo();
        if (cpuInfo != null && cpuInfo.length >= 2) {
            String str = cpuInfo[1];
        }
        return DeviceInfo.getCPUMaxFreqKHz() + "";
    }

    public String getCpuModel() throws IOException {
        String[] cpuInfo = getCpuInfo();
        if (cpuInfo == null || cpuInfo.length < 1) {
            return null;
        }
        return cpuInfo[0];
    }

    public String getTransid() throws ClassNotFoundException {
        Exception e;
        String str;
        InvocationTargetException e2;
        NoSuchMethodException e3;
        IllegalArgumentException e4;
        IllegalAccessException e5;
        ClassNotFoundException e6;
        if (this.mContext == null) {
            return "";
        }
        try {
            Class<?> cls = Class.forName("com.netease.ntunisdk.base.UniSdkUtils");
            str = (String) cls.getDeclaredMethod("getTransid", Context.class).invoke(cls, this.mContext);
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
            LogUtil.i(TAG, "ReportUtil [getTransid] use reflex");
        } catch (ClassNotFoundException e13) {
            e6 = e13;
            LogUtil.w(TAG, "ReportUtil [getTransid] ClassNotFoundException=" + e6);
            return str;
        } catch (IllegalAccessException e14) {
            e5 = e14;
            LogUtil.w(TAG, "ReportUtil [getTransid] IllegalAccessException=" + e5);
            return str;
        } catch (IllegalArgumentException e15) {
            e4 = e15;
            LogUtil.w(TAG, "ReportUtil [getTransid] IllegalArgumentException=" + e4);
            return str;
        } catch (NoSuchMethodException e16) {
            e3 = e16;
            LogUtil.w(TAG, "ReportUtil [getTransid] NoSuchMethodException=" + e3);
            return str;
        } catch (InvocationTargetException e17) {
            e2 = e17;
            LogUtil.w(TAG, "ReportUtil [getTransid] InvocationTargetException=" + e2);
            return str;
        } catch (Exception e18) {
            e = e18;
            LogUtil.w(TAG, "ReportUtil [getTransid] Exception=" + e);
            return str;
        }
        return str;
    }

    public String getChannel() throws ClassNotFoundException {
        String str;
        Exception e;
        InvocationTargetException e2;
        NoSuchMethodException e3;
        IllegalArgumentException e4;
        IllegalAccessException e5;
        ClassNotFoundException e6;
        try {
            Class<?> cls = Class.forName("com.netease.ntunisdk.base.SdkDownload");
            str = (String) cls.getDeclaredMethod("getChannel", new Class[0]).invoke(cls, new Object[0]);
            try {
                LogUtil.i(TAG, "ReportUtil [getAppChannel] use reflex");
            } catch (ClassNotFoundException e7) {
                e6 = e7;
                LogUtil.w(TAG, "ReportUtil [getAppChannel] ClassNotFoundException=" + e6);
                return str;
            } catch (IllegalAccessException e8) {
                e5 = e8;
                LogUtil.w(TAG, "ReportUtil [getAppChannel] IllegalAccessException=" + e5);
                return str;
            } catch (IllegalArgumentException e9) {
                e4 = e9;
                LogUtil.w(TAG, "ReportUtil [getAppChannel] IllegalArgumentException=" + e4);
                return str;
            } catch (NoSuchMethodException e10) {
                e3 = e10;
                LogUtil.w(TAG, "ReportUtil [getAppChannel] NoSuchMethodException=" + e3);
                return str;
            } catch (InvocationTargetException e11) {
                e2 = e11;
                LogUtil.w(TAG, "ReportUtil [getAppChannel] InvocationTargetException=" + e2);
                return str;
            } catch (Exception e12) {
                e = e12;
                LogUtil.w(TAG, "ReportUtil [getAppChannel] Exception=" + e);
                return str;
            }
        } catch (ClassNotFoundException e13) {
            str = "";
            e6 = e13;
        } catch (IllegalAccessException e14) {
            str = "";
            e5 = e14;
        } catch (IllegalArgumentException e15) {
            str = "";
            e4 = e15;
        } catch (NoSuchMethodException e16) {
            str = "";
            e3 = e16;
        } catch (InvocationTargetException e17) {
            str = "";
            e2 = e17;
        } catch (Exception e18) {
            str = "";
            e = e18;
        }
        return str;
    }

    public String getUnisdkVer() throws ClassNotFoundException {
        String str;
        Exception e;
        InvocationTargetException e2;
        NoSuchMethodException e3;
        IllegalArgumentException e4;
        IllegalAccessException e5;
        ClassNotFoundException e6;
        try {
            Class<?> cls = Class.forName("com.netease.ntunisdk.base.SdkMgr");
            str = (String) cls.getDeclaredMethod("getBaseVersion", new Class[0]).invoke(cls, new Object[0]);
            try {
                LogUtil.i(TAG, "ReportUtil [getUnisdkVer] use reflex");
            } catch (ClassNotFoundException e7) {
                e6 = e7;
                LogUtil.w(TAG, "ReportUtil [getUnisdkVer] ClassNotFoundException=" + e6);
                return str;
            } catch (IllegalAccessException e8) {
                e5 = e8;
                LogUtil.w(TAG, "ReportUtil [getUnisdkVer] IllegalAccessException=" + e5);
                return str;
            } catch (IllegalArgumentException e9) {
                e4 = e9;
                LogUtil.w(TAG, "ReportUtil [getUnisdkVer] IllegalArgumentException=" + e4);
                return str;
            } catch (NoSuchMethodException e10) {
                e3 = e10;
                LogUtil.w(TAG, "ReportUtil [getUnisdkVer] NoSuchMethodException=" + e3);
                return str;
            } catch (InvocationTargetException e11) {
                e2 = e11;
                LogUtil.w(TAG, "ReportUtil [getUnisdkVer] InvocationTargetException=" + e2);
                return str;
            } catch (Exception e12) {
                e = e12;
                LogUtil.w(TAG, "ReportUtil [getUnisdkVer] Exception=" + e);
                return str;
            }
        } catch (ClassNotFoundException e13) {
            str = "";
            e6 = e13;
        } catch (IllegalAccessException e14) {
            str = "";
            e5 = e14;
        } catch (IllegalArgumentException e15) {
            str = "";
            e4 = e15;
        } catch (NoSuchMethodException e16) {
            str = "";
            e3 = e16;
        } catch (InvocationTargetException e17) {
            str = "";
            e2 = e17;
        } catch (Exception e18) {
            str = "";
            e = e18;
        }
        return str;
    }

    public String getChanelVer() throws ClassNotFoundException {
        String str;
        Exception e;
        InvocationTargetException e2;
        NoSuchMethodException e3;
        IllegalArgumentException e4;
        IllegalAccessException e5;
        ClassNotFoundException e6;
        try {
            Class<?> cls = Class.forName("com.netease.ntunisdk.base.SdkDownload");
            str = (String) cls.getDeclaredMethod("getSDKVersion", new Class[0]).invoke(cls, new Object[0]);
            try {
                LogUtil.i(TAG, "ReportUtil [getAppChanelVer] use reflex");
            } catch (ClassNotFoundException e7) {
                e6 = e7;
                LogUtil.w(TAG, "ReportUtil [getAppChanelVer] ClassNotFoundException=" + e6);
                return str;
            } catch (IllegalAccessException e8) {
                e5 = e8;
                LogUtil.w(TAG, "ReportUtil [getAppChanelVer] IllegalAccessException=" + e5);
                return str;
            } catch (IllegalArgumentException e9) {
                e4 = e9;
                LogUtil.w(TAG, "ReportUtil [getAppChanelVer] IllegalArgumentException=" + e4);
                return str;
            } catch (NoSuchMethodException e10) {
                e3 = e10;
                LogUtil.w(TAG, "ReportUtil [getAppChanelVer] NoSuchMethodException=" + e3);
                return str;
            } catch (InvocationTargetException e11) {
                e2 = e11;
                LogUtil.w(TAG, "ReportUtil [getAppChanelVer] InvocationTargetException=" + e2);
                return str;
            } catch (Exception e12) {
                e = e12;
                LogUtil.w(TAG, "ReportUtil [getAppChanelVer] Exception=" + e);
                return str;
            }
        } catch (ClassNotFoundException e13) {
            str = "";
            e6 = e13;
        } catch (IllegalAccessException e14) {
            str = "";
            e5 = e14;
        } catch (IllegalArgumentException e15) {
            str = "";
            e4 = e15;
        } catch (NoSuchMethodException e16) {
            str = "";
            e3 = e16;
        } catch (InvocationTargetException e17) {
            str = "";
            e2 = e17;
        } catch (Exception e18) {
            str = "";
            e = e18;
        }
        return str;
    }
}