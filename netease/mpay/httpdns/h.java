package com.netease.mpay.httpdns;

import android.text.TextUtils;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import com.netease.download.Const;
import java.util.Collections;
import java.util.HashMap;
import org.json.JSONException;

/* loaded from: classes5.dex */
public class h {
    public WebResourceResponse a(WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
        if (webResourceRequest == null) {
            return webResourceResponse;
        }
        String scheme = webResourceRequest.getUrl().getScheme();
        if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
            return webResourceResponse;
        }
        try {
            String strTrim = webResourceRequest.getUrl().toString().trim();
            if (HttpDns.getInstance().isHttpDnsMode() && !TextUtils.isEmpty(strTrim)) {
                n nVarA = a(webResourceRequest, HttpDns.getInstance().resolve(strTrim));
                return new WebResourceResponse(nVarA.e, nVarA.f, nVarA.f1596a, nVarA.b, nVarA.d, nVarA.c);
            }
        } catch (Exception unused) {
        }
        return webResourceResponse;
    }

    public a a(String str) {
        p.a("start fetchAnyCastIp: " + str);
        try {
            return new a(new String(f.a("GET", str, new HashMap(), new HashMap(), null, null, 3000, 3000).b));
        } catch (e unused) {
            if (HttpDns.getInstance().switchHttpDnsMode(str)) {
                return a(str);
            }
            return null;
        } catch (JSONException unused2) {
            if (HttpDns.getInstance().switchHttpDnsMode(str)) {
                return a(str);
            }
            return null;
        } catch (Exception unused3) {
            return null;
        }
    }

    public i a(String str, String str2, String str3, String str4) {
        p.a("start fetchHostAddress: " + str4);
        HashMap map = new HashMap();
        map.put("AUTH-PROJECT", str2);
        map.put("AUTH-TOKEN", str3);
        map.put("X-NTES-PROJECT", HttpDns.getInstance().getJfGameId());
        map.put("X-NTES-SDK", "httpdns");
        HashMap map2 = new HashMap();
        map2.put(Const.NT_PARAM_DOMAIN, str4);
        String str5 = "https://" + str + "/v2";
        try {
            i iVar = new i(new String(f.a("GET", str5, map, map2, null, new ResolveDnsResult(str5, d.c, str, false), 3000, 3000).b));
            if (iVar.b == null || iVar.b.size() <= 0 || !(TextUtils.isEmpty(iVar.c) || TextUtils.equals(str4, iVar.c))) {
                return null;
            }
            p.a("======>>> HttpDnsResponse : " + iVar.b);
            Collections.shuffle(iVar.b);
            return iVar;
        } catch (Exception e) {
            p.a("fetch host ip failed : " + str5 + ",domain:" + str4);
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x0035 A[PHI: r3
  0x0035: PHI (r3v19 java.lang.String) = (r3v0 java.lang.String), (r3v1 java.lang.String) binds: [B:4:0x0033, B:7:0x003f] A[DONT_GENERATE, DONT_INLINE]] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.netease.mpay.httpdns.n a(android.webkit.WebResourceRequest r7, com.netease.mpay.httpdns.ResolveDnsResult r8) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 269
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.mpay.httpdns.h.a(android.webkit.WebResourceRequest, com.netease.mpay.httpdns.ResolveDnsResult):com.netease.mpay.httpdns.n");
    }
}