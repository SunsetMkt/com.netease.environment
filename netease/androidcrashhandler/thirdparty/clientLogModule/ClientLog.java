package com.netease.androidcrashhandler.thirdparty.clientLogModule;

import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes6.dex */
public class ClientLog {
    private static final String TAG = "ClientLog";
    private static ClientLog sClientLog = null;
    private static final String sModuleName = "clientLog";
    private static final String sSource = "crashhunter";

    public void addModuleCallback() {
    }

    public void sendClientLog(String str) {
    }

    private ClientLog() {
    }

    public static ClientLog getInstence() {
        if (sClientLog == null) {
            sClientLog = new ClientLog();
        }
        return sClientLog;
    }

    private JSONObject createUploadJsonInfo(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "sendClientLog");
            JSONObject jSONObject2 = NTCrashHunterKit.sharedKit().getmCurrentParamsInfo().getmParamsJson();
            jSONObject2.put("moduleEventInfo", str);
            jSONObject.put(ClientLogConstant.LOG, jSONObject2);
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "ClientLog [sendClientLog ] Exception=" + e.toString());
            e.printStackTrace();
        }
        return jSONObject;
    }
}