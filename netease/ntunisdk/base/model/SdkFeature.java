package com.netease.ntunisdk.base.model;

import com.xiaomi.onetrack.api.b;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SdkFeature extends SdkModel {
    public String feature;

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public void wrapFrom(Object obj) {
    }

    public SdkFeature(String str) {
        this.feature = str;
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public void fromJson(JSONObject jSONObject) {
        this.feature = jSONObject.optString(b.n);
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public JSONObject toJson() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(b.n, this.feature);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    @Override // com.netease.ntunisdk.base.model.SdkModel
    public Object wrapTo() {
        String str = null;
        for (com.netease.ntunisdk.base.constant.b bVar : com.netease.ntunisdk.base.constant.b.values()) {
            if (bVar.e.equals(this.feature)) {
                str = bVar.f;
            }
        }
        return str;
    }
}