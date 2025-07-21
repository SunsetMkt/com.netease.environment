package com.netease.androidcrashhandler.processCenter;

import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes5.dex */
public class TaskProxy {
    private static final String TAG = "OtherInfoProxy";
    private static TaskProxy mTaskProxy;
    private ArrayList<Future<Integer>> mAl = new ArrayList<>();
    private int mStatus = 0;
    private BlockingQueue<Callable<Integer>> mTaskQueue;

    public void init() {
    }

    private TaskProxy() {
        this.mTaskQueue = null;
        LogUtils.i(LogUtils.TAG, "TaskProxy [TaskProxy] queue size=" + ConfigCore.getInstance().getmQueueSize());
        this.mTaskQueue = new LinkedBlockingQueue();
    }

    public static TaskProxy getInstances() {
        if (mTaskProxy == null) {
            mTaskProxy = new TaskProxy();
        }
        return mTaskProxy;
    }

    public boolean put(Callable<Integer> callable) {
        LogUtils.i(LogUtils.TAG, "TaskProxy [put]");
        try {
            if (this.mTaskQueue != null) {
                LogUtils.i(LogUtils.TAG, "TaskProxy [put] call size:" + this.mTaskQueue.size());
                this.mTaskQueue.add(callable);
                return true;
            }
            LogUtils.i(LogUtils.TAG, "TaskProxy [put] mTaskQueue is null");
            return false;
        } catch (Throwable th) {
            LogUtils.i(LogUtils.TAG, "TaskProxy [put] Throwable" + th.toString());
            th.printStackTrace();
            return false;
        }
    }

    public void start() {
        LogUtils.i(LogUtils.TAG, "TaskProxy [start] mStatus=" + this.mStatus);
        if (this.mStatus == 2) {
            LogUtils.i(LogUtils.TAG, "TaskProxy [start] \u7ebf\u7a0b\u6c60\u6b63\u5728\u8fdb\u884c\u4e2d");
        } else {
            this.mStatus = 2;
            CUtil.runOnNewChildThread(new CUtil.ThreadTask() { // from class: com.netease.androidcrashhandler.processCenter.TaskProxy.1
                @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
                public void run() {
                    LogUtils.i(LogUtils.TAG, "TaskProxy [start] thread start");
                    ExecutorService executorServiceNewSingleThreadExecutor = TaskExecutor.getInstance().newSingleThreadExecutor();
                    while (true) {
                        try {
                            Callable callable = (Callable) TaskProxy.this.mTaskQueue.take();
                            if (callable == null || TaskProxy.this.mStatus == 5) {
                                break;
                            }
                            try {
                                executorServiceNewSingleThreadExecutor.submit(callable);
                            } catch (Throwable th) {
                                th.printStackTrace();
                            }
                        } catch (Throwable th2) {
                            try {
                                LogUtils.i(LogUtils.TAG, "TaskProxy [start] Exception=" + th2.toString());
                                th2.printStackTrace();
                                if (executorServiceNewSingleThreadExecutor == null || executorServiceNewSingleThreadExecutor.isShutdown()) {
                                    return;
                                }
                            } finally {
                                if (executorServiceNewSingleThreadExecutor != null && !executorServiceNewSingleThreadExecutor.isShutdown()) {
                                    executorServiceNewSingleThreadExecutor.shutdown();
                                }
                            }
                        }
                    }
                }
            }, "crashhunter_otherproxy");
        }
    }

    public void stop() {
        this.mStatus = 5;
    }

    public void reset() {
        LogUtils.i(LogUtils.TAG, "\u6062\u590d\u9ed8\u8ba4\u72b6\u6001");
        this.mStatus = 0;
    }

    public int getStatus() {
        return this.mStatus;
    }
}