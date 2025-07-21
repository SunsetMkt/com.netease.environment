package com.netease.mpay.httpdns;

import android.util.Base64;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class a {

    /* renamed from: a, reason: collision with root package name */
    public final List<String> f1585a;
    public final List<String> b;

    public a(String str) {
        JSONObject jSONObject = new JSONObject(new String(Base64.decode(str, 0)));
        JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("mainland");
        JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("oversea");
        this.f1585a = new ArrayList();
        this.b = new ArrayList();
        if (jSONArrayOptJSONArray != null) {
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                this.f1585a.add(jSONArrayOptJSONArray.getString(i));
            }
        }
        if (jSONArrayOptJSONArray2 != null) {
            for (int i2 = 0; i2 < jSONArrayOptJSONArray2.length(); i2++) {
                this.b.add(jSONArrayOptJSONArray2.getString(i2));
            }
        }
    }
}