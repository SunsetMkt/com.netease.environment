package com.netease.mpay.httpdns;

import com.xiaomi.mipush.sdk.Constants;
import java.util.Arrays;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class g {

    /* renamed from: a, reason: collision with root package name */
    public final List<String> f1591a;
    public int b;
    public boolean e = false;
    public boolean f = false;
    public long c = 0;
    public long d = Constants.ASSEMBLE_PUSH_NETWORK_INTERVAL;

    public g(List<String> list) {
        this.f1591a = list;
    }

    public String a() throws JSONException {
        String string;
        try {
            JSONObject jSONObject = new JSONObject();
            List<String> list = this.f1591a;
            if (list.isEmpty()) {
                string = "";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(list.get(0));
                for (int i = 1; i < list.size(); i++) {
                    sb.append("#");
                    sb.append(list.get(i));
                }
                string = sb.toString();
            }
            jSONObject.put(com.xiaomi.onetrack.api.b.H, string);
            jSONObject.put("timestamp", this.c);
            jSONObject.put("ttl", this.d);
            return jSONObject.toString();
        } catch (JSONException e) {
            p.a(e);
            return "";
        }
    }

    public boolean b() {
        return System.currentTimeMillis() - this.c < this.d;
    }

    public boolean c() {
        int i = this.b + 1;
        return b() && i >= 0 && i < this.f1591a.size();
    }

    public boolean d() {
        boolean zC = c();
        if (zC) {
            this.b++;
        }
        return zC;
    }

    public String toString() {
        return "HttpDnsEntry{ipAddresses=" + this.f1591a + ", currentIndex=" + this.b + ", syncTimestamp=" + this.c + ", syncTtlMillis=" + this.d + ", isIpPrior=" + this.e + ", syncSuccess=" + this.f + '}';
    }

    public static g a(String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            g gVar = new g(Arrays.asList(jSONObject.getString(com.xiaomi.onetrack.api.b.H).split("#")));
            gVar.c = jSONObject.getLong("timestamp");
            gVar.d = jSONObject.getLong("ttl");
            return gVar;
        } catch (JSONException e) {
            p.a(e);
            return null;
        }
    }
}