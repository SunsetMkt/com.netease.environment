package com.netease.androidcrashhandler.other;

import com.netease.androidcrashhandler.config.ConfigCore;
import com.netease.androidcrashhandler.processCenter.TaskExecutor;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes5.dex */
public class OtherProxy {
    private static final String TAG = "OtherProxy";
    private static OtherProxy sOtherProxy;
    private BlockingQueue<OtherCore> mCallQueue;
    private int mStatus = 0;
    private ArrayList<Future<Integer>> mAl = new ArrayList<>();

    private OtherProxy() {
        this.mCallQueue = null;
        int i = ConfigCore.getInstance().getmQueueSize();
        LogUtils.i(LogUtils.TAG, "OtherProxy [TaskProxy] queue size=" + i);
        this.mCallQueue = new LinkedBlockingQueue(i <= 0 ? 20 : i);
    }

    public static OtherProxy getInstance() {
        if (sOtherProxy == null) {
            sOtherProxy = new OtherProxy();
        }
        return sOtherProxy;
    }

    public boolean put(OtherCore otherCore) {
        LogUtils.i(LogUtils.TAG, "OtherProxy [put]");
        boolean z = false;
        try {
            if (this.mCallQueue != null) {
                LogUtils.i(LogUtils.TAG, "OtherProxy [put] call size:" + this.mCallQueue.size());
                this.mCallQueue.add(otherCore);
                z = true;
            } else {
                LogUtils.i(LogUtils.TAG, "OtherProxy [put] mCallQueue is null");
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return z;
    }

    public void start() {
        LogUtils.i(LogUtils.TAG, "OtherProxy [start] mStatus=" + this.mStatus);
        if (this.mStatus == 2) {
            LogUtils.i(LogUtils.TAG, "OtherProxy [start] \u7ebf\u7a0b\u6c60\u6b63\u5728\u8fdb\u884c\u4e2d");
        } else {
            this.mStatus = 2;
            CUtil.runOnNewChildThread(new CUtil.ThreadTask() { // from class: com.netease.androidcrashhandler.other.OtherProxy.1
                AnonymousClass1() {
                }

                @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
                public void run() {
                    LogUtils.i(LogUtils.TAG, "OtherProxy [start] thread start");
                    ExecutorService executorServiceNewSingleThreadExecutor = TaskExecutor.getInstance().newSingleThreadExecutor();
                    while (true) {
                        try {
                            Callable callable = (Callable) OtherProxy.this.mCallQueue.take();
                            if (callable == null || OtherProxy.this.mStatus == 5) {
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
            }, "httpdns dispacher");
        }
    }

    /* renamed from: com.netease.androidcrashhandler.other.OtherProxy$1 */
    class AnonymousClass1 implements CUtil.ThreadTask {
        AnonymousClass1() {
        }

        @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
        public void run() {
            LogUtils.i(LogUtils.TAG, "OtherProxy [start] thread start");
            ExecutorService executorServiceNewSingleThreadExecutor = TaskExecutor.getInstance().newSingleThreadExecutor();
            while (true) {
                try {
                    Callable callable = (Callable) OtherProxy.this.mCallQueue.take();
                    if (callable == null || OtherProxy.this.mStatus == 5) {
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
    }

    public void reset() {
        LogUtils.i(LogUtils.TAG, "\u6062\u590d\u9ed8\u8ba4\u72b6\u6001");
        this.mStatus = 0;
    }

    public void stop() {
        this.mStatus = 5;
    }

    public int getStatus() {
        return this.mStatus;
    }
}