package com.netease.pushclient;

import android.text.TextUtils;
import android.util.Log;
import androidx.browser.trusted.sharing.ShareTarget;
import com.alipay.sdk.m.s.a;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/* loaded from: classes3.dex */
public class NetUtil {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    private static final String TAG = "UniSDK NetUtil";

    public static String doGetDefault(String str) {
        return doHttp(str, "GET", null, null, null);
    }

    public static String doPostBodyPairs(String str, Map<String, String> map) {
        HashMap map2 = new HashMap();
        map2.put("Content-type", ShareTarget.ENCODING_TYPE_URL_ENCODED);
        return doHttp(str, "POST", null, map, map2);
    }

    public static String doPostJson(String str, String str2) {
        HashMap map = new HashMap();
        map.put("Content-type", ClientLogConstant.NORMAL_TYPE_VALUE);
        return doHttp(str, "POST", str2, null, map);
    }

    public static String doHttp(String str, String str2, String str3, Map<String, String> map, Map<String, String> map2) {
        String httpResponse;
        HttpURLConnection httpURLConnection;
        httpResponse = "";
        HttpURLConnection httpURLConnection2 = null;
        try {
            try {
                URL url = new URL(str);
                if (str.trim().startsWith("https")) {
                    httpURLConnection = (HttpsURLConnection) url.openConnection();
                } else {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                }
                httpURLConnection2 = httpURLConnection;
                if ("POST".equalsIgnoreCase(str2)) {
                    httpURLConnection2.setRequestMethod("POST");
                    httpURLConnection2.setDoOutput(true);
                } else {
                    httpURLConnection2.setRequestMethod("GET");
                    httpURLConnection2.setUseCaches(false);
                }
                if (map2 != null && !map2.isEmpty()) {
                    for (Map.Entry<String, String> entry : map2.entrySet()) {
                        httpURLConnection2.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }
                if ("POST".equalsIgnoreCase(str2)) {
                    if (!TextUtils.isEmpty(str3)) {
                        OutputStream outputStream = httpURLConnection2.getOutputStream();
                        outputStream.write(str3.getBytes("UTF-8"));
                        outputStream.flush();
                        outputStream.close();
                    } else if (map != null && map.size() > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (Map.Entry<String, String> entry2 : map.entrySet()) {
                            if (sb.length() > 0) {
                                sb.append(a.l);
                            }
                            sb.append(URLEncoder.encode(entry2.getKey(), "UTF-8"));
                            sb.append("=");
                            if (!TextUtils.isEmpty(entry2.getValue())) {
                                sb.append(URLEncoder.encode(entry2.getValue(), "UTF-8"));
                            }
                        }
                        String string = sb.toString();
                        OutputStream outputStream2 = httpURLConnection2.getOutputStream();
                        outputStream2.write(string.getBytes("UTF-8"));
                        outputStream2.flush();
                        outputStream2.close();
                    }
                }
                Log.d(TAG, "httRequst code:" + httpURLConnection2.getResponseCode());
                InputStream inputStream = httpURLConnection2.getInputStream();
                httpResponse = inputStream != null ? readHttpResponse(inputStream) : "";
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "httPost exception:" + e.getMessage());
                if (httpURLConnection2 != null) {
                }
            }
            return httpResponse;
        } finally {
            if (httpURLConnection2 != null) {
                httpURLConnection2.disconnect();
            }
        }
    }

    private static String readHttpResponse(InputStream inputStream) throws IOException {
        String string = "";
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[2048];
            while (true) {
                int i = inputStream.read(bArr, 0, bArr.length);
                if (i != -1) {
                    byteArrayOutputStream.write(bArr, 0, i);
                } else {
                    byteArrayOutputStream.flush();
                    string = byteArrayOutputStream.toString("UTF-8");
                    inputStream.close();
                    byteArrayOutputStream.close();
                    return string;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return string;
        }
    }

    public static String encodeQs(Map<String, String> map) {
        try {
            return encodeQs(map, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    private static String encodeQs(Map<String, String> map, String str) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append(a.l);
            }
            sb.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            sb.append("=");
            if (!TextUtils.isEmpty(entry.getValue())) {
                sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
        }
        return sb.toString();
    }
}