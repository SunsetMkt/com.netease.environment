package com.netease.mpay.httpdns;

import android.text.TextUtils;
import com.netease.download.Const;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/* loaded from: classes5.dex */
public class i {

    /* renamed from: a, reason: collision with root package name */
    public int f1592a;
    public ArrayList<String> b;
    public final String c;

    public i(String str) throws JSONException {
        try {
            JSONObject jSONObject = (JSONObject) new JSONTokener(str).nextValue();
            this.c = jSONObject.optString(Const.NT_PARAM_DOMAIN);
            this.f1592a = jSONObject.optInt("ttl");
            this.b = new ArrayList<>();
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("addrs");
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                String string = jSONArrayOptJSONArray.getString(i);
                if (!TextUtils.isEmpty(string) && 3 == string.replaceAll("\\d", "").length()) {
                    this.b.add(string);
                }
            }
        } catch (ClassCastException e) {
            throw new JSONException(e.getMessage());
        } catch (Exception e2) {
            throw new JSONException(e2.getMessage());
        }
    }
}