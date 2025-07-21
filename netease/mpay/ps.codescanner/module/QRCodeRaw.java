package com.netease.mpay.ps.codescanner.module;

import android.text.TextUtils;
import android.util.Log;
import com.alipay.sdk.m.s.a;
import com.netease.mpay.ps.codescanner.Configs;
import com.netease.mpay.ps.codescanner.utils.Logging;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes6.dex */
public class QRCodeRaw {

    private class QS_KEY {
        static final String DATA_ID = "data_id";
        static final String SCENE = "scene";
        static final String UID = "uid";
        static final String UUID = "uuid";

        private QS_KEY() {
        }
    }

    public static QRCodeRaw decode(String str) {
        String str2 = Configs.getHost() + "/api/qrcode/scan";
        Log.d("qrcode", "qrcode url:" + str2);
        if (!TextUtils.isEmpty(str) && str.trim().startsWith(str2)) {
            try {
                Map<String, String> mapSplitQuery = splitQuery(new URL(str.trim()));
                String str3 = mapSplitQuery.get("uuid");
                String str4 = mapSplitQuery.get("uid");
                String str5 = mapSplitQuery.get("data_id");
                String str6 = mapSplitQuery.get("scene");
                if (!TextUtils.isEmpty(str4) && !TextUtils.isEmpty(str5)) {
                    return new QRCodePayRaw(str4, str5);
                }
                if (!TextUtils.isEmpty(str3)) {
                    return new QRCodeLoginRaw(str3, str6);
                }
            } catch (Exception e) {
                Logging.logStackTrace(e);
            }
        }
        return new QRCodeRaw();
    }

    private static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (String str : url.getQuery().split(a.l)) {
            int iIndexOf = str.indexOf("=");
            linkedHashMap.put(URLDecoder.decode(str.substring(0, iIndexOf), "UTF-8"), URLDecoder.decode(str.substring(iIndexOf + 1), "UTF-8"));
        }
        return linkedHashMap;
    }
}