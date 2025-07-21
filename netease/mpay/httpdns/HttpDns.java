package com.netease.mpay.httpdns;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import com.netease.pharos.Const;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes5.dex */
public class HttpDns {
    public volatile boolean d;
    public boolean e;
    public String f;
    public volatile boolean g;
    public volatile boolean i;
    public h j;
    public Context l;
    public int m;
    public String o;
    public boolean k = true;
    public boolean n = false;
    public j h = null;

    /* renamed from: a */
    public final ConcurrentHashMap<String, Boolean> f1578a = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, ResolveDnsResult> b = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, ArrayList<com.netease.mpay.httpdns.c>> c = new ConcurrentHashMap<>();

    public class a implements Runnable {
        public a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            HttpDns httpDns = HttpDns.this;
            if (httpDns.k) {
                if (httpDns.h == null) {
                    httpDns.h = j.b(httpDns.l);
                }
                for (String str : HttpDns.this.f1578a.keySet()) {
                    HttpDns httpDns2 = HttpDns.this;
                    if (!httpDns2.k) {
                        return;
                    }
                    Boolean bool = httpDns2.f1578a.get(str);
                    if (bool == null || !bool.booleanValue()) {
                        if (!str.startsWith("http")) {
                            HttpDns.this.f1578a.put(str, Boolean.valueOf(HttpDns.getInstance().b(str) == null ? HttpDns.this.a(str, true) : true));
                        }
                    }
                }
            }
        }
    }

    public class b implements Runnable {
        public b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            String str;
            p.a("======>>> initial anycast ip : " + HttpDns.this.f);
            HttpDns httpDns = HttpDns.this;
            if (httpDns.h == null) {
                httpDns.h = j.b(httpDns.l);
                HttpDns.this.i = true;
            }
            HttpDns httpDns2 = HttpDns.this;
            httpDns2.f = j.a(httpDns2.l, httpDns2.m);
            if (TextUtils.isEmpty(HttpDns.this.f)) {
                HttpDns.this.f = com.netease.mpay.httpdns.d.b;
            } else {
                com.netease.mpay.httpdns.a aVarA = HttpDns.this.j.a(com.netease.mpay.httpdns.d.f1590a);
                if (aVarA != null) {
                    String str2 = aVarA.f1585a.get(0);
                    String str3 = aVarA.b.get(0);
                    str = HttpDns.this.m == 0 ? str2 : str3;
                    Context context = HttpDns.this.l;
                    if (context != null) {
                        SharedPreferences.Editor editorEdit = context.getSharedPreferences(j.b, 0).edit();
                        if (!TextUtils.isEmpty(str2)) {
                            editorEdit.putString("any_cast_ip_mainland", Base64.encodeToString(str2.getBytes(), 0));
                        }
                        if (!TextUtils.isEmpty(str3)) {
                            editorEdit.putString("any_cast_ip_oversea", Base64.encodeToString(str3.getBytes(), 0));
                        }
                        editorEdit.apply();
                    }
                } else {
                    str = null;
                }
                if (!TextUtils.isEmpty(str)) {
                    HttpDns.this.f = str;
                    p.a("======>>> updated anyCast ip : " + str);
                }
            }
            HttpDns.this.g = true;
        }
    }

    public class c implements Runnable {

        /* renamed from: a */
        public final /* synthetic */ String f1581a;
        public final /* synthetic */ String b;

        public c(String str, String str2) {
            this.f1581a = str;
            this.b = str2;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean zSwitchHttpDnsMode = HttpDns.this.switchHttpDnsMode(this.f1581a);
            ArrayList<com.netease.mpay.httpdns.c> arrayList = HttpDns.this.c.get(this.b);
            if (arrayList == null) {
                return;
            }
            Iterator<com.netease.mpay.httpdns.c> it = arrayList.iterator();
            while (it.hasNext()) {
                it.next().a(Boolean.valueOf(zSwitchHttpDnsMode));
            }
        }
    }

    public static class d {

        /* renamed from: a */
        public static final HttpDns f1582a = new HttpDns(null);
    }

    public HttpDns() {
    }

    public /* synthetic */ HttpDns(a aVar) {
    }

    public static HttpDns getInstance() {
        return d.f1582a;
    }

    public final g a(String str) {
        return b(m.a(str));
    }

    public final boolean a() {
        return this.d && this.h != null;
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean a(java.lang.String r12, boolean r13) {
        /*
            r11 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r12)
            r1 = 0
            if (r0 == 0) goto L8
            return r1
        L8:
            com.netease.mpay.httpdns.h r0 = r11.j
            java.lang.String r2 = r11.f
            java.lang.String r3 = "mpay"
            java.lang.String r4 = "N8dui4CX"
            com.netease.mpay.httpdns.i r0 = r0.a(r2, r3, r4, r12)
            r2 = 1
            if (r0 == 0) goto L23
            java.util.ArrayList<java.lang.String> r3 = r0.b
            if (r3 == 0) goto L24
            boolean r4 = r3.isEmpty()
            if (r4 != 0) goto L24
            r4 = r2
            goto L25
        L23:
            r3 = 0
        L24:
            r4 = r1
        L25:
            if (r4 == 0) goto L75
            com.netease.mpay.httpdns.j r5 = r11.h
            if (r5 != 0) goto L33
            android.content.Context r5 = r11.l
            com.netease.mpay.httpdns.j r5 = com.netease.mpay.httpdns.j.b(r5)
            r11.h = r5
        L33:
            com.netease.mpay.httpdns.j r5 = r11.h
            java.util.Map<java.lang.String, com.netease.mpay.httpdns.g> r5 = r5.f1593a
            com.netease.mpay.httpdns.g r6 = new com.netease.mpay.httpdns.g
            r6.<init>(r3)
            int r0 = r0.f1592a
            r6.f = r2
            long r7 = (long) r0
            r9 = 1000(0x3e8, double:4.94E-321)
            long r7 = r7 * r9
            r6.d = r7
            long r7 = java.lang.System.currentTimeMillis()
            r6.c = r7
            r6.e = r13
            if (r13 != 0) goto L52
            r6.b = r1
        L52:
            r5.put(r12, r6)
            com.netease.mpay.httpdns.j r13 = r11.h
            android.content.Context r0 = r11.l
            r13.a(r0)
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            java.lang.String r0 = "======>>> new ip : "
            r13.<init>(r0)
            r13.append(r3)
            java.lang.String r0 = ", domain : "
            r13.append(r0)
            r13.append(r12)
            java.lang.String r12 = r13.toString()
            com.netease.mpay.httpdns.p.a(r12)
        L75:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.mpay.httpdns.HttpDns.a(java.lang.String, boolean):boolean");
    }

    public final g b(String str) {
        g gVar = (TextUtils.isEmpty(str) || !a()) ? null : this.h.f1593a.get(str);
        if (gVar == null || !gVar.b()) {
            return null;
        }
        return gVar;
    }

    public void b() {
        ConcurrentHashMap<String, Boolean> concurrentHashMap = this.f1578a;
        if (concurrentHashMap == null || concurrentHashMap.isEmpty()) {
            return;
        }
        com.netease.mpay.httpdns.b.f1586a.execute(new a());
    }

    public final synchronized void c() {
        if (this.g) {
            return;
        }
        p.a("start updateAnyCastIp");
        com.netease.mpay.httpdns.b.f1586a.execute(new b());
    }

    public String getJfGameId() {
        return TextUtils.isEmpty(this.o) ? "" : this.o;
    }

    public int getPublishArea() {
        return this.m;
    }

    public String getVersion() {
        return "0.1.4";
    }

    public WebResourceResponse getWebResourceResponse(WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
        h hVar = this.j;
        return hVar != null ? hVar.a(webResourceRequest, webResourceResponse) : webResourceResponse;
    }

    public synchronized void init(Context context, String str, int i, boolean z) {
        String str2;
        if (context == null) {
            return;
        }
        this.o = str;
        p.a(z);
        if (this.d) {
            return;
        }
        this.d = true;
        if (context instanceof Activity) {
            context = context.getApplicationContext();
        }
        this.l = context;
        this.m = i;
        if (!this.n) {
            p.a("changeDefaultIp:" + i);
            if (i == 0) {
                com.netease.mpay.httpdns.d.c = Const.HTTP_DNS_SERVER_MAINLAND;
                com.netease.mpay.httpdns.d.b = "103.71.201.4";
                str2 = "https://dns.update.netease.com/hdserver2";
            } else {
                com.netease.mpay.httpdns.d.c = Const.HTTP_DNS_SERVER_OVERSEA;
                com.netease.mpay.httpdns.d.b = "34.49.54.14";
                str2 = "https://dns.update.easebar.com/hdserver2";
            }
            com.netease.mpay.httpdns.d.f1590a = str2;
        }
        this.j = new h();
        this.f = com.netease.mpay.httpdns.d.b;
        c();
    }

    public boolean isEnable() {
        return this.k;
    }

    public synchronized boolean isHttpDnsMode() {
        return this.e;
    }

    public synchronized void registerUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        String strA = (str.startsWith("https://") || str.startsWith("http://")) ? m.a(str) : str;
        if (TextUtils.isEmpty(strA)) {
            return;
        }
        if (str.startsWith("http")) {
            return;
        }
        p.a("registerUrl:" + strA);
        this.f1578a.put(strA, false);
    }

    public boolean remove(String str) {
        StringBuilder sb;
        if (a()) {
            this.f1578a.put(str, false);
            g gVarB = b(m.a(str));
            if (gVarB != null) {
                if (gVarB.b()) {
                    gVarB.e = false;
                    gVarB.b = 0;
                    sb = new StringBuilder("======>>> remain ip : ");
                } else {
                    this.h.f1593a.remove(m.a(str));
                    this.h.a(this.l);
                    sb = new StringBuilder("======>>> delete ip : ");
                }
                sb.append(gVarB);
                p.a(sb.toString());
                return true;
            }
        }
        return false;
    }

    public String replaceOriginUrl(String str) {
        ResolveDnsResult resolveDnsResultResolve;
        return (TextUtils.isEmpty(str) || !this.e || (resolveDnsResultResolve = resolve(str)) == null) ? str : resolveDnsResultResolve.url;
    }

    public synchronized ResolveDnsResult resolve(String str) {
        int i;
        p.a("start resolve url");
        try {
            ResolveDnsResult resolveDnsResult = this.b.get(str);
            if (resolveDnsResult == null) {
                String strA = m.a(str);
                g gVarB = b(m.a(str));
                if (gVarB != null && gVarB.e) {
                    if (gVarB.b() && (i = gVarB.b) >= 0 && i < gVarB.f1591a.size()) {
                        resolveDnsResult = new ResolveDnsResult(str.replaceFirst(strA, gVarB.f1591a.get(gVarB.b)), strA, gVarB.f1591a.get(gVarB.b));
                        this.b.put(str, resolveDnsResult);
                    }
                }
            }
            if (resolveDnsResult != null) {
                p.a("resolve url:" + resolveDnsResult.url);
            }
            return resolveDnsResult;
        } catch (NullPointerException | Exception e) {
            p.a(e);
            return null;
        }
    }

    public void setCustomHttpDns(String str, String str2, String str3, int i) {
        if (!TextUtils.isEmpty(str)) {
            com.netease.mpay.httpdns.d.f1590a = str;
        }
        if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str3)) {
            com.netease.mpay.httpdns.d.b = str2;
            com.netease.mpay.httpdns.d.c = str3;
            this.n = true;
        }
        this.m = i;
    }

    public void setEnable(boolean z) {
        this.k = z;
    }

    public void setJfGameId(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.o = str;
    }

    public void setSniInConnection(String str, HttpURLConnection httpURLConnection) {
        ResolveDnsResult resolveDnsResultResolve;
        if (TextUtils.isEmpty(str) || httpURLConnection == null || !this.e || (resolveDnsResultResolve = resolve(str)) == null) {
            return;
        }
        resolveDnsResultResolve.setSNI(httpURLConnection);
    }

    public synchronized void switchHttpDnsMode(String str, com.netease.mpay.httpdns.c<Boolean> cVar) {
        if (cVar == null) {
            return;
        }
        if (this.l == null || this.j == null || TextUtils.isEmpty(str) || !this.k) {
            cVar.a(false);
        }
        String strA = m.a(str);
        ArrayList<com.netease.mpay.httpdns.c> arrayList = this.c.get(strA);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        arrayList.add(cVar);
        com.netease.mpay.httpdns.b.f1586a.execute(new c(str, strA));
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x0056  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean switchHttpDnsMode(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 275
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.mpay.httpdns.HttpDns.switchHttpDnsMode(java.lang.String):boolean");
    }
}