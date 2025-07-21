package com.netease.ntunisdk.modules.clientlog.core;

import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.clientlog.database.ClientLogTable;
import com.netease.ntunisdk.modules.clientlog.database.DatabaseManager;
import com.netease.ntunisdk.modules.clientlog.utils.HttpUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ClientLogTask implements Runnable {
    private final ClientLogTable clientLogTable;
    private final DatabaseManager databaseManager;

    public ClientLogTask(ClientLogTable clientLogTable, DatabaseManager databaseManager) {
        this.clientLogTable = clientLogTable;
        this.databaseManager = databaseManager;
    }

    public boolean submit(ClientLogTable clientLogTable) {
        String str;
        try {
            if (ClientLogConstant.EB_TAG == 1) {
                str = ClientLogConstant.OVERSEA_URL;
            } else {
                int i = ClientLogConstant.EB_TAG;
                str = ClientLogConstant.INTERNAL_URL;
            }
            return HttpUtil.postData(str, clientLogTable.getJsonString(), "UTF-8", "Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "submit Exception: " + e.getMessage());
            return false;
        }
    }

    @Override // java.lang.Runnable
    public void run() throws JSONException {
        boolean zSubmit = false;
        for (int i = 0; i < 2; i++) {
            try {
                zSubmit = submit(this.clientLogTable);
                if (zSubmit) {
                    break;
                }
            } catch (Exception e) {
                LogModule.d(ClientLogConstant.TAG, "call clientLogCallable execute Exception: " + e);
                return;
            }
        }
        if (zSubmit) {
            this.databaseManager.delete(ClientLogConstant.ID, new String[]{"" + this.clientLogTable.getID()});
            return;
        }
        JSONObject jSONObject = new JSONObject(this.clientLogTable.getJsonString());
        jSONObject.put("status", 0);
        this.databaseManager.update(jSONObject, ClientLogConstant.ID, new String[]{"" + this.clientLogTable.getID()});
    }
}