package com.netease.ntunisdk.modules.clientlog.core;

import android.content.Context;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogCode;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.clientlog.database.DatabaseManager;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ClientLogManager {
    private static volatile ClientLogManager instance;
    private ClientLogThread clientLogThread;
    private DatabaseManager databaseManager;
    private boolean isInit;

    private ClientLogManager() {
    }

    public DatabaseManager getDatabaseManager() {
        return this.databaseManager;
    }

    public static ClientLogManager getInstance() {
        if (instance == null) {
            synchronized (ClientLogManager.class) {
                if (instance == null) {
                    instance = new ClientLogManager();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        if (this.isInit) {
            return;
        }
        this.isInit = true;
        if (this.databaseManager == null) {
            DatabaseManager databaseManager = new DatabaseManager(context);
            this.databaseManager = databaseManager;
            databaseManager.getWritableDatabase();
        }
    }

    private synchronized void startConsumerThread() {
        ClientLogThread clientLogThread = this.clientLogThread;
        if ((clientLogThread == null || !clientLogThread.isAlive()) && this.databaseManager != null) {
            ClientLogThread clientLogThread2 = new ClientLogThread(this.databaseManager, "clientLogThread");
            this.clientLogThread = clientLogThread2;
            clientLogThread2.start();
            LogModule.d(ClientLogConstant.TAG, "startConsumerThread: " + this.clientLogThread.getName());
        }
    }

    public ClientLogCode startSubmit(String str) {
        try {
            if (!this.databaseManager.isOpen()) {
                return ClientLogCode.DATABASE_NOT_OPEN_ERROR;
            }
            if (str == null) {
                return ClientLogCode.PARAM_ERROR;
            }
            if (!isJsonString(str)) {
                return ClientLogCode.PARAM_ERROR;
            }
            ClientLogCode clientLogCodeInsert = this.databaseManager.insert(new JSONObject(str));
            startConsumerThread();
            return clientLogCodeInsert;
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "startSubmit Exception: " + e);
            return ClientLogCode.UNKNOWN_ERROR;
        }
    }

    public ClientLogCode stopSubmit() {
        try {
            ClientLogThread clientLogThread = this.clientLogThread;
            if (clientLogThread != null) {
                clientLogThread.interrupt();
                this.clientLogThread.isExistData = false;
            }
            DatabaseManager databaseManager = this.databaseManager;
            if (databaseManager != null) {
                databaseManager.closeDatabase();
            }
            return ClientLogCode.SUCCESS;
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "stopSubmit Exception: " + e);
            return ClientLogCode.UNKNOWN_ERROR;
        }
    }

    private boolean isJsonString(String str) {
        try {
            new JSONObject(str);
            return true;
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "isJsonString: " + e);
            return false;
        }
    }
}