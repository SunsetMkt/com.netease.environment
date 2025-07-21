package com.netease.cloud.nos.android.core;

import com.netease.cloud.nos.android.utils.LogUtil;

/* loaded from: classes5.dex */
public class UploadTaskExecutor {
    private static final String LOGTAG = LogUtil.makeLogTag(UploadTaskExecutor.class);
    private volatile UploadTask task;

    public UploadTaskExecutor() {
    }

    public UploadTaskExecutor(UploadTask uploadTask) {
        this.task = uploadTask;
    }

    public void cancel() {
        if (this.task != null) {
            try {
                this.task.cancel();
            } catch (Exception e) {
                LogUtil.e(LOGTAG, "cancel async task exception", e);
            }
        }
    }

    public CallRet get() {
        if (this.task == null) {
            return null;
        }
        try {
            return this.task.get();
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "get async task exception", e);
            return null;
        }
    }

    public boolean isUpCancelled() {
        return this.task != null && this.task.isUpCancelled();
    }

    public void setTask(UploadTask uploadTask) {
        this.task = uploadTask;
    }
}