package com.netease.ntunisdk.modules.clientlog.core;

import android.text.TextUtils;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogThreadPool;
import com.netease.ntunisdk.modules.clientlog.database.ClientLogTable;
import com.netease.ntunisdk.modules.clientlog.database.DatabaseManager;
import com.netease.ntunisdk.modules.clientlog.utils.HttpUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ClientLogThread extends Thread {
    private static final String TAG = "ClientLogThread";
    private final DatabaseManager databaseManager;
    public volatile boolean isExistData;

    public ClientLogThread(DatabaseManager databaseManager, String str) {
        super(str);
        this.isExistData = true;
        this.databaseManager = databaseManager;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (ClientLogConstant.SUBMIT_MODULE == ClientLogConstant.SubmitModel.MODEL_PATCH) {
            startPatchSubmit();
        } else {
            startSingleSubmit();
        }
    }

    private synchronized void startSingleSubmit() {
        ExecutorService executorService = ClientLogThreadPool.getInstance().getExecutorService();
        if (isInterrupted()) {
            executorService.shutdown();
            LogModule.d(ClientLogConstant.TAG, "ConsumerThread is interrupted, shutdown ThreadPool");
            return;
        }
        try {
            if (this.isExistData) {
                int iQueryAllCount = this.databaseManager.queryAllCount();
                int iMax = 20;
                if (iQueryAllCount <= 50) {
                    iMax = Math.max(20, iQueryAllCount);
                }
                for (int i = 0; i < iMax; i++) {
                    ClientLogTable clientLogTableQueryAllNoSubmitRecord = this.databaseManager.queryAllNoSubmitRecord();
                    if (clientLogTableQueryAllNoSubmitRecord != null && !TextUtils.isEmpty(clientLogTableQueryAllNoSubmitRecord.getTimestamp())) {
                        ClientLogTask clientLogTask = new ClientLogTask(clientLogTableQueryAllNoSubmitRecord, this.databaseManager);
                        JSONObject jSONObject = new JSONObject(clientLogTableQueryAllNoSubmitRecord.getJsonString());
                        jSONObject.put("status", 1);
                        this.databaseManager.update(jSONObject, ClientLogConstant.ID, new String[]{"" + clientLogTableQueryAllNoSubmitRecord.getID()});
                        executorService.submit(clientLogTask);
                    }
                    if (clientLogTableQueryAllNoSubmitRecord == null) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "startSingleSubmit Exception: " + e);
        }
    }

    private synchronized void startPatchSubmit() {
        try {
            if (this.isExistData) {
                while (this.databaseManager.queryAllCount() >= 10 && submitPatch(this.databaseManager.queryAllRecord())) {
                }
            }
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "startSubmitTasks Exception: " + e);
        }
    }

    public boolean submitPatch(List<ClientLogTable> list) {
        String str;
        try {
            if (ClientLogConstant.EB_TAG == 1) {
                str = ClientLogConstant.OVERSEA_URL;
            } else {
                int i = ClientLogConstant.EB_TAG;
                str = ClientLogConstant.INTERNAL_URL;
            }
            ArrayList arrayList = new ArrayList();
            Iterator<ClientLogTable> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().getJsonString());
            }
            boolean zPostData = HttpUtil.postData(str, Arrays.toString((String[]) arrayList.toArray(new String[arrayList.size()])), "UTF-8", ClientLogConstant.PATCH_TYPE_KEY, ClientLogConstant.PATCH_TYPE_VALUE);
            if (zPostData) {
                for (ClientLogTable clientLogTable : list) {
                    this.databaseManager.delete(ClientLogConstant.ID, new String[]{"" + clientLogTable.getID()});
                }
            }
            return zPostData;
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "submit Exception: " + e.getMessage());
            return false;
        }
    }
}