package com.netease.ntunisdk.base.model;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public abstract class SdkModel {
    public abstract void fromJson(JSONObject jSONObject);

    public abstract JSONObject toJson();

    public abstract void wrapFrom(Object obj);

    public abstract Object wrapTo();

    public SdkModel() {
    }

    public SdkModel(String str) {
        fromJson(str);
    }

    public void fromJson(String str) {
        try {
            fromJson(new JSONObject(str));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        JSONObject json = toJson();
        return json == null ? "{}" : json.toString();
    }
}