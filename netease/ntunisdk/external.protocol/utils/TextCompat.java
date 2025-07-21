package com.netease.ntunisdk.external.protocol.utils;

import android.app.Activity;
import android.text.TextUtils;
import com.alipay.sdk.m.s.a;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.external.protocol.data.ProtocolProp;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class TextCompat {
    public static int getResId(Activity activity, String str, String str2) {
        return activity.getResources().getIdentifier(str, str2, activity.getPackageName());
    }

    public static Locale getProtocolLocale(ProtocolProp protocolProp) {
        if (protocolProp == null) {
            return null;
        }
        return getResourcesConfigLocale(protocolProp.getLocale(), protocolProp.getUrl());
    }

    public static Locale getResourcesConfigLocale(String str, String str2) {
        L.d("getResourcesConfigLocale[NT_COMPACT_LOCALE]", str);
        if ("JAPAN".equals(str)) {
            return Locale.JAPAN;
        }
        if ("KOREA".equals(str)) {
            return Locale.KOREA;
        }
        if ("TRADITIONAL_CHINESE".equals(str)) {
            return Locale.TRADITIONAL_CHINESE;
        }
        if ("ENGLISH".equals(str)) {
            return Locale.ENGLISH;
        }
        if (ConstProp.LANGUAGE_CODE_VI.equals(str)) {
            return new Locale("vi", "VN");
        }
        if ("SIMPLIFIED_CHINESE".equals(str)) {
            return Locale.SIMPLIFIED_CHINESE;
        }
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        if (str2.endsWith("latest_v23.tw.json")) {
            return Locale.JAPAN;
        }
        if (str2.endsWith("latest_v35.tw.json")) {
            return Locale.KOREA;
        }
        if (str2.endsWith("latest_v6.tw.json")) {
            return Locale.TRADITIONAL_CHINESE;
        }
        return null;
    }

    public static synchronized String md5(String str) {
        String mD5Str = SDKRuntime.getInstance().getMD5Str(str);
        if (!TextUtils.isEmpty(mD5Str)) {
            return mD5Str;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            String strBytesToHex = bytesToHex(messageDigest.digest());
            SDKRuntime.getInstance().addMD5Cache(str, strBytesToHex);
            return strBytesToHex;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String bytesToHex(byte[] bArr) {
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

    public static String wrapperUrl(String str) {
        if (TextUtils.isEmpty(str) || str.contains("data=")) {
            return str;
        }
        HashMap map = new HashMap();
        String dataStr = SDKRuntime.getInstance().getDataStr();
        if (TextUtils.isEmpty(dataStr)) {
            return str;
        }
        map.put("data", dataStr);
        return encodeQs(str, map);
    }

    private static HashMap<String, String> extractQueryString(String str) {
        HashMap<String, String> map = new HashMap<>();
        int iIndexOf = str.indexOf(63);
        if (iIndexOf != -1) {
            for (String str2 : str.substring(iIndexOf + 1).split(a.l)) {
                int iIndexOf2 = str2.indexOf("=");
                if (iIndexOf2 != -1) {
                    map.put(str2.substring(0, iIndexOf2), str2.substring(iIndexOf2 + 1));
                } else {
                    map.put(str2, "");
                }
            }
        }
        return map;
    }

    private static String encodeQs(String str, HashMap<String, String> map) {
        if (TextUtils.isEmpty(str) || map == null || map.isEmpty()) {
            return str;
        }
        HashMap<String, String> mapExtractQueryString = extractQueryString(str);
        StringBuilder sb = new StringBuilder(str);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(key) && !mapExtractQueryString.containsKey(key)) {
                try {
                    if (sb.toString().contains("?")) {
                        sb.append(a.l);
                    } else {
                        sb.append("?");
                    }
                    sb.append(URLEncoder.encode(key, RSASignature.c));
                    sb.append("=");
                    sb.append(URLEncoder.encode(value, RSASignature.c));
                } catch (Exception unused) {
                }
            }
        }
        return sb.toString();
    }

    public static String encodeQs(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        if (str.contains("?")) {
            sb.append(a.l);
        } else {
            sb.append("?");
        }
        try {
            sb.append(URLEncoder.encode(str2, RSASignature.c));
            sb.append("=");
            sb.append(URLEncoder.encode(str3, RSASignature.c));
            return sb.toString();
        } catch (Exception unused) {
            return str;
        }
    }
}