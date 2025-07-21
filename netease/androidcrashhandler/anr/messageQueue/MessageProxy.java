package com.netease.androidcrashhandler.anr.messageQueue;

import android.app.ActivityManager;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.anr.ANRWatchDogProxy;
import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.util.StorageToFileProxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class MessageProxy {
    private static MessageProxy sMessageProxy;
    private boolean hasStorageMessageContextInfo = false;

    private MessageProxy() {
    }

    public static MessageProxy getInstance() {
        if (sMessageProxy == null) {
            sMessageProxy = new MessageProxy();
        }
        return sMessageProxy;
    }

    public void storageMessageContextInfo() {
        if (this.hasStorageMessageContextInfo) {
            LogUtils.i(LogUtils.TAG, "MessageProxy [storageMessageContextInfo] \u4efb\u52a1\u5df2\u6267\u884c");
            return;
        }
        LogUtils.i(LogUtils.TAG, "MessageProxy [storageMessageContextInfo] start");
        this.hasStorageMessageContextInfo = true;
        try {
            if (ConfigCore.getInstance().ismMessageEnabled()) {
                JSONObject jSONObject = new JSONObject();
                fillHandleMessage(jSONObject);
                fillNonHandleMessage(jSONObject);
                CUtil.str2File(jSONObject.toString(), ANRWatchDogProxy.sAnrUploadFilePath, "messageContext_" + System.currentTimeMillis() + Const.FileNameTag.MESSAGE_FILE, false);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        CUtil.runOnNewChildThread(new CUtil.ThreadTask() { // from class: com.netease.androidcrashhandler.anr.messageQueue.MessageProxy.1
            @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
            public void run() {
                try {
                    MessageProxy.this.storageProcessErrorStateInfo(20);
                    StorageToFileProxy.getInstances().finish();
                } catch (Exception e) {
                    LogUtils.i(LogUtils.TAG, "MessageProxy [storageMessageContextInfo] Exception = " + e.toString());
                    e.printStackTrace();
                }
            }
        }, null);
        this.hasStorageMessageContextInfo = false;
    }

    private void fillHandleMessage(JSONObject jSONObject) {
        LooperMessageLoggingManager.getInstance().getAnrMessage(jSONObject);
    }

    private void fillNonHandleMessage(JSONObject jSONObject) throws NoSuchFieldException {
        JSONArray jSONArray = new JSONArray();
        HookMessage.getAllMessageFromMessageQueue(false);
        ArrayList<NonHandleMessage> nonHandleMessageArrayList = HookMessage.getNonHandleMessageArrayList();
        for (int i = 0; i < nonHandleMessageArrayList.size(); i++) {
            jSONArray.put(nonHandleMessageArrayList.get(i).parse2Json());
        }
        try {
            jSONObject.put("nonhandle_message", jSONArray);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean storageProcessErrorStateInfo(int i) throws Exception {
        LogUtils.i(LogUtils.TAG, "MessageProxy [storageProcessErrorStateInfo] start");
        ActivityManager activityManager = (ActivityManager) NTCrashHunterKit.sharedKit().getContext().getSystemService("activity");
        boolean z = false;
        while (true) {
            List<ActivityManager.ProcessErrorStateInfo> processesInErrorState = activityManager.getProcessesInErrorState();
            if (processesInErrorState == null) {
                LogUtils.i(LogUtils.TAG, "MessageProxy [storageProcessErrorStateInfo] iprocessErrorStateInfoList is null");
            } else {
                Iterator<ActivityManager.ProcessErrorStateInfo> it = processesInErrorState.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    ActivityManager.ProcessErrorStateInfo next = it.next();
                    if (next != null) {
                        LogUtils.i(LogUtils.TAG, "MessageProxy [storageProcessErrorStateInfo] longMsg= " + next.longMsg);
                        CUtil.str2File("\n\n\n" + next.longMsg + "\n\n\n", ANRWatchDogProxy.sAnrUploadFilePath, "ProcessState_" + System.currentTimeMillis() + Const.FileNameTag.MESSAGE_FILE, false);
                        z = true;
                        break;
                    }
                }
            }
            if (z) {
                LogUtils.i(LogUtils.TAG, "MessageProxy [storageProcessErrorStateInfo] \u83b7\u53d6cpu\u4fe1\u606f\u6210\u529f");
                break;
            }
            i--;
            if (i <= 0) {
                LogUtils.i(LogUtils.TAG, "MessageProxy [storageProcessErrorStateInfo] \u83b7\u53d6cpu\u4fe1\u606f\u5931\u8d25\uff0c\u4e14\u8d85\u65f6\u65f6\u95f4\u5df2\u5230");
                break;
            }
            LogUtils.i(LogUtils.TAG, "MessageProxy [storageProcessErrorStateInfo] \u7b49\u5f85\u83b7\u53d6cpu\u4fe1\u606f\uff0c\u8fd8\u5269\u8d85\u65f6\u65f6\u95f4\uff1a" + i + "s");
            Thread.sleep(500L);
        }
        return z;
    }
}