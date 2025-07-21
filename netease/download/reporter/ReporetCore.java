package com.netease.download.reporter;

import com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager;
import com.netease.download.util.LogUtil;

/* loaded from: classes4.dex */
public class ReporetCore {
    private static final String TAG = "ReporetCore";
    private static ReporetCore sReporetCore;
    private Thread mStorageLoopThread = null;
    private boolean mOpen = true;

    private ReporetCore() {
    }

    public static ReporetCore getInstance() {
        if (sReporetCore == null) {
            sReporetCore = new ReporetCore();
        }
        return sReporetCore;
    }

    public void setOpen(boolean z) {
        this.mOpen = z;
    }

    public void init() {
        LogUtil.i(TAG, "ReporetCore [init] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---ReporetCore \u521d\u59cb\u5316");
        startStorageLoop();
        setOpen(true);
    }

    public void close(long j) {
        LogUtil.i(TAG, "ReporetCore [close] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u6301\u4e45\u5316\u7ed3\u675f\uff0c\u53d1\u8d77\u7ed3\u675f\u547d\u4ee4");
        finish(j);
    }

    public void test() {
        LogUtil.i(TAG, "ReporetCore [test] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---ReporetCore \u6a21\u62df\u8c03\u7528");
        new Thread(new Runnable() { // from class: com.netease.download.reporter.ReporetCore.1
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                while (true) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() { // from class: com.netease.download.reporter.ReporetCore.2
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                while (true) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new Runnable() { // from class: com.netease.download.reporter.ReporetCore.3
            @Override // java.lang.Runnable
            public void run() throws InterruptedException {
                while (true) {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void finish(long j) {
        LogUtil.i(TAG, "ReporetCore [finish] \u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---\u6301\u4e45\u5316\u7ed3\u675f\uff0c\u53d1\u8d77\u7ed3\u675f\u547d\u4ee4");
        ReportFile.getInstances().cleanAndAdd("finish_over");
    }

    public void startStorageLoop() {
        if (this.mStorageLoopThread == null) {
            Thread thread = new Thread(new Runnable() { // from class: com.netease.download.reporter.ReporetCore.4
                @Override // java.lang.Runnable
                public void run() throws InterruptedException {
                    LogUtil.i(ReporetCore.TAG, "ReporetCore [startStorageLoop] mOpen=" + ReporetCore.this.mOpen);
                    while (ReporetCore.this.mOpen) {
                        try {
                            ReportFile.getInstances().add(ReportInfo.getInstance().getInfo(false));
                            Thread.sleep(LooperMessageLoggingManager.LAG_TIME);
                        } catch (InterruptedException e) {
                            LogUtil.w(ReporetCore.TAG, "ReporetCore [startStorageLoop] InterruptedException=" + e);
                        } catch (Exception e2) {
                            LogUtil.w(ReporetCore.TAG, "ReporetCore [startStorageLoop] Exception=" + e2);
                        }
                    }
                }
            });
            this.mStorageLoopThread = thread;
            thread.start();
        }
    }
}