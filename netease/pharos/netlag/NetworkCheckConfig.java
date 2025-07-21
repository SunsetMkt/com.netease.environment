package com.netease.pharos.netlag;

import android.text.TextUtils;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.util.LogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NetworkCheckConfig {
    ArrayList<IpInfo> mIpInfos;
    private String mProject;
    private String mLagID = "";
    private int mLagPks = 10;
    private int mLagTimeout = 1000;
    private String mLagString = "#PHAROS#";
    private String mProtocol = "udp";
    private int mRetryCount = 1;
    private int mRetryInterval = 30;
    private boolean mLogOpen = false;
    private String mArea = "0";
    private String mTestLog = "0";
    private String mMethodId = "";

    public String getProject() {
        return this.mProject;
    }

    public String getLagID() {
        return this.mLagID;
    }

    public int getLagPks() {
        return this.mLagPks;
    }

    public int getLagTimeout() {
        return this.mLagTimeout;
    }

    public String getLagString() {
        return this.mLagString;
    }

    public String getProtocol() {
        return this.mProtocol;
    }

    public int getRetryCount() {
        return this.mRetryCount;
    }

    public int getRetryInterval() {
        return this.mRetryInterval;
    }

    public boolean isLogOpen() {
        return this.mLogOpen;
    }

    public void setLogOpen(boolean z) {
        this.mLogOpen = z;
    }

    public String getArea() {
        return this.mArea;
    }

    public String getTestLog() {
        return this.mTestLog;
    }

    public void init(JSONObject jSONObject) throws Exception {
        this.mProject = jSONObject.optString("project", "");
        this.mMethodId = jSONObject.optString("methodId", "");
        this.mLagID = jSONObject.optString("lag_id", "");
        this.mLagPks = jSONObject.optInt("lag_pks", 10);
        this.mLagString = jSONObject.optString("lag_string");
        this.mProtocol = jSONObject.optString("protocol", "udp");
        this.mRetryCount = jSONObject.optInt("counter", 1);
        this.mRetryInterval = jSONObject.optInt("interval", 30);
        this.mLagTimeout = jSONObject.optInt("lag_timeout", 1) * 1000;
        boolean zOptBoolean = jSONObject.optBoolean("logopen", PharosProxy.getInstance().isDebug());
        this.mLogOpen = zOptBoolean;
        LogUtil.setIsShowLog(zOptBoolean);
        this.mArea = jSONObject.optString("area", "0");
        this.mTestLog = jSONObject.optString("testlog", "0");
        this.mIpInfos = new ArrayList<>();
        JSONObject jSONObject2 = jSONObject.getJSONObject("server");
        Iterator<String> itKeys = jSONObject2.keys();
        while (itKeys.hasNext()) {
            try {
                String next = itKeys.next();
                this.mIpInfos.add(IpInfo.parse(next, jSONObject2.getJSONArray(next)));
            } catch (Exception unused) {
            }
        }
        if (TextUtils.isEmpty(this.mMethodId) || !"pharosnetlag".equalsIgnoreCase(this.mMethodId)) {
            throw new Exception("Error Parameters: methodId is not equal pharosnetlag. current methodId is :" + this.mMethodId);
        }
        if (TextUtils.isEmpty(this.mProject)) {
            throw new Exception("Error Parameters: project is null!!!");
        }
        if (this.mIpInfos.isEmpty()) {
            throw new Exception("Error Parameters: ip is empty!!!");
        }
    }

    public static class IpInfo {
        public String ip;
        public String name;
        public String port;

        /* JADX INFO: Access modifiers changed from: private */
        public static IpInfo parse(String str, JSONArray jSONArray) throws JSONException {
            if (jSONArray == null) {
                return null;
            }
            IpInfo ipInfo = new IpInfo();
            if (jSONArray.length() < 2) {
                throw new JSONException("ip is illegal");
            }
            ipInfo.name = str;
            ipInfo.ip = jSONArray.getString(0);
            ipInfo.port = jSONArray.getString(1);
            return ipInfo;
        }
    }
}