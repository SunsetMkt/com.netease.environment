package com.netease.environment.OIIO0II;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: JsonUtils.java */
/* loaded from: classes5.dex */
public class OIIO0II {
    public static String OIIO00I(int i, String str, String str2, String str3, String str4) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", i);
            jSONObject.put("message", str);
            jSONObject.put("regularId", str2);
            jSONObject.put("match_words", str4);
            com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(i, str3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static String OIIO00I(int i, String str, String str2, String str3) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", i);
            jSONObject.put("message", str);
            jSONObject.put("regularId", str2);
            com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(i, str3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static String OIIO00I(int i, String str, List<String> list, String str2) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("code", i);
            jSONObject.put("message", str);
            if (list != null && !list.isEmpty()) {
                JSONArray jSONArray = new JSONArray();
                Iterator<String> it = list.iterator();
                while (it.hasNext()) {
                    jSONArray.put(it.next());
                }
                jSONObject.put("regularIds", jSONArray);
            }
            com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(i, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static String OIIO00I(Exception exc, String str) {
        String simpleName = "exception";
        try {
            simpleName = exc.getClass().getSimpleName();
            if (TextUtils.equals(simpleName, "InterruptedException")) {
                simpleName = "time out";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return OIIO00I(100, simpleName, "-1", str);
    }

    public static boolean OIIO00I(String str) {
        if (str != null && !str.isEmpty()) {
            try {
                new JSONObject(str);
                return true;
            } catch (Exception unused) {
            }
        }
        return false;
    }
}