package com.netease.pharos.network;

import android.text.TextUtils;
import com.alipay.sdk.m.s.a;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NetUtil {
    private static final String TAG = "NetUtil";

    public static Object doHttpReq(String str, Map<String, Object> map, String str2, Map<String, String> map2, NetworkDealer networkDealer) throws IOException {
        LogUtil.i(TAG, "NetUtil [doHttpReq]");
        LogUtil.i(TAG, "NetUtil [doHttpReq] pUrl=" + str);
        if (!TextUtils.isEmpty(str)) {
            if (str.startsWith("https")) {
                return excuteHttpsReq(str, map, str2, map2, networkDealer);
            }
            if (!str.startsWith("http")) {
                return 11;
            }
            return excuteHttpReq(str, map, str2, map2, networkDealer);
        }
        LogUtil.i(TAG, "NetUtil [doHttpReq] pUrl error");
        return 11;
    }

    public static Object excuteHttpReq(String str, Map<String, Object> map, String str2, Map<String, String> map2, NetworkDealer networkDealer) throws IOException {
        int i;
        String strReplaceAll;
        int responseCode;
        URL url;
        Object objProcessContent;
        LogUtil.i(TAG, "NetUtil [excuteHttpReq]");
        String strReplaceAll2 = PharosProxy.getInstance().ismEB() ? str.replaceAll("netease.com", "easebar.com") : str;
        StringBuilder sb = new StringBuilder();
        try {
            URL url2 = new URL(strReplaceAll2);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url2.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            httpURLConnection.setRequestProperty(Const.HEADER_ACCEPT_ENCODING, "");
            if (map2 != null && map2.size() > 0) {
                for (Iterator<String> it = map2.keySet().iterator(); it.hasNext(); it = it) {
                    String next = it.next();
                    httpURLConnection.setRequestProperty(next, map2.get(next));
                }
            }
            if ("POST".equalsIgnoreCase(str2)) {
                LogUtil.i(TAG, "NetUtil [excuteHttpReq] method post");
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
            }
            HashMap map3 = new HashMap();
            if (map != null && map.containsKey("extra_data")) {
                map3.put("extra_data", ((JSONObject) map.get("extra_data")).toString());
            }
            if ("POST".equalsIgnoreCase(str2) || com.netease.pharos.Const.METHOD_DELETE.equalsIgnoreCase(str2)) {
                LogUtil.i(TAG, "NetUtil [excuteHttpReq] method post || method delete");
                httpURLConnection.setRequestMethod(str2);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                if (map != null && map.containsKey("post_content")) {
                    sb.append(map.get("post_content"));
                } else {
                    sb.append("\u5185\u5bb9\u4e3a\u7a7a");
                }
                bufferedWriter.write(sb.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
            } else {
                if (map != null) {
                    LogUtil.i(TAG, "NetUtil [excuteHttpReq] params=" + map);
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        if (sb.length() > 0) {
                            sb.append(a.l);
                        }
                        sb.append((Object) entry.getKey());
                        sb.append("=");
                        sb.append(entry.getValue());
                    }
                    if ("GET".equalsIgnoreCase(str2) && sb.length() > 0) {
                        strReplaceAll2 = strReplaceAll2 + "?" + ((Object) sb);
                    }
                }
                httpURLConnection.setRequestMethod(str2);
            }
            if (map2 != null && map2.containsKey("Host") && Util.isIpAddrDomain(strReplaceAll2)) {
                System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
                strReplaceAll = map2.get("Host");
                if (!TextUtils.isEmpty(strReplaceAll)) {
                    LogUtil.i(TAG, "NetUtil [excuteHttpReq] \u8bbe\u7f6ehost =" + strReplaceAll);
                    if (PharosProxy.getInstance().ismEB()) {
                        strReplaceAll = strReplaceAll.replaceAll("netease.com", "easebar.com");
                    }
                    httpURLConnection.setRequestProperty("Host", strReplaceAll);
                }
            } else {
                strReplaceAll = null;
            }
            LogUtil.i(TAG, "NetUtil [excuteHttpReq] StrUtil.isIpAddrDomain(reqUrl) =" + Util.isIpAddrDomain(strReplaceAll2));
            long jCurrentTimeMillis = System.currentTimeMillis();
            try {
                LogUtil.i(TAG, "NetUtil [excuteHttpReq] reqUrl=" + strReplaceAll2);
                if (!TextUtils.isEmpty(strReplaceAll)) {
                    LogUtil.i(TAG, "NetUtil [excuteHttpReq] host=" + strReplaceAll);
                }
                responseCode = httpURLConnection.getResponseCode();
            } catch (UnknownHostException e) {
                LogUtil.w(TAG, "NetUtil [excuteHttpReq] UnknownHostException = " + e + ", url=" + strReplaceAll2);
                e.printStackTrace();
                responseCode = 503;
            } catch (IOException e2) {
                LogUtil.w(TAG, "NetUtil [excuteHttpReq] IOException = " + e2 + ", url=" + strReplaceAll2);
                e2.printStackTrace();
                responseCode = 408;
            } catch (Exception e3) {
                LogUtil.i(TAG, "NetUtil [excuteHttpReq] Exception1 = " + e3 + ", url=" + strReplaceAll2);
                e3.printStackTrace();
                responseCode = 400;
            }
            if (networkDealer != null) {
                HashMap map4 = new HashMap();
                map4.put("url", strReplaceAll2);
                networkDealer.processHeader(httpURLConnection.getHeaderFields(), responseCode, map4);
            }
            try {
                InputStream inputStream = httpURLConnection.getInputStream();
                LogUtil.i(TAG, "NetUtil [net cost] " + (System.currentTimeMillis() - jCurrentTimeMillis));
                if (responseCode != 200 || networkDealer == null) {
                    url = url2;
                    objProcessContent = 0;
                } else {
                    try {
                        objProcessContent = networkDealer.processContent(inputStream, responseCode, map3);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("NetUtil [excuteHttpReq] processContent result=");
                        sb2.append(objProcessContent);
                        sb2.append(", url=");
                        url = url2;
                        try {
                            sb2.append(url);
                            LogUtil.i(TAG, sb2.toString());
                        } catch (FileNotFoundException e4) {
                            e = e4;
                            objProcessContent = 4;
                            LogUtil.i(TAG, "NetUtil [excuteHttpReq] FileNotFoundException1 = " + e + ", url=" + strReplaceAll2);
                            e.printStackTrace();
                            inputStream.close();
                            httpURLConnection.disconnect();
                            LogUtil.i(TAG, "NetUtil [excuteHttpReq] result=" + objProcessContent + ", url=" + url);
                            return objProcessContent;
                        } catch (SocketException e5) {
                            e = e5;
                            objProcessContent = 13;
                            LogUtil.i(TAG, "NetUtil [excuteHttpReq] SocketException1 = " + e + ", url=" + strReplaceAll2);
                            e.printStackTrace();
                            inputStream.close();
                            httpURLConnection.disconnect();
                            LogUtil.i(TAG, "NetUtil [excuteHttpReq] result=" + objProcessContent + ", url=" + url);
                            return objProcessContent;
                        } catch (Exception e6) {
                            e = e6;
                            LogUtil.i(TAG, "NetUtil [excuteHttpReq] Exception3 = " + e + ", url=" + url);
                            e.printStackTrace();
                            try {
                                objProcessContent = 11;
                                inputStream.close();
                                httpURLConnection.disconnect();
                                LogUtil.i(TAG, "NetUtil [excuteHttpReq] result=" + objProcessContent + ", url=" + url);
                                return objProcessContent;
                            } catch (Exception e7) {
                                e = e7;
                                i = 11;
                                Integer numValueOf = Integer.valueOf(i);
                                LogUtil.w(TAG, "NetUtil [excuteHttpReq] Exception4 =" + e);
                                e.printStackTrace();
                                return numValueOf;
                            }
                        }
                    } catch (FileNotFoundException e8) {
                        e = e8;
                        url = url2;
                    } catch (SocketException e9) {
                        e = e9;
                        url = url2;
                    } catch (Exception e10) {
                        e = e10;
                        url = url2;
                    }
                }
                inputStream.close();
                httpURLConnection.disconnect();
                LogUtil.i(TAG, "NetUtil [excuteHttpReq] result=" + objProcessContent + ", url=" + url);
                return objProcessContent;
            } catch (Exception e11) {
                LogUtil.i(TAG, "NetUtil [excuteHttpReq] Exception2 = " + e11);
                e11.printStackTrace();
                httpURLConnection.disconnect();
                return 1;
            }
        } catch (Exception e12) {
            e = e12;
            i = 11;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:117:0x0415 A[Catch: Exception -> 0x0436, TryCatch #7 {Exception -> 0x0436, blocks: (B:7:0x0040, B:9:0x0062, B:11:0x0068, B:12:0x0070, B:14:0x0076, B:15:0x008c, B:17:0x0093, B:18:0x00a1, B:20:0x00a8, B:22:0x00ae, B:23:0x00bb, B:25:0x00c2, B:29:0x00cd, B:30:0x00e9, B:32:0x00ef, B:34:0x00fb, B:35:0x0100, B:36:0x0114, B:38:0x011c, B:40:0x0122, B:41:0x0136, B:51:0x0199, B:53:0x019f, B:59:0x01ab, B:61:0x01be, B:63:0x01dc, B:64:0x01e0, B:66:0x01e5, B:84:0x030c, B:117:0x0415, B:118:0x0418, B:101:0x0353, B:104:0x0379, B:107:0x039f, B:110:0x03c5, B:114:0x03ec, B:115:0x040e, B:74:0x0283, B:76:0x02a6, B:78:0x02c7, B:80:0x02e5, B:42:0x013d, B:44:0x015e, B:46:0x0164, B:48:0x0171, B:47:0x016c, B:88:0x0321, B:68:0x020f, B:70:0x0229, B:71:0x023d), top: B:123:0x0040, inners: #9, #10, #11, #11 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.Object excuteHttpsReq(java.lang.String r21, java.util.Map<java.lang.String, java.lang.Object> r22, java.lang.String r23, java.util.Map<java.lang.String, java.lang.String> r24, com.netease.pharos.network.NetworkDealer r25) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1105
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.network.NetUtil.excuteHttpsReq(java.lang.String, java.util.Map, java.lang.String, java.util.Map, com.netease.pharos.network.NetworkDealer):java.lang.Object");
    }
}