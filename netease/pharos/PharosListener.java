package com.netease.pharos;

import com.netease.pharos.util.LogUtil;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PharosListener implements PharosListenerImpl {
    private static final String TAG = "PharosListener";

    @Override // com.netease.pharos.PharosListenerImpl
    public void onPharosQos(JSONObject jSONObject) {
    }

    @Override // com.netease.pharos.PharosListenerImpl
    public void onResult(JSONObject jSONObject) {
        LogUtil.i(TAG, "[onResult]=" + jSONObject);
    }

    @Override // com.netease.pharos.PharosListenerImpl
    public void onPharosPolicy(JSONObject jSONObject) {
        LogUtil.i(TAG, "[onPharosPolicy]\uff1a" + jSONObject);
    }

    @Override // com.netease.pharos.PharosListenerImpl
    public void onPharosServer(JSONObject jSONObject) {
        LogUtil.i(TAG, "[onPharosServer]\uff1a" + jSONObject);
    }

    @Override // com.netease.pharos.PharosListenerImpl
    public void onNetworkChanged(JSONObject jSONObject) {
        LogUtil.i(TAG, "[onNetworkChanged]\uff1a" + jSONObject);
    }
}