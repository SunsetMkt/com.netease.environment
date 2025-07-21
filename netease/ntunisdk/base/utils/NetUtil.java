package com.netease.ntunisdk.base.utils;

import android.text.TextUtils;
import com.netease.mpay.httpdns.HttpDns;
import com.netease.mpay.httpdns.ResolveDnsResult;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes3.dex */
public class NetUtil {
    private static final String CHARSET_UTF8 = "utf-8";
    public static int CONNECTION_TIMEOUT = 5000;
    private static final String ENCODING_GZIP = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HEADER_RANGE = "Range";
    private static final String HEADER_RANGE_BYTES_PREF = "bytes=";
    private static final String HEADER_RANGE_BYTES_SUFF = "-";
    public static final String HEADER_RANGE_END = "END";
    public static final String HEADER_RANGE_START = "START";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static int SO_TIMEOUT = 10000;
    private static String TAG = "UniSDK NetUtil";

    public interface HttpURLConnectionListener {
        void afterConnect(String str, HttpURLConnection httpURLConnection);

        void preConnect(HttpURLConnection httpURLConnection);
    }

    public interface InputStreamDealer<T> {
        T process(InputStream inputStream) throws Exception;
    }

    public static void wget(final String str, final WgetDoneCallback wgetDoneCallback, final HttpURLConnectionListener httpURLConnectionListener) {
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.utils.NetUtil.1
            /* JADX WARN: Removed duplicated region for block: B:56:0x00e7  */
            /* JADX WARN: Removed duplicated region for block: B:65:0x00fd  */
            /* JADX WARN: Removed duplicated region for block: B:67:0x0108  */
            /* JADX WARN: Removed duplicated region for block: B:77:0x011b  */
            /* JADX WARN: Removed duplicated region for block: B:86:0x0120 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:90:0x00ec A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public final void run() throws java.lang.Throwable {
                /*
                    Method dump skipped, instructions count: 297
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.NetUtil.AnonymousClass1.run():void");
            }
        });
    }

    public static void wget(String str, WgetDoneCallback wgetDoneCallback) {
        wget(str, wgetDoneCallback, null);
    }

    public static void wgetIncludeNewLine(final String str, final WgetDoneCallback wgetDoneCallback) {
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.utils.NetUtil.2
            /* JADX WARN: Removed duplicated region for block: B:49:0x00e5  */
            /* JADX WARN: Removed duplicated region for block: B:57:0x010e  */
            /* JADX WARN: Removed duplicated region for block: B:59:0x0119  */
            /* JADX WARN: Removed duplicated region for block: B:66:0x0125  */
            /* JADX WARN: Removed duplicated region for block: B:77:0x00ea A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:79:0x012a A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public final void run() throws java.lang.Throwable {
                /*
                    Method dump skipped, instructions count: 327
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.NetUtil.AnonymousClass2.run():void");
            }
        });
    }

    public static void wpost(final String str, final String str2, final WgetDoneCallback wgetDoneCallback) {
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.utils.NetUtil.3
            /* JADX WARN: Removed duplicated region for block: B:37:0x00d2  */
            /* JADX WARN: Removed duplicated region for block: B:41:0x00de  */
            /* JADX WARN: Removed duplicated region for block: B:43:0x00e9  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public final void run() throws java.lang.Throwable {
                /*
                    Method dump skipped, instructions count: 247
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.NetUtil.AnonymousClass3.run():void");
            }
        });
    }

    public static void wpost_http_https(String str, String str2, WgetDoneCallback wgetDoneCallback) {
        wpost_http_https(str, str2, null, wgetDoneCallback);
    }

    public static void wpost_http_https(final String str, final String str2, final Map<String, String> map, final WgetDoneCallback wgetDoneCallback) {
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.utils.NetUtil.4
            /* JADX WARN: Removed duplicated region for block: B:45:0x00e2  */
            /* JADX WARN: Removed duplicated region for block: B:49:0x00ee  */
            /* JADX WARN: Removed duplicated region for block: B:51:0x00f9  */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public final void run() throws java.lang.Throwable {
                /*
                    Method dump skipped, instructions count: 263
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.NetUtil.AnonymousClass4.run():void");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void httpDnsDo(String str, WgetMethodListener wgetMethodListener) {
        HttpURLConnection httpURLConnection;
        UniSdkUtils.i(TAG, "HTTPDnsDo");
        HttpURLConnection httpURLConnection2 = null;
        try {
            try {
                ResolveDnsResult resolveDnsResultResolve = (!HttpDns.getInstance().isHttpDnsMode() || TextUtils.isEmpty(str)) ? null : HttpDns.getInstance().resolve(str);
                if (resolveDnsResultResolve != null && !TextUtils.isEmpty(resolveDnsResultResolve.url)) {
                    str = resolveDnsResultResolve.url;
                }
                if (str.trim().startsWith("https")) {
                    httpURLConnection = (HttpsURLConnection) new URL(str).openConnection();
                } else {
                    httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                }
                httpURLConnection2 = httpURLConnection;
                if (resolveDnsResultResolve != null) {
                    resolveDnsResultResolve.setSNI(httpURLConnection2);
                }
                if (wgetMethodListener != null) {
                    wgetMethodListener.execute(httpURLConnection2);
                }
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
            }
        } catch (Throwable th) {
            if (httpURLConnection2 != null) {
                httpURLConnection2.disconnect();
            }
            throw th;
        }
    }

    public static byte[] readAll(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[16384];
        while (true) {
            try {
                int i = inputStream.read(bArr, 0, 16384);
                if (i == -1) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byteArrayOutputStream.flush();
        return byteArrayOutputStream.toByteArray();
    }

    public static Object doHttpReq(String str, Map<String, Object> map, String str2, Map<String, Long> map2, InputStreamDealer inputStreamDealer) throws IOException {
        Object objProcess;
        StringBuilder sb = new StringBuilder();
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (sb.length() > 0) {
                    sb.append(com.alipay.sdk.m.s.a.l);
                }
                sb.append((Object) entry.getKey());
                sb.append("=");
                sb.append(entry.getValue());
            }
            if ("GET".equalsIgnoreCase(str2) && sb.length() > 0) {
                str = str + "?" + sb.toString();
            }
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        boolean z = true;
        if ("POST".equalsIgnoreCase(str2)) {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(sb.toString().getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
        } else {
            httpURLConnection.setRequestMethod("GET");
        }
        boolean z2 = (map2 == null || map2.isEmpty()) ? false : true;
        if (z2) {
            Long l = map2.get("START");
            Long l2 = map2.get("END");
            StringBuilder sb2 = new StringBuilder("bytes=");
            sb2.append(l != null ? l.longValue() : 0L);
            sb2.append("-");
            if (l2 != null) {
                sb2.append(l2);
            }
            httpURLConnection.setRequestProperty("Range", sb2.toString());
        }
        httpURLConnection.connect();
        int responseCode = httpURLConnection.getResponseCode();
        InputStream inputStream = httpURLConnection.getInputStream();
        if (responseCode != 200 && (responseCode != 206 || !z2)) {
            z = false;
        }
        if (!z || inputStreamDealer == null) {
            objProcess = null;
        } else {
            try {
                objProcess = inputStreamDealer.process(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        inputStream.close();
        httpURLConnection.disconnect();
        return objProcess;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x009f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String doGet(java.lang.String r7) throws java.lang.Throwable {
        /*
            java.lang.String r0 = "doget >> "
            r1 = 0
            java.net.URL r2 = new java.net.URL     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L6a
            r2.<init>(r7)     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L6a
            java.net.URLConnection r7 = r2.openConnection()     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L6a
            java.net.HttpURLConnection r7 = (java.net.HttpURLConnection) r7     // Catch: java.lang.Throwable -> L65 java.lang.Exception -> L6a
            int r2 = com.netease.ntunisdk.base.utils.NetUtil.CONNECTION_TIMEOUT     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            r7.setConnectTimeout(r2)     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            int r2 = com.netease.ntunisdk.base.utils.NetUtil.SO_TIMEOUT     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            r7.setReadTimeout(r2)     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            java.io.InputStream r3 = r7.getInputStream()     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            java.lang.String r4 = "utf-8"
            r2.<init>(r3, r4)     // Catch: java.lang.Throwable -> L5d java.lang.Exception -> L62
            java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch: java.lang.Exception -> L5b java.lang.Throwable -> L97
            r3.<init>(r2)     // Catch: java.lang.Exception -> L5b java.lang.Throwable -> L97
            java.lang.StringBuffer r4 = new java.lang.StringBuffer     // Catch: java.lang.Exception -> L5b java.lang.Throwable -> L97
            r4.<init>()     // Catch: java.lang.Exception -> L5b java.lang.Throwable -> L97
        L2d:
            java.lang.String r5 = r3.readLine()     // Catch: java.lang.Exception -> L5b java.lang.Throwable -> L97
            if (r5 == 0) goto L37
            r4.append(r5)     // Catch: java.lang.Exception -> L5b java.lang.Throwable -> L97
            goto L2d
        L37:
            java.lang.String r1 = r4.toString()     // Catch: java.lang.Exception -> L5b java.lang.Throwable -> L97
            if (r7 == 0) goto L40
            r7.disconnect()
        L40:
            r2.close()     // Catch: java.io.IOException -> L44
            goto L96
        L44:
            r7 = move-exception
            java.lang.String r2 = com.netease.ntunisdk.base.utils.NetUtil.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r0)
        L4c:
            java.lang.String r7 = r7.getMessage()
            r3.append(r7)
            java.lang.String r7 = r3.toString()
            com.netease.ntunisdk.base.UniSdkUtils.e(r2, r7)
            goto L96
        L5b:
            r3 = move-exception
            goto L6d
        L5d:
            r2 = move-exception
            r6 = r2
            r2 = r1
            r1 = r6
            goto L98
        L62:
            r3 = move-exception
            r2 = r1
            goto L6d
        L65:
            r7 = move-exception
            r2 = r1
            r1 = r7
            r7 = r2
            goto L98
        L6a:
            r3 = move-exception
            r7 = r1
            r2 = r7
        L6d:
            java.lang.String r4 = com.netease.ntunisdk.base.utils.NetUtil.TAG     // Catch: java.lang.Throwable -> L97
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L97
            r5.<init>(r0)     // Catch: java.lang.Throwable -> L97
            java.lang.String r3 = r3.getMessage()     // Catch: java.lang.Throwable -> L97
            r5.append(r3)     // Catch: java.lang.Throwable -> L97
            java.lang.String r3 = r5.toString()     // Catch: java.lang.Throwable -> L97
            com.netease.ntunisdk.base.UniSdkUtils.e(r4, r3)     // Catch: java.lang.Throwable -> L97
            if (r7 == 0) goto L87
            r7.disconnect()
        L87:
            if (r2 == 0) goto L96
            r2.close()     // Catch: java.io.IOException -> L8d
            goto L96
        L8d:
            r7 = move-exception
            java.lang.String r2 = com.netease.ntunisdk.base.utils.NetUtil.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r0)
            goto L4c
        L96:
            return r1
        L97:
            r1 = move-exception
        L98:
            if (r7 == 0) goto L9d
            r7.disconnect()
        L9d:
            if (r2 == 0) goto Lb9
            r2.close()     // Catch: java.io.IOException -> La3
            goto Lb9
        La3:
            r7 = move-exception
            java.lang.String r2 = com.netease.ntunisdk.base.utils.NetUtil.TAG
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>(r0)
            java.lang.String r7 = r7.getMessage()
            r3.append(r7)
            java.lang.String r7 = r3.toString()
            com.netease.ntunisdk.base.UniSdkUtils.e(r2, r7)
        Lb9:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.NetUtil.doGet(java.lang.String):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0063 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String doGetOnce(java.lang.String r6) throws java.lang.Throwable {
        /*
            r0 = 0
            java.net.URL r1 = new java.net.URL     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L45
            r1.<init>(r6)     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L45
            java.net.URLConnection r6 = r1.openConnection()     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L45
            java.net.HttpURLConnection r6 = (java.net.HttpURLConnection) r6     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L45
            int r1 = com.netease.ntunisdk.base.utils.NetUtil.CONNECTION_TIMEOUT     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3d
            r6.setConnectTimeout(r1)     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3d
            int r1 = com.netease.ntunisdk.base.utils.NetUtil.SO_TIMEOUT     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3d
            r6.setReadTimeout(r1)     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3d
            java.io.InputStream r1 = r6.getInputStream()     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3d
            byte[] r2 = readAll(r1)     // Catch: java.lang.Exception -> L36 java.lang.Throwable -> L5b
            java.lang.String r3 = new java.lang.String     // Catch: java.lang.Exception -> L36 java.lang.Throwable -> L5b
            java.lang.String r4 = "UTF-8"
            r3.<init>(r2, r4)     // Catch: java.lang.Exception -> L36 java.lang.Throwable -> L5b
            if (r6 == 0) goto L2a
            r6.disconnect()
        L2a:
            if (r1 == 0) goto L34
            r1.close()     // Catch: java.io.IOException -> L30
            goto L34
        L30:
            r6 = move-exception
            r6.printStackTrace()
        L34:
            r0 = r3
            goto L5a
        L36:
            r2 = move-exception
            goto L48
        L38:
            r1 = move-exception
            r5 = r1
            r1 = r0
            r0 = r5
            goto L5c
        L3d:
            r2 = move-exception
            r1 = r0
            goto L48
        L40:
            r6 = move-exception
            r1 = r0
            r0 = r6
            r6 = r1
            goto L5c
        L45:
            r2 = move-exception
            r6 = r0
            r1 = r6
        L48:
            r2.printStackTrace()     // Catch: java.lang.Throwable -> L5b
            if (r6 == 0) goto L50
            r6.disconnect()
        L50:
            if (r1 == 0) goto L5a
            r1.close()     // Catch: java.io.IOException -> L56
            goto L5a
        L56:
            r6 = move-exception
            r6.printStackTrace()
        L5a:
            return r0
        L5b:
            r0 = move-exception
        L5c:
            if (r6 == 0) goto L61
            r6.disconnect()
        L61:
            if (r1 == 0) goto L6b
            r1.close()     // Catch: java.io.IOException -> L67
            goto L6b
        L67:
            r6 = move-exception
            r6.printStackTrace()
        L6b:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.NetUtil.doGetOnce(java.lang.String):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean hasHttpDnsSDK(boolean z, String str) {
        return z && HttpDns.getInstance().switchHttpDnsMode(str);
    }

    private static String stripIpWithPort(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.split(":")[0];
    }

    private static boolean isIp(String str) {
        return !TextUtils.isEmpty(str) && str.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    }
}