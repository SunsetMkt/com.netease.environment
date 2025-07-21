package com.netease.download.downloadpart;

import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.network.NetController;
import com.netease.download.util.LogUtil;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* loaded from: classes3.dex */
public class DownloadAllProxy {
    private static final String TAG = "DownloadAllProxy";
    private static DownloadAllProxy mDownloadAllProxy;
    private static Object sObject = new Object();
    private ArrayList<DownloadParams> mParamsList = null;
    private int mIndexHasSubmit = 0;
    private int mHasFinishCount = 0;
    private int mStatus = 0;
    private int mExecutorServiceQueueSize = 10;
    private ExecutorService mExs = null;
    private ArrayList<Future<Integer>> mAl = new ArrayList<>();
    private long mStartTime = 0;
    DownloadCallBack mDownloadCallBack = new DownloadCallBack() { // from class: com.netease.download.downloadpart.DownloadAllProxy.1
        /* JADX WARN: Removed duplicated region for block: B:15:0x0103 A[Catch: all -> 0x016e, TryCatch #1 {, blocks: (B:4:0x004d, B:6:0x0064, B:8:0x00ad, B:12:0x00b5, B:11:0x00b2, B:13:0x00f1, B:15:0x0103, B:16:0x016b), top: B:24:0x004d, inners: #0 }] */
        @Override // com.netease.download.downloadpart.DownloadCallBack
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public int onFinish(int r8, int r9, com.netease.download.downloader.DownloadParams r10) {
            /*
                Method dump skipped, instructions count: 369
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.download.downloadpart.DownloadAllProxy.AnonymousClass1.onFinish(int, int, com.netease.download.downloader.DownloadParams):int");
        }
    };

    static /* synthetic */ int access$208(DownloadAllProxy downloadAllProxy) {
        int i = downloadAllProxy.mHasFinishCount;
        downloadAllProxy.mHasFinishCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$308(DownloadAllProxy downloadAllProxy) {
        int i = downloadAllProxy.mIndexHasSubmit;
        downloadAllProxy.mIndexHasSubmit = i + 1;
        return i;
    }

    public int hashCode() {
        return super.hashCode();
    }

    private DownloadAllProxy() {
    }

    public static DownloadAllProxy getInstances() {
        if (mDownloadAllProxy == null) {
            mDownloadAllProxy = new DownloadAllProxy();
        }
        return mDownloadAllProxy;
    }

    public void init(ArrayList<DownloadParams> arrayList) {
        reset();
        this.mParamsList = arrayList;
    }

    public void stop() {
        LogUtil.i(TAG, "DownloadAllProxy \u7ec8\u6b62\u7ebf\u7a0b\u6c60");
        ExecutorService executorService = this.mExs;
        if (executorService == null || executorService.isShutdown()) {
            return;
        }
        this.mExs.shutdownNow();
        this.mExs = null;
    }

    public void start() {
        LogUtil.i(TAG, "mStatus=" + this.mStatus);
        NetController.getInstances().restore();
        int threadnum = TaskHandleOp.getInstance().getTaskHandle().getThreadnum();
        LogUtil.i(TAG, "\u603b\u4e0b\u8f7d\u7ebf\u7a0b\u6c60\u7ebf\u7a0b\u6570=" + threadnum);
        this.mExs = Executors.newFixedThreadPool(threadnum);
        this.mAl = new ArrayList<>();
        this.mStartTime = System.currentTimeMillis();
        ArrayList<DownloadParams> arrayList = this.mParamsList;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        int i = threadnum * 2;
        if (i < this.mParamsList.size()) {
            this.mExecutorServiceQueueSize = i;
        } else {
            this.mExecutorServiceQueueSize = this.mParamsList.size();
        }
        TaskHandleOp.getInstance().getTaskHandle().setTimeToStartDwonloadFile(System.currentTimeMillis());
        this.mIndexHasSubmit = 0;
        while (this.mIndexHasSubmit < this.mExecutorServiceQueueSize) {
            synchronized (sObject) {
                LogUtil.i(TAG, "\u4e00\u5171\u6709" + this.mParamsList.size() + "\u4e2a\u6587\u4ef6\u9700\u8981\u4e0b\u8f7d\u3002 \u7b2c " + this.mIndexHasSubmit + " \u4e2a\u5f00\u59cb\u4e0b\u8f7d, \u53c2\u6570=" + this.mParamsList.get(this.mIndexHasSubmit).toString());
                DownloadAllCore downloadAllCore = new DownloadAllCore();
                downloadAllCore.init(this.mParamsList.get(this.mIndexHasSubmit), this.mDownloadCallBack, this.mIndexHasSubmit);
                this.mAl.add(this.mExs.submit(downloadAllCore));
                this.mIndexHasSubmit = this.mIndexHasSubmit + 1;
            }
        }
    }

    public void reset() {
        LogUtil.i(TAG, "\u6062\u590d\u9ed8\u8ba4\u72b6\u6001");
        this.mIndexHasSubmit = 0;
        this.mStatus = 0;
    }

    public int getStatus() {
        return this.mStatus;
    }
}