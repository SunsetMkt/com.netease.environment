package com.netease.mpay.auth.plugins;

import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class PluginResult {
    public static int RESULT_FAILED = 1;
    public static int RESULT_SUCCESS;
    public JSONObject data;
    public boolean isEnd;
    public int status;

    private PluginResult(int i, JSONObject jSONObject, boolean z) {
        this.isEnd = false;
        this.status = i;
        this.data = jSONObject;
        this.isEnd = z;
    }

    public boolean isSuccess() {
        return this.status == RESULT_SUCCESS;
    }

    public static PluginResult newInstance(int i, JSONObject jSONObject, Plugin plugin) throws JSONException {
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        if (plugin != null) {
            try {
                jSONObject.put("plugin", plugin.getName());
            } catch (Exception unused) {
            }
        }
        jSONObject.put("status", i);
        return new PluginResult(i, jSONObject, plugin.isEndNode());
    }

    public static PluginResult newInstance(int i, Plugin plugin) {
        return newInstance(i, null, plugin);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v0, types: [com.netease.mpay.auth.plugins.PluginResult] */
    /* JADX WARN: Type inference failed for: r4v1, types: [org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r4v3, types: [org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r4v4, types: [org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r4v5, types: [org.json.JSONObject] */
    public static PluginResult newInstance(PluginResult pluginResult, Plugin plugin) throws JSONException {
        int i = RESULT_SUCCESS;
        try {
            if (pluginResult == 0) {
                pluginResult = new JSONObject();
                if (plugin != null) {
                    pluginResult.put("plugin", plugin.getName());
                }
                pluginResult.put("status", i);
            } else {
                i = pluginResult.status;
                pluginResult = pluginResult.data;
                if (pluginResult == 0) {
                    pluginResult = new JSONObject();
                    if (plugin != null) {
                        pluginResult.put("plugin", plugin.getName());
                    }
                    pluginResult.put("status", i);
                }
            }
        } catch (Exception unused) {
        }
        return newInstance(i, pluginResult, plugin);
    }
}