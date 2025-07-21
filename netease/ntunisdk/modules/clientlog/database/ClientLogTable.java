package com.netease.ntunisdk.modules.clientlog.database;

import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ClientLogTable {
    private int ID;
    private String log;
    private String platform;
    private String sdk;
    private int status;
    private String timestamp;
    private String transid;
    private String type;
    private String udid;

    public int getID() {
        return this.ID;
    }

    public void setID(int i) {
        this.ID = i;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String str) {
        this.type = str;
    }

    public String getSdk() {
        return this.sdk;
    }

    public void setSdk(String str) {
        this.sdk = str;
    }

    public String getPlatform() {
        return this.platform;
    }

    public void setPlatform(String str) {
        this.platform = str;
    }

    public String getLog() {
        return this.log;
    }

    public void setLog(String str) {
        this.log = str;
    }

    public String getUdid() {
        return this.udid;
    }

    public void setUdid(String str) {
        this.udid = str;
    }

    public String getTransid() {
        return this.transid;
    }

    public void setTransid(String str) {
        this.transid = str;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String str) {
        this.timestamp = str;
    }

    public String getJsonString() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(ClientLogConstant.LOG, this.log);
            jSONObject.put(ClientLogConstant.SDK, this.sdk);
            jSONObject.put("udid", this.udid);
            jSONObject.put("type", this.type);
            jSONObject.put(ClientLogConstant.TRANSID, this.transid);
            jSONObject.put("platform", this.platform);
            jSONObject.put("timestamp", this.timestamp);
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "getJsonString Exception: " + e.getMessage());
        }
        return jSONObject.toString();
    }
}