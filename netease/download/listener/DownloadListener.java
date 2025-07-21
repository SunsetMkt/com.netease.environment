package com.netease.download.listener;

import org.json.JSONObject;

/* loaded from: classes5.dex */
public interface DownloadListener {
    void onFinish(JSONObject jSONObject);

    void onProgress(JSONObject jSONObject);
}