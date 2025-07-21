package com.netease.pharos.netlag;

import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.network.NetworkStatus;
import com.netease.pharos.util.Util;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NetworkResult {
    public static final int CANCEL = -1;
    public static final int ERROR_HAS_RUNNING = 11;
    public static final int ERROR_NETWORK = 2;
    public static final int ERROR_PARAMS_ILLEGAL = 1;
    public static final int FAILED = -2;
    public static final int SUCCESS = 0;
    public static final int UNKNOWN = -3;
    private final int code;
    private final String errorMsg;
    private JSONObject resultObj;
    private int type;

    private static String getErrorMessage(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 11 ? "netlag unknown error" : "netlag processing" : "netlag network error" : "netlag params illegal" : "";
    }

    private NetworkResult(int i, String str) {
        this.type = -3;
        this.code = i;
        this.errorMsg = str;
    }

    private NetworkResult(int i) {
        this(i, getErrorMessage(i));
    }

    public static NetworkResult fail(int i) throws JSONException {
        NetworkResult networkResult = new NetworkResult(i);
        networkResult.type = -2;
        networkResult.resultObj = new JSONObject();
        try {
            NetworkCheckConfig config = NetworkCheckProxy.getInstance().getConfig();
            networkResult.resultObj.put("project", config.getProject());
            networkResult.resultObj.put("testlog", config.getTestLog());
            networkResult.resultObj.put(ClientLogConstant.TRANSID, Util.getTransId());
            networkResult.resultObj.put("lag_id", config.getLagID());
            networkResult.resultObj.put("udid", PharosProxy.getInstance().getUdid());
            networkResult.resultObj.put("network", NetworkStatus.getInstance().getNetwork());
            networkResult.resultObj.put("code", networkResult.code);
            networkResult.resultObj.put("errlog", networkResult.errorMsg);
            networkResult.resultObj.put("result", new JSONObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return networkResult;
    }

    public static NetworkResult success(JSONObject jSONObject) throws JSONException {
        NetworkResult networkResult = new NetworkResult(0);
        networkResult.type = 0;
        networkResult.resultObj = new JSONObject();
        try {
            NetworkCheckConfig config = NetworkCheckProxy.getInstance().getConfig();
            networkResult.resultObj.put("project", config.getProject());
            networkResult.resultObj.put("testlog", config.getTestLog());
            networkResult.resultObj.put(ClientLogConstant.TRANSID, Util.getTransId());
            networkResult.resultObj.put("udid", PharosProxy.getInstance().getUdid());
            networkResult.resultObj.put("network", NetworkStatus.getInstance().getNetwork());
            networkResult.resultObj.put("lag_id", config != null ? config.getLagID() : "");
            networkResult.resultObj.put("code", networkResult.code);
            networkResult.resultObj.put("errlog", networkResult.errorMsg);
            networkResult.resultObj.put("result", jSONObject);
        } catch (Exception unused) {
        }
        return networkResult;
    }

    public static NetworkResult cancel() throws JSONException {
        NetworkResult networkResult = new NetworkResult(0, "netlag cancel");
        networkResult.type = -1;
        networkResult.resultObj = new JSONObject();
        try {
            NetworkCheckConfig config = NetworkCheckProxy.getInstance().getConfig();
            networkResult.resultObj.put("project", config.getProject());
            networkResult.resultObj.put("testlog", config.getTestLog());
            networkResult.resultObj.put(ClientLogConstant.TRANSID, Util.getTransId());
            networkResult.resultObj.put("udid", PharosProxy.getInstance().getUdid());
            networkResult.resultObj.put("network", NetworkStatus.getInstance().getNetwork());
            networkResult.resultObj.put("lag_id", config != null ? config.getLagID() : "");
            networkResult.resultObj.put("code", networkResult.code);
            networkResult.resultObj.put("errlog", networkResult.errorMsg);
            networkResult.resultObj.put("result", new JSONObject());
        } catch (Exception unused) {
        }
        return networkResult;
    }

    public static NetworkResult isProcessing(JSONObject jSONObject) throws JSONException {
        NetworkResult networkResult = new NetworkResult(11, "netlag processing");
        networkResult.type = -2;
        if (jSONObject != null) {
            networkResult.resultObj = jSONObject;
        } else {
            networkResult.resultObj = new JSONObject();
        }
        try {
            networkResult.resultObj.put("udid", PharosProxy.getInstance().getUdid());
            networkResult.resultObj.put(ClientLogConstant.TRANSID, Util.getTransId());
            networkResult.resultObj.put("network", NetworkStatus.getInstance().getNetwork());
            networkResult.resultObj.put("code", networkResult.code);
            networkResult.resultObj.put("errlog", networkResult.errorMsg);
            networkResult.resultObj.put("result", new JSONObject());
        } catch (Exception unused) {
        }
        return networkResult;
    }

    public int getType() {
        return this.type;
    }

    public boolean isSuccess() {
        return this.type == 0;
    }

    public JSONObject getResult() {
        return this.resultObj;
    }
}