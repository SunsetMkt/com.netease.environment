package com.netease.ntunisdk.external.protocol.view;

import android.app.Activity;

/* loaded from: classes.dex */
public class ProgressImpl {
    private static ProgressImpl progressImpl;
    private TextProgressDialog mProgress;

    private ProgressImpl() {
    }

    public static ProgressImpl getInstance() {
        synchronized (ProgressImpl.class) {
            if (progressImpl == null) {
                progressImpl = new ProgressImpl();
            }
        }
        return progressImpl;
    }

    public Progress showProgress(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return null;
        }
        if (this.mProgress == null) {
            this.mProgress = TextProgressDialog.newInstance(activity, false);
        }
        this.mProgress.showProgress();
        return this.mProgress;
    }

    public void dismissProgress() {
        TextProgressDialog textProgressDialog = this.mProgress;
        if (textProgressDialog != null) {
            textProgressDialog.dismissProgress();
            this.mProgress = null;
        }
    }
}