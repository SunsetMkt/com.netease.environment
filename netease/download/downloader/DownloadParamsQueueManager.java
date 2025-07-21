package com.netease.download.downloader;

import com.netease.download.util.LogUtil;
import com.xiaomi.onetrack.c.c;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class DownloadParamsQueueManager {
    private static final String TAG = "TaskManager";
    private static DownloadParamsQueueManager sTaskManager;
    private List<JSONObject> mParamsList = null;
    private boolean mHasStart = false;
    private boolean mPause = false;

    private DownloadParamsQueueManager() {
    }

    public static DownloadParamsQueueManager getInstance() {
        if (sTaskManager == null) {
            sTaskManager = new DownloadParamsQueueManager();
        }
        return sTaskManager;
    }

    public void put(JSONObject jSONObject) {
        LogUtil.i(TAG, "TaskManager [put] start");
        if (jSONObject == null) {
            return;
        }
        if (this.mParamsList == null) {
            this.mParamsList = new CopyOnWriteArrayList();
        }
        LogUtil.i(TAG, "TaskManager [put] pre mParamsList size = " + this.mParamsList.size() + ", mParamsList=" + this.mParamsList.toString());
        JSONObject jSONObjectTaskSortAddToParamsList = TaskSortAddToParamsList(jSONObject);
        LogUtil.i(TAG, "TaskManager [put] after mParamsList size = " + this.mParamsList.size() + ", mParamsList=" + this.mParamsList.toString());
        start(jSONObjectTaskSortAddToParamsList);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.json.JSONObject TaskSortAddToParamsList(org.json.JSONObject r10) throws org.json.JSONException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 256
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.downloader.DownloadParamsQueueManager.TaskSortAddToParamsList(org.json.JSONObject):org.json.JSONObject");
    }

    public void start(JSONObject jSONObject) {
        int iOptInt;
        boolean zOptBoolean;
        LogUtil.i(TAG, "TaskManager [start] mParamsList size = " + this.mParamsList.size());
        if (jSONObject != null && jSONObject.has(c.a.g) && jSONObject.has("priority_task")) {
            iOptInt = jSONObject.optInt(c.a.g);
            zOptBoolean = jSONObject.optBoolean("priority_task");
        } else {
            iOptInt = 0;
            zOptBoolean = false;
        }
        LogUtil.i(TAG, "[ORBIT] New Task downloadid=" + jSONObject.optString("downloadid") + " priority=" + iOptInt + " priority_task=" + zOptBoolean);
        if (TaskHandleOp.getInstance().getTaskHandle() != null && TaskHandleOp.getInstance().getTaskHandle().getPriority() != -1) {
            LogUtil.i(TAG, "TaskManager [start] \u76ee\u524d\u5df2\u6709\u4efb\u52a1\u6b63\u5728\u4e0b\u8f7d");
            if (TaskHandleOp.getInstance().getTaskHandle().getPriority() == 0 && iOptInt > 0 && zOptBoolean) {
                LogUtil.i(TAG, "TaskManager [start] \u76ee\u524d\u5df2\u6709\u4efb\u52a1\u6b63\u5728\u4e0b\u8f7d\uff0c\u6b63\u5728\u4e0b\u8f7d\u7684\u662f\u666e\u901a\u4efb\u52a1\uff0c\u65b0\u6dfb\u52a0\u8fdb\u6765\u7684\u662f\u4f18\u5148\u4efb\u52a1\uff0c\u4e14\u6307\u5b9a\u8981\u53d6\u6d88\u5f53\u524d\u4efb\u52a1\uff0c\u5219\u53d6\u6d88\u6389\u5f53\u524d\u4efb\u52a1");
                LogUtil.i(TAG, "[ORBIT] Cancel Task downloadid=" + TaskHandleOp.getInstance().getTaskHandle().getDownloadId() + " priority=" + TaskHandleOp.getInstance().getTaskHandle().getPriority());
                DownloadProxy.getInstance();
                DownloadProxy.stopAll();
            } else {
                LogUtil.i(TAG, "[ORBIT] Retain Task downloadid=" + TaskHandleOp.getInstance().getTaskHandle().getDownloadId() + " priority=" + TaskHandleOp.getInstance().getTaskHandle().getPriority());
                LogUtil.i(TAG, "TaskManager [start] \u76ee\u524d\u5df2\u6709\u4efb\u52a1\u6b63\u5728\u4e0b\u8f7d\uff0c\u6b63\u5728\u4e0b\u8f7d\u7684\u662f\u4f18\u5148\u7ea7\u4efb\u52a1\uff0c\u6216\u8005\u65b0\u589e\u52a0\u8fdb\u6765\u7684\u662f\u666e\u901a\u4efb\u52a1\uff0c\u6216\u8005\u6307\u5b9a\u4e0d\u9700\u8981\u53d6\u6d88\u5f53\u524d\u4efb\u52a1\uff0c\u5219\u65e0\u9700\u53d6\u6d88\u5f53\u524d\u4efb\u52a1");
            }
        } else {
            LogUtil.i(TAG, "TaskManager [start] \u76ee\u524d\u6ca1\u6709\u4efb\u52a1\u6b63\u5728\u4e0b\u8f7d");
        }
        if (this.mHasStart) {
            LogUtil.i(TAG, "TaskManager [start] \u4efb\u52a1\u961f\u5217\u5df2\u7ecf\u542f\u52a8\u4e2d\uff0c\u65e0\u6cd5\u518d\u6b21\u542f\u52a8");
        } else {
            this.mHasStart = true;
            dispatch();
        }
    }

    public void dispatch() {
        LogUtil.i(TAG, "TaskManager [dispatch] start");
        List<JSONObject> list = this.mParamsList;
        if (list == null || list.size() <= 0) {
            LogUtil.w(TAG, "TaskManager [dispatch] \u4efb\u52a1\u961f\u5217\u4e2d\uff0c\u5df2\u7ecf\u5168\u90e8\u4e0b\u8f7d\u5b8c\u6bd5\uff0c\u5173\u95ed\u4efb\u52a1\u961f\u5217\u5904\u7406");
            this.mHasStart = false;
            return;
        }
        if (this.mPause) {
            LogUtil.w(TAG, "TaskManager [dispatch] \u4efb\u52a1\u961f\u5217, \u5df2\u5904\u4e8e\u6682\u505c\u72b6\u6001");
            return;
        }
        try {
            JSONObject jSONObject = this.mParamsList.get(0);
            this.mParamsList.remove(0);
            DownloadProxy.getInstance().dispachMethod(jSONObject);
            LogUtil.w(TAG, "TaskManager [dispatch] mParamsList size = " + this.mParamsList.size());
        } catch (Exception e) {
            LogUtil.w(TAG, "TaskManager [dispatch] Exception = " + e.toString());
            e.printStackTrace();
        }
    }

    public void clear() {
        LogUtil.i(TAG, "TaskManager [clear] start");
        try {
            List<JSONObject> list = this.mParamsList;
            if (list != null || list.size() > 0) {
                this.mParamsList.clear();
                this.mPause = false;
            }
        } catch (Exception e) {
            LogUtil.w(TAG, "TaskManager [clear] Exception = " + e.toString());
            e.printStackTrace();
        }
    }

    public void pause() {
        List<JSONObject> list = this.mParamsList;
        if (list != null || list.size() > 0) {
            this.mPause = true;
        }
    }

    public List<JSONObject> getParamsList() {
        return this.mParamsList;
    }
}