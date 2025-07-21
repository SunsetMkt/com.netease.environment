package com.netease.mpay.httpdns;

import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/* loaded from: classes5.dex */
public class f {
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0216 A[Catch: all -> 0x021e, IllegalStateException -> 0x0220, IOException -> 0x0222, NullPointerException -> 0x0224, IllegalAccessError -> 0x0226, ProtocolException -> 0x0228, UnsupportedEncodingException -> 0x022a, ConnectException -> 0x022c, SSLException -> 0x022e, TRY_ENTER, TryCatch #17 {IllegalAccessError -> 0x0226, IllegalStateException -> 0x0220, NullPointerException -> 0x0224, all -> 0x021e, blocks: (B:51:0x00cb, B:52:0x00ce, B:54:0x00d3, B:56:0x00d9, B:57:0x00e1, B:59:0x00e7, B:61:0x00f9, B:62:0x010b, B:65:0x011b, B:105:0x0216, B:106:0x021d, B:67:0x0120), top: B:189:0x00cb }] */
    /* JADX WARN: Removed duplicated region for block: B:172:0x02b5  */
    /* JADX WARN: Removed duplicated region for block: B:181:0x02b0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x011b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:189:0x00cb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:195:? A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00e7 A[Catch: all -> 0x021e, IllegalStateException -> 0x0220, IOException -> 0x0222, NullPointerException -> 0x0224, IllegalAccessError -> 0x0226, ProtocolException -> 0x0228, UnsupportedEncodingException -> 0x022a, ConnectException -> 0x022c, SSLException -> 0x022e, LOOP:0: B:57:0x00e1->B:59:0x00e7, LOOP_END, TryCatch #17 {IllegalAccessError -> 0x0226, IllegalStateException -> 0x0220, NullPointerException -> 0x0224, all -> 0x021e, blocks: (B:51:0x00cb, B:52:0x00ce, B:54:0x00d3, B:56:0x00d9, B:57:0x00e1, B:59:0x00e7, B:61:0x00f9, B:62:0x010b, B:65:0x011b, B:105:0x0216, B:106:0x021d, B:67:0x0120), top: B:189:0x00cb }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00f9 A[Catch: all -> 0x021e, IllegalStateException -> 0x0220, IOException -> 0x0222, NullPointerException -> 0x0224, IllegalAccessError -> 0x0226, ProtocolException -> 0x0228, UnsupportedEncodingException -> 0x022a, ConnectException -> 0x022c, SSLException -> 0x022e, TryCatch #17 {IllegalAccessError -> 0x0226, IllegalStateException -> 0x0220, NullPointerException -> 0x0224, all -> 0x021e, blocks: (B:51:0x00cb, B:52:0x00ce, B:54:0x00d3, B:56:0x00d9, B:57:0x00e1, B:59:0x00e7, B:61:0x00f9, B:62:0x010b, B:65:0x011b, B:105:0x0216, B:106:0x021d, B:67:0x0120), top: B:189:0x00cb }] */
    /* JADX WARN: Type inference failed for: r12v0, types: [int] */
    /* JADX WARN: Type inference failed for: r12v21 */
    /* JADX WARN: Type inference failed for: r12v28, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r12v30 */
    /* JADX WARN: Type inference failed for: r1v0, types: [java.util.HashMap] */
    /* JADX WARN: Type inference failed for: r1v12, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.netease.mpay.httpdns.k a(java.lang.String r17, java.lang.String r18, java.util.HashMap<java.lang.String, java.lang.String> r19, java.util.HashMap<java.lang.String, java.lang.String> r20, java.util.HashMap<java.lang.String, java.lang.String> r21, com.netease.mpay.httpdns.ResolveDnsResult r22, int r23, int r24) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 730
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.mpay.httpdns.f.a(java.lang.String, java.lang.String, java.util.HashMap, java.util.HashMap, java.util.HashMap, com.netease.mpay.httpdns.ResolveDnsResult, int, int):com.netease.mpay.httpdns.k");
    }

    public static String a(HashMap<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        try {
            return a(map, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static String a(HashMap<String, String> map, String str) {
        StringBuilder sb = new StringBuilder();
        for (String str2 : map.keySet()) {
            String str3 = map.get(str2);
            if (!TextUtils.isEmpty(str3)) {
                sb.append(URLEncoder.encode(str2, str));
                sb.append("=");
                sb.append(URLEncoder.encode(str3, str));
            }
        }
        return sb.toString();
    }

    public static HttpURLConnection a(String str, int i, int i2) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
        httpURLConnection.setConnectTimeout(i);
        httpURLConnection.setReadTimeout(i2);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(true);
        return httpURLConnection;
    }

    public static byte[] a(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[16384];
        while (true) {
            int i = inputStream.read(bArr, 0, 16384);
            if (i == -1) {
                byteArrayOutputStream.flush();
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, i);
        }
    }
}