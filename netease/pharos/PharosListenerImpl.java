package com.netease.pharos;

import org.json.JSONObject;

/* loaded from: classes2.dex */
public interface PharosListenerImpl {
    void onNetworkChanged(JSONObject jSONObject);

    void onPharosPolicy(JSONObject jSONObject);

    void onPharosQos(JSONObject jSONObject);

    void onPharosServer(JSONObject jSONObject);

    void onResult(JSONObject jSONObject);
}