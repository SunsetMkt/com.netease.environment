package com.netease.mpay.ps.codescanner.auth;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.netease.mpay.auth.plugins.DefaultPlugin;
import com.netease.mpay.auth.plugins.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class DecisionPlugin extends DefaultPlugin {
    private static final String PATH = "neteasempay://route/%s/unisdk/login/game_auth";
    private static final String TAG = "Auth Decision";

    @Override // com.netease.mpay.auth.plugins.Plugin
    public void execute() throws JSONException {
        Intent intent = DefaultAuthRules.getInstance().getIntent();
        String dataString = intent != null ? intent.getDataString() : null;
        Log.d(TAG, "data:" + dataString);
        if (TextUtils.isEmpty(dataString) || intent == null) {
            this.mPluginCallback.onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, this));
            return;
        }
        Uri data = intent.getData();
        String query = data.getQuery();
        Log.d(TAG, "uri#authority:" + data.getAuthority() + ",scheme:" + data.getScheme() + ",query:" + query + ",path=" + data.getPath());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("data", dataString);
            jSONObject.put("query", query);
            this.mPluginCallback.onFinish(PluginResult.newInstance(PluginResult.RESULT_SUCCESS, jSONObject, this));
        } catch (Exception unused) {
            this.mPluginCallback.onFinish(PluginResult.newInstance(PluginResult.RESULT_FAILED, this));
        }
    }

    @Override // com.netease.mpay.auth.plugins.Plugin
    public String getName() {
        return DecisionPlugin.class.getName();
    }
}