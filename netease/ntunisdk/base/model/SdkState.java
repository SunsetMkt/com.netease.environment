package com.netease.ntunisdk.base.model;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SdkState extends SdkModel {
    public int code;
    public String message;
    public String module;
    public int subcode;

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public void wrapFrom(Object obj) {
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public Object wrapTo() {
        return null;
    }

    public SdkState() {
        this.code = -1;
    }

    public SdkState(String str) {
        super(str);
        this.code = -1;
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public void fromJson(JSONObject jSONObject) {
        this.code = jSONObject.optInt("code", -1);
        this.subcode = jSONObject.optInt("subcode");
        this.message = jSONObject.optString("message");
        this.module = jSONObject.optString("module");
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public JSONObject toJson() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            int i = this.code;
            if (i >= 0) {
                jSONObject.putOpt("code", Integer.valueOf(i));
            }
            jSONObject.putOpt("subcode", Integer.valueOf(this.subcode));
            jSONObject.putOpt("message", this.message);
            jSONObject.putOpt("module", this.module);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }
}