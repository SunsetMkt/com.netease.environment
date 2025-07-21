package com.netease.ntunisdk.modules.clientlog.core;

import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ClientLogMessage {
    private final String callType;
    private final JSONObject originJson;
    private final String source;
    private final JSONObject submitJson;

    public String getSource() {
        return this.source;
    }

    public String getCallType() {
        return this.callType;
    }

    public JSONObject getOriginJson() {
        return this.originJson;
    }

    public JSONObject getSubmitJson() {
        return this.submitJson;
    }

    public ClientLogMessage(String str, String str2, JSONObject jSONObject, JSONObject jSONObject2) {
        this.source = str;
        this.callType = str2;
        this.originJson = jSONObject;
        this.submitJson = jSONObject2;
    }
}