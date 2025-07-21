package com.netease.mpay.httpdns;

import android.text.TextUtils;
import java.net.HttpURLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/* loaded from: classes5.dex */
public class ResolveDnsResult {

    /* renamed from: a, reason: collision with root package name */
    public boolean f1583a;
    public String host;
    public String ip;
    public boolean isNeedSwitchDNSMode;
    public String url;

    public class a implements HostnameVerifier {
        public a() {
        }

        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String str, SSLSession sSLSession) {
            HostnameVerifier defaultHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            ResolveDnsResult resolveDnsResult = ResolveDnsResult.this;
            return TextUtils.equals(str, resolveDnsResult.a(resolveDnsResult.ip)) ? defaultHostnameVerifier.verify(ResolveDnsResult.this.host, sSLSession) : defaultHostnameVerifier.verify(str, sSLSession);
        }
    }

    public ResolveDnsResult(String str, String str2, String str3) {
        this(str, str2, str3, true);
    }

    public ResolveDnsResult(String str, String str2, String str3, boolean z) {
        this.url = str;
        this.host = str2;
        this.ip = str3;
        this.isNeedSwitchDNSMode = z;
        this.f1583a = !TextUtils.isEmpty(str) && str.trim().startsWith("https://");
    }

    public final String a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.split(":")[0];
    }

    public void setSNI(HttpURLConnection httpURLConnection) {
        httpURLConnection.setRequestProperty("host", this.host);
        if (this.f1583a && (httpURLConnection instanceof HttpsURLConnection)) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            httpsURLConnection.setSSLSocketFactory(new l(httpsURLConnection));
            httpsURLConnection.setHostnameVerifier(new a());
        }
    }
}