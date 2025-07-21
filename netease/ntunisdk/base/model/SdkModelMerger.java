package com.netease.ntunisdk.base.model;

import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SdkModelMerger {
    public static String merge(SdkModel... sdkModelArr) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            for (SdkModel sdkModel : sdkModelArr) {
                JSONObject json = sdkModel.toJson();
                Iterator<String> itKeys = json.keys();
                while (itKeys.hasNext()) {
                    String next = itKeys.next();
                    jSONObject.putOpt(next, json.opt(next));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jSONObject.toString();
    }

    public static String merge(SdkModel sdkModel, String str) throws JSONException {
        JSONObject json = sdkModel.toJson();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                json.putOpt(next, jSONObject.opt(next));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}