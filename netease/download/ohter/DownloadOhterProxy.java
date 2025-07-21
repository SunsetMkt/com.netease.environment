package com.netease.download.ohter;

import com.netease.download.downloader.DownloadParams;
import com.netease.download.downloader.DownloadProxy;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.network.NetController;
import com.netease.download.taskManager.TaskExecutor;
import com.netease.download.util.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/* loaded from: classes5.dex */
public class DownloadOhterProxy {
    private static final String TAG = "DownloadOhterProxy";
    private static DownloadOhterProxy mDownloadOhterProxy;
    private List<DownloadParams> mParamsList = null;
    private int mIndexHasSubmit = 0;
    private int mIndexhasResult = 0;
    private int mStatus = 0;
    private int mExecutorServiceQueueSize = 10;
    private ArrayList<Future<Integer>> mAl = new ArrayList<>();

    public void stop() {
    }

    private DownloadOhterProxy() {
    }

    public static DownloadOhterProxy getInstances() {
        if (mDownloadOhterProxy == null) {
            mDownloadOhterProxy = new DownloadOhterProxy();
        }
        return mDownloadOhterProxy;
    }

    public void init(List<DownloadParams> list) {
        LogUtil.i(TAG, "DownloadOhterProxy [init]");
        reset();
        this.mParamsList = list;
    }

