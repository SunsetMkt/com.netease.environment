package com.netease.mpay.httpdns;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes5.dex */
public class j {
    public static final String b;

    /* renamed from: a, reason: collision with root package name */
    public final Map<String, g> f1593a = new ConcurrentHashMap();

    static {
        String strEncode;
        try {
            strEncode = URLEncoder.encode(Base64.encodeToString("mpay.dns.cache".getBytes(), 8).replace("\n", ""), "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            strEncode = URLEncoder.encode(Base64.encodeToString("mpay.dns.cache".getBytes(), 8).replace("\n", ""));
        }
        b = strEncode;
    }

    public static j b(Context context) {
        String string = context.getSharedPreferences(b, 0).getString("mpay.dns.cache.key", "");
        j jVar = new j();
        if (!TextUtils.isEmpty(string)) {
            try {
                String str = new String(Base64.decode(string, 0));
                p.a("loadString = ".concat(str));
                String[] strArrSplit = str.split(";");
                for (String str2 : strArrSplit) {
                    String[] strArrSplit2 = str2.trim().split("%");
                    if (strArrSplit2.length > 1) {
                        jVar.f1593a.put(strArrSplit2[0].trim(), g.a(strArrSplit2[1].trim()));
                    }
                }
            } catch (IndexOutOfBoundsException | Exception e) {
                p.a(e);
            }
        }
        return jVar;
    }

    public void a(Context context) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, g> entry : this.f1593a.entrySet()) {
                String key = entry.getKey();
                g value = entry.getValue();
                if (!TextUtils.isEmpty(key) && value != null && !value.f1591a.isEmpty()) {
                    if (sb.length() > 0) {
                        sb.append(";");
                    }
                    sb.append(key);
                    sb.append("%");
                    sb.append(value.a());
                }
            }
            if (context == null) {
                return;
            }
            SharedPreferences.Editor editorEdit = context.getSharedPreferences(b, 0).edit();
            String string = sb.toString();
            p.a("dumpString = " + string);
            editorEdit.putString("mpay.dns.cache.key", Base64.encodeToString(string.getBytes(), 0));
            editorEdit.apply();
        } catch (NullPointerException | Exception e) {
            p.a(e);
        }
    }

    public static String a(Context context, int i) {
        if (context == null) {
            return null;
        }
        if (i == 0) {
            String string = context.getSharedPreferences(b, 0).getString("any_cast_ip_mainland", "");
            if (!TextUtils.isEmpty(string)) {
                return new String(Base64.decode(string, 0));
            }
        } else {
            String string2 = context.getSharedPreferences(b, 0).getString("any_cast_ip_oversea", "");
            if (!TextUtils.isEmpty(string2)) {
                return new String(Base64.decode(string2, 0));
            }
        }
        return null;
    }
}