    public void reset() {
        LogUtil.i(TAG, "DownloadOhterProxy [reset] \u6062\u590d\u9ed8\u8ba4\u72b6\u6001");
        this.mIndexHasSubmit = 0;
        this.mIndexhasResult = 0;
        this.mStatus = 0;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public void start() {
        DownloadOtherCore downloadOtherCore;
        StringBuilder sb;
        DownloadParams downloadParams;
        LogUtil.i(TAG, "DownloadOhterProxy [start] \u5f00\u59cb\u5176\u4ed6\u6587\u4ef6\u4e0b\u8f7d");
        LogUtil.i(TAG, "DownloadOhterProxy [start] mStatus=" + this.mStatus);
        NetController.getInstances().restore();
        int threadnum = TaskHandleOp.getInstance().getTaskHandle().getThreadnum();
        LogUtil.i(TAG, "DownloadOhterProxy [start] \u7ebf\u7a0b\u6c60\u603b\u7ebf\u7a0b\u6570=" + threadnum);
        this.mAl = new ArrayList<>();
        long jCurrentTimeMillis = System.currentTimeMillis();
        List<DownloadParams> list = this.mParamsList;
        if (list == null || list.size() <= 0) {
            return;
        }
        int i = threadnum * 2;
        if (i < this.mParamsList.size()) {
            this.mExecutorServiceQueueSize = i;
        } else {
            this.mExecutorServiceQueueSize = this.mParamsList.size();
        }
        this.mIndexHasSubmit = 0;
        while (this.mIndexHasSubmit < this.mExecutorServiceQueueSize) {
            LogUtil.i(TAG, "\u4e00\u5171\u6709" + this.mParamsList.size() + "\u4e2a\u6587\u4ef6\u9700\u8981\u4e0b\u8f7d\u3002 \u7b2c " + this.mIndexHasSubmit + " \u4e2a\u5f00\u59cb\u4e0b\u8f7d, \u53c2\u6570=" + this.mParamsList.get(this.mIndexHasSubmit).toString());
            DownloadOtherCore downloadOtherCore2 = new DownloadOtherCore();
            downloadOtherCore2.init(this.mParamsList.get(this.mIndexHasSubmit));
            this.mAl.add(TaskExecutor.getInstance().getFixedThreadPool().submit(downloadOtherCore2));
            this.mIndexHasSubmit = this.mIndexHasSubmit + 1;
        }
        int iIntValue = 11;
        while (true) {
            List<DownloadParams> list2 = this.mParamsList;
            if (list2 == null || this.mIndexhasResult >= list2.size()) {
                break;
            }
            try {
                try {
                    try {
                        try {
                            downloadParams = this.mParamsList.get(this.mIndexhasResult);
                            if (this.mAl.size() > 0) {
                                iIntValue = this.mAl.get(0).get().intValue();
                                this.mAl.remove(0);
                            }
                        } catch (CancellationException e) {
                            LogUtil.w(TAG, "DownloadOhterProxy [start] CancellationException=" + e);
                            e.printStackTrace();
                            if (this.mIndexHasSubmit < this.mParamsList.size()) {
                                downloadOtherCore = new DownloadOtherCore();
                                downloadOtherCore.init(this.mParamsList.get(this.mIndexHasSubmit));
                                sb = new StringBuilder("\u4e00\u5171\u6709");
                            }
                        } catch (ExecutionException e2) {
                            LogUtil.w(TAG, "DownloadOhterProxy [start] ExecutionException=" + e2);
                            e2.printStackTrace();
                            if (this.mIndexHasSubmit < this.mParamsList.size()) {
                                downloadOtherCore = new DownloadOtherCore();
                                downloadOtherCore.init(this.mParamsList.get(this.mIndexHasSubmit));
                                sb = new StringBuilder("\u4e00\u5171\u6709");
                            }
                        }
                    } catch (InterruptedException e3) {
                        LogUtil.w(TAG, "DownloadOhterProxy [start] InterruptedException=" + e3);
                        e3.printStackTrace();
                        if (this.mIndexHasSubmit < this.mParamsList.size()) {
                            downloadOtherCore = new DownloadOtherCore();
                            downloadOtherCore.init(this.mParamsList.get(this.mIndexHasSubmit));
                            sb = new StringBuilder("\u4e00\u5171\u6709");
                        }
                    }
                } catch (Exception e4) {
                    LogUtil.w(TAG, "DownloadOhterProxy [start] Exception=" + e4);
                    e4.printStackTrace();
                    if (this.mIndexHasSubmit < this.mParamsList.size()) {
                        downloadOtherCore = new DownloadOtherCore();
                        downloadOtherCore.init(this.mParamsList.get(this.mIndexHasSubmit));
                        sb = new StringBuilder("\u4e00\u5171\u6709");
                    }
                }
                if (downloadParams != null) {
                    LogUtil.i(TAG, "\u7b2c " + this.mIndexhasResult + " \u4e2a\u4e0b\u8f7d\u7ed3\u679c = " + iIntValue + ", \u6587\u4ef6\u8def\u5f84 = " + downloadParams.getFilePath());
                    this.mIndexhasResult = this.mIndexhasResult + 1;
                    if (this.mIndexHasSubmit < this.mParamsList.size()) {
                        downloadOtherCore = new DownloadOtherCore();
                        downloadOtherCore.init(this.mParamsList.get(this.mIndexHasSubmit));
                        sb = new StringBuilder("\u4e00\u5171\u6709");
                        sb.append(this.mParamsList.size());
                        sb.append("\u4e2a\u6587\u4ef6\u9700\u8981\u4e0b\u8f7d\u3002 \u7b2c ");
                        sb.append(this.mIndexHasSubmit);
                        sb.append(" \u4e2a\u5f00\u59cb\u4e0b\u8f7d, \u53c2\u6570=");
                        sb.append(this.mParamsList.get(this.mIndexHasSubmit).toString());
                        LogUtil.i(TAG, sb.toString());
                        this.mAl.add(TaskExecutor.getInstance().getFixedThreadPool().submit(downloadOtherCore));
                        this.mIndexHasSubmit++;
                    }
                } else if (this.mIndexHasSubmit < this.mParamsList.size()) {
                    downloadOtherCore = new DownloadOtherCore();
                    downloadOtherCore.init(this.mParamsList.get(this.mIndexHasSubmit));
                    sb = new StringBuilder("\u4e00\u5171\u6709");
                    sb.append(this.mParamsList.size());
                    sb.append("\u4e2a\u6587\u4ef6\u9700\u8981\u4e0b\u8f7d\u3002 \u7b2c ");
                    sb.append(this.mIndexHasSubmit);
                    sb.append(" \u4e2a\u5f00\u59cb\u4e0b\u8f7d, \u53c2\u6570=");
                    sb.append(this.mParamsList.get(this.mIndexHasSubmit).toString());
                    LogUtil.i(TAG, sb.toString());
                    this.mAl.add(TaskExecutor.getInstance().getFixedThreadPool().submit(downloadOtherCore));
                    this.mIndexHasSubmit++;
                }
            } catch (Throwable th) {
                if (this.mIndexHasSubmit < this.mParamsList.size()) {
                    DownloadOtherCore downloadOtherCore3 = new DownloadOtherCore();
                    downloadOtherCore3.init(this.mParamsList.get(this.mIndexHasSubmit));
                    LogUtil.i(TAG, "\u4e00\u5171\u6709" + this.mParamsList.size() + "\u4e2a\u6587\u4ef6\u9700\u8981\u4e0b\u8f7d\u3002 \u7b2c " + this.mIndexHasSubmit + " \u4e2a\u5f00\u59cb\u4e0b\u8f7d, \u53c2\u6570=" + this.mParamsList.get(this.mIndexHasSubmit).toString());
                    this.mAl.add(TaskExecutor.getInstance().getFixedThreadPool().submit(downloadOtherCore3));
                    this.mIndexHasSubmit = this.mIndexHasSubmit + 1;
                }
                throw th;
            }
        }
        LogUtil.i(TAG, "\u5168\u90e8\u4e0b\u8f7d\u82b1\u8d39\u603b\u65f6\u95f4 = " + (System.currentTimeMillis() - jCurrentTimeMillis) + " ms");
        DownloadProxy.unregisterReceiver();
        StringBuilder sb2 = new StringBuilder("\u603b\u5927\u5c0f=");
        DownloadListenerProxy.getInstances();
        sb2.append(DownloadListenerProxy.getmTotalSize());
        LogUtil.i(TAG, sb2.toString());
        LogUtil.i(TAG, "AllSize\u7684\u603b\u5927\u5c0f=" + DownloadListenerProxy.getInstances().getHasDownloadSize());
        LogUtil.i(TAG, "DownloadOhterProxy [start] \u4e0b\u8f7d\u540e\u671f\uff0c\u53d1\u9001\u65e5\u5fd7\uff08Other\u6587\u4ef6\uff09");
    }
}